package com.proper.enterprise.platform.auth.common.mongo

import com.proper.enterprise.platform.api.auth.common.mongo.DataRestrainMongoDAO
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.bson.Document
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
/**
 * 本测试依赖 mongodb 测试服务
 * 需要在 host 中配置 mongo-pep（${mongodb.host}）指向测试服务地址
 */
class DataRestrainMongoDAOImplTest extends AbstractTest {

    @Autowired
    DataRestrainMongoDAO mongoDAO

    private static final String COLLECTION_NAME = "mongodao"

    @After
    public void tearDown() {
        mongoDAO.drop(COLLECTION_NAME)
    }

    @Test
    public void deleteById() {
        def doc = insertOne()
        def id = getId doc
        mongoDAO.deleteById(COLLECTION_NAME, id)
        assert query(id) == null
    }

    private Document insertOne(doc = [year: DateUtil.currentYear, timestamp: DateUtil.getTimestamp(true)]) {
        mongoDAO.insertOne(COLLECTION_NAME, JSONUtil.toJSON(doc))
    }

    private static String getId(Document doc) {
        doc.getObjectId('_id').toString()
    }

    private Document query(String id) {
        mongoDAO.queryById(COLLECTION_NAME, id)
    }

    @Test
    public void deleteByIds() {
        def doc1 = insertOne()
        def doc2 = insertOne()

        mongoDAO.deleteByIds(COLLECTION_NAME, [getId(doc1), getId(doc2)] as String[])
        assert query(getId(doc1)) == null
        assert query(getId(doc2)) == null
    }

    @Test
    public void updateById() {
        def doc = insertOne()
        mongoDAO.updateById(COLLECTION_NAME, getId(doc), '{ $set: {abc: 123, def: 456}, $unset: {year: "", notexist: ""}}')
        def newDoc = query(getId(doc))
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
        assert mongoDAO.query(COLLECTION_NAME, "{year: ${DateUtil.currentYear} }").size() >= times
        assert mongoDAO.query(COLLECTION_NAME, "{year: ${DateUtil.currentYear} }", 3).size() == 3

        def max = mongoDAO.query(COLLECTION_NAME, "{year: ${DateUtil.currentYear} }", "{timestamp: -1}")[0].get('timestamp')
        def min = mongoDAO.query(COLLECTION_NAME, "{year: ${DateUtil.currentYear} }", "{timestamp: 1}")[0].get('timestamp')
        assert max > min
    }

    @Test
    @Sql
    public void sqlDataRestrains() {
        mockRequest.setRequestURI('/platform/auth/security')
        mockRequest.setMethod('GET')

        int times = 5
        times.times { idx ->
            insertOne([a: idx, b: times - idx])
        }
        def result = mongoDAO.query(COLLECTION_NAME, null)
        assert result.size() == 1
        assert result[0].get('a') == 4
        assert result[0].get('b') == 1
    }

}
