package com.proper.enterprise.platform.auth.common.jpa.service.impl

import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.model.Menu
import com.proper.enterprise.platform.api.auth.model.Role
import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserGroupService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserGroupEntity
import com.proper.enterprise.platform.auth.common.jpa.repository.MenuRepository
import com.proper.enterprise.platform.auth.common.jpa.repository.ResourceRepository
import com.proper.enterprise.platform.auth.common.jpa.repository.RoleRepository
import com.proper.enterprise.platform.auth.common.jpa.repository.UserGroupRepository
import com.proper.enterprise.platform.auth.common.jpa.repository.UserRepository
import com.proper.enterprise.platform.auth.common.vo.UserVO
import com.proper.enterprise.platform.core.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMethod

class UserServiceImplTest extends AbstractJPATest {

    @Autowired
    UserService userService

    @Autowired
    UserRepository userRepository

    @Autowired
    RoleService roleService

    @Autowired
    RoleRepository roleRepository

    @Autowired
    UserGroupService userGroupService

    @Autowired
    UserGroupRepository userGroupRepository

    @Autowired
    ResourceService resourceService

    @Autowired
    ResourceRepository resourceRepository

    @Autowired
    MenuRepository menuRepository

    @Autowired
    MenuService menuService

    @Autowired
    I18NService i18NService

    @Test
    void addAndDeleteRoleTest() {
        UserEntity user = new UserEntity();
        user.setName("user12")
        user.setUsername("username1")
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
    void testGetUserGroupRolesByUserId() {
        RoleEntity roleEntity1 = new RoleEntity()
        roleEntity1.setName('role1')
        roleEntity1 = roleService.save(roleEntity1)

        RoleEntity roleEntity2 = new RoleEntity()
        roleEntity2.setName('role2')
        roleEntity2 = roleService.save(roleEntity2)

        RoleEntity roleEntity3 = new RoleEntity()
        roleEntity3.setName('role3')
        roleEntity3 = roleService.save(roleEntity3)

        RoleEntity roleEntity4 = new RoleEntity()
        roleEntity4.setName('role4')
        roleEntity4 = roleService.save(roleEntity4)

        RoleEntity roleEntity5 = new RoleEntity()
        roleEntity5.setName('role5')
        roleEntity5 = roleService.save(roleEntity5)



        UserGroupEntity userGroupEntity1 = new UserGroupEntity()
        userGroupEntity1.setName('group1')
        userGroupEntity1.setSeq(1)
        userGroupEntity1.add(roleEntity1)
        userGroupEntity1.add(roleEntity2)
        userGroupService.save(userGroupEntity1)

        UserGroupEntity userGroupEntity2 = new UserGroupEntity()
        userGroupEntity2.setName('group2')
        userGroupEntity2.setSeq(2)
        userGroupEntity2.add(roleEntity3)
        userGroupEntity2.add(roleEntity4)
        userGroupService.save(userGroupEntity2)

        UserEntity userEntity = new UserEntity('user', 'pwd')
        userEntity.add(roleEntity5)
        userEntity.add(userGroupEntity1)
        userEntity.add(userGroupEntity2)
        userEntity = userService.save(userEntity)

        Map<String, RoleEntity> map = roleService.getUserGroupRolesByUserId(userEntity.getId())
        assert map.size() == 4
    }

    @Test
    @NoTx
    void testHasPermissionByUrl() {
        ResourceEntity resourceEntity = new ResourceEntity()
        resourceEntity.setName('res1')
        resourceEntity.addURL('/auth/users/**')
        resourceEntity.setMethod(RequestMethod.POST)
        resourceEntity = resourceService.save(resourceEntity)

        ResourceEntity resourceEntity1 = new ResourceEntity()
        resourceEntity1.setName('res2')
        resourceEntity1.addURL('/auth/users/*')
        resourceEntity1.setMethod(RequestMethod.POST)
        resourceEntity1 = resourceService.save(resourceEntity1)

        Collection<ResourceEntity> resourceEntities = new HashSet<>()
        resourceEntities.add(resourceEntity)
        resourceEntities.add(resourceEntity1)

        RoleEntity roleEntity1 = new RoleEntity()
        roleEntity1.setName('role1')
        roleEntity1.addResources(resourceEntities)
        roleEntity1 = roleService.save(roleEntity1)

        UserEntity userEntity = new UserEntity('user1', 'pwd1')
        userEntity.add(roleEntity1)
        userEntity = userService.save(userEntity)

        //------------------------------------------------------------------------------------

        ResourceEntity resourceEntity2 = new ResourceEntity()
        resourceEntity2.setName('res3')
        resourceEntity2.addURL('/auth/users/{userId}/role/{roleId}')
        resourceEntity2.setMethod(RequestMethod.GET)
        resourceEntity2 = resourceService.save(resourceEntity2)
        Collection<ResourceEntity> resourceEntities1 = new HashSet<>()
        resourceEntities1.add(resourceEntity2)

        RoleEntity roleEntity2 = new RoleEntity()
        roleEntity2.setName('role2')
        roleEntity2.addResources(resourceEntities1)
        roleEntity2 = roleService.save(roleEntity2)

        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName('group1')
        userGroupEntity.setSeq(1)
        userGroupEntity.add(roleEntity2)
        userGroupService.save(userGroupEntity)
        userEntity.add(userGroupEntity)
        User user = userService.save(userEntity)
        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())
        assert user.getUserGroups()[0].getName() == 'group1'
    }

