package com.proper.enterprise.platform.auth.common.jpa.controller

import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.dictionary.MenuType
import com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.jpa.repository.*
import com.proper.enterprise.platform.auth.common.vo.MenuVO
import com.proper.enterprise.platform.auth.common.vo.ResourceVO
import com.proper.enterprise.platform.auth.common.vo.UserVO
import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.core.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.annotation.NoTx
import groovy.json.JsonSlurper
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.bind.annotation.RequestMethod

@Sql([
    "/com/proper/enterprise/platform/auth/common/jpa/resources.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/menus.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/menus-resources.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/roles.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/roles-menus.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/users.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/users-roles.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/datadics.sql"
])
class MenusControllerTest extends AbstractJPATest {

    @Autowired
    private MenuType menuType
    @Autowired
    private UserRepository userRepository
    @Autowired
    private MenuRepository menuRepository
    @Autowired
    private RoleRepository roleRepository
    @Autowired
    private ResourceRepository resourceRepository
    @Autowired
    private ResourceService resourceService
    @Autowired
    private UserGroupRepository userGroupRepository
    @Autowired
    private DataDicRepository dataDicRepository
    @Autowired
    private UserService userService
    @Autowired
    private MenuService menusService
    @Autowired
    private I18NService i18NService

    @Autowired
    RoleService roleService

    @Test
    @NoTx
    void diffUsersGetDiffMenus() {
        // super user
        mockUser('test1', 't1', 'pwd', true)
        assert resOfGet('/auth/menus', HttpStatus.OK).count == 14

        // super user with condition
        mockUser('test1', 't1', 'pwd', true)
        assert resOfGet('/auth/menus?name=1&description=des&route=a1&enable=Y', HttpStatus.OK).count == 1

        // has role
        mockUser('test2', 't2', 'pwd', false)
        def res = resOfGet('/auth/menus', HttpStatus.OK)
        assert res.count == 8

        // without role
        mockUser('test3', 't3', 'pwd', false)
        def res1 = resOfGet('/auth/menus', HttpStatus.OK)
        assert res1.count == 0
    }

