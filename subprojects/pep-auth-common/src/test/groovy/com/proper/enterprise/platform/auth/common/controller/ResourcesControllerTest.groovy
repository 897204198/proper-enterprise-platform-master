package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.api.auth.enums.ResourceType
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MvcResult
import org.springframework.web.bind.annotation.RequestMethod

@Sql
class ResourcesControllerTest extends AbstractTest {

    @Autowired
    ResourceRepository repository

    @Test
    public void checkUniqueConstraint() {
        mockUser('id', 'name', 'pwd', true)

        doPost()
        doPost()

        // 查询一下触发数据插入操作
        def result = get('/auth/resources', HttpStatus.INTERNAL_SERVER_ERROR)
        assert result.getResponse().getContentAsString().contains('ConstraintViolationException')
    }

    private ResourceEntity doPost() {
        def resource = new ResourceEntity()
        resource.setURL('/foo/bar')
        resource.setMethod(RequestMethod.PUT)

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
    public void notFoundAnyResources() {
        assert get('/auth/resources', HttpStatus.OK).getResponse().getContentLength() == 0
    }

    @Test
    public void normalUserRetrieve() {
        mockUser('test-user2', 'user', '123456', false)
        def resources = toResources(get('/auth/resources', HttpStatus.OK))
        assert resources.length == 2
    }

    private static ResourceEntity[] toResources(MvcResult result) {
        def str = result.response.contentAsString
        println str
        JSONUtil.parse(str, ResourceEntity[])
    }

    @Test
    public void normalUserRetrieveWithType() {
        mockUser('test-user2', 'user', '123456', false)
        def resources = toResources(get('/auth/resources?type=API', HttpStatus.OK))
        assert resources.length == 1
        assert resources[0].resourceType == ResourceType.API
    }

    @Test
    public void superUserRetrieve() {
        mockUser('test-user1', 'admin', '123456', true)
        def resources = toResources get('/auth/resources', HttpStatus.OK)
        assert resources.length == 4
    }

    @Test
    public void superUserRetrieveWithType() {
        mockUser('test-user1', 'admin', '123456', true)
        def resources = toResources get('/auth/resources?type=MENU', HttpStatus.OK)
        assert resources.length == 2
        resources.each {
            assert it.resourceType == ResourceType.MENU
        }
    }

    @Test
    public void checkCRUD() {
        def resource = new ResourceEntity()
        resource.setURL('/foo/bar')
        resource.setMethod(RequestMethod.PUT)

        checkBaseCRUD('/auth/resources', resource)
    }

}
