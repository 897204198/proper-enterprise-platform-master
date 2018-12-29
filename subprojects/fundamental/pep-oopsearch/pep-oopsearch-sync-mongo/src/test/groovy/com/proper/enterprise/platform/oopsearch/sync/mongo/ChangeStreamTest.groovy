package com.proper.enterprise.platform.oopsearch.sync.mongo

import com.mongodb.MongoClient
import com.mongodb.client.model.Filters
import com.proper.enterprise.platform.core.mongo.dao.MongoDAO
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType
import com.proper.enterprise.platform.oopsearch.config.entity.SearchConfigEntity
import com.proper.enterprise.platform.oopsearch.config.repository.SearchConfigRepository
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService
import com.proper.enterprise.platform.oopsearch.sync.mongo.monitor.OplogMonitor
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.bson.Document
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mongo.MongoProperties

@Ignore
class ChangeStreamTest extends AbstractJPATest {

    public static final String TEST_COLLECTION = "mongo_sync_test"

    public static final String SEARCH_COL = "search_column"

    @Autowired
    MongoClient mongoClient

    @Autowired
    MongoProperties mongoProperties

    @Autowired
    MongoDAO mongoDAO

    @Autowired
    OplogMonitor oplogMonitor

    @Autowired
    private SearchConfigRepository searchConfigRepository;

    @Autowired
    SearchConfigService searchConfigService;

    private String insertConfig() {
        SearchConfigEntity searchConfigEntity = new SearchConfigEntity()
        searchConfigEntity.setModuleName("mongoSyncTest")
        searchConfigEntity.setTableName("mongo_sync_test")
        searchConfigEntity.setSearchColumn("name")
        searchConfigEntity.setColumnType("string")
        searchConfigEntity.setColumnDesc("名称")
        searchConfigEntity.setColumnAlias("demotest_name")
        searchConfigEntity.setUrl("/demotest")
        searchConfigEntity.setDataBaseType(DataBaseType.MONGODB)
        SearchConfigEntity save = searchConfigRepository.save(searchConfigEntity)
        return save.getId()
    }

    @Test
    @NoTx
    public void testTargetCollectionInsert() {
        def id = insertConfig()
        assert mongoDAO.query(SEARCH_COL, "{'tab':'" + TEST_COLLECTION + "','name':'name'}").size() == 0
        oplogMonitor.start(TEST_COLLECTION)
        Document insertDoc = new Document()
        insertDoc.put("name", "name")
        mongoClient
            .getDatabase(mongoProperties.getDatabase())
            .getCollection(TEST_COLLECTION)
            .insertOne(insertDoc)
        sleep(5000)
        assert mongoDAO.query(SEARCH_COL, "{'tab':'" + TEST_COLLECTION + "','con':'name'}").size() == 1
        searchConfigRepository.deleteById(id)
    }

    @Test
    @NoTx
    public void testTargetCollectionDelete() {
        def id = insertConfig()
        oplogMonitor.start(TEST_COLLECTION)
        Document delDoc = new Document()
        delDoc.put("name", "name")
        mongoClient
            .getDatabase(mongoProperties.getDatabase())
            .getCollection(TEST_COLLECTION)
            .insertOne(delDoc)
        sleep(5000)
        assert mongoDAO.query(SEARCH_COL, "{'tab':'" + TEST_COLLECTION + "','con':'name'}").size() == 1

        Document document = mongoClient
            .getDatabase(mongoProperties.getDatabase())
            .getCollection(TEST_COLLECTION)
            .findOneAndDelete(Filters.eq("name", "name"))

        sleep(5000)
        assert mongoDAO.query(SEARCH_COL, "{'tab':'" + TEST_COLLECTION + "','con':'name'}").size() == 0
        searchConfigRepository.deleteById(id)
    }

    @Test
    @NoTx
    public void testTargetCollectionUpdate() {
        def id = insertConfig()
        oplogMonitor.start(TEST_COLLECTION)
        Document updateDoc = new Document()
        updateDoc.put("name", "name")
        mongoClient
            .getDatabase(mongoProperties.getDatabase())
            .getCollection(TEST_COLLECTION)
            .insertOne(updateDoc)
        sleep(5000)
        assert mongoDAO.query(SEARCH_COL, "{'tab':'" + TEST_COLLECTION + "','con':'name'}").size() == 1


        mongoClient
            .getDatabase(mongoProperties.getDatabase())
            .getCollection(TEST_COLLECTION)
            .findOneAndUpdate(Filters.eq("_id", updateDoc.getObjectId("_id")), Document.parse('{$set:{name:"update2"}}'))
        sleep(5000)
        assert mongoDAO.query(SEARCH_COL, "{'tab':'" + TEST_COLLECTION + "','con':'name'}").size() == 0
        assert mongoDAO.query(SEARCH_COL, "{'tab':'" + TEST_COLLECTION + "','con':'update2'}").size() == 1
        searchConfigRepository.deleteById(id)
    }
}
