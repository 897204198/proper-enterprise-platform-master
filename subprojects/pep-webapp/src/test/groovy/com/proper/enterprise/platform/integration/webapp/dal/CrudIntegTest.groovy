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
class CrudIntegTest {
    
    @Autowired
    BaseDAOTestRepository repository
    
    static String id
    
    @Test
    void crud() {
        create()
        retrieve()
        update()
        updateCheck()
        delete()
    }
    
    void create() {
        UserEntity user = new UserEntity()
        user.setCreateUserId('1')
        user.setLastModifyUserId('1')
        user.setLoginName('test')
        user.setAccount('testaccount')
        user.setPassword('testpassword')
        repository.save(user)
        id = user.getId()
    }
    
    void retrieve() {
        UserEntity user = repository.findOne(id)
        
        // check set values
        assert user.createUserId == '1'
        assert user.lastModifyUserId == '1'
        assert user.loginName == 'test'
        assert user.account == 'testaccount'
        assert user.password == 'testpassword'
        
        // check default values
        assert user.activeStatus == ActiveStatus.INACTIVE
        assert user.useStatus == UseStatus.STOP
        assert user.neverExpired == false
    }
    
    void update() {
        UserEntity user = repository.findOne(id)
        user.decription = 'desc of user'
        repository.save(user)
    }
    
    void updateCheck() {
        UserEntity user = repository.findOne(id)
        assert user.decription == 'desc of user'
    }

    void delete() {
        repository.delete(id)
    }
    
}