    @Test
    @NoTx
    void menuUnionTest() {
        mockUser('test1', 't1', 'pwd')

        def menu = [:]
        menu['icon'] = 'test_icon'
        menu['name'] = 'test_name1'
        menu['route'] = '/bbb'
        menu['enable'] = true
        menu['sequenceNumber'] = 55
        menu['menuCode'] = '1'
        def menuObj = JSONUtil.parse(post('/auth/menus', JSONUtil.toJSON(menu), HttpStatus.CREATED)
            .getResponse().getContentAsString(), Map.class)
        def id = menuObj.get('id')
        menuObj = JSONUtil.parse(get('/auth/menus/' + id, HttpStatus.OK)
            .getResponse().getContentAsString(), Map.class)
        assert menuObj.get("icon") == 'test_icon'

        def resource = [:]
        resource['name'] = 'test123'
        resource['url'] = 'test123'
        resource['method'] = RequestMethod.GET
        resource['enable'] = true
        resource['resourceCode'] = '2'
        def value = JSONUtil.parse(post('/auth/menus/' + menuObj.get('id') + '/resources', JSONUtil.toJSON(resource), HttpStatus.CREATED)
            .getResponse().getContentAsString(), Map.class)
        assert value.get('name') == 'test123'

        def resourcesOfMenu = resOfGet("/auth/menus/${menuObj['id']}/resources", HttpStatus.OK)
        assert resourcesOfMenu.size() > 0
        assert resourcesOfMenu.get(0).name == 'test123'

        def childMenu = [:]
        childMenu['icon'] = 'child'
        childMenu['name'] = 'child'
        childMenu['route'] = '/child'
        childMenu['enable'] = true
        childMenu['sequenceNumber'] = 56
        childMenu['menuCode'] = '1'
        childMenu['parentId'] = id
        def childObje = JSONUtil.parse(post('/auth/menus', JSONUtil.toJSON(childMenu), HttpStatus.CREATED)
            .getResponse().getContentAsString(), Map.class)
        def childId = childObje.get('id')

        def req1 = [:]
        def list1 = [id]
        req1['ids'] = list1
        req1['enable'] = false
//        put('/auth/menus', JSONUtil.toJSON(req1), HttpStatus.OK).getResponse().getContentAsString() ==
//            i18NService.getMessage("pep.auth.common.menu.parent")
        // delete menu
        delete('/auth/menus?ids=' + childId, HttpStatus.NO_CONTENT)

        def req = [:]
        def list = [id]
        req['ids'] = list
        req['enable'] = false
        put('/auth/menus', JSONUtil.toJSON(req), HttpStatus.OK)
        menuObj = JSONUtil.parse(get('/auth/menus/' + id, HttpStatus.OK)
            .getResponse().getContentAsString(), Map.class)
        assert false == menuObj.get('enable')

        req['enable'] = true
        put('/auth/menus', JSONUtil.toJSON(req), HttpStatus.OK)
        menuObj = JSONUtil.parse(get('/auth/menus/' + id, HttpStatus.OK)
            .getResponse().getContentAsString(), Map.class)
        assert menuObj.get('enable')
        assert menuObj.get("route") == "/bbb"

        menu['icon'] = 'test_icon_change'
        put('/auth/menus/' + id, JSONUtil.toJSON(menu), HttpStatus.OK)

        // role add menu
        def addReq = [:]
        addReq['ids'] = [id]
        post('/auth/roles/role1/menus', JSONUtil.toJSON(addReq), HttpStatus.CREATED)
        def resList = JSONUtil.parse(get('/auth/menus/' + id + '/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'role1'
        // delete role's menu
        delete('/auth/roles/role1/menus?ids=' + id, HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/menus/' + id + '/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        get('/auth/menus/' + id, HttpStatus.OK).getResponse().getContentAsString() == ''

        def parents = JSONUtil.parse(get('/auth/menus/parents', HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)
        assert parents.size() == 3
        assert parents.get(0).size() == 13

    }

    @Test
    @NoTx
    void testEnableMenu() {
        mockUser('test1', 't1', 'pwd')

        def menu = [:]
        menu['icon'] = 'test_icon'
        menu['name'] = 'test_name1'
        menu['route'] = '/bbb'
        menu['enable'] = true
        menu['sequenceNumber'] = 55
        menu['menuCode'] = '1'
        def menuObj = JSONUtil.parse(post('/auth/menus', JSONUtil.toJSON(menu), HttpStatus.CREATED)
            .getResponse().getContentAsString(), Map.class)
        def id = menuObj.get('id')
        menuObj = JSONUtil.parse(get('/auth/menus/' + id, HttpStatus.OK)
            .getResponse().getContentAsString(), Map.class)
        assert menuObj.get("icon") == 'test_icon'

        menu['icon'] = 'test_icon_change'
        menu['sequenceNumber'] = 88
        menu['enable'] = false
        put('/auth/menus/' + id, JSONUtil.toJSON(menu), HttpStatus.OK)
        menuObj = JSONUtil.parse(get('/auth/menus/' + id, HttpStatus.OK)
            .getResponse().getContentAsString(), Map.class)
        assert false == menuObj.get('enable')
        assert 88 == menuObj.get('sequenceNumber')

        menu['enable'] = true
        menu['sequenceNumber'] = 55
        put('/auth/menus/' + id, JSONUtil.toJSON(menu), HttpStatus.OK)
        menuObj = JSONUtil.parse(get('/auth/menus/' + id, HttpStatus.OK)
            .getResponse().getContentAsString(), Map.class)
        assert menuObj.get('enable')
        assert 55 == menuObj.get('sequenceNumber')
    }

    @Test
    void test() {
        mockUser('test1', 't1', 'pwd')
        Collection<MenuEntity> collection = new HashSet<>()
        MenuEntity menuEntity = new MenuEntity()
        menuEntity.setName("test_name1")
        menuEntity.setEnable(true)
        menuEntity.setIcon('test_icon')
        menuEntity.setSequenceNumber(55)
        menuEntity.setRoute("/bbb")
        menuEntity = menuRepository.save(menuEntity)

        MenuEntity menuEntity2 = new MenuEntity()
        menuEntity2.setName("test_name2")
        menuEntity2.setEnable(true)
        menuEntity2.setIcon('test_icon1')
        menuEntity2.setSequenceNumber(52)
        menuEntity2.setRoute("/bbc")
        menuEntity2 = menuRepository.save(menuEntity2)
        collection.add(menuEntity2.getId())
        collection.add(menuEntity.getId())

        menusService.updateEnable(collection, true)
        assert collection.size() == 2
        MenuEntity menuEntity3 = new MenuEntity()
        menuEntity3.setName("test_name3")
        menuEntity3.setEnable(true)
        menuEntity3.setIcon('test_icon1')
        menuEntity3.setSequenceNumber(52)
        menuEntity3.setRoute("/bbc/b")
        menuEntity3.setParent(menuEntity2)
        menuEntity3 = menuRepository.save(menuEntity3)
        DataTrunk menus = JSONUtil.parse(get("/auth/menus", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        Integer menuSize = menus.data.size()
        Collection<String> disableCollection = new HashSet<>()
        disableCollection.add(menuEntity2.getId())
        menusService.updateEnable(disableCollection, false)
        DataTrunk menus2 = JSONUtil.parse(get("/auth/menus", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert menus2.count + 1 == menuSize
    }

    @Test
    void testEntity() {
        MenuEntity menu = new MenuEntity()
        menu.setName("tar")
        menu.setId("9999")
        menu.setIdentifier("edit")
        menu.setMenuCode("a")
        menu.setSequenceNumber(0)

        ResourceEntity resourceEntity = new ResourceEntity()
        menu.remove(resourceEntity)

        MenuEntity menuEntity = new MenuEntity()
        menuEntity.addChild(menu)
        assert menuEntity.children.size() == 1

        menuEntity.removeChild(menu)
        assert menuEntity.getChildren().size() == 0

        menuEntity.setParent(menu)
        menuEntity.getName() == "tar"
    }

    @NoTx
    @Test
    void testPageIsOrNot() {
        // 分页的两种情况
        mockUser('test1', 't1', 'pwd', true)
        Authentication.setCurrentUserId('test1')
        def value = JSONUtil.parse(get('/auth/menus?name=&description=&route=&enable=&pageNo=1&pageSize=2', HttpStatus.OK).getResponse()
            .getContentAsString(), DataTrunk.class)
        value.count == 14
        value.count.value == 2
        value.data[0].size() == 12

        def value1 = resOfGet('/auth/menus?name=&description=&route=&enable=Y', HttpStatus.OK)
        value1.size() == 2

        def result = resOfGet('/auth/menus?name=菜单1&description=&route=a1&enable=Y&pageNo=1&pageSize=2', HttpStatus.OK)
        result.count == 4
        result.data[0].size() == 12

        def value2 = resOfGet('/auth/menus?name=菜单2&description=&parentId=a1&route=/a1/m2&enable=Y&pageNo=1&pageSize=2', HttpStatus.OK)
        value2.count == 1
    }

    @Test
    @NoTx
    void testMenuResources() {
        mockUser('test1', 't1', 'pwd', true)

        ResourceEntity resourceEntity1 = new ResourceEntity()
        resourceEntity1.addURL("/foo11/bar")
        resourceEntity1.setName("name11")
        resourceEntity1.setMethod(RequestMethod.GET)
        resourceEntity1.setEnable(true)
        resourceEntity1 = resourceService.save(resourceEntity1)

        ResourceEntity resourceEntity2 = new ResourceEntity()
        resourceEntity2.addURL("/foo22/bar")
        resourceEntity2.setName("name22")
        resourceEntity2.setEnable(false)
        resourceEntity2.setMethod(RequestMethod.POST)
        resourceEntity2 = resourceRepository.save(resourceEntity2)

        MenuEntity menuEntity = new MenuEntity()
        menuEntity.setId("idd")
        menuEntity.setName("test_namea")
        menuEntity.setEnable(true)
        menuEntity.setIcon('test_icona')
        menuEntity.setSequenceNumber(50)
        menuEntity.setRoute("/bbc")

        menuEntity.add(resourceEntity1)
        menuEntity.add(resourceEntity2)
        menuEntity = menuRepository.save(menuEntity)

        def res = JSONUtil.parse(get('/auth/menus/resources', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert res.size() == 15
        assert res.get(2).name == 'test_namea'
        assert res.get(2).resources.size() == 1
        assert res.get(2).resources.name.get(0) == 'name11'
        assert !res.contains('name22')

        MenuEntity parentEntity = new MenuEntity()
        parentEntity.setName("123")
        parentEntity.setSequenceNumber(1)
        parentEntity.setRoute("123")
        parentEntity = menuRepository.save(parentEntity)
        MenuVO menuVO = JSONUtil.parse(get('/auth/menus/' + menuEntity.getId(), HttpStatus.OK).getResponse().getContentAsString(), MenuVO.class)
        menuVO.setParentId(parentEntity.getId())
        MenuVO updateVO = JSONUtil.parse(put('/auth/menus/' + menuVO.getId(), JSONUtil.toJSON(menuVO), HttpStatus.OK).getResponse().getContentAsString(), MenuVO.class)
        List<MenuVO> menuVOs = JSONUtil.parse(get('/auth/menus/resources', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        for (MenuVO menu : menuVOs) {
            if ("/bbc".equals(menu.getRoute())) {
                assert menu.getParentId() == parentEntity.getId()
                assert menu.getResources().size() == 1
            }
        }
    }

    @Test
    void testMenuResourcesDelete() {
        mockUser('test1', 't1', 'pwd', true)

        ResourceEntity resourceEntity = new ResourceEntity()
        resourceEntity.addURL("/testMenuResource/delete")
        resourceEntity.setName("MenuResourcesDelete")
        resourceEntity.setMethod(RequestMethod.DELETE)
        resourceEntity.setEnable(true)
        resourceEntity = resourceService.save(resourceEntity)

        MenuEntity menuEntity = new MenuEntity()
        menuEntity.setId("tmrDelete")
        menuEntity.setName("test_menu_resource_delete")
        menuEntity.setEnable(true)
        menuEntity.setIcon('test_icona')
        menuEntity.setSequenceNumber(50)
        menuEntity.setRoute("/testMenuResource")

        menuEntity.add(resourceEntity)
        menuEntity = menuRepository.save(menuEntity)

        def resBefore = JSONUtil.parse(get('/auth/menus/' + menuEntity.getId() +'/resources', HttpStatus.OK).getResponse().getContentAsString(), List.class)

        delete('/auth/menus/' + menuEntity.getId() + '/resources/' + resourceEntity.getId(), HttpStatus.NO_CONTENT)
        def resAfter = JSONUtil.parse(get('/auth/menus/' + menuEntity.getId() +'/resources', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert 1 == resBefore.size() - resAfter.size()
    }

    @Sql("/com/proper/enterprise/platform/auth/common/jpa/datadics.sql")
    @Test
    void testMenuType() {
        menuType.getCatalog()
        menuType.page().getCode()
        menuType.app().getCode()
        menuType.function().getCode()
    }

    @After
    void tearDown() {
        userGroupRepository.deleteAll()
        userRepository.deleteAll()
        roleRepository.deleteAll()
        menuRepository.deleteAll()
        resourceRepository.deleteAll()
        dataDicRepository.deleteAll()
    }

    @Test
    void testMenuOrder() {
        mockUser('test1', 't1', 'pwd', true)

        // 初始化列表测试
        def value = JSONUtil.parse(get('/auth/menus?name=&description=&route=&enable=&parentId=-1&pageNo=1&pageSize=20', HttpStatus.OK).getResponse()
            .getContentAsString(), DataTrunk.class)
        assert value.data.size() == 2
        assert value.data[0].name == '应用1'
        assert value.data[1].name == 'CRUD'

        // 分页测试
        value = JSONUtil.parse(get('/auth/menus?name=&description=&route=&enable=&parentId=-1&pageNo=2&pageSize=1', HttpStatus.OK).getResponse()
            .getContentAsString(), DataTrunk.class)
        assert value.data.size() == 1
        assert value.data[0].name == 'CRUD'

        // 点击节点后列表页面查询测试
        value = JSONUtil.parse(get('/auth/menus?name=&description=&route=&enable=&parentId=a1&pageNo=1&pageSize=10', HttpStatus.OK).getResponse()
            .getContentAsString(), DataTrunk.class)
        assert value.data.size() == 2
        assert value.data[0].name == '菜单1'
        assert value.data[1].name == '菜单2'
    }

    @Test
    @NoTx
    void couldAddSameRouteAfterDelete() {
        def menu = [:]
        menu['icon'] = 'test_icon'
        menu['name'] = 'test_name1'
        menu['route'] = '/bbb'
        menu['enable'] = true
        menu['sequenceNumber'] = 55
        menu['menuCode'] = '1'
        def newMenu = new JsonSlurper().parseText(post('/auth/menus', JSONUtil.toJSON(menu), HttpStatus.CREATED).response.contentAsString)

        def id = newMenu.id
        delete("/auth/menus?ids=$id", HttpStatus.NO_CONTENT)

        post('/auth/menus', JSONUtil.toJSON(menu), HttpStatus.CREATED)
    }

    @Test
    @NoTx
    void addSameRouteWillGetErrAfterDisable() {
        def menu = [:]
        menu['icon'] = 'test_icon'
        menu['name'] = 'test_name1'
        menu['route'] = '/bbb/ccc'
        menu['enable'] = true
        menu['sequenceNumber'] = 55
        menu['menuCode'] = '1'
        def newMenu = new JsonSlurper().parseText(post('/auth/menus', JSONUtil.toJSON(menu), HttpStatus.CREATED).response.contentAsString)

        def id = newMenu.id
        menu['id'] = id
        menu['enable'] = false
        put("/auth/menus/$id", JSONUtil.toJSON(menu), HttpStatus.OK)

        menu.remove('id')
        menu['enable'] = true
        def res = post('/auth/menus', JSONUtil.toJSON(menu), HttpStatus.INTERNAL_SERVER_ERROR).response
        assert res.getHeader(PEPConstants.RESPONSE_HEADER_ERROR_TYPE) == PEPConstants.RESPONSE_BUSINESS_ERROR
    }

    @Test
    @NoTx
    void menuTreeHaveResourceTest() {
        mockUser("test", "test", "test", true)
        def menuParent = [:]
        menuParent['icon'] = 'test_icon'
        menuParent['name'] = 'test_name1'
        menuParent['route'] = '/ccc2/ddd'
        menuParent['enable'] = true
        menuParent['sequenceNumber'] = 55
        menuParent['menuCode'] = '1'
        MenuVO menuParentVO = JSONUtil.parse(post('/auth/menus', JSONUtil.toJSON(menuParent), HttpStatus.CREATED).getResponse().getContentAsString(), MenuVO.class)
        def menuChildren = [:]
        menuChildren['icon'] = 'test_icon'
        menuChildren['name'] = 'test_name2'
        menuChildren['route'] = '/ccc/ddd/eee'
        menuChildren['enable'] = true
        menuChildren['sequenceNumber'] = 55
        menuChildren['menuCode'] = '1'
        menuChildren['parentId'] = menuParentVO.getId()
        MenuVO menuChildrenVO = JSONUtil.parse(post('/auth/menus', JSONUtil.toJSON(menuChildren), HttpStatus.CREATED).getResponse().getContentAsString(), MenuVO.class)
        ResourceVO resourceVO = new ResourceVO()
        resourceVO.setName('resource')
        resourceVO.addURL("/aa")
        resourceVO.setIdentifier("edit")

        post('/auth/menus/' + menuChildrenVO.getId() + "/resources", JSONUtil.toJSON(resourceVO), HttpStatus.CREATED)
        List<MenuVO> menuVOs = JSONUtil.parse(get('/auth/menus/resources', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert menuVOs.size() > 0
        boolean validate = false
        for (MenuVO menuVO : menuVOs) {
            if ("/ccc/ddd/eee".equals(menuVO.getRoute())) {
                assert menuVO.getParentId() == menuParentVO.getId()
                List<ResourceVO> resourceVOs = menuVO.getResources()
                assert resourceVOs.get(0).name == resourceVO.getName()
                validate = true
            }
        }
        assert validate
    }


    @Test
    void testLevel3Menu() {
        def menu1 = [:]
        menu1['icon'] = 'test_icon'
        menu1['name'] = 'test_Level_1'
        menu1['route'] = '/bbb/ccc'
        menu1['enable'] = true
        menu1['sequenceNumber'] = 55
        menu1['menuCode'] = '1'
        MenuVO menuVO1 = JSONUtil.parse(post('/auth/menus', JSONUtil.toJSON(menu1), HttpStatus.CREATED).response.contentAsString, MenuVO.class)
        def menu2 = [:]
        menu2['icon'] = 'test_icon'
        menu2['name'] = 'test_Level_2'
        menu2['route'] = '/bbb/ccc/ddd'
        menu2['enable'] = true
        menu2['sequenceNumber'] = 55
        menu2['menuCode'] = '1'
        menu2['parentId'] = menuVO1.getId()
        MenuVO menuVO2 = JSONUtil.parse(post('/auth/menus', JSONUtil.toJSON(menu2), HttpStatus.CREATED).response.contentAsString, MenuVO.class)
        def menu3 = [:]
        menu3['icon'] = 'test_icon'
        menu3['name'] = 'test_Level_3'
        menu3['route'] = '/bbb/ccc/ddd/eee'
        menu3['enable'] = true
        menu3['sequenceNumber'] = 55
        menu3['menuCode'] = '1'
        menu3['parentId'] = menuVO2.getId()
        MenuVO menuVO3 = JSONUtil.parse(post('/auth/menus', JSONUtil.toJSON(menu3), HttpStatus.CREATED).response.contentAsString, MenuVO.class)
        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('menurole')
        roleEntity = roleService.save(roleEntity)
        def addReq = ['ids': [menuVO3.getId()]]
        post('/auth/roles/' + roleEntity.getId() + '/menus', JSONUtil.toJSON(addReq), HttpStatus.CREATED)
        def userReq = [:]
        userReq['username'] = 'td_username'
        userReq['name'] = 'td_name'
        userReq['password'] = 'td_password'
        userReq['email'] = 'td_email'
        userReq['phone'] = '12345678901'
        userReq['enable'] = true
        userReq['avatar'] = 'avatar'
        UserVO userVO = JSONUtil.parse(post("/auth/users", JSONUtil.toJSON(userReq), HttpStatus.CREATED).getResponse().getContentAsString(), UserVO.class)
        post('/auth/users/' + userVO.getId() + '/role/' + roleEntity.getId(), JSONUtil.toJSON(addReq), HttpStatus.CREATED)

        mockUser(userVO.getId(), userVO.getUsername(), userVO.getPassword())
        def res = resOfGet('/auth/menus', HttpStatus.OK)
        assert res.count == 3
    }
}
