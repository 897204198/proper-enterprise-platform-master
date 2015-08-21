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
        em.flush()
    }

    @Test
    public void getRoleFromUser() {
        UserEntity user = userRepo.findByLoginName(worker.user1name)
        em.refresh(user)
        assert user.roles.size() == 2
        user.roles.each {
            assert it.users.contains(user)
        }
    }

    @Test
    public void getUserFromRole() {
        RoleEntity role = roleRepo.findByCode(worker.roleAcode)
        em.refresh(role)
        assert role.users.size() == 2
    }

    @Test
    public void removeUser1() {
        UserEntity user = userRepo.findByLoginName(worker.user1name)
        userRepo.delete(user.id)
        assert userRepo.findOne(user.id) == null
        em.flush()

        RoleEntity roleA = roleRepo.findByCode(worker.roleAcode)
        em.refresh(roleA)
        assert roleA.users.size() == 1
        assert roleA.users.getAt(0).loginName == worker.user2name
    }

    @Test
    public void removeRoleADirectlyFailed() {
        RoleEntity roleA = roleRepo.findByCode(worker.roleAcode)
        roleRepo.delete(roleA.id)

        assert roleRepo.findByCode(worker.roleAcode) != null
        assert userRepo.findByLoginName(worker.user1name).roles.size() == 2
        assert userRepo.findByLoginName(worker.user2name).roles.size() == 3
    }

    @Test
    public void removeRoleANeedToRemoveItFromUserFirst() {
        RoleEntity roleA = roleRepo.findByCode(worker.roleAcode)
        em.refresh(roleA)
        assert roleA.users.size() == 2

        roleA.users.each {
            it.roles.remove(roleA)
        }
        roleRepo.delete(roleA.id)

        assert roleRepo.findByCode(worker.roleAcode) == null

        userRepo.findAll().each {
            println it
        }

        roleRepo.findAll().each {
            println it
        }

//        UserEntity user = userRepo.findByLoginName(worker.user1name)
//        em.refresh(user)
//        assert user.roles.size() == 1
//        assert user.roles.getAt(0).code == worker.roleBcode
    }

}
