package com.proper.enterprise.platform.auth.common.neo4j.service.impl

import com.proper.enterprise.platform.api.auth.service.AuthcService
import com.proper.enterprise.platform.api.auth.service.PasswordEncryptService
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserNodeRepository
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AuthcServiceImplTest extends AbstractNeo4jTest{

    @Autowired
    AuthcService authcService
    @Autowired
    UserNodeRepository userNodeRepository;
    @Autowired
    PasswordEncryptService pwdService;

    @Test
    void test(){
        Map<String, String> map = new HashMap<>()
        map.put("username", "name")
        map.put("pwd", "123")
        assert authcService.getPassword(map) == "123"
        assert authcService.getUsername(map) == "name"

        UserNodeEntity userVO = new UserNodeEntity()
        userVO.setUsername("name")
        userVO.setPassword("123")
        userNodeRepository.save(userVO)
        authcService.authenticate(userVO.getUsername(), pwdService.encrypt(userVO.getPassword()))
    }
}
