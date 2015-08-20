package com.proper.enterprise.platform.integration.auth

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import com.proper.enterprise.platform.api.auth.Resource
import com.proper.enterprise.platform.api.auth.User
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.entity.ResourceEntity
import com.proper.enterprise.platform.auth.entity.RoleEntity

import com.proper.enterprise.platform.auth.entity.UserEntity

import com.proper.enterprise.platform.auth.repository.ResourceRepository
import com.proper.enterprise.platform.auth.repository.RoleRepository

import com.proper.enterprise.platform.auth.repository.UserRepository

import com.proper.enterprise.platform.test.integration.AbstractIntegTest

class GetUserResourcesIntegTest extends AbstractIntegTest {
    
    @Autowired
    UserRepository userRepo
    
    @Autowired
    RoleRepository roleRepo
    
    @Autowired
    ResourceRepository resRepo

    @Autowired
    UserService userService;
    
    @Autowired
    ResourceService resService;
    
    UserEntity user
    
    RoleEntity roleA
    
    RoleEntity roleB

    ResourceEntity[] resources = new ResourceEntity[10]

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
        6.times { idx ->
            roleA.setResources([resources[idx]])
            roleRepo.save(roleA)
        }
        (4..9).each { idx ->
            roleB.setResources([resources[idx]])
            roleRepo.save(roleB)
        }
    }

}
