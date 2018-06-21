package com.proper.enterprise.platform.oopsearch.controller

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.oopsearch.config.repository.SearchConfigRepository
import com.proper.enterprise.platform.oopsearch.repository.SyncMongoRepository
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService
import com.proper.enterprise.platform.oopsearch.entity.DemoDeptEntity
import com.proper.enterprise.platform.oopsearch.entity.DemoUserEntity
import com.proper.enterprise.platform.oopsearch.repository.DemoDeptRepository
import com.proper.enterprise.platform.oopsearch.repository.DemoUserRepository
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import java.text.SimpleDateFormat

class OopSearchControllerTest extends AbstractTest {

    @Autowired
    private MongoDataSyncService mongoDataSyncService

    @Autowired
    DemoUserRepository demoUserRepository

    @Autowired
    DemoDeptRepository demoDeptRepository

    @Autowired
    SearchConfigRepository searchConfigRepository

    @Autowired
    SyncMongoRepository syncMongoRepository

    @Test
    @NoTx
    @Sql([
        "/sql/oopsearch/demoUserConfigData.sql",
        "/sql/oopsearch/demoDeptConfigData.sql",
        "/sql/oopsearch/testControllerEntity.sql"
    ])
    void testSearchServiceImpl() {
        initDemoUserData()
        initDemoDeptData()
        //初始化mysql到mongo
        mongoDataSyncService.fullSynchronization()

        String moduleName = "demouser"

        //mongo反向查询
        def query = JSONUtil.parse(get("/search/inverse?data=张&moduleName=" + moduleName, HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)
        // 张一 张二 张三
        assert query.size() == 3

        String restPath = java.net.URLEncoder.encode("{\"myName2\":\"myName\",\"id\":\"123456\"}", "UTF-8")
        String req = java.net.URLEncoder.encode("[{\"key\":\"createTime\",\"value\":\"2018-05-05\",\"operate\":\"like\",\"table\":\"TEST_CONTROLLER_ENTITY\"}]", "UTF-8")
        //rest参数没对上404
        get("/search/query" + "?restPath=" + restPath + "&req=" + req + "&moduleName=testcontroller", HttpStatus.NOT_FOUND)
        //req解析异常
        String restPath3 = java.net.URLEncoder.encode("{\"myName\":\"myName\",\"id\":\"123456\"}", "UTF-8")
        String req3 = java.net.URLEncoder.encode("REQ: [{\"key\":\"createTime\",\"value\":\"2018-05-05\",\"operate\":\"like\",\"table\":\"TEST_CONTROLLER_ENTITY\"}]", "GBK")
        get("/search/query" + "?restPath=" + restPath3 + "&req=" + req3 + "&moduleName=testcontroller", HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()=="oopSearch config req error"

        //restPath解析异常
        String restPath4 = java.net.URLEncoder.encode("{\"myName\":\"myName\",\"id\":\"123456", "UTF-8")
        String req4 = java.net.URLEncoder.encode("REQ: [{\"key\":\"createTime\",\"value\":\"2018-05-05\",\"operate\":\"like\",\"table\":\"TEST_CONTROLLER_ENTITY\"}]", "GBK")
        get("/search/query" + "?restPath=" + restPath4 + "&req=" + req4 + "&moduleName=testcontroller", HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()=="oopSearch parse restPath error"

        searchConfigRepository.deleteAll()
        demoUserRepository.deleteAll()
        demoDeptRepository.deleteAll()
    }

    @Before
    void initMongo() {
        syncMongoRepository.deleteAll()
    }


    void initDemoUserData() {
        DemoUserEntity demoUserEntity = new DemoUserEntity()
        demoUserEntity.setUserId("001")
        demoUserEntity.setUserName("张一")
        demoUserEntity.setAge(30)
        demoUserEntity.setDeptId("001")
        demoUserEntity.setCreateTime("2018-01-01")

        DemoUserEntity demoUserEntity2 = new DemoUserEntity()
        demoUserEntity2.setUserId("002")
        demoUserEntity2.setUserName("张二")
        demoUserEntity2.setAge(32)
        demoUserEntity2.setDeptId("002")
        demoUserEntity2.setCreateTime("2018-01-02")

        DemoUserEntity demoUserEntity3 = new DemoUserEntity()
        demoUserEntity3.setUserId("003")
        demoUserEntity3.setUserName("张三")
        demoUserEntity3.setAge(33)
        demoUserEntity3.setDeptId("003")
        demoUserEntity3.setCreateTime("2018-02-03")

        demoUserRepository.save(demoUserEntity)
        demoUserRepository.save(demoUserEntity2)
        demoUserRepository.save(demoUserEntity3)
    }

    void initDemoDeptData() {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(Calendar.getInstance().getTime())

        DemoDeptEntity demoDeptEntity = new DemoDeptEntity()
        demoDeptEntity.setDeptId("001")
        demoDeptEntity.setDeptName("研发部")
        demoDeptEntity.setDeptDesc("产品研发")
        demoDeptEntity.setCreateTime(date)
        demoDeptEntity.setDeptMemberCount(10)

        DemoDeptEntity demoDeptEntity2 = new DemoDeptEntity()
        demoDeptEntity2.setDeptId("002")
        demoDeptEntity2.setDeptName("实施部")
        demoDeptEntity2.setDeptDesc("产品实施")
        demoDeptEntity2.setCreateTime(date)
        demoDeptEntity2.setDeptMemberCount(20)

        DemoDeptEntity demoDeptEntity3 = new DemoDeptEntity()
        demoDeptEntity3.setDeptId("003")
        demoDeptEntity3.setDeptName("销售部")
        demoDeptEntity3.setDeptDesc("产品销售")
        demoDeptEntity3.setCreateTime(date)
        demoDeptEntity3.setDeptMemberCount(30)

        demoDeptRepository.save(demoDeptEntity)
        demoDeptRepository.save(demoDeptEntity2)
        demoDeptRepository.save(demoDeptEntity3)
    }

}
