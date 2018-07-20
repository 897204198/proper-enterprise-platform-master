package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import com.proper.enterprise.platform.workflow.api.TaskAssigneeNotice;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("taskAssigneeNotice")
public class TaskAssigneeNoticeImpl implements TaskAssigneeNotice {

    @Value("${pep.mail.mailDefaultFrom}")
    private String from;

    private RepositoryService repositoryService;

    private JavaMailSender mailSender;

    private UserDao userDao;

    @Autowired
    TaskAssigneeNoticeImpl(JavaMailSender mailSender,
                           UserDao userDao,
                           RepositoryService repositoryService) {
        this.mailSender = mailSender;
        this.userDao = userDao;
        this.repositoryService = repositoryService;
    }

    @Override
    public void notice(TaskEntity task) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(task.getProcessDefinitionId()).singleResult();
        User user = userDao.findOne(task.getAssignee());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(user.getEmail());
        message.setSubject(task.getName());
        message.setText(String.format(I18NUtil.getMessage("workflow.notice.msg"), processDefinition.getName()));
        mailSender.send(message);
    }

}
