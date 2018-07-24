package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import com.proper.enterprise.platform.workflow.api.TaskAssigneeNotice;
import com.proper.enterprise.platform.workflow.api.WorkflowAsyncNotice;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service("taskAssigneeNotice")
public class TaskAssigneeNoticeImpl implements TaskAssigneeNotice {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskAssigneeNoticeImpl.class);

    @Value("${pep.mail.mailDefaultFrom}")
    private String from;

    private RepositoryService repositoryService;

    private UserDao userDao;

    private WorkflowAsyncNotice workflowAsyncNotice;

    @Autowired
    TaskAssigneeNoticeImpl(WorkflowAsyncNotice workflowAsyncNotice,
                           UserDao userDao,
                           RepositoryService repositoryService) {
        this.userDao = userDao;
        this.repositoryService = repositoryService;
        this.workflowAsyncNotice = workflowAsyncNotice;
    }

    @Override
    public void notice(TaskEntity task) {
        try {
            String initiator = (String) task.getVariable(WorkFlowConstants.INITIATOR);
            User initiatorUser = userDao.findOne(initiator);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId()).singleResult();
            User user = userDao.findOne(task.getAssignee());
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(user.getEmail());
            message.setSubject(task.getName());
            message.setText(String.format(I18NUtil.getMessage("workflow.notice.msg"),
                initiatorUser.getName(), processDefinition.getName(), task.getName()));
            workflowAsyncNotice.notice(message);
        } catch (Exception e) {
            LOGGER.error("taskAssigneeNoticeError", e);
        }

    }

}
