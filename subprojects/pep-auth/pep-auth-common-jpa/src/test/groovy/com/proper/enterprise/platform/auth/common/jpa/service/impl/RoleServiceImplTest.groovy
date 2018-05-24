package com.proper.enterprise.platform.auth.common.jpa.service.impl

import com.proper.enterprise.platform.api.auth.dao.RoleDao
import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.common.jpa.repository.RoleRepository
import com.proper.enterprise.platform.auth.common.jpa.repository.UserRepository
import com.proper.enterprise.platform.auth.common.vo.RoleVO
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.sys.i18n.I18NUtil
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class RoleServiceImplTest extends AbstractTest {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MenuService menuService

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService

    @Autowired
    I18NService i18NService

    @Autowired
    UserRepository userRepository

    @Autowired
    RoleDao roleDao

    void initRoleData(boolean hasCircleInherit) {
        for (int i = 1; i < 11; i++) {
            RoleEntity roleEntity = new RoleEntity()
            roleEntity.setName('roleC' + i)
            roleRepository.save(roleEntity)
        }
        List<RoleEntity> list = roleRepository.findAll()
        Map<String, RoleEntity> map = new TreeMap<>()
        for (int i = 0; i < list.size(); i++) {
            RoleEntity roleEntity = list.get(i)
            map.put(roleEntity.getName(), roleEntity)
        }
        List<RoleEntity> list1 = new ArrayList<>(list.size())
        for (int i = 1; i < 11; i++) {
            RoleEntity roleEntity = new RoleEntity()
            roleEntity = map.get('roleC' + i)
            if (i == 1) {
                if (hasCircleInherit) {
                    roleEntity.setParent(map.get('roleC10'))
                }
            } else if (i == 2 || i == 3 || i == 4) {
                roleEntity.setParent(map.get('roleC1'))
            } else if (i == 5) {
                roleEntity.setParent(map.get('roleC2'))
            } else if (i == 6) {
                roleEntity.setParent(map.get('roleC3'))
            } else if (i == 7) {
                roleEntity.setParent(map.get('roleC4'))
            } else if (i == 8) {
                roleEntity.setParent(map.get('roleC4'))
            } else if (i == 9) {
                roleEntity.setParent(map.get('roleC5'))
            } else if (i == 10) {
                roleEntity.setParent(map.get('roleC9'))
            }
            list1.add(roleEntity)
        }
        roleRepository.save(list1)

    }

    @Test
    void testEnable() {
        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('role1')
        roleEntity = roleService.save(roleEntity)

        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setName("role2")
        roleEntity1.setParent(roleEntity)
        roleService.save(roleEntity1)

        RoleEntity roleEntity2 = new RoleEntity()
        roleEntity2.setName('role3')
        roleEntity2.setParent(roleEntity)
        roleService.save(roleEntity2)

        Map<String, Object> map = new HashMap<>()
        map.put("id", roleEntity.getId())
        map.put("name", "roleService")
        map.put("enable", false)

        UserEntity userEntity = new UserEntity('u', 'p')
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)

        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())

        RoleVO roleVO = JSONUtil.parse(post('/auth/roles', JSONUtil.toJSON(map), HttpStatus.CREATED).getResponse()
            .getContentAsString(), RoleVO.class)
        assert !roleVO.enable
        roleService.delete(roleEntity1)

        RoleEntity roleEntity3 = new RoleEntity()
        try {
            roleService.delete(roleEntity3)
        } catch (Exception e) {
            i18NService.getMessage("pep.auth.common.role.get.failed")
        }
        assert roleDao.findRolesLike(roleEntity2.getName(), EnableEnum.ALL).size() == 1
    }

    @Test
    void testHasCircleInherit() {
        RoleVO a = new RoleVO()
        a.setName("a")
        RoleVO b = new RoleVO()
        b.setName("b")
        RoleVO c = new RoleVO()
        c.setName("c")

        RoleVO av = postAndReturn("/auth/roles", a)
        RoleVO bv = postAndReturn("/auth/roles", b)
        RoleVO cv = postAndReturn("/auth/roles", c)
        bv.setParentId(av.getId())
        put('/auth/roles/' + bv.getId(), JSONUtil.toJSON(bv), HttpStatus.OK)
        av.setParentId(cv.getId())
        put('/auth/roles/' + av.getId(), JSONUtil.toJSON(av), HttpStatus.OK)
        cv.setParentId(bv.getId())
        put('/auth/roles/' + cv.getId(), JSONUtil.toJSON(cv),
            HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() ==
            I18NUtil.getMessage("pep.auth.common.role.circle.error")
    }

    @Test
    @NoTx
    void testErrDelete() {
        UserEntity userEntity = new UserEntity("u11", "p11")
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)
        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())

        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName("role1")
        roleEntity.setEnable(false)
        roleEntity = roleService.save(roleEntity)

        delete('/auth/roles?ids=' + roleEntity.getId(), HttpStatus.NO_CONTENT).getResponse().getContentAsString()

        roleEntity.setEnable(true)
        roleEntity = roleService.save(roleEntity)
        userEntity.add(roleEntity)
        userEntity = userService.save(userEntity)

        assert delete('/auth/roles?ids=' + roleEntity.getId(), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() == i18NService.getMessage(
            "pep.auth.common.role.delete.relation.failed")

        userEntity.remove(roleEntity)
        userEntity = userService.save(userEntity)

        RoleEntity roleEntity1 = new RoleEntity()
        roleEntity1.setName('role2')
        roleEntity1.setParent(roleEntity)
        roleEntity1 = roleService.save(roleEntity1)

        assert delete('/auth/roles?ids=' + roleEntity.getId(), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()==I18NUtil.getMessage("pep.auth.common.role.has.role")
        roleService.findAll()
    }

    @Test
    @NoTx
    void testGetRoleMenus() {
        MenuEntity menuEntity = new MenuEntity()
        menuEntity.setName('menu1')
        menuEntity.setRoute("route1")
        menuEntity = menuService.save(menuEntity)

        MenuEntity menuEntity1 = new MenuEntity()
        menuEntity1.setName('menu2')
        menuEntity1.setRoute("route2")
        menuEntity1 = menuService.save(menuEntity1)

        MenuEntity menuEntity2 = new MenuEntity()
        menuEntity2.setName('menu3')
        menuEntity2.setRoute("route3")
        menuEntity2 = menuService.save(menuEntity2)

        Collection<MenuEntity> collection = new HashSet<>()
        collection.add(menuEntity)
        collection.add(menuEntity1)
        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('parentRole')
        roleEntity.add(collection)
        roleEntity = roleService.save(roleEntity)

        collection.clear()
        collection.add(menuEntity1)
        collection.add(menuEntity2)
        RoleEntity roleEntity1 = new RoleEntity()
        roleEntity1.setName('currentRole')
        roleEntity1.add(collection)
        roleEntity1.setParent(roleEntity)
        roleEntity1 = roleService.save(roleEntity1)

        Collection result = roleService.getRoleMenus(roleEntity1.getId(), EnableEnum.ENABLE)
        assert result.size() == 3
        assert roleDao.findRoles('currentRole', EnableEnum.ALL).size() == 1

        roleDao.findRoles('currentRole', EnableEnum.ALL).size() == 1
    }

    @NoTx
    @Test
    void testParentRoleDaoErr() {
        RoleEntity roleEntity = new RoleEntity()
        try {
            roleDao.findParentRoles(roleEntity.getId())
        } catch (Exception e) {
            i18NService.getMessage("pep.auth.common.role.get.failed")
        }

        try {
            roleService.getRoleUserGroups(roleEntity.getId(), EnableEnum.ENABLE, EnableEnum.ENABLE)
        } catch (Exception e1) {
            i18NService.getMessage("pep.auth.common.role.get.failed")
        }

        RoleEntity role = new RoleEntity()
        role.setId("aa")
        role.setName('role')
        role = roleService.save(role)

        RoleEntity role11 = new RoleEntity()
        role11.setId("aa1")
        role11.setName('role1')
        role11.setParentId(role.getId())
        role11 = roleService.save(role11)

        try {
            roleDao.findParentRoles(role11.getId())
        } catch (Exception e) {
            i18NService.getMessage("pep.auth.common.role.circle.error")
        }
    }


    @After
    void clearAll() {
        userRepository.deleteAll()
        roleRepository.deleteAll()
    }

}
