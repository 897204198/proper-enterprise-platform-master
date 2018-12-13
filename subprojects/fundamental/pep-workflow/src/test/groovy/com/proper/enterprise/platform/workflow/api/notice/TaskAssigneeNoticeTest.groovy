package com.proper.enterprise.platform.workflow.api.notice

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.core.PEPPropertiesLoader
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.sys.datadic.enums.AppConfigEnum
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import com.proper.enterprise.platform.test.AbstractJPATest;
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.api.AbstractWorkFlowNoticeSupport
import com.proper.enterprise.platform.workflow.model.PEPWorkflowNoticeUrlParam;
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.apache.commons.codec.binary.Base64
import org.flowable.engine.IdentityService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql


class TaskAssigneeNoticeTest extends AbstractJPATest {

    private static final String TASK_ASSIGNEE_NOTICE_KEY = "taskAssigneeNotice"
    @Autowired
    private IdentityService identityService

    @Autowired
    private UserService userService

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/adminUsers.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void taskAssigneeNotice() {
        identityService.setAuthenticatedUserId("PEP_SYS")
        Authentication.setCurrentUserId("PEP_SYS")
        start(TASK_ASSIGNEE_NOTICE_KEY, new HashMap<String, Object>())
        String param = Authentication.getCurrentUserId().replace(DataDicUtil.get(AppConfigEnum.WEB_ADDRESS).getName(), "")
            .replace(AbstractWorkFlowNoticeSupport.TASK_PAGE_URL, "")
        Base64 base64Decoder = new Base64()
        PEPWorkflowNoticeUrlParam noticeUrlParam = JSONUtil.parse(URLDecoder.decode(new String(base64Decoder.decode(param),
            PEPPropertiesLoader.load(CoreProperties.class).getCharset()),
            PEPPropertiesLoader.load(CoreProperties.class).getCharset()),
            PEPWorkflowNoticeUrlParam.class);
        assert StringUtil.isNotEmpty(noticeUrlParam.getBusinessObj().getFormTitle())
    }

    private String start(String key, Map<String, Object> form) {
        PEPProcInstVO pepProcInstVO = JSONUtil.parse(post('/workflow/process/' + key, JSONUtil.toJSON(form), HttpStatus.CREATED).getResponse().getContentAsString(), PEPProcInstVO.class)
        return pepProcInstVO.getProcInstId()
    }

}
