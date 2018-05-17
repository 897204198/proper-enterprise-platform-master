package com.proper.enterprise.platform.auth.common.jpa.service.impl

import com.proper.enterprise.platform.api.auth.dao.UserDao
import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.jpa.entity.*
import com.proper.enterprise.platform.auth.common.jpa.repository.*
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMethod

class UserServiceImplTest extends AbstractTest {

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

    @Autowired
    MenuRepository menuRepository

    @Autowired
    MenuService menuService

    @Autowired
    I18NService i18NService

    @Autowired
    UserDao userDao

    @Test
    @Transactional
    void addAndDeleteRoleTest() {
        UserEntity user = new UserEntity();
        user.setName("user1")
        user.setUsername("usernameTTT")
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



        UserGroupEntity userGroupEntity1 = new UserGroupEntity()
        userGroupEntity1.setName('group1')
        userGroupEntity1.setSeq(1)
        userGroupEntity1.add(roleEntity1)
        userGroupEntity1.add(roleEntity2)
        userGroupRepository.save(userGroupEntity1)

        UserGroupEntity userGroupEntity2 = new UserGroupEntity()
        userGroupEntity2.setName('group2')
        userGroupEntity2.setSeq(2)
        userGroupEntity2.add(roleEntity3)
        userGroupEntity2.add(roleEntity4)
        userGroupRepository.save(userGroupEntity2)

        UserEntity userEntity = new UserEntity('user','pwd')
        userEntity.add(roleEntity5)
        userEntity.add(userGroupEntity1)
        userEntity.add(userGroupEntity2)
        userEntity = userRepository.save(userEntity)

        Map<String,RoleEntity> map = roleService.getUserGroupRolesByUserId(userEntity.getId())
        assert map.size() == 4
    }

    @Test
    @NoTx
    void testHasPermissionByUrl(){
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
        userEntity = userRepository.save(userEntity)

        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('role2')
        roleEntity = roleRepository.save(roleEntity)

        MenuEntity menuEntity = new MenuEntity()
        menuEntity.setName("ralm")
        menuEntity.setEnable(true)
        menuEntity.setRoute("route")
        menuEntity = menuRepository.save(menuEntity)

        MenuEntity menuEntity1 = new MenuEntity()
        menuEntity1.setName("ralm")
        menuEntity1.setEnable(true)
        menuEntity1.setRoute("route6")
        menuEntity1 = menuRepository.save(menuEntity1)

        List<MenuEntity> list = new ArrayList<>()
        list.add(menuEntity)
        list.add(menuEntity1)

        roleEntity.add(list)
        userEntity.add(roleEntity)

        MenuEntity res = userService.getMenus()
        assert res.enable == true
    }
    @Test
    void testUser1(){
        UserEntity userEntitw = new UserEntity()
        userEntitw.setName("user1")
        userEntitw.setId("userId")
        userEntitw.setUsername("usernameqqq")
        userEntitw.setPassword("pwd1")
        userEntitw.setCreateUserId("00000")
        userEntitw.setEnable(true)
        userEntitw = userService.save(userEntitw)
        userDao.getCurrentUserByUserId(userEntitw.getId())
        UserEntity user = userService.getByUsername(userEntitw.getUsername())
        MenuEntity menu = userService.getMenusByUsername(user.getUsername())
        menu.enable == true
    }

    @Test
    void testDelException1(){
        // test Exception
        UserEntity user = new UserEntity()
        user.setName("user1")
        user.setId("userId")
        user.setUsername("username1")
        user.setPassword("pwd1")
        user.setCreateUserId("00000")
        user.setEnable(false)
        user = userService.save(user)
        try {
            userService.delete(user)
        }catch (Exception e){
            i18NService.getMessage("pep.auth.common.user.get.failed")
        }
    }

    @Test
    void testDelException2(){
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
        }catch (Exception e){
            i18NService.getMessage("pep.auth.common.user.delete.role.super.failed")
        }
    }

    @Test
    void testDelException3(){
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
        }catch (Exception e){
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
        assert userService.getMenus(userEntity.getId()).size() == 0
    }
    @After
    void clearAll() {
        userRepository.deleteAll()
        userGroupRepository.deleteAll()
        roleRepository.deleteAll()
        resourceRepository.deleteAll()
    }

}
