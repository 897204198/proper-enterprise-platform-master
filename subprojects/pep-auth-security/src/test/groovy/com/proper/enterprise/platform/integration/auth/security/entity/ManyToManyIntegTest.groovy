package com.proper.enterprise.platform.integration.auth.security.entity

import com.proper.enterprise.platform.auth.common.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.repository.RoleRepository
import com.proper.enterprise.platform.auth.common.repository.UserRepository
import com.proper.enterprise.platform.test.auth.InsertDataWorker
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import com.proper.enterprise.platform.test.integration.SqlWorker
import org.junit.AfterClass
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

    static boolean hasSetUp = false

    @Before
    public void setUp() {
        if (!hasSetUp) {
            push(worker.getBeforeDMLs())
            executeSqls()
            hasSetUp = true
        }
    }

    @AfterClass
    public static void tearDown() {
        SqlWorker sqlWorker = new SqlWorker()
        InsertDataWorker worker = new InsertDataWorker()
        sqlWorker.push(worker.getAfterDMLs())
        sqlWorker.work()
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
        assert roleA.getUsers().getAt(0).username == worker.user2name
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

    @Test
    public void getResourceByRole() {
        RoleEntity roleA = roleRepo.findByCode(worker.roleAcode)
        assert roleA.getResources().size() == 6

        RoleEntity roleB = roleRepo.findByCode(worker.roleBcode)
        assert roleB.getResources().size() == 6

        RoleEntity roleC = roleRepo.findByCode(worker.roleCcode)
        assert roleC.getResources().size() == 10
    }

    @Test
    public void getResourceByUser() {
        assert getAllResources(worker.user1name).size() == 10
        assert getAllResources(worker.user2name).size() == 10
    }

    private Set<ResourceEntity> getAllResources(String username) {
        UserEntity user = userRepo.findByLoginName(username)
        Set<ResourceEntity> resources = new HashSet<>()
        user.getRoles().each {
            resources.addAll(it.getResources())
        }
        resources
    }

}
