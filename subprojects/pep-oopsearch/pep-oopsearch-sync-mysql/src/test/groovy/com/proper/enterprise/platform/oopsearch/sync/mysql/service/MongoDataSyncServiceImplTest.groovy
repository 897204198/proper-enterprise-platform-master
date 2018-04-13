package com.proper.enterprise.platform.oopsearch.sync.mysql.service

import com.proper.enterprise.platform.oopsearch.api.document.SearchDocument
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel
import com.proper.enterprise.platform.oopsearch.api.repository.SyncMongoRepository
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService
import com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl.MySQLMongoDataSync
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MongoDataSyncServiceImplTest extends AbstractTest{

    @Autowired
    MongoDataSyncService mongoDataSyncService

    @Autowired
    SyncMongoRepository syncMongoRepository

//    @Autowired
//    MongoDataSyncServiceImpl mongoDataSyncServiceImpl

    @Test
    void test(){
        syncMongoRepository.deleteAll()

        SyncDocumentModel doc = new SyncDocumentModel()
        doc.setTab("test")
        doc.setPri("1")
        doc.setCona("张一")
        doc.setCol("username")
        doc.setDes("用户名")
        mongoDataSyncService.singleSynchronization(doc, "insert")
        List<SearchDocument> result = syncMongoRepository.findAll()
        assert result.size() == 1

        doc.setConb("张一")
        doc.setCona("张二")
        mongoDataSyncService.singleSynchronization(doc, "update")
        result = syncMongoRepository.findAll()
        SearchDocument searchDocument = result.get(0)
        assert searchDocument.getCon() == "张二"

        mongoDataSyncService.singleSynchronization(doc, "delete")
        result = syncMongoRepository.findAll()
        assert result.size() == 0
    }

    @Test
    void testGetSearchColumnMongo(){
        MySQLMongoDataSync mongoDataSyncServiceImpl = new MySQLMongoDataSync()
        List<SearchColumnModel> searchColumnList = new ArrayList<>()
        def result = mongoDataSyncServiceImpl.getSearchColumnMongo(searchColumnList, "column")
        assert result == null
//        String oldUrl = hikariConfig.getDataSourceProperties().get("url")
//        String newUrl = "jdbc:mysql://localhost:3306/pep_test"
//        hikariConfig.addDataSourceProperty("url",newUrl)
//        def result = mongoDataSyncServiceImpl.getPrimaryKeys("test_table")
//        println result.size()
    }
}
