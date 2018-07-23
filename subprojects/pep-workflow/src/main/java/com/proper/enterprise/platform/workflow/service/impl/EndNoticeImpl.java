package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("endNotice")
public class EndNoticeImpl implements EndNotice {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndNoticeImpl.class);

    @Value("${pep.mail.mailDefaultFrom}")
    private String from;

    private JavaMailSender mailSender;

    private RepositoryService repositoryService;

    private UserDao userDao;

    @Autowired
    EndNoticeImpl(JavaMailSender mailSender, UserDao userDao, RepositoryService repositoryService) {
        this.mailSender = mailSender;
        this.userDao = userDao;
        this.repositoryService = repositoryService;
    }

    @Override
    @Async
    public void notice(ExecutionEntity execution) {
        try {
            String initiator = (String) execution.getVariable(WorkFlowConstants.INITIATOR);
            User initiatorUser = userDao.findOne(initiator);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(execution.getProcessDefinitionId()).singleResult();
            User user = userDao.findOne(initiator);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(user.getEmail());
            message.setSubject(I18NUtil.getMessage("workflow.notice.end..msg.subject"));
            message.setText(String.format(I18NUtil.getMessage("workflow.notice.end.msg"),
                initiatorUser.getName(), processDefinition.getName(), execution.getName()));
            mailSender.send(message);
        } catch (Exception e) {
            LOGGER.error("endNoticeError", e);
        }
    }
}
