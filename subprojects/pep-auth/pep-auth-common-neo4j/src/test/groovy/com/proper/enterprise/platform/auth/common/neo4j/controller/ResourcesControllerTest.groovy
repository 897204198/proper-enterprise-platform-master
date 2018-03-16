package com.proper.enterprise.platform.auth.common.neo4j.controller

import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.auth.common.dictionary.MenuType
import com.proper.enterprise.platform.auth.common.dictionary.ResourceType
import com.proper.enterprise.platform.auth.common.neo4j.entity.MenuNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.ResourceNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.RoleNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.repository.ResourceNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.RoleNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserGroupNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserNodeRepository
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMethod

class ResourcesControllerTest extends AbstractNeo4jTest {

    @Autowired
    ResourceNodeRepository repository
    @Autowired
    ResourceService resourceService
    @Autowired
    UserNodeRepository userNodeRepository
    @Autowired
    RoleNodeRepository roleNodeRepository
    @Autowired
    UserGroupNodeRepository userGroupNodeRepository
    @Autowired
    I18NService i18NService
    @Autowired
    ResourceType resourceType
    @Autowired
    MenuType menuType

    @Before
    void loginUser() {
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
        userNodeRepository.deleteAll()
        repository.deleteAll()
        repository.deleteAll()
        roleNodeRepository.deleteAll()
        userGroupNodeRepository.deleteAll()
    }

    @Test
    void testPostResource() {
        doPost()
        repository.findAll()
    }

    private ResourceNodeEntity doPost() {
        Map<String, Object> resource = new HashMap()
        resource.put("url", '/fo1/bar')
        resource.put("method", RequestMethod.PUT)
        resource.put("enable", true)

        JSONUtil.parse(post('/auth/resources', JSONUtil.toJSON(resource), HttpStatus.CREATED).getResponse().getContentAsString(), ResourceNodeEntity)
    }

