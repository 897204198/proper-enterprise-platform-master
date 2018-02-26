package com.proper.enterprise.platform.search.demo.entity

import com.proper.enterprise.platform.search.demo.repository.DemoDeptRepository
import com.proper.enterprise.platform.search.demo.repository.DemoUserRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class DemoEntityTest extends AbstractTest{

    @Autowired
    private DemoUserRepository demoUserRepository

    @Autowired
    private DemoDeptRepository demoDeptRepository

    @Test
    void demoUserEntityTest() {
        DemoUserEntity demoUserEntity = new DemoUserEntity()
        demoUserEntity.setId("1")
        demoUserEntity.setUserId("001")
        demoUserEntity.setUserName("张三")
        demoUserEntity.setAge(33)
        demoUserEntity.setDeptId("003")
        demoUserEntity.setCreateTime("2018-01-03")

        demoUserRepository.save(demoUserEntity)
        List<DemoUserEntity> result = demoUserRepository.findAll()
        for (DemoUserEntity temp:result) {
            println(temp.getId())
            println(temp.getUserId())
            println(temp.getUserName())
            println(temp.getAge())
            println(temp.getDeptId())
            println(temp.getCreateTime())
        }
        assert result.size() == 1
    }

    @Test
    void demoDeptEntityTest() {
        DemoDeptEntity demoDeptEntity = new DemoDeptEntity()
        demoDeptEntity.setId("1")
        demoDeptEntity.setDeptId("001")
        demoDeptEntity.setDeptName("研发部")
        demoDeptEntity.setDeptDesc("负责公司产品研发")
        demoDeptEntity.setCreateTime("2018-01-03")
        demoDeptEntity.setDeptMemberCount(10)

        demoDeptRepository.save(demoDeptEntity)
        List<DemoDeptEntity> result = demoDeptRepository.findAll()
        for (DemoDeptEntity temp:result) {
            println(temp.getId())
            println(temp.getDeptId())
            println(temp.getDeptName())
            println(temp.getDeptDesc())
            println(temp.getCreateTime())
            println(temp.getDeptMemberCount())
        }
        assert result.size() == 1
    }
}
