package com.proper.enterprise.platform.integration.webapp.dal

import org.junit.Test

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
