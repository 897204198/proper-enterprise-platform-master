package com.proper.enterprise.platform.integration.webapp.dal

import org.junit.Test
import org.springframework.dao.DataIntegrityViolationException

class UniqueIntegTest extends Crud {
    
    @Test(expected=DataIntegrityViolationException)
    void duplicateUniqueCol() {
        create()
        create()
        
        repository.findAll()
    }
    
}