    @Test
    void testResource() {
        // super user
        //  路径 : '/auth/resources/'
        ResourceNodeEntity resourceNodeEntity = new ResourceNodeEntity()
        resourceNodeEntity.setName("name")
        resourceNodeEntity.setURL("/foo/bar")
        resourceNodeEntity.setMethod(RequestMethod.PUT)
        resourceNodeEntity.setEnable(true)
        resourceNodeEntity.setValid(true)
        resourceNodeEntity.setCreateUserId("userId")
        resourceNodeEntity = repository.save(resourceNodeEntity)

        def result = get('/auth/resources/' + resourceNodeEntity.getId(), HttpStatus.OK).getResponse().getContentAsString()
        result = JSONUtil.parse(result, ResourceNodeEntity.class)
        assert result.getId() == resourceNodeEntity.getId()

        def req = [:]
        def list = [resourceNodeEntity.getId()]
        req['ids'] = list
        req['enable'] = false
        put('/auth/resources', JSONUtil.toJSON(req), HttpStatus.OK)
        result = get('/auth/resources/' + resourceNodeEntity.getId(), HttpStatus.OK).getResponse().getContentAsString()
        assert result == ''

        ResourceNodeEntity resource = new ResourceNodeEntity()
        resource.setId("test-u")
        resource.setName("改")
        resource.setURL("/auth/test/*")
        resource.setMethod(RequestMethod.PUT)
        resource.setCreateUserId("userId")
        repository.save(resource)
        delete('/auth/resources?ids=test-u', HttpStatus.NO_CONTENT)
        result = get('/auth/resources/' + resource.getId(), HttpStatus.OK).getResponse().getContentAsString()
        assert result == ''

        // 路径 : '/auth/resources/{resourceId}'
        ResourceNodeEntity resources = new ResourceNodeEntity()
        resources.setId("test-c")
        resources.setName("增")
        resources.setURL("/test/url")
        resources.setMethod(RequestMethod.POST)
        resources.setCreateUserId("userId")
        resources = repository.save(resources)
        put('/auth/resources/test-c', JSONUtil.toJSON(resources), HttpStatus.OK)
        result = get('/auth/resources/' + resources.getId(), HttpStatus.OK).getResponse().getContentAsString()
        assert result.contains("/test/url")
        assert result.contains('enable')

        ResourceNodeEntity entity = new ResourceNodeEntity()
        entity.setId("test-ee")
        entity.setName("name")
        entity.setURL("/test/*")
        entity.setMethod(RequestMethod.PUT)
        entity.setCreateUserId("userId")
        repository.save(entity)
        delete('/auth/resources/test-ee', HttpStatus.NO_CONTENT)
        get('/auth/resources/' + entity.getId(), HttpStatus.OK).getResponse().getContentAsString() == ''

        // 路径 : "/auth/resources/{resourceId}/menus"
        // menu add resource
        ResourceNodeEntity resourceEntity = new ResourceNodeEntity()
        resourceEntity.setId("test-d")
        resourceEntity.setName('删')
        resourceEntity.setMethod(RequestMethod.DELETE)
        resourceEntity.setURL('/auth/test/*')
        resourceEntity = resourceService.save(resourceEntity)

        MenuNodeEntity menu = new MenuNodeEntity()
        menu.setId("a2")
        menu.setName("CRUD")
        menu.setRoute("/a2")
        menu.setIcon(null)
        menu.setSequenceNumber(1)
        menu.add(resourceEntity)
        menu = repository.save(menu)
        post('/auth/menus/' + menu.getId() + '/resource/' + resourceEntity.getId(), '', HttpStatus.CREATED)
        def resList = JSONUtil.parse(get('/auth/resources/test-d/menus', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'a2'

        // menu delete resource
        delete('/auth/menus/' + menu.getId() + '/resource/' + resourceEntity.getId(), HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/resources/test-d/menus', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        // 路径 : "/auth/resources/{resourceId}/roles"
        // role add resource
        ResourceNodeEntity entity1 = new ResourceNodeEntity()
        entity1.setId("test-u")
        entity1.setName('改')
        entity1.setMethod(RequestMethod.PUT)
        entity1.setURL('/auth/test/*')
        entity1 = resourceService.save(entity1)
        RoleNodeEntity roleNodeEntity = new RoleNodeEntity()
        roleNodeEntity.addResource(entity1)
        roleNodeEntity.setName("testrole")
        roleNodeEntity.setId("role1")
        roleNodeEntity.setDescription("des")
        roleNodeRepository.save(roleNodeEntity)
        post('/auth/roles/role1/resources', JSONUtil.toJSON(roleNodeEntity), HttpStatus.CREATED)
        resList = JSONUtil.parse(get('/auth/resources/' + entity1.getId() + '/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'role1'
        // role delete resource
        delete('/auth/roles/' + roleNodeEntity.getId() + '/resources?ids=test-u', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/resources/' + entity1.getId() + '/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        // deleteByIds 2 exception
        RoleNodeEntity roles = new RoleNodeEntity()
        roles.setName("testrole2")
        roles.setId("role2")
        roles.setDescription("des")
        roleNodeRepository.save(roles)
        ResourceNodeEntity entity11 = new ResourceNodeEntity()
        entity11.setId("test-ua")
        entity11.setName('查')
        entity11.setMethod(RequestMethod.GET)
        entity11.setURL('/auth/test/1')
        entity11.add(roles)
        resourceService.save(entity11)
        post('/auth/roles/role1/resources', JSONUtil.toJSON(roles), HttpStatus.CREATED)
        delete('/auth/resources?ids=test-ua', HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService.getMessage("pep.auth.common.resource" +
            ".delete.relation.role")

        MenuNodeEntity menu1 = new MenuNodeEntity()
        menu1.setId("a2")
        menu1.setName("CRUD")
        menu1.setRoute("/a2")
        menu1.setIcon(null)
        menu1.setSequenceNumber(1)
        menu1 = repository.save(menu1)
        ResourceNodeEntity entity12 = new ResourceNodeEntity()
        entity12.setId("test-ub")
        entity12.setName('删')
        entity12.setMethod(RequestMethod.DELETE)
        entity12.setURL('/auth/test/2')
        entity12.add(menu1)
        resourceService.save(entity11)
        post('/auth/menus/' + menu1.getId() + '/resource/' + resourceEntity.getId(), '', HttpStatus.CREATED)
        delete('/auth/resources?ids=test-d', HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService.getMessage("pep.auth.common.resource.delete.relation.menu")

        // test service.get()
        ResourceNodeEntity entity13 = new ResourceNodeEntity()
        entity13.setId("test-uc")
        entity13.setName('增')
        entity13.setMethod(RequestMethod.POST)
        entity13.setURL('/test/url1')
        resourceService.save(entity13)
        resourceService.get('/test/url1', RequestMethod.POST)

    }
}
