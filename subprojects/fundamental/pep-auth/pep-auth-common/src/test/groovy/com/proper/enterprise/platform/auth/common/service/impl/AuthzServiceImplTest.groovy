package com.proper.enterprise.platform.auth.common.service.impl

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockServletContext

import javax.annotation.Resource

class AuthzServiceImplTest extends AbstractJPATest {

    @Autowired
    private ResourceService resourceService

    @Autowired
    private MenuService menuService

    @Autowired
    private AccessTokenService accessTokenService

    @Test
    void shouldIgnoreTest() {
        def service = new AuthzServiceImpl(menuService, resourceService, accessTokenService)

        def ignoreList = []
        ignoreList[0] = 'GET:/auth/resources'
        ignoreList[1] = 'GET:/workflow/service/model/*/json'
        ignoreList[2] = 'GET:/auth/resources/*/*/xml'
        ignoreList[3] = 'GET:/workflow/service/model/3*/json'
        ignoreList[4] = 'GET:/workflow/*.html'
        ignoreList[5] = 'POST:/auth/login'
        ignoreList[6] = 'PUT:/jwt/filter/ignore/method'
        ignoreList[7] = '*/workflow/**'

        service.setIgnoreList(ignoreList)

        assert service.shouldIgnore(reserve('/workflow/service/model/50/json', 'GET'))
        assert service.shouldIgnore(reserve('/workflow/service/model/5/0/json', 'GET'))
        assert service.shouldIgnore(reserve('/auth/resources', 'GET'))
        assert service.shouldIgnore(reserve('/auth/resources?name=1&type=2', 'GET'))
        assert service.shouldIgnore(reserve('/auth/resources/1/2/xml', 'GET'))
        assert !service.shouldIgnore(reserve('/auth/resources/12/xml', 'GET'))
        assert !service.shouldIgnore(reserve('/auth/resources/1/2/xml', 'POST'))
        assert service.shouldIgnore(reserve('/workflow/service/model/3225/json', 'GET'))
        assert !service.shouldIgnore(reserve('非法://内容', 'GET'))

        assert service.shouldIgnore(reserve('/pep', '/auth/login', 'POST'))
        assert !service.shouldIgnore(reserve('/pep', '/auth/login', 'GET'))
    }

    private static def reserve(String url, String method) {
        new MockHttpServletRequest(method, url)
    }

    private static def reserve(String contextPath, String url, String method) {
        def servletContext = new MockServletContext()
        servletContext.setContextPath(contextPath)
        new MockHttpServletRequest(servletContext, method, url)
    }

    @Resource(name = "ignorePatternsList")
    private List ignorePatternsList

    @Test
    void hasBeanList() {
        // com.proper.enterprise.platform.configs.PEPConfiguration.ignorePatternsListOptions
        boolean flag = false
        for (String str : ignorePatternsList) {
            if ("OPTIONS:/**".equals(str)) {
                flag = true
            }
        }
        assert flag
    }

}
