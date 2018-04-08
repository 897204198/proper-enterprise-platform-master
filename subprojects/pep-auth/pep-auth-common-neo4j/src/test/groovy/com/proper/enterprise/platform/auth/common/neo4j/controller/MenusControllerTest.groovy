package com.proper.enterprise.platform.auth.common.neo4j.controller

import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.auth.common.dictionary.MenuType
import com.proper.enterprise.platform.auth.common.neo4j.repository.MenuNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.ResourceNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.RoleNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserGroupNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.entity.MenuNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.ResourceNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.RoleNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserNodeRepository
import com.proper.enterprise.platform.auth.neo4j.repository.*
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMethod

class MenusControllerTest extends AbstractNeo4jTest {
    @Autowired
    UserNodeRepository userNodeRepository
    @Autowired
    MenuNodeRepository repository
    @Autowired
    MenuService service
    @Autowired
    I18NService i18NService
    @Autowired
    MenuType menuType
    @Autowired
    RoleNodeRepository roleRepository
    @Autowired
    UserGroupNodeRepository userGroupNodeRepository
    @Autowired
    ResourceNodeRepository resourceNodeRepository


    @Before
    void loginUser() {
        tearDown()
        UserNodeEntity userNodeEntity = new UserNodeEntity()
        userNodeEntity.setName('admin')
        userNodeEntity.setPassword('123456')
        userNodeEntity.setPhone('15948525865')
        userNodeEntity.setUsername('admin')
        userNodeEntity.setSuperuser(true)
        userNodeEntity = userNodeRepository.save(userNodeEntity)
        mockUser(userNodeEntity.getId(), userNodeEntity.getUsername(), userNodeEntity.getPassword())
    }

    @After
    void cleanAll() {
        tearDown()
    }

    void tearDown() {
        repository.deleteAll()
        roleRepository.deleteAll()
        userGroupNodeRepository.deleteAll()
        resourceNodeRepository.deleteAll()
        userNodeRepository.deleteAll()
    }

