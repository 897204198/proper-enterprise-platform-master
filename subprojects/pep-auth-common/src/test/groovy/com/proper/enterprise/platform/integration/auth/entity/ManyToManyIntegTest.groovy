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
import org.springframework.dao.DataIntegrityViolationException

class ManyToManyIntegTest extends AbstractIntegTest {

    @Autowired
    InsertDataWorker worker

    @Autowired
    UserRepository userRepo

    @Autowired
    RoleRepository roleRepo

    @Before
    public void setUp() {
        try {
            worker.insertData()
        } catch (ex) {
        }
    }

    @Test
    public void getRoleFromUser() {
        UserEntity user = userRepo.findByLoginName(worker.user1name)
        assert user.roles.size() == 2
        user.roles.each {
            assert it.users.contains(user)
        }
    }

    @Test
    public void getUserFromRole() {
        RoleEntity role = roleRepo.findByCode(worker.roleAcode)
        assert role.users.size() == 2
    }

    @Test
    public void removeUser1() {
        UserEntity user = userRepo.findByLoginName(worker.user1name)
        userRepo.delete(user.id)
        assert userRepo.findOne(user.id) == null
        userRepo.flush()

        RoleEntity roleA = roleRepo.findByCode(worker.roleAcode)
        assert roleA.getUsers().size() == 1
        assert roleA.getUsers().getAt(0).loginName == worker.user2name
    }

    @Test(expected = DataIntegrityViolationException)
    public void removeRoleADirectlyWillCauseException() {
        RoleEntity roleA = roleRepo.findByCode(worker.roleAcode)
        roleRepo.delete(roleA.id)
        roleRepo.flush()
    }

    @Test
    public void removeRoleANeedToRemoveItFromUserFirst() {
        RoleEntity roleA = roleRepo.findByCode(worker.roleAcode)
        assert roleA.getUsers().size() == 2

        roleA.getUsers().each { user ->
            user.getRoles().remove(roleA)
        }
        roleRepo.delete(roleA.id)
        assert roleRepo.findByCode(worker.roleAcode) == null

        UserEntity user = userRepo.findByLoginName(worker.user1name)
        assert user.roles.size() == 1
        assert user.roles.getAt(0).code == worker.roleBcode
    }

}
