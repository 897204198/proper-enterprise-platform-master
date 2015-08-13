package com.proper.enterprise.platform.integration.webapp.dal

import org.junit.Test
import org.springframework.transaction.annotation.Transactional

class TxIntegTest extends Crud {
    
    @Test
    void normal() {
        int before = repository.count()
        create()
        assert repository.count() > before
    }
    
    @Test
    @Transactional(readOnly=true)
    void readonly() {
        int before = repository.count()
        create()
        assert repository.count() == before
    }

}
