package com.proper.enterprise.platform.auth.common.mongo

import com.proper.enterprise.platform.core.mongo.dao.MongoDAO

import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.bson.Document
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.jdbc.Sql

class DataRestrainMongoDAOImplTest extends AbstractTest {

    @Autowired
    @Qualifier("dataRestrainMongoDAOImpl")
    MongoDAO mongoDAO

    private static final String COLLECTION_NAME = "drmongodao"

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

        def result = mongoDAO.deleteByIds(COLLECTION_NAME, [getId(doc1), getId(doc2), '5823f6b4c9e77c0001ad0b72'] as String[])
        assert result.size() == 2
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
        assert result.size() == mongoDAO.count(COLLECTION_NAME, null)
        assert result[0].get('a') == 4
        assert result[0].get('b') == 1

        assert mongoDAO.count(COLLECTION_NAME, '{$where: "this.a < this.b"}') == 0
    }

}
