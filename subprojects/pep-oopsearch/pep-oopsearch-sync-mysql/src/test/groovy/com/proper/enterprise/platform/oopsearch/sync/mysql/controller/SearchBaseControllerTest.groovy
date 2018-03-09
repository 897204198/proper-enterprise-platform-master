package com.proper.enterprise.platform.oopsearch.sync.mysql.controller

import com.proper.enterprise.platform.oopsearch.sync.mysql.entity.DemoDeptEntity
import com.proper.enterprise.platform.oopsearch.sync.mysql.repository.DemoDeptRepository
import com.proper.enterprise.platform.oopsearch.sync.mysql.repository.SyncMongoRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class SearchBaseControllerTest extends AbstractTest{

    @Autowired
    private DemoDeptRepository demoDeptRepository

    @Autowired
    private SyncMongoRepository syncMongoRepository

    @Test
    void testSyncMongoFromDB(){
        initDB()
        get("/search/init",  HttpStatus.OK)
        int count = syncMongoRepository.findAll().size()
        // 5 cols * 3 rows - 2 duplicated value
        assert count == 5 * 3 - 1
    }

    void initDB(){
        DemoDeptEntity searchEntity = new DemoDeptEntity()
        searchEntity.setDeptId("001")
        searchEntity.setDeptName("研发部")
        searchEntity.setDeptDesc("产品研发")
        searchEntity.setCreateTime("2018-01-01")
        searchEntity.setDeptMemberCount(10)

        demoDeptRepository.save(searchEntity)

        DemoDeptEntity searchEntity2 = new DemoDeptEntity()
        searchEntity2.setDeptId("002")
        searchEntity2.setDeptName("实施部")
        // 与dept_id字段内容重复，但分属不同字段，仍然会插入mongodb中
        searchEntity2.setDeptDesc("001")
        searchEntity2.setCreateTime("2018-01-02")
        searchEntity2.setDeptMemberCount(20)

        demoDeptRepository.save(searchEntity2)

        DemoDeptEntity searchEntity3 = new DemoDeptEntity()
        // 与dept_id字段内容重复，且字段相同，不插入mongodb
        searchEntity3.setDeptId("001")
        searchEntity3.setDeptName("销售部")
        searchEntity3.setDeptDesc("产品销售")
        searchEntity3.setCreateTime("2018-01-03")
        searchEntity3.setDeptMemberCount(30)

        demoDeptRepository.save(searchEntity3)
    }
}
