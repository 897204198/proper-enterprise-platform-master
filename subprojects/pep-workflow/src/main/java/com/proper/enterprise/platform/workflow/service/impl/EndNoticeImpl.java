package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.client.NoticeSender;
import com.proper.enterprise.platform.workflow.api.EndNotice;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("endNotice")
public class EndNoticeImpl implements EndNotice {

    public static final String END_NOTICE_USER_ID_KEY = "endNoticeUserId";

    public static final String END_NOTICE_CODE_KEY = "endNoticeCode";

    private static final Logger LOGGER = LoggerFactory.getLogger(EndNoticeImpl.class);

    private RepositoryService repositoryService;

    private UserDao userDao;

    @Autowired
    private NoticeSender noticeSender;

    @Autowired
    EndNoticeImpl(UserDao userDao, RepositoryService repositoryService) {
        this.userDao = userDao;
        this.repositoryService = repositoryService;
    }

    @Override
    public void notice(ExecutionEntity execution) {
        try {
            String initiator = (String) execution.getVariable(WorkFlowConstants.INITIATOR);
            User initiatorUser = userDao.findOne(initiator);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(execution.getProcessDefinitionId()).singleResult();
            Map<String, Object> templateParams = new HashMap<>(5);
            templateParams.put("initiatorName", initiatorUser.getName());
            templateParams.put("processDefinitionName", processDefinition.getName());
            Map<String, Object> custom = new HashMap<>(0);
            custom.put("gdpr_mpage", "examList");
            custom.put("title", processDefinition.getName());
            String endNoticeUserKey = (String) execution.getVariable(END_NOTICE_USER_ID_KEY);
            String endNoticeUserId = StringUtil.isNotEmpty(endNoticeUserKey)
                ? (String) execution.getVariable(endNoticeUserKey)
                : "";
            String endNoticeCode = (String) execution.getVariable(END_NOTICE_CODE_KEY);
            noticeSender.sendNotice(StringUtil.isEmpty(endNoticeCode) ? "EndCode" : endNoticeCode, custom,
                StringUtil.isEmpty(endNoticeUserId) ? initiator : endNoticeUserId, templateParams);
        } catch (Exception e) {
            LOGGER.error("endNoticeError", e);
        }
    }
}
