package com.proper.enterprise.platform.oopsearch.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.proper.enterprise.platform.auth.jpa.entity.RoleEntity
import com.proper.enterprise.platform.auth.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.jpa.repository.RoleRepository
import com.proper.enterprise.platform.auth.jpa.repository.UserRepository
import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.oopsearch.api.serivce.QueryResultService
import com.proper.enterprise.platform.oopsearch.configs.UserRoleConfigTest
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MutiTableQueryResultServiceImplTest extends AbstractTest{

    @Autowired
    UserRoleConfigTest userRoleConfigTest

    @Autowired
    QueryResultService queryResultService

    @Autowired
    RoleRepository repository

    @Autowired
    UserRepository userRepository

    @Test
    void testSearchServiceImpl(){
        String moduleName = "userRoleConfigTest"
        String req = "[{\"key\":\"name\",\"value\":\"用户\",\"operate\":\"like\",\"table\":\"pep_auth_users\"}]"
        req = URLDecoder.decode(req, PEPConstants.DEFAULT_CHARSET.toString())
        ObjectMapper objectMapper = new ObjectMapper()
        JsonNode jn = objectMapper.readValue(req, JsonNode.class)
        List result = queryResultService.assemble(jn, moduleName)
        assert result.size() > 1

    }

    @Before
    void initData() {

        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName("操作员")
        repository.save(roleEntity)

        UserEntity userEntity1 = new UserEntity('user1', '123456')
        userEntity1.add(roleEntity)
        userEntity1.setName("用户1")
        userEntity1.setPhone("1860000000")
        userEntity1.setEmail("user1@propersoft.com")
        userRepository.save(userEntity1)

        UserEntity userEntity2 = new UserEntity('user2', '123456')
        userEntity2.add(roleEntity)
        userEntity2.setName("用户2")
        userEntity2.setPhone("1860000000")
        userEntity2.setEmail("user2@propersoft.com")
        userRepository.save(userEntity2)

        UserEntity userEntity3 = new UserEntity('user3', '123456')
        userEntity3.add(roleEntity)
        userEntity3.setName("用户3")
        userEntity3.setPhone("1860000000")
        userEntity3.setEmail("user3@propersoft.com")
        userRepository.save(userEntity3)

    }
}
