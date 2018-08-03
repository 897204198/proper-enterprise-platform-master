package com.proper.enterprise.platform.workflow.model;

import com.proper.enterprise.platform.api.auth.dao.RoleDao;
import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.*;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLinkInfo;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.proper.enterprise.platform.core.PEPConstants.DEFAULT_DATETIME_FORMAT;

public class PEPTask {

    public PEPTask(Task task) {
        this.setProcInstId(task.getProcessInstanceId());
        this.setTaskId(task.getId());
        this.setAssignee(task.getAssignee());
        this.setForm(new PEPExtForm(task).convert());
        this.setName(task.getName());
        this.setCreateTime(DateUtil.toString(task.getCreateTime(), DEFAULT_DATETIME_FORMAT));
        buildIdentityMsg(this, task.getIdentityLinks());
    }


    public PEPTask(HistoricTaskInstance historicTaskInstance) {
        this.setProcInstId(historicTaskInstance.getProcessInstanceId());
        this.setTaskId(historicTaskInstance.getId());
        this.setForm(new PEPExtForm(historicTaskInstance).convert());
        this.setAssignee(historicTaskInstance.getAssignee());
        this.setName(historicTaskInstance.getName());
        this.setEndTime(null != historicTaskInstance.getEndTime()
            ? DateUtil.toString(historicTaskInstance.getEndTime(), DEFAULT_DATETIME_FORMAT)
            : null);
        this.setCreateTime(DateUtil.toString(historicTaskInstance.getCreateTime(), DEFAULT_DATETIME_FORMAT));
    }

