package com.proper.enterprise.platform.workflow.api;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.workflow.model.PEPWorkflowNoticeUrlBusinessParam;
import com.proper.enterprise.platform.workflow.model.PEPWorkflowNoticeUrlParam;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public abstract class AbstractWorkFlowNoticeSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWorkFlowNoticeSupport.class);

    public static final String TASK_PAGE_URL = "/web/#/webapp/workflowMainPop?param=";

    @Value("${sys.base.path}")
    private String baseUrl;

    public String buildTaskUrl(Task task) {
        Map<String, Object> vars = ((TaskEntityImpl) task).getVariables();
        PEPWorkflowNoticeUrlBusinessParam noticeUrlBusinessParam = new PEPWorkflowNoticeUrlBusinessParam();
        noticeUrlBusinessParam.setFormTitle((String) vars.get(WorkFlowConstants.PROCESS_TITLE));
        PEPWorkflowNoticeUrlParam noticeUrlParam = new PEPWorkflowNoticeUrlParam();
        noticeUrlParam.setBusinessObj(noticeUrlBusinessParam);
        noticeUrlParam.setLaunch(false);
        noticeUrlParam.setProcInstId(task.getProcessInstanceId());
        noticeUrlParam.setTaskOrProcDefKey(task.getId());
        noticeUrlParam.setName(task.getName());
        try {
            return baseUrl + TASK_PAGE_URL + URLEncoder.encode(JSONUtil.toJSONIgnoreException(noticeUrlParam), PEPConstants.DEFAULT_CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("taskUrl encode error", e);
        }
        return null;
    }
}
