package com.proper.enterprise.platform.integration.auth.service.impl

import com.proper.enterprise.platform.api.auth.Resource
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.integration.auth.InsertDataWorker
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class UserServiceImplIntegTest extends AbstractIntegTest {

    @Autowired
    InsertDataWorker worker
    
    @Autowired
    UserService userService;
    
    @Test
    public void getUserResources() {
        worker.insertData()
        
        Collection<Resource> resources = userService.getUserResourcesByUsername(worker.user1name)
        assert resources.size() == 10
    }

}
