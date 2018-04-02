package com.proper.enterprise.platform.auth.common.neo4j.service.impl

import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.neo4j.entity.MenuNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.RoleNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.repository.MenuNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.ResourceNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.RoleNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserGroupNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserNodeRepository
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import com.proper.enterprise.platform.test.annotation.NoTx
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class RoleServiceImplTest extends AbstractNeo4jTest {

    @Autowired
    RoleNodeRepository roleRepository

    @Autowired
    UserNodeRepository userRepository

    @Autowired
    UserGroupNodeRepository userGroupNodeRepository

    @Autowired
    MenuNodeRepository menuNodeRepository

    @Autowired
    ResourceNodeRepository resourceNodeRepository

    @Autowired
    MenuService menuService

    @Autowired
    RoleService roleService

    @Autowired
    UserService userService

    @Autowired
    I18NService i18NService

    @Before
    void initData() {
        tearDown()
    }

    @After
    void clearAll() {
        tearDown()
    }

    void tearDown() {
        userRepository.deleteAll()
        roleRepository.deleteAll()
        menuNodeRepository.deleteAll()
        resourceNodeRepository.deleteAll()
        userGroupNodeRepository.deleteAll()
    }

    void initRoleData(boolean hasCircleInherit) {
        for (int i = 1; i < 11; i++) {
            RoleNodeEntity roleNodeEntity = new RoleNodeEntity()
            roleNodeEntity.setName('role' + i)
            roleRepository.save(roleNodeEntity)
        }
        List<RoleNodeEntity> list = roleRepository.findAll()
        Map<String, RoleNodeEntity> map = new TreeMap<>()
        for (int i = 0; i < list.size(); i++) {
            RoleNodeEntity roleNodeEntity = list.get(i)
            map.put(roleNodeEntity.getName(), roleNodeEntity)
        }
        List<RoleNodeEntity> list1 = new ArrayList<>(list.size())
        for (int i = 1; i < 11; i++) {
            RoleNodeEntity roleNodeEntity = new RoleNodeEntity()
            roleNodeEntity = map.get('role' + i)
            if (i == 1) {
                if (hasCircleInherit) {
                    roleNodeEntity.setParent(map.get('role10'))
                }
            } else if (i == 2 || i == 3 || i == 4) {
                roleNodeEntity.setParent(map.get('role1'))
            } else if (i == 5) {
                roleNodeEntity.setParent(map.get('role2'))
            } else if (i == 6) {
                roleNodeEntity.setParent(map.get('role3'))
            } else if (i == 7) {
                roleNodeEntity.setParent(map.get('role4'))
            } else if (i == 8) {
                roleNodeEntity.setParent(map.get('role4'))
            } else if (i == 9) {
                roleNodeEntity.setParent(map.get('role5'))
            } else if (i == 10) {
                roleNodeEntity.setParent(map.get('role9'))
            }
            list1.add(roleNodeEntity)
        }
        roleRepository.save(list1)

    }

    @Test
    void testEnable() {
        clearAll()
        RoleNodeEntity roleNodeEntity = new RoleNodeEntity()
        roleNodeEntity.setName('role1')
        roleNodeEntity = roleRepository.save(roleNodeEntity)

        RoleNodeEntity roleNodeEntity1 = new RoleNodeEntity();
        roleNodeEntity1.setName("role2")
        roleNodeEntity1.setParent(roleNodeEntity)
        roleRepository.save(roleNodeEntity1)

        RoleNodeEntity roleNodeEntity2 = new RoleNodeEntity()
        roleNodeEntity2.setName('role3')
        roleNodeEntity2.setParent(roleNodeEntity)
        roleRepository.save(roleNodeEntity2)

        Map<String, Object> map = new HashMap<>()
        map.put("id", roleNodeEntity.getId())
        map.put("enable", false)

        UserNodeEntity userEntity = new UserNodeEntity('u', 'p')
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)

        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())

        assert post('/auth/roles', JSONUtil.toJSON(map), HttpStatus.BAD_REQUEST).getResponse()
            .getContentAsString() == i18NService.getMessage("pep.auth.common.role.delete.relation.failed")

        roleService.delete(roleNodeEntity1)

        RoleNodeEntity roleNodeEntity3 = new RoleNodeEntity()
        try {
            roleService.delete(roleNodeEntity3)
        } catch (Exception e) {
            i18NService.getMessage("pep.auth.common.role.get.failed")
        }

        clearAll()

    }

    @Test
    void testHasCircleInherit() {
        clearAll()
        initRoleData(true)
        assert roleService.hasCircleInherit(roleRepository.findAll())
        roleRepository.deleteAll()

        initRoleData(false)
        assert !roleService.hasCircleInherit(roleRepository.findAll())
        roleRepository.deleteAll()
        clearAll()
    }

    @Test
    @NoTx
    void testErrDelete() {
        clearAll()
        UserNodeEntity userEntity = new UserNodeEntity("u11", "p11")
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)
        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())

        RoleNodeEntity roleNodeEntity = new RoleNodeEntity()
        roleNodeEntity.setName("role1")
        roleNodeEntity.setEnable(false)
        roleNodeEntity = roleService.save(roleNodeEntity)

        assert delete('/auth/roles?ids=' + roleNodeEntity.getId(), HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService.getMessage(
            "pep.auth.common.role.get.failed")

        roleNodeEntity.setEnable(true)
        roleNodeEntity = roleService.save(roleNodeEntity)
        userEntity.add(roleNodeEntity)
        userEntity = userService.save(userEntity)

        assert delete('/auth/roles?ids=' + roleNodeEntity.getId(), HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService
            .getMessage(
            "pep.auth.common.role.delete.relation.failed")

        userEntity.remove(roleNodeEntity)
        userEntity = userService.save(userEntity)

        RoleNodeEntity roleNodeEntity1 = new RoleNodeEntity()
        roleNodeEntity1.setName('role2')
        roleNodeEntity1.setParent(roleNodeEntity)
        roleNodeEntity1 = roleService.save(roleNodeEntity1)

        assert delete('/auth/roles?ids=' + roleNodeEntity.getId(), HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService.getMessage(
            "pep.auth.common.role.delete.relation.failed")
        clearAll()
    }

    @Test
    @NoTx
    void testGetRoleMenus() {
        clearAll()
        UserNodeEntity userEntity = new UserNodeEntity("u22", "p22")
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)
        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())

        MenuNodeEntity menuEntity = new MenuNodeEntity()
        menuEntity.setName('menu1')
        menuEntity.setRoute("route1")
        menuEntity = menuService.save(menuEntity)

        MenuNodeEntity menuEntity1 = new MenuNodeEntity()
        menuEntity1.setName('menu2')
        menuEntity1.setRoute("route2")
        menuEntity1 = menuService.save(menuEntity1)

        MenuNodeEntity menuEntity2 = new MenuNodeEntity()
        menuEntity2.setName('menu3')
        menuEntity2.setRoute("route3")
        menuEntity2 = menuService.save(menuEntity2)

        Collection<MenuNodeEntity> collection = new HashSet<>()
        collection.add(menuEntity)
        collection.add(menuEntity1)
        RoleNodeEntity roleNodeEntity = new RoleNodeEntity()
        roleNodeEntity.setName('parentRole')
        roleNodeEntity.add(collection)
        roleNodeEntity = roleService.save(roleNodeEntity)

        collection.clear()
        collection.add(menuEntity1)
        collection.add(menuEntity2)
        RoleNodeEntity roleNodeEntity1 = new RoleNodeEntity()
        roleNodeEntity1.setName('currentRole')
        roleNodeEntity1.add(collection)
        roleNodeEntity1.setParent(roleNodeEntity)
        roleNodeEntity1 = roleService.save(roleNodeEntity1)

        Collection result = roleService.getRoleMenus(roleNodeEntity1.getId(), EnableEnum.ALL, EnableEnum.ENABLE)
        assert result.size() == 3
        assert roleService.getByName('currentRole').size() == 1

        def resAllPage = JSONUtil.parse(get('/auth/roles?name=currentRole&description=&roleEnable=&pageNo=1&pageSize=2',
        HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAllPage.count == 1
        assert resAllPage.data.size() == 1
        clearAll()
    }

}
