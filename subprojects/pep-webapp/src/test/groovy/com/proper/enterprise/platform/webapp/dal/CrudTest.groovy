package com.proper.enterprise.platform.webapp.dal

import org.junit.Test

class CrudTest extends CrudBaseTest {
    
    @Test
    void crud() {
        create()
        retrieve()
        update()
        updateCheck()
        delete()
    }
    
}
