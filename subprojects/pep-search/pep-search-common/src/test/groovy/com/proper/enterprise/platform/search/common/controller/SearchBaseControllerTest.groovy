package com.proper.enterprise.platform.search.common.controller

import com.proper.enterprise.platform.search.common.entity.TableEntityTest
import com.proper.enterprise.platform.search.common.repository.TableRepositoryTest
import com.proper.enterprise.platform.search.common.repository.SearchMongoRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class SearchBaseControllerTest extends AbstractTest{

    @Autowired
    private TableRepositoryTest repositoryTest

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
        TableEntityTest searchEntity = new TableEntityTest()
        searchEntity.setUserId("001")
        searchEntity.setUserName("张一")
        searchEntity.setAge(30)
        searchEntity.setDeptId("001")
        searchEntity.setCreateTime("2018-01-01")

        TableEntityTest searchEntity2 = new TableEntityTest()
        searchEntity2.setUserId("002")
        searchEntity2.setUserName("张二")
        searchEntity2.setAge(32)
        searchEntity2.setDeptId("002")
        searchEntity2.setCreateTime("2018-01-02")

        TableEntityTest searchEntity3 = new TableEntityTest()
        searchEntity3.setUserId("001")
        searchEntity3.setUserName("张三")
        searchEntity3.setAge(33)
        searchEntity3.setDeptId("003")
        searchEntity3.setCreateTime("2018-01-03")

        repositoryTest.save(searchEntity)
        repositoryTest.save(searchEntity2)
        repositoryTest.save(searchEntity3)
    }
}
