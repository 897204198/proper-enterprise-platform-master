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

    def username = 'hinex'

    @Autowired
    UserRepository userRepo

    @Autowired
    RoleRepository roleRepo

    @Autowired
    ResourceRepository resRepo

    UserEntity user

    RoleEntity roleA

    RoleEntity roleB

    ResourceEntity[] resources = new ResourceEntity[10]

    public void insertData() {
        createUser()
        createRoles()
        grantUserRoles()
        createResources()
        grantRoleResources()
    }

    private void createUser() {
        user = userRepo.save(new UserEntity(username, 'hinex_password'))
        assert user.id > ''
    }

    private void createRoles() {
        roleA = new RoleEntity('roleA')
        roleB = new RoleEntity('roleB')
        roleRepo.save([roleA, roleB])
        assert roleA.id > ''
        assert roleB.id > ''
    }

    private void grantUserRoles() {
        user.setRoles([roleA, roleB])
        userRepo.save(user)
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
        roleRepo.save(roleA)

        roleB.setResources(resources[4..9])
        roleRepo.save(roleB)
    }

}
