package com.proper.enterprise.platform.oopsearch.api.service

import com.proper.enterprise.platform.oopsearch.api.conf.DemoDeptConfigs
import com.proper.enterprise.platform.oopsearch.api.document.SearchDocument
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel
import com.proper.enterprise.platform.oopsearch.api.repository.SyncMongoRepository
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService
import com.proper.enterprise.platform.oopsearch.api.service.impl.TestMongoDataSync
import com.proper.enterprise.platform.test.AbstractTest
import com.zaxxer.hikari.HikariConfig
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AbstractMongoDataSyncTest extends AbstractTest{

    @Autowired
    MongoDataSyncService mongoDataSyncService

    @Autowired
    SyncMongoRepository syncMongoRepository

    @Autowired
    HikariConfig hikariConfig

    @Autowired
    DemoDeptConfigs demoDeptConfigs

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
        TestMongoDataSync testMongoDataSync = new TestMongoDataSync()
        List<SearchColumnModel> searchColumnList = new ArrayList<>()
        def result = testMongoDataSync.getSearchColumnMongo(searchColumnList, "column")
        assert result == null

        SearchColumnModel searchColumnModel = new SearchColumnModel()
        searchColumnModel.setColumn("column")
        searchColumnList.add(searchColumnModel)
        def result2 = testMongoDataSync.getSearchColumnMongo(searchColumnList, "column")
        assert result2.getColumn() == "column"
    }

    @Test
    void testFullSync(){
        mongoDataSyncService.fullSynchronization()
    }
}
