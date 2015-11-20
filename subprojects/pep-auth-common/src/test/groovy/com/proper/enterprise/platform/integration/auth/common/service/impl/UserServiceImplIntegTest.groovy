package com.proper.enterprise.platform.integration.auth.service.impl
import com.proper.enterprise.platform.api.auth.Resource
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.integration.auth.InsertDataWorker
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser

class UserServiceImplIntegTest extends AbstractIntegTest {

    @Autowired
    InsertDataWorker worker
    
    @Autowired
    UserService userService;
    
    @Test
    public void getUserResources() {
        push(worker.getBeforeDMLs())
        executeSqls()
        
        Collection<Resource> resources = userService.getUserResourcesByUsername(worker.user1name)
        assert resources.size() == 10

        push(worker.getAfterDMLs())
        executeSqls()
    }

    @Test
    @WithMockUser("admin")
    public void getCurrentUser() {
        assert userService.getCurrentUser().getUsername() == 'admin'
    }

}
