package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.dao.RoleDao;
import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import com.proper.enterprise.platform.workflow.api.TaskAssigneeOrCandidateNotice;
import com.proper.enterprise.platform.workflow.api.WorkflowAsyncNotice;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("taskAssigneeOrCandidateNotice")
public class TaskAssigneeOrCandidateNoticeImpl implements TaskAssigneeOrCandidateNotice {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskAssigneeOrCandidateNoticeImpl.class);

    @Value("${pep.mail.mailDefaultFrom}")
    private String from;

    private RepositoryService repositoryService;

    private UserDao userDao;

    private UserGroupDao userGroupDao;

    private RoleDao roleDao;

    private WorkflowAsyncNotice workflowAsyncNotice;

    @Autowired
    TaskAssigneeOrCandidateNoticeImpl(WorkflowAsyncNotice workflowAsyncNotice,
                                      UserDao userDao,
                                      UserGroupDao userGroupDao,
                                      RoleDao roleDao,
                                      RepositoryService repositoryService) {
        this.userDao = userDao;
        this.userGroupDao = userGroupDao;
        this.roleDao = roleDao;
        this.repositoryService = repositoryService;
        this.workflowAsyncNotice = workflowAsyncNotice;
    }

    @Override
    public void notice(TaskEntity task) {
        try {
            Set<String> emails = queryEmails(task);
            if (CollectionUtil.isEmpty(emails)) {
                return;
            }
            String initiator = (String) task.getVariable(WorkFlowConstants.INITIATOR);
            User initiatorUser = userDao.findById(initiator);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId()).singleResult();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setSubject(task.getName());
            message.setText(String.format(I18NUtil.getMessage("workflow.notice.msg"),
                initiatorUser.getName(), processDefinition.getName(), task.getName()));
            message.setTo(emails.toArray(new String[emails.size()]));
            workflowAsyncNotice.notice(message);
        } catch (Exception e) {
            LOGGER.error("taskAssigneeNoticeError", e);
        }
    }

    protected Set<String> queryEmails(TaskEntity task) {
        Set<String> userIds = new HashSet<>();
        if (StringUtil.isNotEmpty(task.getAssignee())) {
            userIds.add(task.getAssignee());
        }
        if (CollectionUtil.isNotEmpty(task.getCandidates())) {
            for (IdentityLinkEntity identityLinkEntity : task.getIdentityLinks()) {
                if (identityLinkEntity.isUser()) {
                    userIds.add(identityLinkEntity.getUserId());
                }
                if (identityLinkEntity.isGroup()) {
                    userIds.addAll(queryUserIdByGroup(identityLinkEntity.getGroupId()));
                }
                if (identityLinkEntity.isRole()) {
                    userIds.addAll(queryUserIdByRole(identityLinkEntity.getGroupId()));
                }
            }
        }
        if (CollectionUtil.isEmpty(userIds)) {
            return userIds;
        }
        Collection<? extends User> users = userDao.findAll(userIds);
        Set<String> emails = new HashSet<>();
        for (User user : users) {
            emails.add(user.getEmail());
        }
        LOGGER.info("taskId:{},emails:{}", task.getId(), emails.toString());
        return emails;
    }

    protected Set<String> queryUserIdByGroup(String groupId) {
        Set<String> userIds = new HashSet<>();
        if (StringUtil.isEmpty(groupId)) {
            return userIds;
        }
        UserGroup userGroup = userGroupDao.findById(groupId);
        if (null == userGroup) {
            return userIds;
        }
        if (CollectionUtil.isEmpty(userGroup.getUsers())) {
            return userIds;
        }
        for (User user : userGroup.getUsers()) {
            userIds.add(user.getId());
        }
        return userIds;
    }

    protected Set<String> queryUserIdByRole(String roleId) {
        Set<String> userIds = new HashSet<>();
        if (StringUtil.isEmpty(roleId)) {
            return userIds;
        }
        Role role = roleDao.findById(roleId);
        if (null == role) {
            return userIds;
        }
        if (CollectionUtil.isEmpty(role.getUsers())) {
            return userIds;
        }
        for (User user : role.getUsers()) {
            userIds.add(user.getId());
        }
        return userIds;
    }
}
