package com.proper.enterprise.platform.oopsearch.service

import com.proper.enterprise.platform.oopsearch.document.OOPSearchDocument
import com.proper.enterprise.platform.oopsearch.config.repository.SearchConfigRepository
import com.proper.enterprise.platform.oopsearch.repository.SyncMongoRepository
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService
import com.proper.enterprise.platform.oopsearch.entity.DemoUserEntity
import com.proper.enterprise.platform.oopsearch.repository.DemoUserRepository
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

import java.text.SimpleDateFormat

class SearchServiceImplTest extends AbstractJPATest{

    @Autowired
    SearchService searchService

    @Autowired
    DemoUserRepository demoUserRepository

    @Autowired
    SearchConfigRepository searchConfigRepository

    @Autowired
    private MongoDataSyncService mongoDataSyncService

    @Autowired
    private SyncMongoRepository syncMongoRepository

    @Test
    @NoTx
    @Sql("/sql/oopsearch/demoUserConfigData.sql")
    void testSearchServiceImpl(){
        initDemoUserData()
        // 调用模拟h2同步服务的全量同步方法
        mongoDataSyncService.fullSynchronization()
        // 进行逆向查询
        List<OOPSearchDocument> resultList = searchService.getSearchInfo("张", "demouser")
        assert resultList.size() == 3

        String date = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        resultList = searchService.getSearchInfo(date, "demouser")
        // 去年 今年 明年 + 1row(人员创建时间为同一天，mongo中只有1条)
        assert resultList.size() == 3 + 1

        date = new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime());
        resultList = searchService.getSearchInfo(date, "demouser")
        // 上月 本月 下月 + 3rows
        assert resultList.size() == 3 + 1


        date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        resultList = searchService.getSearchInfo(date, "demouser")
        // 昨天 今天 明天 + 1row
        assert resultList.size() == 3 + 1

        searchConfigRepository.deleteAll()
        demoUserRepository.deleteAll()
    }

    @Before
    void initMongo(){
        syncMongoRepository.deleteAll()
    }


//    @Before
//    void initConfig(){
//        initDemoUserConfig()
//        initDemoUserData()
//    }

//    void initDemoUserConfig(){
//        SearchConfigEntity searchConfigEntity = new SearchConfigEntity()
//        searchConfigEntity.setModuleName("demouser")
//        searchConfigEntity.setTableName("demo_user")
//        searchConfigEntity.setSearchColumn("user_id")
//        searchConfigEntity.setColumnType("string")
//        searchConfigEntity.setColumnDesc("用户id")
//        searchConfigEntity.setColumnAlias("demouser_user_id")
//        searchConfigEntity.setUrl("/demouser")
//
//        searchConfigRepository.save(searchConfigEntity)
//
//        SearchConfigEntity searchConfigEntity2 = new SearchConfigEntity()
//        searchConfigEntity2.setModuleName("demouser")
//        searchConfigEntity2.setTableName("demo_user")
//        searchConfigEntity2.setSearchColumn("user_name")
//        searchConfigEntity2.setColumnType("string")
//        searchConfigEntity2.setColumnDesc("用户名")
//        searchConfigEntity2.setColumnAlias("demouser_user_name")
//        searchConfigEntity2.setUrl("/demouser")
//
//        searchConfigRepository.save(searchConfigEntity2)
//
//        SearchConfigEntity searchConfigEntity3 = new SearchConfigEntity()
//        searchConfigEntity3.setModuleName("demouser")
//        searchConfigEntity3.setTableName("demo_user")
//        searchConfigEntity3.setSearchColumn("age")
//        searchConfigEntity3.setColumnType("int")
//        searchConfigEntity3.setColumnDesc("年龄")
//        searchConfigEntity3.setColumnAlias("demouser_age")
//        searchConfigEntity3.setUrl("/demouser")
//
//        searchConfigRepository.save(searchConfigEntity3)
//
//        SearchConfigEntity searchConfigEntity4 = new SearchConfigEntity()
//        searchConfigEntity4.setModuleName("demouser")
//        searchConfigEntity4.setTableName("demo_user")
//        searchConfigEntity4.setSearchColumn("create_time")
//        searchConfigEntity4.setColumnType("date")
//        searchConfigEntity4.setColumnDesc("人员创建时间")
//        searchConfigEntity4.setColumnAlias("demouser_create_time")
//        searchConfigEntity4.setUrl("/demouser")
//
//        searchConfigRepository.save(searchConfigEntity4)
//    }

    void initDemoUserData() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())

        DemoUserEntity demoUserEntity = new DemoUserEntity()
        demoUserEntity.setUserId("001")
        demoUserEntity.setUserName("张一")
        demoUserEntity.setAge(30)
        demoUserEntity.setDeptId("001")
        demoUserEntity.setCreateTime(date)

        DemoUserEntity demoUserEntity2 = new DemoUserEntity()
        demoUserEntity2.setUserId("002")
        demoUserEntity2.setUserName("张二")
        demoUserEntity2.setAge(32)
        demoUserEntity2.setDeptId("002")
        demoUserEntity2.setCreateTime(date)

        DemoUserEntity demoUserEntity3 = new DemoUserEntity()
        demoUserEntity3.setUserId("003")
        demoUserEntity3.setUserName("张三")
        demoUserEntity3.setAge(33)
        demoUserEntity3.setDeptId("003")
        demoUserEntity3.setCreateTime(date)

        demoUserRepository.save(demoUserEntity)
        demoUserRepository.save(demoUserEntity2)
        demoUserRepository.save(demoUserEntity3)

    }
}
