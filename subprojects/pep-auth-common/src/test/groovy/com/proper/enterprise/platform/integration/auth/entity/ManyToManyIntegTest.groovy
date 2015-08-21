package com.proper.enterprise.platform.integration.auth.entity

import com.proper.enterprise.platform.auth.entity.RoleEntity
import com.proper.enterprise.platform.auth.entity.UserEntity
import com.proper.enterprise.platform.auth.repository.RoleRepository
import com.proper.enterprise.platform.auth.repository.UserRepository
import com.proper.enterprise.platform.integration.auth.InsertDataWorker
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class ManyToManyIntegTest extends AbstractIntegTest {

    @Autowired
    InsertDataWorker worker

    @Autowired
    UserRepository userRepo

    @Autowired
    RoleRepository roleRepo

    @PersistenceContext
    private EntityManager em

    @Before
    public void setUp() {
        worker.insertData()
    }

    @Test
    public void getRoleFromUser() {
        UserEntity user = userRepo.findByLoginName(worker.username)
        em.refresh(user)
        assert user.roles.size() == 2
        user.roles.each {
            assert it.users.contains(user)
        }
    }

    @Test
    public void getUserFromRole() {
        RoleEntity role = roleRepo.findByCode('roleA')
        em.refresh(role)
        assert role.users.size() == 1
        role.users.each {
            assert it.loginName == worker.username
        }
    }

}
