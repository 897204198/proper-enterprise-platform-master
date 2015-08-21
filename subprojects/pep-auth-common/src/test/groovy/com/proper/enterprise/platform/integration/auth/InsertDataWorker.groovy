package com.proper.enterprise.platform.integration.auth
import com.proper.enterprise.platform.auth.entity.ResourceEntity
import com.proper.enterprise.platform.auth.entity.RoleEntity
import com.proper.enterprise.platform.auth.entity.UserEntity
import com.proper.enterprise.platform.auth.repository.ResourceRepository
import com.proper.enterprise.platform.auth.repository.RoleRepository
import com.proper.enterprise.platform.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class InsertDataWorker {

    @Autowired
    UserRepository userRepo

    @Autowired
    RoleRepository roleRepo

    @Autowired
    ResourceRepository resRepo

    def user1name = 'hinex1', user2name = 'hinex2'
    def userpwd = 'pwd'
    def roleAcode = 'roleA', roleBcode = 'roleB', roleCcode = 'roleC'

    UserEntity user1, user2
    RoleEntity roleA, roleB, roleC
    ResourceEntity[] resources = new ResourceEntity[10]

    public void insertData() {
        createUser()
        createRoles()
        grantUserRoles()
        createResources()
        grantRoleResources()
    }

    private void createUser() {
        user1 = userRepo.save(new UserEntity(user1name, userpwd))
        user2 = userRepo.save(new UserEntity(user2name, userpwd))
        assert user1.id > ''
        assert user2.id > ''
    }

    private void createRoles() {
        roleA = new RoleEntity(roleAcode)
        roleB = new RoleEntity(roleBcode)
        roleC = new RoleEntity(roleCcode)
        roleRepo.save([roleA, roleB, roleC])
        assert roleA.id > ''
        assert roleB.id > ''
        assert roleC.id > ''
    }

    private void grantUserRoles() {
        user1.setRoles([roleA, roleB])
        user2.setRoles([roleA, roleB, roleC])
        userRepo.save([user1, user2])
    }

    private void createResources() {
        10.times { idx ->
            ResourceEntity res = new ResourceEntity()
            res.setUrl("/auth/res$idx")
            resRepo.save(res)
            resources[idx] = res
            assert resources[idx].id > ''
        }
    }

    private void grantRoleResources() {
        roleA.setResources(resources[0..5])
        roleB.setResources(resources[4..9])
        roleC.setResources(resources.toList())
        roleRepo.save([roleA, roleB, roleC])
    }

}
