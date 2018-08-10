package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.dao.RoleDao;
import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.client.NoticeSender;
import com.proper.enterprise.platform.workflow.api.AbstractWorkFlowNoticeSupport;
import com.proper.enterprise.platform.workflow.api.TaskAssigneeOrCandidateNotice;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("taskAssigneeNotice")
public class TaskAssigneeNoticeImpl extends AbstractWorkFlowNoticeSupport implements TaskAssigneeOrCandidateNotice {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskAssigneeOrCandidateNoticeImpl.class);

    private RepositoryService repositoryService;

    private UserDao userDao;

    private UserGroupDao userGroupDao;

    private RoleDao roleDao;

    private NoticeSender noticeSender;

    @Autowired
    TaskAssigneeNoticeImpl(NoticeSender noticeSender,
                           UserDao userDao,
                           UserGroupDao userGroupDao,
                           RoleDao roleDao,
                           RepositoryService repositoryService) {
        this.userDao = userDao;
        this.userGroupDao = userGroupDao;
        this.roleDao = roleDao;
        this.repositoryService = repositoryService;
        this.noticeSender = noticeSender;
    }

    @Override
    public void notice(TaskEntity task) {
        try {
            Set<String> userIds = queryUserIds(task);
            if (CollectionUtil.isEmpty(userIds)) {
                return;
            }
            String initiator = (String) task.getVariable(WorkFlowConstants.INITIATOR);
            User initiatorUser = userDao.findOne(initiator);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId()).singleResult();
            Map<String, String> templateParams = new HashMap<>(5);
            templateParams.put("initiatorName", initiatorUser.getName());
            templateParams.put("processDefinitionName", processDefinition.getName());
            templateParams.put("taskName", task.getName());
            templateParams.put("pageurl", buildTaskUrl(task) + "&from=email");
            Map<String, Object> custom = new HashMap<>(0);
            custom.put("gdpr_mpage", "examList");
            custom.put("url", buildTaskUrl(task) + "&from=app");
            custom.put("title", task.getName());
            noticeSender.sendNotice("EndNotice", "BPM", "TaskAssignee",
                custom, userIds, templateParams);
        } catch (Exception e) {
            LOGGER.error("taskAssigneeNoticeError", e);
        }
    }

    protected Set<String> queryUserIds(TaskEntity task) {
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
        return userIds;
    }

    protected Set<String> queryUserIdByGroup(String groupId) {
        Set<String> userIds = new HashSet<>();
        if (StringUtil.isEmpty(groupId)) {
            return userIds;
        }
        UserGroup userGroup = userGroupDao.findOne(groupId);
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
        Role role = roleDao.findOne(roleId);
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
