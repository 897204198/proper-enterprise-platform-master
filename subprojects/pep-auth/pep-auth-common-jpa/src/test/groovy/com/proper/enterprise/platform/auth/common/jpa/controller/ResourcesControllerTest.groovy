package com.proper.enterprise.platform.auth.common.jpa.controller

import com.proper.enterprise.platform.api.auth.model.Resource
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.auth.common.dictionary.ResourceType
import com.proper.enterprise.platform.auth.common.jpa.entity.DataRestrainEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.MenuEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity
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
import org.springframework.web.bind.annotation.RequestMethod

@Sql([
    "/com/proper/enterprise/platform/auth/common/jpa/resources.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/menus.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/roles.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/datadics.sql"
])
class ResourcesControllerTest extends AbstractTest {

    @Autowired
    ResourceRepository repository

    @Autowired
    ResourceType resourceType

    @Autowired
    ResourceService resourceService

    @Autowired
    I18NService i18NService

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
        Collection<Resource> resources = resourceService.getByIds(idd)
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
    void testGetResourceMenus(){
        ResourceEntity resourceEntity = new ResourceEntity()
        resourceEntity.setName('resource')
        resourceEntity.setURL("/aa")
        resourceEntity.setIdentifier("edit")
        resourceEntity = resourceService.save(resourceEntity)

        MenuEntity menuEntity = new MenuEntity()
        menuEntity.setName('menu1')
        menuEntity.setRoute("route1")
        menuEntity.add(resourceEntity)
        menuEntity = menuRepository.save(menuEntity)

        def value = JSONUtil.parse(get('/auth/resources/'+ resourceEntity.getId() + '/menus', HttpStatus.OK).getResponse().getContentAsString(), java
                .lang.Object.class)
        assert value.size() == 1
        assert value.get(0).name == "menu1"

        delete('/auth/resources?ids='+ resourceEntity.getId(), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() == i18NService.getMessage(" pep.auth.common.resource.delete.relation.menu")

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
