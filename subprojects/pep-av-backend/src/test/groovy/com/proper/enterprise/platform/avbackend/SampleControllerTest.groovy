package com.proper.enterprise.platform.avbackend

import com.proper.enterprise.platform.avbackend.model.CountReturnModel
import com.proper.enterprise.platform.avbackend.model.CreateReturnModel
import com.proper.enterprise.platform.avbackend.model.QueryReturnModel
import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.mongo.constants.MongoConstants
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.bson.Document
import org.junit.After
import org.junit.Test
import org.springframework.http.HttpStatus

class SampleControllerTest extends AbstractTest {

    private static final String URL = "/avdemo/classes"
    def collectionName = "avDemo"

    @Test
    void create() {
        def document = [:]
        document['name'] = 'test'
        CreateReturnModel returnModel = createDocument(collectionName, document)
        assert null != returnModel.getObjectId()
        deleteDocument(collectionName, returnModel.getObjectId())
    }

    @Test
    void delete() {
        def document = [:]
        document['name'] = 'test'
        CreateReturnModel returnModel = createDocument(collectionName, document)
        CreateReturnModel returnMode2 = createDocument(collectionName, document)
        deleteDocument(collectionName, returnModel.getObjectId() + "," + returnMode2.getObjectId())
        assert 0 == countDocument(collectionName, null)
    }


    @Test
    void update() {
        mockUser("test1")
        Authentication.setCurrentUserId("test1")
        def document = [:]
        document['name'] = 'test'
        document['enable'] = true
        CreateReturnModel returnModel = createDocument(collectionName, document)
        document['name'] = 'test1'
        document['enable'] = false
        QueryReturnModel queryReturnOldModel = queryDocument(collectionName, null)
        List<Map> listOld = queryReturnOldModel.getResults()
        String oldModifyTime = listOld.get(0).get(MongoConstants.LAST_MODIFY_TIME)
        assert "test1" == listOld.get(0).get(MongoConstants.CREATE_USER_ID)
        mockUser("test2")
        Authentication.setCurrentUserId("test2")
        Thread.sleep(1000)
        putDocument(collectionName, document, returnModel.getObjectId())
        QueryReturnModel queryReturnModel = queryDocument(collectionName, null)
        List<Map> list = queryReturnModel.getResults()
        assert list.get(0).get("name") == 'test1'
        assert list.get(0).get("enable") == false
        assert DateUtil.toDate(oldModifyTime, PEPConstants.DEFAULT_DATETIME_FORMAT).getTime() <
            DateUtil.toDate(list.get(0).get(MongoConstants.LAST_MODIFY_TIME).toString(), PEPConstants.DEFAULT_DATETIME_FORMAT).getTime()
        assert "test2" == list.get(0).get(MongoConstants.LAST_MODIFY_USER_ID)
        deleteDocument(collectionName, returnModel.getObjectId())
    }


    @Test
    void query() {
        def document = [:]
        document['name'] = 'test'
        CreateReturnModel returnModel1 = createDocument(collectionName, document)
        QueryReturnModel queryReturnModel = queryDocument(collectionName, null)
        List<Document> list = queryReturnModel.getResults()
        Map idMap = list.get(0).get("_id")
        assert returnModel1.getObjectId() == idMap.get("\$oid")

        document['name'] = 'test2'
        CreateReturnModel returnModel2 = createDocument(collectionName, document)
        document['name'] = 'test3'
        CreateReturnModel returnModel3 = createDocument(collectionName, document)

        /*limit*/
        QueryReturnModel querySkipLimit2 = queryDocument(collectionName, null, 1, 2)
        List listlimit2 = querySkipLimit2.getResults()
        assert listlimit2.size() == 2
        QueryReturnModel querySkipLimit1 = queryDocument(collectionName, null, 1, 1)
        List listlimit1 = querySkipLimit1.getResults()
        assert listlimit1.size() == 1

        /*where*/
        Map equalQuery = new HashMap()
        equalQuery.put("name", "test3")
        QueryReturnModel equalQueryModel = queryDocument(collectionName, equalQuery)
        List<Document> eqlist = equalQueryModel.getResults()
        assert eqlist.get(0).get("name") == 'test3'
        /*queryOne*/
        Map queryOne = new HashMap()
        queryOne.put("objectId", returnModel1.getObjectId())
        QueryReturnModel queryOneModel = queryDocument(collectionName, queryOne)
        List<Document> queryOneList = queryOneModel.getResults()
        assert queryOneList.size() == 1
        assert queryOneList.get(0).get("name") == "test"
        /*in*/
        Map inQuery = new HashMap()
        Map ids = new HashMap()
        List<String> idsAttr = new ArrayList<>()
        idsAttr.add(returnModel1.getObjectId())
        idsAttr.add(returnModel2.getObjectId())
        ids.put("\$in", idsAttr)
        inQuery.put("_id", ids)
        QueryReturnModel inQueryModel = queryDocument(collectionName, inQuery)
        List<Document> inlist = inQueryModel.getResults()
        assert inlist.size() == 2
        deleteDocument(collectionName, returnModel1.getObjectId() + ","
            + returnModel2.getObjectId() + "," + returnModel3.objectId)
    }

