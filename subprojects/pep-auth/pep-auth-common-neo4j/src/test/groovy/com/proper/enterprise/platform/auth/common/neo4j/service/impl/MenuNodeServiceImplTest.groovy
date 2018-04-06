package com.proper.enterprise.platform.auth.common.neo4j.service.impl

import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.auth.common.neo4j.entity.MenuNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.ResourceNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.RoleNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.repository.*
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMethod

class MenuNodeServiceImplTest extends AbstractNeo4jTest {

    @Autowired
    MenuService service

    @Autowired
    MenuNodeRepository menuNodeRepository

    @Autowired
    ResourceService resourceService

    @Autowired
    ResourceNodeRepository resourceNodeRepository

    @Autowired
    RoleNodeRepository roleRepository

    @Autowired
    UserGroupNodeRepository userGroupNodeRepository

    @Autowired
    UserNodeRepository userNodeRepository

    @Before
    void clearUp() {
        tearDown()
    }

    @After
    void cleanAll() {
        tearDown()
    }

    void tearDown() {
        menuNodeRepository.deleteAll()
        roleRepository.deleteAll()
        userGroupNodeRepository.deleteAll()
        resourceNodeRepository.deleteAll()
        userNodeRepository.deleteAll()
    }

    @Test
    void getMenuTree() {
        MenuNodeEntity menu = new MenuNodeEntity()
        menu.setId("a2")
        menu.setName("CRUD")
        menu.setRoute("/a2")
        menu.setIcon(null)
        menu.setSequenceNumber(1)
        menu = menuNodeRepository.save(menu)

        MenuNodeEntity menu1 = new MenuNodeEntity()
        menu1.setId("a2-m2")
        menu1.setName("编辑")
        menu1.setRoute("/a2/m2")
        menu1.setIcon(null)
        menu1.setSequenceNumber(1)
        menu1.setParent(menu)
        menu1 = menuNodeRepository.save(menu1)

        MenuNodeEntity menu2 = new MenuNodeEntity()
        menu2.setId("a2-m2-1")
        menu2.setName("查")
        menu2.setRoute("/a2/m2/1")
        menu2.setIcon(null)
        menu2.setSequenceNumber(0)
        menu2.setParent(menu1)
        menu2 = menuNodeRepository.save(menu2)

        MenuNodeEntity menu3 = new MenuNodeEntity()
        menu3.setId("a2-m2-1-1")
        menu3.setName("检索")
        menu3.setRoute("/a2/m2/1/1")
        menu3.setIcon(null)
        menu3.setSequenceNumber(0)
        menu3.setParent(menu2)
        menuNodeRepository.save(menu3)

        def menuEntity = service.get(menu2.getId())
        assert menuEntity.getParent().getRoute() == '/a2/m2'
        assert menu.getApplication().getRoute() == '/a2'
        assert menuEntity.getChildren().size() == 1

        MenuNodeEntity menuNode = new MenuNodeEntity()
        menuNode.setId("a1")
        menuNode.setName("应用1")
        menuNode.setRoute("/a1")
        menuNode.setIcon(null)
        menuNode.setSequenceNumber(0)
        menuNode.setDescription("des")
        menuNode = menuNodeRepository.save(menuNode)

        MenuNodeEntity menuNode1 = new MenuNodeEntity()
        menuNode1.setId("a1-m1")
        menuNode1.setName("菜单1")
        menuNode1.setRoute("/a1/m1")
        menuNode1.setIcon(null)
        menuNode1.setParent(menuNode)
        menuNode1.setSequenceNumber(0)
        menuNodeRepository.save(menuNode1)

        def a1 = service.get(menuNode.getId())
        assert a1.getApplication() == a1
        assert !a1.isLeaf()

        MenuNodeEntity menuNodes = new MenuNodeEntity()
        menuNodes.setId("a1-m1-3")
        menuNodes.setName("菜单13")
        menuNodes.setRoute("/a1/m1/3")
        menuNodes.setIcon(null)
        menuNodes.setParent(menuNode1)
        menuNodes.setSequenceNumber(2)
        menuNodes = menuNodeRepository.save(menuNodes)

        def menuNodeEntitys = service.get(menuNodes.getId())
        assert menuNodeEntitys.isLeaf()

    }

