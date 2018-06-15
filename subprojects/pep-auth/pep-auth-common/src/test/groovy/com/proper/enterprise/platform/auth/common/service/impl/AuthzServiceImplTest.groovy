package com.proper.enterprise.platform.auth.common.service.impl

import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AuthzServiceImplTest extends AbstractTest {

    @Autowired
    private ResourceService resourceService

    @Autowired
    private MenuService menuService

    @Test
    void shouldIgnoreTest() {
        def service = new AuthzServiceImpl(menuService, resourceService)

        def patterns = [
            'GET:/auth/resources',
            'GET:/workflow/service/model/*/json',
            'GET:/auth/resources/*/*/xml',
            'GET:/workflow/service/model/3*/json',
            'GET:/workflow/*.html',
            'POST:/auth/login',
            'PUT:/jwt/filter/ignore/method',
            '*/workflow/**'
        ]
        service.setPatterns(patterns.join(","))

        service.setHasContext(false)
        assert service.shouldIgnore('/workflow/service/model/50/json', 'GET')
        assert service.shouldIgnore('/workflow/service/model/5/0/json', 'GET')
        assert service.shouldIgnore('/auth/resources', 'GET')
        assert service.shouldIgnore('/auth/resources?name=1&type=2', 'GET')
        assert service.shouldIgnore('/auth/resources/1/2/xml', 'GET')
        assert !service.shouldIgnore('/auth/resources/12/xml', 'GET')
        assert !service.shouldIgnore('/auth/resources/1/2/xml', 'POST')
        assert service.shouldIgnore('/workflow/service/model/3225/json', 'GET')
        assert !service.shouldIgnore('非法://内容', 'GET')

        service.setHasContext(true)
        assert service.shouldIgnore('/pep/auth/login', 'POST')
        assert !service.shouldIgnore('/pep/auth/login', 'GET')
    }

}
