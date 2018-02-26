package com.proper.enterprise.platform.search.demo.controller

import com.proper.enterprise.platform.search.common.repository.SearchMongoRepository
import com.proper.enterprise.platform.search.demo.entity.DemoUserEntity
import com.proper.enterprise.platform.search.demo.repository.DemoUserRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class SearchBaseControllerTest extends AbstractTest{

    @Autowired
    private DemoUserRepository demoUserRepository

    @Autowired
    private SearchMongoRepository searchMongoRepository

    @Test
    void testSyncMongoFromDB(){
        initDB()
        get("/search/init",  HttpStatus.OK)
        int count = searchMongoRepository.findAll().size()
        // 4 cols * 3 rows - 1 duplicated value
        assert count == 4 * 3 - 1
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
