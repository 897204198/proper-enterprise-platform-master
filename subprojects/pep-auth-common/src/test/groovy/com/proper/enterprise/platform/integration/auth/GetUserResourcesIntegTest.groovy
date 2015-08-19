package com.proper.enterprise.platform.integration.auth

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import com.proper.enterprise.platform.api.auth.Resource
import com.proper.enterprise.platform.api.auth.User
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.entity.ResourceEntity
import com.proper.enterprise.platform.auth.entity.RoleEntity
import com.proper.enterprise.platform.auth.entity.RoleResourceEntity
import com.proper.enterprise.platform.auth.entity.UserEntity
import com.proper.enterprise.platform.auth.entity.UserRoleEntity
import com.proper.enterprise.platform.auth.repository.ResourceRepository
import com.proper.enterprise.platform.auth.repository.RoleRepository
import com.proper.enterprise.platform.auth.repository.RoleResourceRepository
import com.proper.enterprise.platform.auth.repository.UserRepository
import com.proper.enterprise.platform.auth.repository.UserRoleRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest

class GetUserResourcesIntegTest extends AbstractIntegTest {
    
    @Autowired
    UserRepository userRepo
    
    @Autowired
    RoleRepository roleRepo
    
    @Autowired
    UserRoleRepository urRepo
    
    @Autowired
    ResourceRepository resRepo
    
    @Autowired
    RoleResourceRepository rrRepo
    
    @Autowired
    UserService userService;
    
    @Autowired
    ResourceService resService;
    
    String userId
    
    String roleAId
    
    String roleBId
    
    String[] resIds = new String[10]

    @Test
    public void getUserResources() {
        insertData()
        
        User user = userService.getUserByUsername('hinex')
        Set<Resource> resources = resService.getResourcesByUser(user.id)
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
        def user = userRepo.save(new UserEntity('hinex', 'hinex_account', 'hinex_password'))
        userId = user.id
        assert userId > ''
    }
    
    private void createRoles() {
        def roleA = new RoleEntity('roleA')
        def roleB = new RoleEntity('roleB')
        roleRepo.save([roleA, roleB])
        roleAId = roleA.id
        roleBId = roleB.id
        assert roleAId > ''
        assert roleBId > ''
    }
    
    private void grantUserRoles() {
        urRepo.save([new UserRoleEntity(userId, roleAId), new UserRoleEntity(userId, roleBId)])
    }
    
    private void createResources() {
        10.times { idx ->
            ResourceEntity res = new ResourceEntity()
            res.setUrl("/auth/res$idx")
            resRepo.save(res)
            resIds[idx] = res.id
            assert resIds[idx] > ''
        }
    }
    
    private void grantRoleResources() {
        6.times { idx ->
            rrRepo.save(new RoleResourceEntity(roleAId, resIds[idx]))
        }
        (4..9).each { idx ->
            rrRepo.save(new RoleResourceEntity(roleBId, resIds[idx]))
        }
    }

}
