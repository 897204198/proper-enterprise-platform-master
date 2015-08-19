package com.proper.enterprise.platform.integration.webapp.dal

import org.junit.Test

class CrudIntegTest extends CrudBaseIntegTest {
    
    @Test
    void crud() {
        create()
        retrieve()
        update()
        updateCheck()
        delete()
    }
    
}