    @Test
    @Sql(value = "/com/proper/enterprise/platform/auth/common/neo4j/datadics.sql")
    @Transactional(transactionManager = "jpaTransactionManager",propagation=Propagation.REQUIRES_NEW)
    void menuUnionTest() {
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

        def parents = JSONUtil.parse(get('/auth/menus/parents', HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)
        assert parents.size() == 1
        assert parents.get(0).size() == 20

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
        menuObj = JSONUtil.parse(get('/auth/menus/' + id, HttpStatus.OK)
            .getResponse().getContentAsString(), Map.class)
        assert menuObj.get('enable')
        menuObj.get("url") == "/auth/test"

        menu['icon'] = 'test_icon_change'
        put('/auth/menus/' + id, JSONUtil.toJSON(menu), HttpStatus.OK)
        // menu add resources
        ResourceNodeEntity resources = new ResourceNodeEntity()
        resources.setId("test-c")
        resources.setName("增")
        resources.setURL("/test/url")
        resources.setMethod(RequestMethod.POST)
        resources.setCreateUserId("userId")
        repository.save(resources)
        post('/auth/menus/' + id + '/resource/test-c', '', HttpStatus.CREATED)
        menuObj = JSONUtil.parse(get('/auth/menus/' + id, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert menuObj.get("icon") == 'test_icon_change'
        def resList = JSONUtil.parse(get('/auth/menus/' + id + '/resources', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'test-c'
        // delete menu
        assert delete('/auth/menus?ids=' + id, HttpStatus.BAD_REQUEST).getResponse().getContentAsString() ==
            i18NService.getMessage("pep.auth.common.menu.delete.relation.resource")
        // delete menu's role
        delete('/auth/menus/' + id + '/resource/test-c', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/menus/' + id + '/resources', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        // role add menu
        RoleNodeEntity role = new RoleNodeEntity()
        role.setId("role1")
        role.setName("name")
        roleRepository.save(role)
        def addReq = [:]
        addReq['ids'] = id
        post('/auth/roles/role1/menus', JSONUtil.toJSON(addReq), HttpStatus.CREATED)
        resList = JSONUtil.parse(get('/auth/menus/' + id + '/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'role1'
        // delete menu
        assert delete('/auth/menus?ids=' + id, HttpStatus.BAD_REQUEST).getResponse().getContentAsString() ==
            i18NService.getMessage("pep.auth.common.menu.delete.relation.role")
        // delete role's menu
        delete('/auth/roles/role1/menus?ids=' + id, HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/menus/' + id + '/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        delete('/auth/menus?ids=' + id, HttpStatus.NO_CONTENT)
        get('/auth/menus/' + id, HttpStatus.OK).getResponse().getContentAsString() == ''
    }

    @Test
    void test() {
        Collection<MenuNodeEntity> collection = new HashSet<>()
        MenuNodeEntity menuEntity = new MenuNodeEntity()
        menuEntity.setName("test_name1")
        menuEntity.setEnable(true)
        menuEntity.setIcon('test_icon')
        menuEntity.setSequenceNumber(55)
        menuEntity.setRoute("/bbb")
        menuEntity = repository.save(menuEntity)

        MenuNodeEntity menuEntity2 = new MenuNodeEntity()
        menuEntity2.setName("test_name2")
        menuEntity2.setEnable(true)
        menuEntity2.setIcon('test_icon1')
        menuEntity2.setSequenceNumber(52)
        menuEntity2.setRoute("/bbc")
        menuEntity2 = repository.save(menuEntity2)
        collection.add(menuEntity2.getId())
        collection.add(menuEntity.getId())

        service.updateEnable(collection, true)
        assert collection.size() == 2

    }

    @Test
    void testCoverage() {
        String id = "test-t"
        String id1 = "test-a"
        Collection<String> idd = new HashSet<>()
        idd.add(id)
        idd.add(id1)
        Collection<MenuNodeEntity> menus = service.getByIds(idd)
        assert menus.size() == 0
    }

    @Test
    void testMenusPageIsOrNot() {
        MenuNodeEntity menuNodeEntity
        for (int i = 0; i < 20; i++) {
            menuNodeEntity = new MenuNodeEntity()
            menuNodeEntity.setName('sun' + i + 's1')
            menuNodeEntity.setDescription('sas' + i)
            menuNodeEntity.setRoute('sses' + i + '@ww.com')
            menuNodeEntity.setEnable(true)
            menuNodeEntity.setSequenceNumber(i)
            repository.save(menuNodeEntity)
        }

        def resAll = JSONUtil.parse(get('/auth/menus?name=sun&description=&route=&enable=y&pageNo=1&pageSize=20', HttpStatus.OK).getResponse()
                .getContentAsString(), DataTrunk.class)
        assert resAll.data.size() == 20
        assert resAll.count == 20
        assert resAll.data[0].get("name") == 'sun0s1'

        resAll = JSONUtil.parse(get('/auth/menus?name=sun&description=&route=&menuEnable=DISABLE&pageNo=1&pageSize=20', HttpStatus.OK)
                .getResponse()
                .getContentAsString(), DataTrunk.class)
        assert resAll.count == 0

        resAll = JSONUtil.parse(get('/auth/menus?name=&description=&route=&menuEnable=&pageNo=3&pageSize=2', HttpStatus.OK).getResponse()
                .getContentAsString(), DataTrunk.class)
        assert resAll.data.size() == 2
        assert resAll.count == 20
        assert resAll.data[0].get("name") == 'sun4s1'
        assert resAll.data[1].get("name") == 'sun5s1'

        resAll = JSONUtil.parse(get('/auth/menus?name=&description=&route=48585688&menuEnable=&pageNo=3&pageSize=2', HttpStatus.OK).getResponse()
                .getContentAsString(), DataTrunk.class)
        assert resAll.count == 0

        def resAllPage = JSONUtil.parse(get('/auth/menus?name=&description=&route=&menuEnable=&pageNo=1&pageSize=2',
                HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAllPage.count == 20
        assert resAllPage.data.size() == 2
        def resAllCollect = JSONUtil.parse(get('/auth/menus?name=&description=&route=&menuEnable=&',
                HttpStatus.OK).getResponse().getContentAsString(), ArrayList.class)
        assert resAllCollect.size() == 20
    }
}
