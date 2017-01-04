package com.proper.enterprise.platform.auth.common.controller
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMethod

class ResourcesControllerTest extends AbstractTest {

    @Autowired
    ResourceRepository repository

    @Test(expected = DataIntegrityViolationException)
    public void checkUniqueConstraint() {
        mockUser('id', 'name', 'pwd', true)

        doPost()
        doPost()

        // 查询一下触发数据插入操作
        repository.findAll()
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
    public void checkCRUD() {
        def resource = new ResourceEntity()
        resource.setURL('/foo/bar')
        resource.setMethod(RequestMethod.PUT)

        checkBaseCRUD('/auth/resources', resource)
    }

}
