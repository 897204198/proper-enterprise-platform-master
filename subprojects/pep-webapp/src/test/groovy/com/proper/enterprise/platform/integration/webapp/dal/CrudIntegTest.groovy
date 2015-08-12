package com.proper.enterprise.platform.integration.webapp.dal

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.proper.enterprise.platform.test.integration.IntegTest;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegTest
class CrudIntegTest extends Crud {
    
    @Test
    void crud() {
        create()
        retrieve()
        update()
        updateCheck()
        delete()
    }
    
}
