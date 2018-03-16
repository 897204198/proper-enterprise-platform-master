package com.proper.enterprise.platform.auth.common.jpa.controller

import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.dictionary.MenuType
import com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity
import com.proper.enterprise.platform.auth.common.jpa.repository.MenuRepository
import com.proper.enterprise.platform.auth.common.jpa.repository.ResourceRepository
import com.proper.enterprise.platform.auth.common.jpa.repository.RoleRepository
import com.proper.enterprise.platform.auth.common.jpa.repository.UserGroupRepository
import com.proper.enterprise.platform.auth.common.jpa.repository.UserRepository
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

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
    MenuType menuType
    @Autowired
    UserRepository userRepository
    @Autowired
    MenuRepository menuRepository
    @Autowired
    RoleRepository roleRepository
    @Autowired
    ResourceRepository resourceRepository
    @Autowired
    UserGroupRepository userGroupRepository
    @Autowired
    DataDicRepository dataDicRepository

    @Autowired
    UserService userService

    @Autowired
    MenuService menusService

    @Autowired
    I18NService i18NService

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
        put('/auth/menus', JSONUtil.toJSON(req1), HttpStatus.BAD_REQUEST).getResponse().getContentAsString() ==
            i18NService.getMessage("pep.auth.common.menu.parent")
        // delete menu
        assert delete('/auth/menus?ids=' + id, HttpStatus.BAD_REQUEST).getResponse().getContentAsString() ==
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
        // menu add resources
        post('/auth/menus/' + id + '/resource/test-c', '', HttpStatus.CREATED)
        menuObj = JSONUtil.parse(get('/auth/menus/' + id,  HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert menuObj.get("icon") == 'test_icon_change'
        def resList = JSONUtil.parse(get('/auth/menus/' + id + '/resources',  HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'test-c'
        // delete menu
        assert delete('/auth/menus?ids=' + id, HttpStatus.BAD_REQUEST).getResponse().getContentAsString() ==
            i18NService.getMessage("pep.auth.common.menu.delete.relation.resource")
        // delete menu's role
        delete('/auth/menus/' + id + '/resource/test-c', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/menus/' + id + '/resources',  HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        // role add menu
        def addReq = [:]
        addReq['ids'] = id
        post('/auth/roles/role1/menus', JSONUtil.toJSON(addReq), HttpStatus.CREATED)
        resList = JSONUtil.parse(get('/auth/menus/' + id + '/roles',  HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'role1'
        // delete menu
        assert delete('/auth/menus?ids=' + id, HttpStatus.BAD_REQUEST).getResponse().getContentAsString() ==
            i18NService.getMessage("pep.auth.common.menu.delete.relation.role")
        // delete role's menu
        delete('/auth/roles/role1/menus?ids=' + id, HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/menus/' + id + '/roles',  HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        delete('/auth/menus?ids=' + id, HttpStatus.NO_CONTENT)
        get('/auth/menus/' + id,  HttpStatus.OK).getResponse().getContentAsString() == ''

        def parents = JSONUtil.parse(get('/auth/menus/parents',  HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)
        assert parents.size() == 5
        assert parents.get(0).size() == 12

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

        MenuEntity menuEntity = new MenuEntity()
        menuEntity.addChild(menu)
        assert menuEntity.children.size() ==1

        menuEntity.removeChild(menu)
        assert menuEntity.getChildren().size() == 0

        menuEntity.setParent(menu)
        menuEntity.getName() == "tar"
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
}
