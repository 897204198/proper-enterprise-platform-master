package com.proper.enterprise.platform.integration.auth.common.service.impl

import com.proper.enterprise.platform.api.auth.Resource
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.integration.auth.common.InsertDataWorker
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser

class ResourceServiceImplIntegTest extends AbstractIntegTest {

    @Autowired
    InsertDataWorker worker

    @Autowired
    ResourceService service

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
    // mock with user1 defined in InsertDataWorker
    @WithMockUser('hinex1')
    public void getAllResourcesofCurrentUser() {
        Set<Resource> resources = service.getAllResourcesOfCurrentUser()
        assert resources.size() == 10
    }

}
