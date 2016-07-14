package com.proper.enterprise.platform.auth.common.controller
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.integration.AbstractTest
import org.hibernate.exception.ConstraintViolationException
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMethod

import static org.junit.Assert.fail

class ResourcesControllerTest extends AbstractTest {

    @Autowired
    ResourceRepository repository

    @Test
    public void checkUniqueConstraint() {
        def resource = new ResourceEntity()
        resource.setURL('/foo/bar')
        resource.setMethod(RequestMethod.PUT)

        doPost(resource)
        doPost(resource)

        System.setProperty('test.mockUser.isSuper', 'true')
        ConfCenter.reload()

        try {
            get('/auth/resources', HttpStatus.OK) // 查询一下触发数据插入操作
            fail() //remember this line, else 'may' false positive
        } catch (Exception e) {
            assert e.cause.cause instanceof ConstraintViolationException
        } finally {
            System.clearProperty('test.mockUser.isSuper')
            ConfCenter.reload()
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
    public void normalUserRetrieve() {
        // TODO
    }

    @Test
    public void superUserRetrieve() {
        System.setProperty('test.mockUser.isSuper', 'true')
        ConfCenter.reload()

        // TODO
        get('/auth/resources', HttpStatus.NOT_FOUND)

        System.clearProperty('test.mockUser.isSuper')
        ConfCenter.reload()
    }

    // TODO with type

}
