package com.proper.enterprise.platform.auth.common.jpa.controller

import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.dictionary.MenuType
import com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.jpa.repository.*
import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractTest
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
class MenusControllerTest extends AbstractTest {

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
    private UserGroupRepository userGroupRepository
    @Autowired
    private DataDicRepository dataDicRepository
    @Autowired
    private UserService userService
    @Autowired
    private MenuService menusService
    @Autowired
    private I18NService i18NService

    @Test
    @NoTx
    void diffUsersGetDiffMenus() {
        // super user
        mockUser('test1', 't1', 'pwd', true)
        assert resOfGet('/auth/menus', HttpStatus.OK).size() == 14

        // super user with condition
        mockUser('test1', 't1', 'pwd', true)
        assert resOfGet('/auth/menus?name=1&description=des&route=a1&enable=Y', HttpStatus.OK).size() == 1

        // has role
        mockUser('test2', 't2', 'pwd', false)
        def res = resOfGet('/auth/menus', HttpStatus.OK)
        assert res.size() == 8

        // without role
        mockUser('test3', 't3', 'pwd', false)
        def res1 = resOfGet('/auth/menus', HttpStatus.OK)
        assert res1.size() == 0
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
        menuObj = JSONUtil.parse(get('/auth/menus/' + id,  HttpStatus.OK)
            .getResponse().getContentAsString(), Map.class)
        assert menuObj.get("icon") == 'test_icon'

        def resource = [:]
        resource['name'] = 'test123'
        resource['url'] = 'test123'
        resource['method'] = RequestMethod.GET
        resource['enable'] = true
        resource['resourceCode'] = '2'
        def value = JSONUtil.parse(post('/auth/menus/'+ menuObj.get('id') + '/resources', JSONUtil.toJSON(resource), HttpStatus.CREATED)
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
        put('/auth/menus', JSONUtil.toJSON(req1), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() ==
            i18NService.getMessage("pep.auth.common.menu.parent")
        // delete menu
        assert delete('/auth/menus?ids=' + id, HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() ==
            i18NService.getMessage("pep.auth.common.menu.delete.relation.failed")
        delete('/auth/menus?ids=' + childId, HttpStatus.NO_CONTENT)

        def req = [:]
        def list = ['test-c']
        req['ids'] = list
        req['enable'] = true
        put('/auth/menus', JSONUtil.toJSON(req), HttpStatus.OK)
        menuObj = JSONUtil.parse(get('/auth/menus/' + id,  HttpStatus.OK)
            .getResponse().getContentAsString(), Map.class)
        assert menuObj.get('enable')
        menuObj.get("url") == "/auth/test"

        menu['icon'] = 'test_icon_change'
        put('/auth/menus/' + id, JSONUtil.toJSON(menu), HttpStatus.OK)
        // delete menu
        assert delete('/auth/menus?ids=' + id, HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() ==
            i18NService.getMessage("pep.auth.common.menu.delete.relation.resource")

        // role add menu
        def addReq = [:]
        addReq['ids'] = [id]
        post('/auth/roles/role1/menus', JSONUtil.toJSON(addReq), HttpStatus.CREATED)
        def resList = JSONUtil.parse(get('/auth/menus/' + id + '/roles',  HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'role1'
        // delete menu
        assert delete('/auth/menus?ids=' + id, HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() ==
            i18NService.getMessage("pep.auth.common.menu.delete.relation.resource")
        // delete role's menu
        delete('/auth/roles/role1/menus?ids=' + id, HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/menus/' + id + '/roles',  HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        delete('/auth/menus?ids=' + id, HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() ==
                i18NService.getMessage("pep.auth.common.menu.delete.relation.resource")
        get('/auth/menus/' + id,  HttpStatus.OK).getResponse().getContentAsString() == ''

        def parents = JSONUtil.parse(get('/auth/menus/parents',  HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)
        assert parents.size() == 6
        assert parents.get(0).size() == 13

    }

    @Test
    void test(){
        mockUser('test1', 't1', 'pwd')
        Collection<MenuEntity> collection = new HashSet<>()
        MenuEntity menuEntity =new MenuEntity()
        menuEntity.setName("test_name1")
        menuEntity.setEnable(true)
        menuEntity.setIcon('test_icon')
        menuEntity.setSequenceNumber(55)
        menuEntity.setRoute("/bbb")
        menuEntity = menuRepository.save(menuEntity)

        MenuEntity menuEntity2 =new MenuEntity()
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
    }

    @Test
    void testCoverage(){
        String id = "test-t"
        String id1 = "test-a"
        Collection<String> idd = new HashSet<>()
        idd.add(id)
        idd.add(id1)
        Collection<MenuEntity> menus = menusService.getByIds(idd)
        assert menus.size() == 0
    }

    @Test
    void testEntity(){
        MenuEntity menu =  new MenuEntity()
        menu.setName("tar")
        menu.setId("9999")
        menu.setIdentifier("edit")
        menu.setMenuCode("a")

        ResourceEntity resourceEntity = new ResourceEntity()
        menu.remove(resourceEntity)

        MenuEntity menuEntity = new MenuEntity()
        menuEntity.addChild(menu)
        assert menuEntity.children.size() ==1

        menuEntity.removeChild(menu)
        assert menuEntity.getChildren().size() == 0

        menuEntity.setParent(menu)
        menuEntity.getName() == "tar"
    }

    @NoTx
    @Test
    void testPageIsOrNot(){
        // 分页的两种情况
        mockUser('test1', 't1', 'pwd', true)
        def value = JSONUtil.parse(get('/auth/menus?name=&description=&route=&enable=&pageNo=1&pageSize=2' ,HttpStatus.OK).getResponse()
                .getContentAsString(), DataTrunk.class)
        value.count == 14
        value.count.value == 2
        value.data[0].size() == 12

        def value1 = resOfGet('/auth/menus?name=&description=&route=&enable=Y', HttpStatus.OK)
        value1.size() == 2

       def result =  resOfGet('/auth/menus?name=菜单1&description=&route=a1&enable=Y&pageNo=1&pageSize=2', HttpStatus.OK)
        result.count == 4
        result.data[0].size()== 12

        def value2 = resOfGet('/auth/menus?name=菜单2&description=&parentId=a1&route=/a1/m2&enable=Y&pageNo=1&pageSize=2', HttpStatus.OK)
        value2.count == 1
    }

    @Test
    void testMenuResources(){
        mockUser('test1', 't1', 'pwd', true)

        ResourceEntity resourceEntity1 = new ResourceEntity()
        resourceEntity1.setURL("/foo11/bar")
        resourceEntity1.setName("name11")
        resourceEntity1.setMethod(RequestMethod.GET)
        resourceEntity1 = resourceRepository.save(resourceEntity1)

        ResourceEntity resourceEntity2 = new ResourceEntity()
        resourceEntity2.setURL("/foo22/bar")
        resourceEntity2.setName("name22")
        resourceEntity2.setEnable(false)
        resourceEntity2.setValid(false)
        resourceEntity2.setMethod(RequestMethod.POST)
        resourceEntity2 = resourceRepository.save(resourceEntity2)

        MenuEntity menuEntity =new MenuEntity()
        menuEntity.setId("idd")
        menuEntity.setName("test_namea")
        menuEntity.setEnable(true)
        menuEntity.setIcon('test_icona')
        menuEntity.setSequenceNumber(50)
        menuEntity.setRoute("/bbc")

        menuEntity.add(resourceEntity1)
        menuEntity.add(resourceEntity2)
        menuRepository.save(menuEntity)

        def res = resOfGet('/auth/menus/resources', HttpStatus.OK)
        assert res.size() == 15
        assert res.get(2).name == 'test_namea'
        assert res.get(2).resources.size() == 1
        assert res.get(2).resources.name.get(0) == 'name11'
        assert !res.contains('name22')
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
    void testMenuOrder(){
        mockUser('test1', 't1', 'pwd', true)

        // 初始化列表测试
        def value = JSONUtil.parse(get('/auth/menus?name=&description=&route=&enable=&parentId=-1&pageNo=1&pageSize=20' ,HttpStatus.OK).getResponse()
            .getContentAsString(), DataTrunk.class)
        assert value.data.size() == 2
        assert value.data[0].name == '应用1'
        assert value.data[1].name == 'CRUD'

        // 分页测试
        value = JSONUtil.parse(get('/auth/menus?name=&description=&route=&enable=&parentId=-1&pageNo=2&pageSize=1' ,HttpStatus.OK).getResponse()
            .getContentAsString(), DataTrunk.class)
        assert value.data.size() == 1
        assert value.data[0].name == 'CRUD'

        // 点击节点后列表页面查询测试
        value = JSONUtil.parse(get('/auth/menus?name=&description=&route=&enable=&parentId=a1&pageNo=1&pageSize=10' ,HttpStatus.OK).getResponse()
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
}