    @Test
    void count() {
        def document = [:]
        document['name'] = 'test'
        CreateReturnModel returnModel1 = createDocument(collectionName, document)
        CreateReturnModel returnModel2 = createDocument(collectionName, document)
        CreateReturnModel returnModel3 = createDocument(collectionName, document)
        CreateReturnModel returnModel4 = createDocument(collectionName, document)
        CreateReturnModel returnModel5 = createDocument(collectionName, document)
        assert 5 == countDocument(collectionName, null)
        deleteDocument(collectionName, StringUtil.join(returnModel1.getObjectId()
            , returnModel2.getObjectId()
            , returnModel3.getObjectId()
            , returnModel4.getObjectId()
            , returnModel5.getObjectId(), ","))
    }

    @After
    void delALLD() {
        deleteAll(collectionName)
    }

    private CreateReturnModel createDocument(String collectionName, Object obj) {
        return JSONUtil.parse(post(URL + "/" + collectionName, JSONUtil.toJSON(obj),
            HttpStatus.OK).getResponse().getContentAsString(), CreateReturnModel.class)
    }

    private void deleteAll(String collectionName) {
        QueryReturnModel queryReturnModel = queryDocument(collectionName, null)
        List<Document> list = queryReturnModel.getResults()
        String delStr = ""
        for (Document document : list) {
            Map idMap = document.get("_id")
            delStr += idMap.get("\$oid") + ","
        }
        if (StringUtil.isNotEmpty(delStr)) {
            deleteDocument(collectionName, delStr)
        }
    }

    private QueryReturnModel queryDocument(String collectionName, Map where) {
        return queryDocument(collectionName, where, null, null)
    }

    private QueryReturnModel queryDocument(String collectionName, Map where, Integer skip, Integer limit) {
        if (null == where) {
            where = new HashMap()
        }
        Map baseMap = new HashMap()
        baseMap.put("_method", "GET")
        baseMap.put("where", where)
        if (null != skip) {
            baseMap.put("skip", skip)
        }
        if (null != limit) {
            baseMap.put("limit", limit)
        }
        QueryReturnModel returnModel = JSONUtil.parse(post(URL + "/" + collectionName, JSONUtil.toJSON(baseMap),
            HttpStatus.OK).getResponse().getContentAsString(), QueryReturnModel.class)
        return returnModel
    }

    private long countDocument(String collectionName, Map whereMap) {
        if (null == whereMap) {
            whereMap = new HashMap()
        }
        Map baseMap = new HashMap()
        baseMap.put("_method", "GET")
        baseMap.put("count", 1)
        baseMap.put("where", whereMap)
        CountReturnModel countReturnModel = JSONUtil.parse(post(URL + "/" + collectionName, JSONUtil.toJSON(baseMap),
            HttpStatus.OK).getResponse().getContentAsString(), CountReturnModel.class)
        return countReturnModel.getCount()
    }

    private void deleteDocument(String collectionName, String ids) {
        Map baseMap = new HashMap()
        baseMap.put("_method", "DELETE")
        post(URL + "/" + collectionName + '/' + ids, JSONUtil.toJSON(baseMap), HttpStatus.OK)
    }


    private void putDocument(String collectionName, Map setMap, String ids) {
        if (null == setMap) {
            setMap = new HashMap()
        }
        Map baseMap = new HashMap()
        baseMap.put("_method", "PUT")
        baseMap.putAll(setMap)
        post(URL + "/" + collectionName + "/" + ids, JSONUtil.toJSON(baseMap),
            HttpStatus.OK)
    }

}
