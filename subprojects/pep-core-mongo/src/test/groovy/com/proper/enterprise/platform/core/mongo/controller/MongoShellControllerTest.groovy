package com.proper.enterprise.platform.core.mongo.controller
import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.mongo.dao.MongoDAO
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.bson.Document
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class MongoShellControllerTest extends AbstractTest {

    @Autowired
    MongoDAO mongoDAO

    private static final String COLLECTION_NAME = "MSCDOC"

    @After
    public void tearDown() {
        mongoDAO.drop(COLLECTION_NAME)
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
    public void queryTest() {
        int times = 5
        times.times {
            insertOne()
        }

        // 可能有历史数据，所以结果数可能大于 5 条
        assert toDocumentStrings("$URL?query=${encode("{year: ${DateUtil.currentYear} }")}").size() >= times
        assert toDocumentStrings("$URL?query=${encode("{year: ${DateUtil.currentYear} }")}&limit=3").size() == 3

        def max = getFirstTimestamp toDocumentStrings("$URL?query=${encode("{year: ${DateUtil.currentYear} }")}&sort=${encode("{timestamp: -1}")}&limit=3")
        def min = getFirstTimestamp toDocumentStrings("$URL?query=${encode("{year: ${DateUtil.currentYear} }")}&sort=${encode("{timestamp: 1}")}")
        assert max > min

        assert toDocumentStrings("$URL?query=").size() >= times
    }

    private static def encode(String url) {
        URLEncoder.encode(url, PEPConstants.DEFAULT_CHARSET.toString())
    }

    private def toDocumentStrings(String url) {
        def res = get(url, HttpStatus.OK).response.contentAsString
        JSONUtil.parse(res, List.class)
    }

    private static def getFirstTimestamp(List<String> strings) {
        Document.parse(strings[0]).get('timestamp')
    }

}
