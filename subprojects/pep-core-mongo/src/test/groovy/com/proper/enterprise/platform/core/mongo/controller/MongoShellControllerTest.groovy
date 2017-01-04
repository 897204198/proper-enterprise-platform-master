package com.proper.enterprise.platform.core.mongo.controller
import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.mongo.service.MongoShellService
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
    public void normalQuery() {
        int times = 5
        times.times {
            insertOne()
        }

        def query = "{year: ${DateUtil.currentYear} }"
        // 可能有历史数据，所以结果数可能大于 5 条
        def total = getDataTrunk("$URL?query=${encode(query)}").total
        assert total == service.query(COLLECTION_NAME, query).size()
        assert total >= times
        def size = getDataTrunk("$URL?query=${encode(query)}&limit=3").data.size()
        assert size == service.query(COLLECTION_NAME, query, 3).size()
        assert size == 3

        def max = getFirstTimestamp getDataTrunk("$URL?query=${encode(query)}&sort=${encode("{timestamp: -1}")}&limit=3").data
        def min = getFirstTimestamp getDataTrunk("$URL?query=${encode(query)}&sort=${encode("{timestamp: 1}")}").data
        assert max > min
        assert min == service.query(COLLECTION_NAME, query, "{timestamp: 1}")[0].get('timestamp')

        def allData = getDataTrunk("$URL?query=")
        assert allData.total == allData.total
        assert allData.total >= times
    }

    @Test
    public void noResultQuery() {
        def query = "{year: ${DateUtil.currentYear + 10} }"
        def dt = getDataTrunk("$URL?query=${encode(query)}")
        assert dt.getData() == []
        assert dt.total == 0
    }

    private static def encode(String url) {
        URLEncoder.encode(url, PEPConstants.DEFAULT_CHARSET.toString())
    }

    private def getDataTrunk(String url) {
        def res = get(url, HttpStatus.OK).response.contentAsString
        JSONUtil.parse(res, DataTrunk.class)
    }

    private static def getFirstTimestamp(List<String> strings) {
        Document.parse(strings[0]).get('timestamp')
    }

}