    /**
     * 任务Id
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 当前经办人
     */
    private String assignee;
    /**
     * 当前经办人名称
     */
    private String assigneeName;
    /**
     * 候选人集合
     */
    private Set<String> candidateUsers;
    /**
     * 候选人集合
     */
    private Set<String> candidateUserNames;
    /**
     * 候选角色集合
     */
    private Set<String> candidateRoles;
    /**
     * 候选角色名称集合
     */
    private Set<String> candidateRoleNames;
    /**
     * 候选用户组集合
     */
    private Set<String> candidateGroups;
    /**
     * 候选用户组集合
     */
    private Set<String> candidateGroupNames;
    /**
     * 任务表单
     */
    private PEPForm form;
    /**
     * 任务开始时间
     */
    private String createTime;
    /**
     * 任务结束时间
     */
    private String endTime;
    /**
     * 流程实例Id
     */
    private String procInstId;
    /**
     * 流程实例信息
     */
    private PEPProcInst pepProcInst;
    /**
     * 是否为相同经办人自动跳过
     */
    private Boolean sameAssigneeSkip;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAssigneeName() {
        if (StringUtil.isNotEmpty(this.assigneeName)) {
            return assigneeName;
        }
        if (StringUtil.isEmpty(this.getAssignee())) {
            return null;
        }
        User user = PEPApplicationContext.getApplicationContext().getBean(UserDao.class).findById(this.getAssignee());
        if (null == user) {
            return null;
        }
        return user.getName();
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public Set<String> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(Set<String> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public Set<String> getCandidateUserNames() {
        if (CollectionUtil.isNotEmpty(this.candidateUserNames)) {
            return this.candidateUserNames;
        }
        if (CollectionUtil.isEmpty(this.getCandidateUsers())) {
            return null;
        }
        List<User> users = new ArrayList<>(PEPApplicationContext.getBean(UserDao.class).findAll(this.getCandidateUsers()));
        Set<String> userNames = new HashSet<>();
        for (User user : users) {
            userNames.add(user.getName());
        }
        return userNames;
    }

    public void setCandidateUserNames(Set<String> candidateUserNames) {
        this.candidateUserNames = candidateUserNames;
    }

    public Set<String> getCandidateRoles() {
        return candidateRoles;
    }

    public void setCandidateRoles(Set<String> candidateRoles) {
        this.candidateRoles = candidateRoles;
    }

    public Set<String> getCandidateRoleNames() {
        if (CollectionUtil.isNotEmpty(this.candidateRoleNames)) {
            return this.candidateRoleNames;
        }
        if (CollectionUtil.isEmpty(this.getCandidateRoles())) {
            return null;
        }
        List<Role> roles = new ArrayList<>(PEPApplicationContext.getBean(RoleDao.class).findAllById(this.getCandidateRoles()));
        Set<String> roleNames = new HashSet<>();
        for (Role role : roles) {
            roleNames.add(role.getName());
        }
        return roleNames;
    }

    public void setCandidateRoleNames(Set<String> candidateRoleNames) {
        this.candidateRoleNames = candidateRoleNames;
    }

    public Set<String> getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(Set<String> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    public Set<String> getCandidateGroupNames() {
        if (CollectionUtil.isNotEmpty(this.candidateGroupNames)) {
            return this.candidateGroupNames;
        }
        if (CollectionUtil.isEmpty(this.getCandidateGroups())) {
            return null;
        }
        List<UserGroup> groups = new ArrayList<>(PEPApplicationContext.getBean(UserGroupDao.class).findAll(this.getCandidateGroups()));
        Set<String> groupNames = new HashSet<>();
        for (UserGroup userGroup : groups) {
            groupNames.add(userGroup.getName());
        }
        return groupNames;
    }

    public void setCandidateGroupNames(Set<String> candidateGroupNames) {
        this.candidateGroupNames = candidateGroupNames;
    }

    public PEPForm getForm() {
        return form;
    }

    public void setForm(PEPForm form) {
        this.form = form;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public PEPProcInst getPepProcInst() {
        if (null != this.pepProcInst) {
            return this.pepProcInst;
        }
        if (StringUtil.isEmpty(this.getProcInstId())) {
            return null;
        }
        ProcessInstance processInstance = PEPApplicationContext.getBean(RuntimeService.class)
            .createProcessInstanceQuery()
            .processInstanceId(this.getProcInstId())
            .singleResult();
        if (null == processInstance) {
            return null;
        }
        return new PEPProcInst(processInstance);
    }

    public void setPepProcInst(PEPProcInst pepProcInst) {
        this.pepProcInst = pepProcInst;
    }

    public Boolean getSameAssigneeSkip() {
        if (null == this.getForm() || null == this.getForm().getGlobalData()) {
            this.sameAssigneeSkip = false;
            return this.sameAssigneeSkip;
        }
        Set<String> noSameAssigneeSkipActIds = (Set<String>) this.getForm()
            .getGlobalData()
            .get(WorkFlowConstants.NO_SAME_ASSIGNEE_SKIP_REMARK);
        if (CollectionUtil.isEmpty(noSameAssigneeSkipActIds)) {
            this.sameAssigneeSkip = false;
            return this.sameAssigneeSkip;
        }
        this.sameAssigneeSkip = !noSameAssigneeSkipActIds.contains(this.getTaskId());
        return this.sameAssigneeSkip;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }


    public void addCandidateUser(String candidateUserId) {
        if (StringUtil.isEmpty(candidateUserId)) {
            return;
        }
        if (null == this.candidateUsers) {
            this.candidateUsers = new HashSet<>();
        }
        candidateUsers.add(candidateUserId);
    }

    public void addCandidateRole(String candidateRoleId) {
        if (StringUtil.isEmpty(candidateRoleId)) {
            return;
        }
        if (null == this.candidateRoles) {
            this.candidateRoles = new HashSet<>();
        }
        candidateRoles.add(candidateRoleId);
    }

    public void addCandidateGroup(String candidateGroupId) {
        if (StringUtil.isEmpty(candidateGroupId)) {
            return;
        }
        if (null == this.candidateGroups) {
            this.candidateGroups = new HashSet<>();
        }
        candidateGroups.add(candidateGroupId);
    }

    private static void buildIdentityMsg(PEPTask pepTask, List<? extends IdentityLinkInfo> identityLinkInfos) {
        if (CollectionUtil.isEmpty(identityLinkInfos)) {
            return;
        }
        for (IdentityLinkInfo identityLinkInfo : identityLinkInfos) {
            if ("candidate".equals(identityLinkInfo.getType())) {
                pepTask.addCandidateUser(identityLinkInfo.getUserId());
                pepTask.addCandidateGroup(identityLinkInfo.getGroupId());
                pepTask.addCandidateRole(identityLinkInfo.getRoleId());
            }
            if ("assigne".equals(identityLinkInfo.getType())) {
                pepTask.setAssignee(identityLinkInfo.getUserId());
            }
        }
    }

    public PEPTaskVO convert() {
        return BeanUtil.convert(this, PEPTaskVO.class);
    }

}
