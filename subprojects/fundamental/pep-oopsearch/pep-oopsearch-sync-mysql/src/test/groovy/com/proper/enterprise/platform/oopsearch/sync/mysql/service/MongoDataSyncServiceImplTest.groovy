package com.proper.enterprise.platform.oopsearch.sync.mysql.service

import com.proper.enterprise.platform.oopsearch.api.enums.SyncMethod
import com.proper.enterprise.platform.oopsearch.document.SearchDocument
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel
import com.proper.enterprise.platform.oopsearch.api.model.SyncDocumentModel
import com.proper.enterprise.platform.oopsearch.repository.SyncMongoRepository
import com.proper.enterprise.platform.oopsearch.sync.SingleSync
import com.proper.enterprise.platform.oopsearch.sync.mysql.service.impl.MySQLMongoDataSync
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

@Ignore
class MongoDataSyncServiceImplTest extends AbstractJPATest{

    @Autowired
    SingleSync singleSync

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
        doc.setMethod(SyncMethod.INSERT)
        singleSync.singleSynchronization(doc)
        List<SearchDocument> result = syncMongoRepository.findAll()
        assert result.size() == 1

        doc.setConb("张一")
        doc.setCona("张二")
        doc.setMethod(SyncMethod.UPDATE)
        singleSync.singleSynchronization(doc)
        result = syncMongoRepository.findAll()
        SearchDocument searchDocument = result.get(0)
        assert searchDocument.getCon() == "张二"
        doc.setMethod(SyncMethod.DELETE)
        singleSync.singleSynchronization(doc)
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
