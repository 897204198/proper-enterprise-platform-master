package com.proper.enterprise.platform.auth.common.controller
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.integration.AbstractTest
import org.hibernate.exception.ConstraintViolationException
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.bind.annotation.RequestMethod

import static org.junit.Assert.fail

@Sql
class ResourcesControllerTest extends AbstractTest {

    @Autowired
    ResourceRepository repository

    @Test
    public void checkUniqueConstraint() {
        mockUser('id', 'name', 'pwd', true)

        def resource = new ResourceEntity()
        resource.setURL('/foo/bar')
        resource.setMethod(RequestMethod.PUT)

        doPost(resource)
        doPost(resource)

        try {
            get('/auth/resources', HttpStatus.OK) // 查询一下触发数据插入操作
            fail() //remember this line, else 'may' false positive
        } catch (Exception e) {
            assert e.cause.cause instanceof ConstraintViolationException
        }
    }

    private ResourceEntity doPost(ResourceEntity resourceEntity) {
        JSONUtil.parse(
            post('/auth/resources',
                JSONUtil.toJSON(resourceEntity),
                HttpStatus.CREATED)
                .getResponse()
                .getContentAsString(),
            ResourceEntity
        )
    }

    @Test
    public void notFoundAnyResources() {
        get('/auth/resources', HttpStatus.NOT_FOUND)
    }

    @Test
    public void normalUserRetrieve() {
        // TODO
        mockUser('test-user2', 'user', '123456', false)
        get('/auth/resources', HttpStatus.OK)
    }

    @Test
    public void normalUserRetrieveWithType() {
        // TODO
    }

    @Test
    public void superUserRetrieve() {
        mockUser('test-user1', 'admin', '123456', true)
        get('/auth/resources', HttpStatus.OK)
    }

    @Test
    public void superUserRetrieveWithType() {
        mockUser('test-user1', 'admin', '123456', true)
        get('/auth/resources', HttpStatus.OK)
    }

}
