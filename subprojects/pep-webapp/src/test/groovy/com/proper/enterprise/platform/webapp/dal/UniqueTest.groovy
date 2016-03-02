package com.proper.enterprise.platform.webapp.dal

import org.junit.Test
import org.springframework.dao.DataIntegrityViolationException

class UniqueTest extends CrudBaseTest {
    
    @Test(expected=DataIntegrityViolationException)
    void duplicateUniqueCol() {
        create()
        create()
        
        repository.findAll()
    }
    
}
