package com.proper.enterprise.platform.search.demo.controller

import com.proper.enterprise.platform.search.api.document.SearchColumn
import com.proper.enterprise.platform.search.api.serivce.MongoDataSyncService
import com.proper.enterprise.platform.search.common.document.SearchDocument
import com.proper.enterprise.platform.search.demo.DemoUserConfigs
import com.proper.enterprise.platform.search.demo.entity.DemoUserEntity
import com.proper.enterprise.platform.search.demo.repository.DemoDeptRepository
import com.proper.enterprise.platform.search.demo.repository.DemoUserRepository
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class DemoUserControllerTest extends AbstractTest{

    @Autowired
    private MongoDataSyncService mongoDataSyncService

    @Autowired
    private DemoUserRepository demoUserRepository

    @Autowired
    private DemoUserConfigs demoUserConfigs

    @Autowired
    private DemoDeptRepository demoDeptRepository

    @Test
    void testSyncMongoFromDB(){
        initDB()
        get("/search/init",  HttpStatus.OK)
        int count = mongoDataSyncService.findAll().size()
        assert count > 0
    }

    @Test
    void testUserSearchInfo(){
        initDB()
        get("/search/init",  HttpStatus.OK)
        String input = "张"
        get("/search/init",  HttpStatus.OK)
        String resultContent = get("/search/user?data=" + input,  HttpStatus.OK).getResponse().getContentAsString()
        List<SearchColumn> resultList = (List<SearchColumn>)JSONUtil.parse(resultContent,Object.class)
        for (SearchDocument document:resultList){
            assert document.getCon().contains(input)
        }
        assert resultList.size() <= demoUserConfigs.getLimit()
    }

    void initDB(){
        DemoUserEntity searchEntity = new DemoUserEntity()
        searchEntity.setUserId("001")
        searchEntity.setUserName("张一")
        searchEntity.setAge(30)
        searchEntity.setDeptId("001")
        searchEntity.setCreateTime("2018-01-01")

        DemoUserEntity searchEntity2 = new DemoUserEntity()
        searchEntity2.setUserId("002")
        searchEntity2.setUserName("张二")
        searchEntity2.setAge(32)
        searchEntity2.setDeptId("002")
        searchEntity2.setCreateTime("2018-01-02")

        DemoUserEntity searchEntity3 = new DemoUserEntity()
        searchEntity3.setUserId("001")
        searchEntity3.setUserName("张三")
        searchEntity3.setAge(33)
        searchEntity3.setDeptId("003")
        searchEntity3.setCreateTime("2018-01-03")

        demoUserRepository.save(searchEntity)
        demoUserRepository.save(searchEntity2)
        demoUserRepository.save(searchEntity3)
    }
}
