package com.proper.enterprise.platform.auth.common.jpa.controller

import com.proper.enterprise.platform.api.auth.dao.ResourceDao
import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.model.Menu
import com.proper.enterprise.platform.api.auth.model.Resource
import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.auth.common.dictionary.ResourceType
import com.proper.enterprise.platform.auth.common.jpa.entity.DataRestrainEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.jpa.repository.*
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.core.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.bind.annotation.RequestMethod

@Sql([
    "/com/proper/enterprise/platform/auth/common/jpa/resources.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/menus.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/roles.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/datadics.sql"
])
class ResourcesControllerTest extends AbstractJPATest {

    @Autowired
    ResourceRepository repository

    @Autowired
    ResourceType resourceType

    @Autowired
    ResourceService resourceService

    @Autowired
    MenuService menuService

    @Autowired
    I18NService i18NService

    @Autowired
    ResourceDao resourceDao

    @Test
    @NoTx
    void resourcesUnionTest() {
        // super user
        mockUser('test1', 't1', 'pwd')

        def resource = JSONUtil.parse(get('/auth/resources/test-c', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert resource.get('url') == '/auth/test'
        assert resource.get('enable')

        mockUser('test5', 't5', 'pwd', true)
        def req = [:]
        def list = ['test-c']
        req['ids'] = list
        req['enable'] = true
        put('/auth/resources', JSONUtil.toJSON(req), HttpStatus.OK)
        resource = JSONUtil.parse(get('/auth/resources/test-c', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert resource.get('enable')
        assert resource.get("url") == "/auth/test"

        def reqMap = [:]
        reqMap['url'] = '/test/url'
        reqMap['enable'] = true
        reqMap['method'] = 'POST'
        reqMap['identifier'] = 'edit'
        put('/auth/resources/test-c', JSONUtil.toJSON(reqMap), HttpStatus.OK)
        resource = JSONUtil.parse(get('/auth/resources/test-c', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert resource.get('url') == '/test/url'
        assert resource.get('enable')

        // role add resource
        def addResourceReq = [:]
        addResourceReq['ids'] = ['test-u']
        post('/auth/roles/role1/resources', JSONUtil.toJSON(addResourceReq), HttpStatus.CREATED)
        def resList = JSONUtil.parse(get('/auth/resources/test-u/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'role1'
        // delete role's resource
        delete('/auth/roles/role1/resources?ids=test-u', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/resources/test-u/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        delete('/auth/resources/test-c', HttpStatus.NO_CONTENT)
        get('/auth/menus/test-c', HttpStatus.OK).getResponse().getContentAsString() == ''

        get('/auth/menus/test-r', HttpStatus.OK).getResponse().getContentAsString() == ''
        get('/auth/menus/test-g', HttpStatus.OK).getResponse().getContentAsString() == ''

        delete('/auth/resources?ids=test-u', HttpStatus.NO_CONTENT)

        resourceService.get('/test/url', RequestMethod.POST)
    }

    @NoTx
    @Test
    void testErrMsgException() {
        mockUser('test5', 't5', 'pwd', true)

        def addResource = [:]
        addResource['ids'] = ['test-u']

        post('/auth/roles/role1/resources', JSONUtil.toJSON(addResource), HttpStatus.CREATED)
        delete('/auth/resources?ids=test-u', HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() == i18NService.getMessage("pep.auth.common.resource" +
            ".delete.relation.role")

    }

    @Test
    void testCoverage1() {
        String id = "test-t"
        String id1 = "test-a"
        Collection<String> idd = new HashSet<>()
        idd.add(id)
        idd.add(id1)
        Collection<Resource> resources = resourceService.findAll(idd, EnableEnum.ALL)
        assert resources.size() == 0
    }

    @Test
    void testEntity() {
        DataRestrainEntity data = new DataRestrainEntity()
        data.setName("ii")
        data.setTableName("ww")
        data.setSqlStr("sql")
        data.setFilterName("filterName")

        ResourceEntity resourceEntity = new ResourceEntity()
        resourceEntity.setResourceCode("aa")
        resourceEntity.add(data)
        assert resourceEntity.dataRestrains.size() == 1
        assert resourceEntity.dataRestrains.get(0).name == "ii"
        assert resourceEntity.dataRestrains.get(0).tableName == "ww"
        assert resourceEntity.dataRestrains.get(0).sqlStr == "sql"
        assert resourceEntity.dataRestrains.get(0).filterName == "filterName"

        resourceEntity.remove(data)

        String tableName = "ww"
        resourceEntity.getDataRestrains(tableName).size() == 0
    }

    @NoTx
    @Test
    void testGetResourceMenus() {
        ResourceEntity resourceEntity = new ResourceEntity()
        resourceEntity.setName('resource')
        resourceEntity.addURL("/aa")
        resourceEntity.setIdentifier("edit")
        resourceEntity = resourceService.save(resourceEntity)

        MenuEntity menuEntity = new MenuEntity()
        menuEntity.setName('menu1')
        menuEntity.setRoute("route1")
        menuEntity.add(resourceEntity)
        menuEntity = menuService.save(menuEntity)

        def value = JSONUtil.parse(get('/auth/resources/' + resourceEntity.getId() + '/menus', HttpStatus.OK).getResponse().getContentAsString(), java
            .lang.Object.class)
        assert value.size() == 1
        assert value.get(0).name == "menu1"

        delete('/auth/resources?ids=' + resourceEntity.getId(), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() == i18NService.getMessage(" pep.auth.common.resource.delete.relation.menu")
        menuRepository.deleteAll()
        resourceDao.deleteAll()
    }

    @NoTx
    @Test
    void testSaveOrUpdateResource() {
        ResourceEntity resourceEntity = new ResourceEntity()
        resourceEntity.setName('resource')
        resourceEntity.addURL("/aa")
        resourceEntity.setIdentifier("edit")
        resourceEntity = resourceService.save(resourceEntity)

        MenuEntity menuEntity = new MenuEntity()
        menuEntity.setName('menu1')
        menuEntity.setRoute("route1")
        menuEntity.setEnable(true)
        menuEntity.add(resourceEntity)
        menuEntity = menuService.save(menuEntity)

        MenuEntity menuEntity1 = new MenuEntity()
        menuEntity1.setName('menu2')
        menuEntity1.setRoute("route2")
        menuEntity1.setEnable(true)
        menuEntity1.add(resourceEntity)
        menuEntity1 = menuService.save(menuEntity1)

        Collection<? extends Menu> collection = new HashSet<>()
        collection.add(menuEntity)
        collection.add(menuEntity1)

        def reqMap = [:]
        reqMap['url'] = '/test/aa'
        reqMap['enable'] = true
        reqMap['method'] = 'POST'
        reqMap['identifier'] = 'edit'
        put('/auth/resources/' + resourceEntity.getId(), JSONUtil.toJSON(reqMap), HttpStatus.OK)
        def resource = JSONUtil.parse(get('/auth/resources/' + resourceEntity.getId(), HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert resource.get('url') == '/test/aa'
        assert resource.get('enable')

    }

    @Test
    void testResourceType() {
        resourceType.getCatalog()
        resourceType.method().getCode()
    }

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
