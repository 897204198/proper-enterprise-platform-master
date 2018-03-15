package com.proper.enterprise.platform.auth.neo4j.service.impl

import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.auth.neo4j.entity.MenuNodeEntity
import com.proper.enterprise.platform.auth.neo4j.entity.ResourceNodeEntity
import com.proper.enterprise.platform.auth.neo4j.entity.RoleNodeEntity
import com.proper.enterprise.platform.auth.neo4j.entity.UserNodeEntity
import com.proper.enterprise.platform.auth.neo4j.repository.*
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
        ResourceNodeEntity resourceNodeEntity = new ResourceNodeEntity()
        resourceNodeEntity.setId('test')
        resourceNodeEntity.setName('无菜单')
        resourceNodeEntity.setURL('/test')
        resourceNodeEntity.setMethod(RequestMethod.GET)
        resourceNodeEntity = resourceNodeRepository.save(resourceNodeEntity)

        ResourceNodeEntity resourceNodeEntity1 = new ResourceNodeEntity()
        resourceNodeEntity1.setId('test1')
        resourceNodeEntity1.setName('无角色')
        resourceNodeEntity1.setURL('/test1')
        resourceNodeEntity1.setMethod(RequestMethod.GET)
        resourceNodeEntity1 = resourceNodeRepository.save(resourceNodeEntity1)

        ResourceNodeEntity resourceNodeEntity2 = new ResourceNodeEntity()
        resourceNodeEntity2.setId('test-d')
        resourceNodeEntity2.setName('删')
        resourceNodeEntity2.setURL('/auth/test/*')
        resourceNodeEntity2.setMethod(RequestMethod.DELETE)
        resourceNodeEntity2 = resourceNodeRepository.save(resourceNodeEntity2)

        ResourceNodeEntity resourceNodeEntity3 = new ResourceNodeEntity()
        resourceNodeEntity3.setId('test-menus')
        resourceNodeEntity3.setName('查菜单')
        resourceNodeEntity3.setURL('/auth/menus')
        resourceNodeEntity3.setMethod(RequestMethod.GET)
        resourceNodeEntity3 = resourceNodeRepository.save(resourceNodeEntity3)

        ResourceNodeEntity resourceNodeEntity4 = new ResourceNodeEntity()
        resourceNodeEntity4.setId('test-u')
        resourceNodeEntity4.setName('改')
        resourceNodeEntity4.setURL('/auth/test/*')
        resourceNodeEntity4.setMethod(RequestMethod.PUT)
        resourceNodeEntity4 = resourceNodeRepository.save(resourceNodeEntity4)

        ResourceNodeEntity resourceNodeEntity5 = new ResourceNodeEntity()
        resourceNodeEntity5.setId('test-g')
        resourceNodeEntity5.setName('查询')
        resourceNodeEntity5.setURL('/auth/test/*')
        resourceNodeEntity5.setMethod(RequestMethod.GET)
        resourceNodeEntity5 = resourceNodeRepository.save(resourceNodeEntity5)

        ResourceNodeEntity resourceNodeEntity6 = new ResourceNodeEntity()
        resourceNodeEntity6.setId('test-r')
        resourceNodeEntity6.setName('检索')
        resourceNodeEntity6.setURL('/auth/test')
        resourceNodeEntity6.setMethod(RequestMethod.GET)
        resourceNodeEntity6 = resourceNodeRepository.save(resourceNodeEntity6)

        ResourceNodeEntity resourceNodeEntity7 = new ResourceNodeEntity()
        resourceNodeEntity7.setId('test-c')
        resourceNodeEntity7.setName('增')
        resourceNodeEntity7.setURL('/auth/test')
        resourceNodeEntity7.setMethod(RequestMethod.POST)
        resourceNodeEntity7 = resourceNodeRepository.save(resourceNodeEntity7)

        UserNodeEntity userNodeEntity = new UserNodeEntity()
        userNodeEntity.setId('test3')
        userNodeEntity.setUsername('t3')
        userNodeEntity.setPassword('e10adc3949ba59abbe56e057f20f883e')
        userNodeEntity.setSuperuser(false)
        userNodeEntity.setPepDtype('UserEntity')
        userNodeEntity.setName('a')
        userNodeEntity.setPhone('12345678903')
        userNodeEntity.setEmail('test3@test.com')
        userNodeEntity.setEnable(true)
        userNodeEntity = userNodeRepository.save(userNodeEntity)

        UserNodeEntity userNodeEntity1 = new UserNodeEntity()
        userNodeEntity1.setId('test2')
        userNodeEntity1.setUsername('t2')
        userNodeEntity1.setPassword('pwd')
        userNodeEntity1.setSuperuser(false)
        userNodeEntity1.setPepDtype('UserEntity')
        userNodeEntity1.setName('b')
        userNodeEntity1.setPhone('12345678902')
        userNodeEntity1.setEmail('test2@test.com')
        userNodeEntity1.setEnable(true)
        userNodeEntity1 = userNodeRepository.save(userNodeEntity1)

        UserNodeEntity userNodeEntity2 = new UserNodeEntity()
        userNodeEntity2.setId('test1')
        userNodeEntity2.setUsername('t1')
        userNodeEntity2.setPassword('pwd')
        userNodeEntity2.setSuperuser(true)
        userNodeEntity2.setPepDtype('UserEntity')
        userNodeEntity2.setName('c')
        userNodeEntity2.setPhone('12345678901')
        userNodeEntity2.setEmail('test1@test.com')
        userNodeEntity2.setEnable(true)
        userNodeEntity2 = userNodeRepository.save(userNodeEntity2)

        RoleNodeEntity roleNodeEntity = new RoleNodeEntity()
        roleNodeEntity.setId('role1')
        roleNodeEntity.setName('testrole')
        roleNodeEntity.setDescription('des')
        roleNodeEntity.addUserNode(userNodeEntity1)
        roleNodeEntity = roleRepository.save(roleNodeEntity)

        RoleNodeEntity roleNodeEntity1 = new RoleNodeEntity()
        roleNodeEntity1.setId('role2')
        roleNodeEntity1.setName('testrole')
        roleNodeEntity1.addUserNode(userNodeEntity1)
        roleNodeEntity1 = roleRepository.save(roleNodeEntity1)

        MenuNodeEntity menuNodeEntity1 = new MenuNodeEntity('a1', '应用1', '/a1', null, null, 0)
        menuNodeEntity1.setDescription('des')
        menuNodeRepository.save(menuNodeEntity1)
        MenuNodeEntity menuNodeEntity11 = new MenuNodeEntity('a1-m1', '菜单1', '/a1/m1', null, menuNodeEntity1, 0)
        menuNodeRepository.save(menuNodeEntity11)
        MenuNodeEntity menuNodeEntity12 = new MenuNodeEntity('a1-m2', '菜单2', '/a1/m2', null, menuNodeEntity1, 1)
        menuNodeRepository.save(menuNodeEntity12)
        MenuNodeEntity menuNodeEntity111 = new MenuNodeEntity('a1-m1-1', '菜单11', '/a1/m1/1', null, menuNodeEntity11, 0)
        menuNodeRepository.save(menuNodeEntity111)
        MenuNodeEntity menuNodeEntity112 = new MenuNodeEntity('a1-m1-2', '菜单12', '/a1/m1/2', null, menuNodeEntity11, 1)
        menuNodeRepository.save(menuNodeEntity112)
        MenuNodeEntity menuNodeEntity113 = new MenuNodeEntity('a1-m1-3', '菜单13', '/a1/m1/3', null, menuNodeEntity11, 2)
        menuNodeEntity113.add(resourceNodeEntity1)
        menuNodeRepository.save(menuNodeEntity113)
        MenuNodeEntity menuNodeEntity2 = new MenuNodeEntity('a2', 'CRUD', '/a2', null, null, 1)
        menuNodeEntity2.add(roleNodeEntity)
        menuNodeEntity2.add(roleNodeEntity1)
        menuNodeRepository.save(menuNodeEntity2)
        MenuNodeEntity menuNodeEntity21 = new MenuNodeEntity('a2-m1', '新建', '/a2/m1', null, menuNodeEntity2, 0)
        menuNodeEntity21.add(resourceNodeEntity7)
        menuNodeEntity21.add(roleNodeEntity1)
        menuNodeRepository.save(menuNodeEntity21)
        MenuNodeEntity menuNodeEntity22 = new MenuNodeEntity('a2-m2', '编辑', '/a2/m2', null, menuNodeEntity2, 1)
        menuNodeEntity22.add(roleNodeEntity)
        menuNodeEntity22.add(roleNodeEntity1)
        menuNodeRepository.save(menuNodeEntity22)
        MenuNodeEntity menuNodeEntity221 = new MenuNodeEntity('a2-m2-1', '查', '/a2/m2/1', null, menuNodeEntity22, 0)
        menuNodeEntity221.add(roleNodeEntity1)
        menuNodeRepository.save(menuNodeEntity221)
        MenuNodeEntity menuNodeEntity222 = new MenuNodeEntity('a2-m2-2', '修改', '/a2/m2/2', null, menuNodeEntity22, 1)
        menuNodeEntity222.add(roleNodeEntity)
        menuNodeEntity222.add(resourceNodeEntity4)
        menuNodeRepository.save(menuNodeEntity222)
        MenuNodeEntity menuNodeEntity2211 = new MenuNodeEntity('a2-m2-1-1', '检索', '/a2/m2/1/1', null, menuNodeEntity221, 0)
        menuNodeEntity2211.add(resourceNodeEntity6)
        menuNodeEntity2211.add(roleNodeEntity)
        menuNodeRepository.save(menuNodeEntity2211)
        MenuNodeEntity menuNodeEntity2212 = new MenuNodeEntity('a2-m2-1-2', '查询', '/a2/m2/1/2', null, menuNodeEntity221, 1)
        menuNodeEntity2212.add(resourceNodeEntity5)
        menuNodeEntity2212.add(roleNodeEntity1)
        menuNodeRepository.save(menuNodeEntity2212)
        MenuNodeEntity menuNodeEntity23 = new MenuNodeEntity('a2-m3', '删除', '/a2/m3', null, menuNodeEntity2, 2)
        menuNodeEntity23.add(resourceNodeEntity2)
        menuNodeEntity23.add(roleNodeEntity)
        menuNodeRepository.save(menuNodeEntity23)

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
