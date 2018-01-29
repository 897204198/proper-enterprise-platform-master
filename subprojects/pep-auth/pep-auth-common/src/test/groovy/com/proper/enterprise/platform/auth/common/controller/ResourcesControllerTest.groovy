package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.api.auth.model.Resource
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.auth.common.dictionary.ResourceType
import com.proper.enterprise.platform.auth.common.entity.DataRestrainEntity
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.repository.*
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.bind.annotation.RequestMethod

@Sql([
    "/com/proper/enterprise/platform/auth/common/resources.sql",
    "/com/proper/enterprise/platform/auth/common/menus.sql",
    "/com/proper/enterprise/platform/auth/common/roles.sql",
    "/com/proper/enterprise/platform/auth/common/datadics.sql"
])
class ResourcesControllerTest extends AbstractTest {

    @Autowired
    ResourceRepository repository

    @Autowired
    ResourceType resourceType

    @Autowired
    ResourceService resourceService

    @Test
    public void checkUniqueConstraint() {
        mockUser('id', 'name', 'pwd', true)

        doPost()

        // 查询一下触发数据插入操作
        repository.findAll()
    }

    private ResourceEntity doPost() {
        def resource = [:]
        resource['url'] = '/foo/bar'
        resource['method'] = RequestMethod.PUT
        resource['enable'] = true

        JSONUtil.parse(
            post('/auth/resources',
                JSONUtil.toJSON(resource),
                HttpStatus.CREATED)
                .getResponse()
                .getContentAsString(),
            ResourceEntity
        )
    }

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
        put('/auth/resources/test-c', JSONUtil.toJSON(reqMap), HttpStatus.OK)
        resource = JSONUtil.parse(get('/auth/resources/test-c', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert resource.get('url') == '/test/url'
        assert resource.get('enable')

        // menu add resource
        post('/auth/menus/a2/resource/test-d', '', HttpStatus.CREATED)
        def resList = JSONUtil.parse(get('/auth/resources/test-d/menus',  HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'a2'
        // delete menu's resource
        delete('/auth/menus/a2/resource/test-d', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/resources/test-d/menus',  HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        // role add resource
        def addResourceReq = [:]
        addResourceReq['ids'] = 'test-u'
        post('/auth/roles/role1/resources', JSONUtil.toJSON(addResourceReq), HttpStatus.CREATED)
        resList = JSONUtil.parse(get('/auth/resources/test-u/roles',  HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'role1'
        // delete role's resource
        delete('/auth/roles/role1/resources?ids=test-u', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/resources/test-u/roles',  HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        delete('/auth/resources/test-c', HttpStatus.NO_CONTENT)
        get('/auth/menus/test-c',  HttpStatus.OK).getResponse().getContentAsString() == ''

        get('/auth/menus/test-r',  HttpStatus.OK).getResponse().getContentAsString() == ''
        get('/auth/menus/test-g',  HttpStatus.OK).getResponse().getContentAsString() == ''

        delete('/auth/resources?ids=test-u', HttpStatus.NO_CONTENT)

        resourceService.get('/test/url', RequestMethod.POST)

        String localResource = '/test/aaa'
        Resource resource11 =  resourceService.get('/test/url', RequestMethod.POST)
        Boolean res = resourceService.hasPerimissionOfResource(resource11, localResource, RequestMethod.POST)
        assert res == false
    }

    @Test
    void testCoverage1(){
        String id = "test-t"
        String id1 = "test-a"
        Collection<String> idd = new HashSet<>()
        idd.add(id)
        idd.add(id1)
        ResourceEntity resourceEntity = resourceService.getByIds(idd)
        assert resourceEntity == null
    }

    @Test
    void testEntity(){
        DataRestrainEntity data = new DataRestrainEntity()
        data.setName("ii")
        data.setTableName("ww")

        ResourceEntity resourceEntity = new ResourceEntity()
        resourceEntity.add(data)
        assert resourceEntity.dataRestrains.size() == 1

        resourceEntity.remove(data)

        String tableName = "ww"
        resourceEntity.getDataRestrains(tableName).size() == 0
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
