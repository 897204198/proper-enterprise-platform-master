package com.proper.enterprise.platform.auth.common.service.impl

import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository
import com.proper.enterprise.platform.auth.common.repository.RoleRepository
import com.proper.enterprise.platform.auth.common.repository.UserGroupRepository
import com.proper.enterprise.platform.auth.common.repository.UserRepository
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMethod

class AbstractUserServiceImplTest extends AbstractTest {

    @Autowired
    UserService userService

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService

    @Autowired
    RoleRepository roleRepository

    @Autowired
    UserGroupRepository userGroupRepository

    @Autowired
    ResourceRepository resourceRepository

    @Test
    @Transactional
    void addAndDeleteRoleTest() {
        UserEntity user = new UserEntity();
        user.setName("user1")
        user.setUsername("username")
        user.setPassword("pwd1")
        user = userService.save(user)

        RoleEntity role = new RoleEntity()
        role.setName("role1")
        role = roleService.save(role)
        User user1 = userService.addUserRole(user.getId(), role.getId())

        assert user1.getRoles().asList().size() == 1
        assert user1.getRoles().asList().get(0).id == role.getId()

        user1 = userService.addUserRole(user.getId(), role.getId())
        assert user1.getRoles().asList().size() == 1

        RoleEntity role1 = new RoleEntity()
        role1.setName("role2")
        role1 = roleService.save(role1)
        user1 = userService.addUserRole(user.getId(), role1.getId())

        assert user1.getRoles().asList().size() == 2
        assert user1.getRoles().asList().get(1).id == role1.getId()

        user1 = userService.deleteUserRole(user1.getId(), role1.getId())

        assert user1.getRoles().asList().size() == 1
        assert user1.getRoles().asList().get(0).id == role.getId()

    }

    @Test
    @NoTx
    void testGetUserGroupRolesByUserId(){
        RoleEntity roleEntity1 = new RoleEntity()
        roleEntity1.setName('role1')
        roleEntity1 = roleRepository.save(roleEntity1)

        RoleEntity roleEntity2 = new RoleEntity()
        roleEntity2.setName('role2')
        roleEntity2 = roleRepository.save(roleEntity2)

        RoleEntity roleEntity3 = new RoleEntity()
        roleEntity3.setName('role3')
        roleEntity3 = roleRepository.save(roleEntity3)

        RoleEntity roleEntity4 = new RoleEntity()
        roleEntity4.setName('role4')
        roleEntity4 = roleRepository.save(roleEntity4)

        RoleEntity roleEntity5 = new RoleEntity()
        roleEntity5.setName('role5')
        roleEntity5 = roleRepository.save(roleEntity5)

        UserEntity userEntity = new UserEntity('user','pwd')
        userEntity.add(roleEntity5)
        userEntity = userRepository.save(userEntity)

        UserGroupEntity userGroupEntity1 = new UserGroupEntity()
        userGroupEntity1.setName('group1')
        userGroupEntity1.setSeq(1)
        userGroupEntity1.add(roleEntity1)
        userGroupEntity1.add(roleEntity2)
        userGroupEntity1.add(userEntity)
        userGroupRepository.save(userGroupEntity1)

        UserGroupEntity userGroupEntity2 = new UserGroupEntity()
        userGroupEntity2.setName('group2')
        userGroupEntity2.setSeq(2)
        userGroupEntity2.add(roleEntity3)
        userGroupEntity2.add(roleEntity4)
        userGroupEntity2.add(userEntity)
        userGroupRepository.save(userGroupEntity2)

        Map<String,RoleEntity> map = roleService.getUserGroupRolesByUserId(userEntity.getId())
        assert map.size() == 4
    }

    @Test
    @NoTx
    void testHasPerimissionByUrl(){
        ResourceEntity resourceEntity = new ResourceEntity()
        resourceEntity.setName('res1')
        resourceEntity.setURL('/auth/users/**')
        resourceEntity.setMethod(RequestMethod.POST)
        resourceEntity = resourceRepository.save(resourceEntity)

        ResourceEntity resourceEntity1 = new ResourceEntity()
        resourceEntity1.setName('res2')
        resourceEntity1.setURL('/auth/users/*')
        resourceEntity1.setMethod(RequestMethod.POST)
        resourceEntity1 = resourceRepository.saveAndFlush(resourceEntity1)

        Collection<ResourceEntity> resourceEntities = new HashSet<>()
        resourceEntities.add(resourceEntity)
        resourceEntities.add(resourceEntity1)

        RoleEntity roleEntity1 = new RoleEntity()
        roleEntity1.setName('role1')
        roleEntity1.addResources(resourceEntities)
        roleEntity1 = roleRepository.save(roleEntity1)

        UserEntity userEntity = new UserEntity('user1','pwd1')
        userEntity.add(roleEntity1)
        userEntity = userRepository.save(userEntity)

        //------------------------------------------------------------------------------------

        ResourceEntity resourceEntity2 = new ResourceEntity()
        resourceEntity2.setName('res2')
        resourceEntity2.setURL('/auth/users/{userId}/role/{roleId}')
        resourceEntity2.setMethod(RequestMethod.GET)
        resourceEntity2 = resourceRepository.save(resourceEntity2)
        Collection<ResourceEntity> resourceEntities1 = new HashSet<>()
        resourceEntities1.add(resourceEntity2)

        RoleEntity roleEntity2 = new RoleEntity()
        roleEntity2.setName('role2')
        roleEntity2.addResources(resourceEntities1)
        roleEntity2 = roleRepository.save(roleEntity2)

        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName('group1')
        userGroupEntity.setSeq(1)
        userGroupEntity.add(userEntity)
        userGroupEntity.add(roleEntity2)
        userGroupRepository.save(userGroupEntity)

        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())

        assert userService.checkPermission('/auth/users/{userId}/role/{roleId}',RequestMethod.POST) == null
        assert userService.checkPermission('/auth/users/{userId}/role/{roleId}',RequestMethod.GET) == null

        User user = userService.groupHasTheUser(userGroupEntity, userEntity.getId())
        assert user.getId() == userEntity.getId()

    }

    @Test
    void testSave(){

        UserEntity userEntity = new UserEntity('user1','pwd1')
        userEntity = userRepository.save(userEntity)

        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName('group1')
        userGroupEntity.setSeq(1)
        userGroupEntity.add(userEntity)
        userGroupEntity = userGroupRepository.save(userGroupEntity)

        User user = userService.groupHasTheUser(userGroupEntity, userEntity.getId())
        assert user.getId() == userEntity.getId()

        Map<String, Object> map = new HashMap<>()
        map.put("name", 'new name')
        map.put("email", '456@sd.com')
        map.put("phone", '12345854896')
        map.put("password", '123abc')
        map.put("enable", true)

        user = userService.save(userEntity.getId(), map)
        assert user.getName() == 'new name'

        userService.delete(user)
        assert userService.get(userEntity.getId()) == null

    }



    @After
    void clearAll() {
        userGroupRepository.deleteAll()
        userRepository.deleteAll()
        roleRepository.deleteAll()
        resourceRepository.deleteAll()
    }

}
