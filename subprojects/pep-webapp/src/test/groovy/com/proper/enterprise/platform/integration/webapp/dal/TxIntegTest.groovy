package com.proper.enterprise.platform.integration.webapp.dal

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

import com.proper.enterprise.platform.api.authc.entity.UserEntity
import com.proper.enterprise.platform.integration.IntegTest
import com.proper.enterprise.platform.integration.webapp.dal.repository.BaseDAOTestRepository

@RunWith(SpringJUnit4ClassRunner.class)
@IntegTest
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
