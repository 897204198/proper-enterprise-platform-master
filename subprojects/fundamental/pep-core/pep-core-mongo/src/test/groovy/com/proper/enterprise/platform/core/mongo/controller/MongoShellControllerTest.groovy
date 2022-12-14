package com.proper.enterprise.platform.core.mongo.controller

import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.core.PEPPropertiesLoader
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.mongo.service.MongoShellService
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.test.AbstractSpringTest
import org.bson.Document
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class MongoShellControllerTest extends AbstractSpringTest {

    @Autowired
    MongoShellService service

    private static final String COLLECTION_NAME = "MSCDOC"

    @After
    public void tearDown() {
        service.drop(COLLECTION_NAME)
    }

    private static final String URL = "/msc/$COLLECTION_NAME"

    @Test
    public void deleteById() {
        def doc = insertOne()
        def id = getId(doc)
        delete("$URL/$id", HttpStatus.NO_CONTENT)
        assert StringUtil.isNull(query(id))
        delete("$URL/$id", HttpStatus.NOT_FOUND)
    }

    private def insertOne() {
        def doc = new Document([year: DateUtil.currentYear, timestamp: DateUtil.getTimestamp(true)])
        def res = post(URL, JSONUtil.toJSON(doc), HttpStatus.CREATED).getResponse().getContentAsString()
        Document.parse(res)
    }

    private static String getId(Document doc) {
        doc.getObjectId('_id').toString()
    }

    private String query(String id) {
        get("$URL/$id", HttpStatus.OK).response.getContentAsString()
    }

    @Test
    public void deleteByIds() {
        def doc1 = insertOne()
        def doc2 = insertOne()

        delete("$URL/${getId(doc1)},${getId(doc2)}", HttpStatus.NO_CONTENT)
        assert StringUtil.isNull(query(getId(doc1)))
        assert StringUtil.isNull(query(getId(doc2)))
        delete("$URL/${getId(doc1)},${getId(doc2)}", HttpStatus.NOT_FOUND)
    }

    @Test
    public void updateById() {
        def doc = insertOne()
        put("$URL/${getId(doc)}", '{$set:{abc:123,def:456},$unset:{year:"",notexist:""}}', HttpStatus.OK)
        def newDoc = Document.parse(query(getId(doc)))
        assert newDoc.get('year') == null
        assert newDoc.get('abc') == 123
        assert newDoc.get('def') == 456
    }

    @Test
    public void testQuery() {
        def doc = insertOne()
        def newDoc = Document.parse(query(getId(doc)))
        assert doc.getObjectId().toString() == newDoc.getObjectId().toString()
    }

    @Test
    public void normalQuery() {
        int times = 5
        times.times {
            insertOne()
        }

        def query = "{year: ${DateUtil.currentYear} }"
        // ??????????????????????????????????????????????????? 5 ???
        def count = getDataTrunk("$URL?query=${encode(query)}").count
        assert count == service.query(COLLECTION_NAME, query).size()
        assert count >= times
        def size = getDataTrunk("$URL?query=${encode(query)}&limit=3").data.size()
        assert size == service.query(COLLECTION_NAME, query, 3).size()
        assert size == 3

        def max = getFirstTimestamp getDataTrunk("$URL?query=${encode(query)}&sort=${encode("{timestamp: -1}")}&limit=3").data
        def min = getFirstTimestamp getDataTrunk("$URL?query=${encode(query)}&sort=${encode("{timestamp: 1}")}").data
        assert max > min
        assert min == service.query(COLLECTION_NAME, query, "{timestamp: 1}")[0].get('timestamp')

        def allData = getDataTrunk("$URL?query=")
        assert allData.count == allData.count
        assert allData.count >= times
    }

    @Test
    public void noResultQuery() {
        def query = "{year: ${DateUtil.currentYear + 10} }"
        def dt = getDataTrunk("$URL?query=${encode(query)}")
        assert dt.getData() == []
        assert dt.count == 0
    }

    private static def encode(String url) {
        URLEncoder.encode(url, PEPPropertiesLoader.load(CoreProperties.class).getCharset())
    }

    private def getDataTrunk(String url) {
        def res = get(url, HttpStatus.OK).response.contentAsString
        return JSONUtil.parse(res, DataTrunk.class)
    }

    private static def getFirstTimestamp(List<Document> documents) {
        documents[0].get('timestamp')
    }

}
