package com.proper.enterprise.platform.integration.webapp.dal

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.proper.enterprise.platform.api.authc.entity.UserEntity
import com.proper.enterprise.platform.core.enums.ActiveStatus
import com.proper.enterprise.platform.core.enums.UseStatus
import com.proper.enterprise.platform.integration.IntegTest
import com.proper.enterprise.platform.integration.webapp.dal.repository.BaseDAOTestRepository

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
