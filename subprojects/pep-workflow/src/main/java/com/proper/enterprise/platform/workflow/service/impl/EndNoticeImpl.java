package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.client.NoticeSender;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import com.proper.enterprise.platform.workflow.api.EndNotice;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("endNotice")
public class EndNoticeImpl implements EndNotice {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndNoticeImpl.class);

    @Value("${pep.mail.mailDefaultFrom}")
    private String from;

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
            User initiatorUser = userDao.findById(initiator);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(execution.getProcessDefinitionId()).singleResult();
            Map<String, String> templateParams = new HashMap<>(5);
            templateParams.put("msg", String.format(I18NUtil.getMessage("workflow.notice.end.msg")));
            templateParams.put("userName", initiatorUser.getName());
            templateParams.put("taskName", processDefinition.getName());
            templateParams.put("executionName", execution.getName());
            Map<String, Object> custom = new HashMap<>(0);
            noticeSender.sendNotice("EndNotice", "BPM", "EndCode", custom, initiator, templateParams);
        } catch (Exception e) {
            LOGGER.error("endNoticeError", e);
        }
    }
}
