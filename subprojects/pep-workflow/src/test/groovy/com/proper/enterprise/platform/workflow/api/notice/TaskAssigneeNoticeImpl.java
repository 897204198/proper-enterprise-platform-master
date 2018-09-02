package com.proper.enterprise.platform.workflow.api.notice;

import com.proper.enterprise.platform.api.auth.dao.RoleDao;
import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.client.NoticeSender;
import com.proper.enterprise.platform.template.service.TemplateService;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import com.proper.enterprise.platform.workflow.api.AbstractWorkFlowNoticeSupport;
import com.proper.enterprise.platform.workflow.api.TaskAssigneeOrCandidateNotice;
import com.proper.enterprise.platform.workflow.service.impl.TaskAssigneeOrCandidateNoticeImpl;
import com.proper.enterprise.platform.workflow.util.VariableUtil;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service("taskAssigneeNoticePri")
@Primary
public class TaskAssigneeNoticeImpl extends AbstractWorkFlowNoticeSupport implements TaskAssigneeOrCandidateNotice {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskAssigneeOrCandidateNoticeImpl.class);

    @Value("${pep.mail.mailDefaultFrom}")
    private String from;

    private UserGroupDao userGroupDao;

    private RoleDao roleDao;

    private NoticeSender noticeSender;

    private TemplateService templateService;

    @Autowired
    TaskAssigneeNoticeImpl(NoticeSender noticeSender,
                           UserGroupDao userGroupDao,
                           RoleDao roleDao,
                           TemplateService templateService) {
        this.userGroupDao = userGroupDao;
        this.roleDao = roleDao;
        this.noticeSender = noticeSender;
        this.templateService = templateService;
    }

    @Override
    public void notice(TaskEntity task) {
        try {
            Set<String> userIds = queryUserIds(task);
            if (CollectionUtil.isEmpty(userIds)) {
                return;
            }
            Map<String, Object> templateParams = new HashMap<>(5);
            templateParams.putAll(VariableUtil.convertVariableToMsgParam(task.getVariables()));
            templateParams.put("taskName", task.getName());
            templateParams.put("pageurl", buildTaskUrl(task) + "&from=email");
            Map<String, Object> custom = new HashMap<>(0);
            custom.put("gdpr_mpage", "examList");
            custom.put("url", buildTaskUrl(task) + "&from=app");
            custom.put("title", task.getName());
            String noticeCode = (String) task.getVariable(TASK_ASSIGNEE_NOTICE_CODE_KEY);
            Authentication.setCurrentUserId(buildTaskUrl(task));
            TemplateVO templateVO = templateService.getTemplates(StringUtil.isEmpty(noticeCode) ? "TaskAssignee" :
                noticeCode, templateParams);
            noticeSender.sendNotice(userIds, templateVO, custom);
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
