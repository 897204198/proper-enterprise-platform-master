package com.proper.enterprise.platform.integration.auth
import com.proper.enterprise.platform.api.auth.Resource
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.entity.ResourceEntity
import com.proper.enterprise.platform.auth.entity.RoleEntity
import com.proper.enterprise.platform.auth.entity.UserEntity
import com.proper.enterprise.platform.auth.repository.ResourceRepository
import com.proper.enterprise.platform.auth.repository.RoleRepository
import com.proper.enterprise.platform.auth.repository.UserRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class GetUserResourcesIntegTest extends AbstractIntegTest {
    
    @Autowired
    UserRepository userRepo
    
    @Autowired
    RoleRepository roleRepo
    
    @Autowired
    ResourceRepository resRepo

    @Autowired
    UserService userService;
    
    UserEntity user
    
    RoleEntity roleA
    
    RoleEntity roleB

    ResourceEntity[] resources = new ResourceEntity[10]

    @Test
    public void getUserResources() {
        insertData()
        
        Collection<Resource> resources = userService.getUserResourcesByUsername('hinex')
        assert resources.size() == 10
    }
    
    private void insertData() {
        createUser()
        createRoles()
        grantUserRoles()
        createResources()
        grantRoleResources()
    }
    
    private void createUser() {
        user = userRepo.save(new UserEntity('hinex', 'hinex_password'))
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
