package com.proper.enterprise.platform.auth.neo4j.service.impl

import com.proper.enterprise.platform.api.auth.service.AuthcService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AuthcServiceImplTest extends AbstractTest{

    @Autowired
    AuthcService authcService

    @Test
    void test(){
        Map<String, String> map = new HashMap<>()
        map.put("username", "name")
        map.put("pwd", "123")
        assert authcService.getPassword(map) == "123"
        assert authcService.getUsername(map) == "name"
    }
}
