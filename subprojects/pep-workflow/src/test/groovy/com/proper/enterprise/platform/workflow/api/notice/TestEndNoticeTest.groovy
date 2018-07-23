package com.proper.enterprise.platform.workflow.api.notice

import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.flowable.engine.IdentityService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class TestEndNoticeTest extends AbstractTest {

    private static final String END_NOTICE_KEY = "testEndNotice"

    @Autowired
    private IdentityService identityService

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/adminUsers.sql"])
    public void testEndNotice() {
        identityService.setAuthenticatedUserId("PEP_SYS")
        Authentication.setCurrentUserId("PEP_SYS")
        start(END_NOTICE_KEY, new HashMap<String, Object>())
        assert END_NOTICE_KEY == Authentication.getCurrentUserId()
    }

    private String start(String key, Map<String, Object> form) {
        PEPProcInstVO pepProcInstVO = JSONUtil.parse(post('/workflow/process/' + key, JSONUtil.toJSON(form), HttpStatus.CREATED).getResponse().getContentAsString(), PEPProcInstVO.class)
        return pepProcInstVO.getProcInstId()
    }

}