    @NoTx
    void initData() {
        def res1 = new ResourceNodeEntity('/test', RequestMethod.GET)
        res1.setId('test')
        res1.setName('无菜单')
        def res2 = new ResourceNodeEntity('/test1', RequestMethod.GET)
        res2.setId('test1')
        res2.setName('无角色')
        def res3 = new ResourceNodeEntity('/auth/test/*', RequestMethod.DELETE)
        res3.setId('test-d')
        res3.setName('删')
        def res4 = new ResourceNodeEntity('/auth/menus', RequestMethod.GET)
        res4.setId('test-menus')
        res4.setName('查菜单')
        def res5 = new ResourceNodeEntity('/auth/test/*', RequestMethod.PUT)
        res5.setId('test-u')
        res5.setName('改')
        def res6 = new ResourceNodeEntity('/auth/test/*', RequestMethod.GET)
        res6.setId('test-g')
        res6.setName('查询')
        def res7 = new ResourceNodeEntity('/auth/test', RequestMethod.GET)
        res7.setId('test-r')
        res7.setName('检索')
        def res8 = new ResourceNodeEntity('/auth/test', RequestMethod.POST)
        res8.setId('test-c')
        res8.setName('增')

        resourceNodeRepository.save([res1, res2, res3, res4, res5, res6, res7, res8])

        def user1 = new UserNodeEntity('t3', 'e10adc3949ba59abbe56e057f20f883e')
        user1.setId('test3')
        user1.setSuperuser(false)
        user1.setPepDtype('UserEntity')
        user1.setName('a')
        user1.setPhone('12345678903')
        user1.setEmail('test3@test.com')
        user1.setEnable(true)
        def user2 = new UserNodeEntity('t2', 'pwd')
        user2.setId('test2')
        user2.setSuperuser(false)
        user2.setPepDtype('UserEntity')
        user2.setName('b')
        user2.setPhone('12345678902')
        user2.setEmail('test2@test.com')
        user2.setEnable(true)
        def user3 = new UserNodeEntity('t1', 'pwd')
        user3.setId('test1')
        user3.setSuperuser(true)
        user3.setPepDtype('UserEntity')
        user3.setName('c')
        user3.setPhone('12345678901')
        user3.setEmail('test1@test.com')
        user3.setEnable(true)

        userNodeRepository.save([user1, user2, user3])

        def role1 = new RoleNodeEntity('testrole1')
        role1.setId('role1')
        role1.setDescription('des')
        role1.addUserNode(user2)
        def role2 = new RoleNodeEntity('testrole2')
        role2.setId('role2')
        role2.addUserNode(user2)

        roleRepository.save([role1, role2])

        def menu1 = new MenuNodeEntity('a1', '应用1', '/a1', null, null, 0)
        menu1.setDescription('des')
        def menu11 = new MenuNodeEntity('a1-m1', '菜单1', '/a1/m1', null, menu1, 0)
        def menu12 = new MenuNodeEntity('a1-m2', '菜单2', '/a1/m2', null, menu1, 1)
        def menu111 = new MenuNodeEntity('a1-m1-1', '菜单11', '/a1/m1/1', null, menu11, 0)
        def menu112 = new MenuNodeEntity('a1-m1-2', '菜单12', '/a1/m1/2', null, menu11, 1)
        def menu113 = new MenuNodeEntity('a1-m1-3', '菜单13', '/a1/m1/3', null, menu11, 2)
        menu113.add(res2)

        menuNodeRepository.save([menu1, menu11, menu12, menu111, menu112, menu113])

        def menu2 = new MenuNodeEntity('a2', 'CRUD', '/a2', null, null, 1)
        menu2.add(role1)
        menu2.add(role2)
        def menu21 = new MenuNodeEntity('a2-m1', '新建', '/a2/m1', null, menu2, 0)
        menu21.add(res8)
        menu21.add(role2)
        def menu22 = new MenuNodeEntity('a2-m2', '编辑', '/a2/m2', null, menu2, 1)
        menu22.add(role1)
        menu22.add(role2)
        def menu221 = new MenuNodeEntity('a2-m2-1', '查', '/a2/m2/1', null, menu22, 0)
        menu221.add(role2)
        def menu222 = new MenuNodeEntity('a2-m2-2', '修改', '/a2/m2/2', null, menu22, 1)
        menu222.add(role1)
        menu222.add(res5)
        def menu2211 = new MenuNodeEntity('a2-m2-1-1', '检索', '/a2/m2/1/1', null, menu221, 0)
        menu2211.add(res7)
        menu2211.add(role1)
        def menu2212 = new MenuNodeEntity('a2-m2-1-2', '查询', '/a2/m2/1/2', null, menu221, 1)
        menu2212.add(res6)
        menu2212.add(role2)
        def menu23 = new MenuNodeEntity('a2-m3', '删除', '/a2/m3', null, menu2, 2)
        menu23.add(res3)
        menu23.add(role1)

        menuNodeRepository.save([menu2, menu21, menu22, menu221, menu222, menu2211, menu2212, menu23])
    }

    @Test
    @NoTx
    void accessible() {
        initData()
        // anyone could access resource not be defined
        assert service.accessible(null, null)
        // anyone could access resource without menu
        assert service.accessible(resourceService.get('test'), null)

        mockUser('test3', 't3')
        assert service.accessible(resourceService.get('test'), 'test3')
        // could not access resource without role
        assert !service.accessible(resourceService.get('test-d'), 'test3')

        mockUser('test2', 't2')
        assert service.accessible(resourceService.get('test'), 'test2')
        assert service.accessible(resourceService.get('test-d'), 'test2')
        // normal user could not access resource without authorization
        assert !service.accessible(resourceService.get('test1'), 'test2')

        // super user could access everything
        mockUser('test1', 't1', 'pwd', true)
        assert service.accessible(null, 'test1')
        assert service.accessible(resourceService.get('test-menus'), 'test1')
        assert service.accessible(resourceService.get('test'), 'test1')
        assert service.accessible(resourceService.get('test-d'), 'test1')
        assert service.accessible(resourceService.get('test1'), 'test1')
    }

    @Test
    void testGetByIds() {
        Collection<String> ids = new HashSet<>()
        ids.add("id1")
        ids.add("id2")
        assert service.getByIds(ids).size() == 0
        assert resourceService.getByIds(ids).size() == 0

        MenuNodeEntity menuEntity = new MenuNodeEntity()
        menuEntity.setName('menu')
        menuEntity.setRoute("route")
        menuEntity = service.save(menuEntity)

        ids.clear()
        ids.add(menuEntity.getId())
        assert service.getByIds(ids).size() == 1

        ResourceNodeEntity resourceEntity = new ResourceNodeEntity()
        resourceEntity.setName('res1')
        resourceEntity.setMethod(RequestMethod.GET)
        resourceEntity.setURL('/auto')
        resourceEntity = resourceService.save(resourceEntity)

        ids.clear()
        ids.add(resourceEntity.getId())
        assert resourceService.getByIds(ids).size() == 1
    }
}
