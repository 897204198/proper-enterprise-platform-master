package com.proper.enterprise.platform.integration.auth.service.impl

import com.proper.enterprise.platform.api.auth.Resource
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.test.auth.InsertDataWorker
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser

class UserServiceImplIntegTest extends AbstractIntegTest {

    @Autowired
    InsertDataWorker worker
    
    @Autowired
    UserService userService;

    @Before
    public void setUp() {
        push(worker.getBeforeDMLs())
        executeSqls()
    }

    @After
    public void tearDown() {
        push(worker.getAfterDMLs())
        executeSqls()
    }
    
    @Test
    public void getUserResources() {
        Collection<Resource> resources = userService.getResources(worker.user1name)
        assert resources.size() == 10
    }

    @Test
    @WithMockUser("admin")
    public void getCurrentUser() {
        assert userService.getCurrentUser().getUsername() == 'admin'
    }

    @Test
    // mock with user1 defined in InsertDataWorker
    @WithMockUser('hinex1')
    public void getAllResourcesofCurrentUser() {
        Collection<Resource> resources = userService.getResources()
        assert resources.size() == 10
    }

}
