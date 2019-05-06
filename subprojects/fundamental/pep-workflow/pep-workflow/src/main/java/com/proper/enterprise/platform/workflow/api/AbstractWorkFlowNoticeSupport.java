package com.proper.enterprise.platform.workflow.api;

import com.proper.enterprise.platform.api.auth.dao.RoleDao;
import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.enums.AppConfigEnum;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.workflow.factory.PEPCandidateExtQueryFactory;
import com.proper.enterprise.platform.workflow.model.PEPCandidateModel;
import com.proper.enterprise.platform.workflow.model.PEPWorkflowNoticeUrlBusinessParam;
import com.proper.enterprise.platform.workflow.model.PEPWorkflowNoticeUrlParam;
import com.proper.enterprise.platform.workflow.util.CandidateIdUtil;
import org.apache.commons.codec.binary.Base64;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntity;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractWorkFlowNoticeSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWorkFlowNoticeSupport.class);

    public static final String TASK_PAGE_URL = "#/webapp/workflow/workflowMainPop?param=";

    private static final String GROUP_CONF_CODE = "GROUP";

    private static final String ROLE_CONF_CODE = "ROLE";

    @Autowired
    private UserGroupDao userGroupDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private CoreProperties coreProperties;

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
            Base64 encoder = new Base64();
            return DataDicUtil.get(AppConfigEnum.WEB_ADDRESS).getName() + TASK_PAGE_URL + new String(encoder.encode(
                URLEncoder.encode(JSONUtil.toJSONIgnoreException(noticeUrlParam), coreProperties.getCharset())
                    .getBytes(coreProperties.getCharset())), coreProperties.getCharset());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("taskUrl encode error", e);
        }
        return null;
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
                    CandidateIdUtil.CandidateId candidateId = CandidateIdUtil.decode(identityLinkEntity.getGroupId());
                    PEPCandidateModel pepCandidateModel = PEPCandidateExtQueryFactory
                        .product(candidateId.getType())
                        .findCandidateById(candidateId.getId());
                    if (GROUP_CONF_CODE.equals(candidateId.getType())) {
                        userIds.addAll(queryUserIdByGroup(pepCandidateModel.getId()));
                    }
                    if (ROLE_CONF_CODE.equals(candidateId.getType())) {
                        userIds.addAll(queryUserIdByRole(pepCandidateModel.getId()));
                    }
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