    @Test
    void testSave() {
        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName('group1')
        userGroupEntity.setSeq(1)
        userGroupEntity = userGroupService.save(userGroupEntity)
        UserEntity userEntity = new UserEntity('user1', 'pwd1')
        userEntity.add(userGroupEntity)
        User user = userService.save(userEntity)
        assert user.getUserGroups().size() == 1
        assert user.getId() == userEntity.getId()
        UserVO userVO = new UserVO()
        userVO.setName('new name')
        userVO.setEmail('456@sd.com')
        userVO.setPhone('12345854896')
        userVO.setPassword('123abc')
        userVO.setEnable(true)
        userVO.setId(userEntity.getId())
        user = userService.update(userVO)
        assert user.getName() == 'new name'
        userService.delete(user.getId())
        assert userService.get(userEntity.getId()) == null

    }

    @Test
    @NoTx
    void testUserImpl() {
        UserEntity userEntity = new UserEntity()
        userEntity.setName("user1")
        userEntity.setId("userId")
        userEntity.setUsername("username")
        userEntity.setPassword("pwd1")
        userEntity.setCreateUserId("00000")
        userEntity.setEnable(true)
        userEntity = userService.save(userEntity)

        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('role2')
        roleEntity = roleService.save(roleEntity)

        MenuEntity menuEntity = new MenuEntity()
        menuEntity.setName("ralm")
        menuEntity.setEnable(true)
        menuEntity.setRoute("routeUser")
        menuEntity = menuService.save(menuEntity)

        MenuEntity menuEntity1 = new MenuEntity()
        menuEntity1.setName("ralm2")
        menuEntity1.setEnable(true)
        menuEntity1.setRoute("routeUser6")
        menuEntity1 = menuService.save(menuEntity1)

        List<MenuEntity> list = new ArrayList<>()
        list.add(menuEntity)
        list.add(menuEntity1)
        roleEntity.add(list)
        Role role = roleService.save(roleEntity)
        userEntity.add(roleEntity)
        User user = userService.save(userEntity)
        Collection<Menu> menus = userService.getUserMenus(userEntity.getId(), EnableEnum.ENABLE)
        assert menus.size() == 2
    }

    @Test
    void testDelException1() {
        // test Exception
        UserEntity user = new UserEntity()
        user.setName("user13131")
        user.setId("userId")
        user.setUsername("username2testDelException1")
        user.setPassword("pwd1")
        user.setCreateUserId("00000")
        user.setEnable(false)
        user = userService.save(user)
        try {
            userService.delete(user)
        } catch (Exception e) {
            i18NService.getMessage("pep.auth.common.user.get.failed")
        }
    }

    @Test
    void testDelException2() {
        UserEntity userEntity = new UserEntity()
        userEntity.setName("user2")
        userEntity.setId("userId2")
        userEntity.setUsername("username2")
        userEntity.setPassword("pwd2")
        userEntity.setCreateUserId("00002")
        userEntity.setSuperuser(true)
        userEntity.setEnable(true)
        userEntity = userService.save(userEntity)
        try {
            userService.delete(userEntity)
        } catch (Exception e) {
            i18NService.getMessage("pep.auth.common.user.delete.role.super.failed")
        }
    }

    @Test
    void testDelException3() {
        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName("roleName")
        roleEntity = roleService.save(roleEntity)

        UserEntity userEntity = new UserEntity()
        userEntity.setName("user2")
        userEntity.setId("userId2")
        userEntity.setUsername("username2")
        userEntity.setPassword("pwd2")
        userEntity.setCreateUserId("00002")
        userEntity.add(roleEntity)
        userEntity = userService.save(userEntity)

        try {
            userService.delete(userEntity)
        } catch (Exception e) {
            i18NService.getMessage("pep.auth.common.user.delete.role.relation.faile")
        }
    }

    @Test
    void testGetMenus() {
        UserEntity userEntity = new UserEntity()
        userEntity.setName("user2")
        userEntity.setUsername("username2")
        userEntity.setPassword("pwd2")
        userEntity.setCreateUserId("00002")
        userEntity = userService.save(userEntity)
        assert userService.getUserMenus(userEntity.getId(), EnableEnum.ALL).size() == 0
    }

    @After
    void clearAll() {
        userRepository.deleteAll()
        userGroupRepository.deleteAll()
        roleRepository.deleteAll()
        resourceRepository.deleteAll()
        menuRepository.deleteAll()
    }

}
