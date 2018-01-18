package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.auth.common.dictionary.ResourceType
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.bind.annotation.RequestMethod

class ResourcesControllerTest extends AbstractTest {

    @Autowired
    ResourceRepository repository

    @Autowired
    ResourceType resourceType

    @Test(expected = DataIntegrityViolationException)
    public void checkUniqueConstraint() {
        mockUser('id', 'name', 'pwd', true)

        doPost()
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
    public void checkCRUD() {
        def resource = new ResourceEntity()
        resource.setURL('/foo/bar')
        resource.setMethod(RequestMethod.PUT)

        checkBaseCRUD('/auth/resources', resource)
    }

    @Sql("/com/proper/enterprise/platform/auth/common/resources.sql")
    @Test
    void resourcesUnionTest() {
        def resource = JSONUtil.parse(get('/auth/resources/test-c', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert resource.get('url') == '/auth/test'
        assert resource.get('enable')

        def req = [:]
        def list = ['test-c']
        req['ids'] = list
        req['enable'] = false
        put('/auth/resources', JSONUtil.toJSON(req), HttpStatus.OK)
        resource = JSONUtil.parse(get('/auth/resources/test-c', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert !resource.get('enable')

        def reqMap = [:]
        reqMap['url'] = '/test/url'
        reqMap['enable'] = true
        reqMap['method'] = 'POST'
        put('/auth/resources/test-c', JSONUtil.toJSON(reqMap), HttpStatus.OK)
        resource = JSONUtil.parse(get('/auth/resources/test-c', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert resource.get('url') == '/test/url'
        assert resource.get('enable')

        delete('/auth/resources/test-c', HttpStatus.NO_CONTENT)
        get('/auth/menus/test-c',  HttpStatus.OK).getResponse().getContentAsString() == ''

        get('/auth/menus/test-r',  HttpStatus.OK).getResponse().getContentAsString() != ''
        get('/auth/menus/test-g',  HttpStatus.OK).getResponse().getContentAsString() != ''
        delete('/auth/menus?ids=test-r,test-g', HttpStatus.NO_CONTENT)

        get('/auth/menus/test-r',  HttpStatus.OK).getResponse().getContentAsString() == ''
        get('/auth/menus/test-g',  HttpStatus.OK).getResponse().getContentAsString() == ''
    }

    @Sql("/com/proper/enterprise/platform/auth/common/datadics.sql")
    @Test
    void testResourceType() {
        resourceType.getCatalog()
        resourceType.method().getCode()
    }

}
