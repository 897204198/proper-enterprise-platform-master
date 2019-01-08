package com.proper.enterprise.platform.workflow.model;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.*;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.workflow.entity.WFIdmQueryConfEntity;
import com.proper.enterprise.platform.workflow.factory.PEPCandidateExtQueryFactory;
import com.proper.enterprise.platform.workflow.service.impl.PEPCandidateUserExtQueryImpl;
import com.proper.enterprise.platform.workflow.util.CandidateIdUtil;
import com.proper.enterprise.platform.workflow.util.WFIdmQueryConfUtil;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import com.proper.enterprise.platform.workflow.vo.enums.ShowType;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.identitylink.api.IdentityLinkInfo;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.*;

public class PEPTask {

    public PEPTask(Task task) {
        this.setProcInstId(task.getProcessInstanceId());
        this.setTaskId(task.getId());
        this.setAssignee(task.getAssignee());
        this.setForm(new PEPExtForm(task).convert());
        this.setName(task.getName());
        this.setCreateTime(DateUtil.toString(DateUtil.toLocalDateTime(task.getCreateTime()),
            PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat()));
        buildIdentityMsg(this, task.getIdentityLinks());
    }


    public PEPTask(HistoricTaskInstance historicTaskInstance) {
        this.setProcInstId(historicTaskInstance.getProcessInstanceId());
        this.setTaskId(historicTaskInstance.getId());
        this.setForm(new PEPExtForm(historicTaskInstance).convert());
        this.setAssignee(historicTaskInstance.getAssignee());
        this.setName(historicTaskInstance.getName());
        this.setEndTime(null != historicTaskInstance.getEndTime()
            ? DateUtil.toString(DateUtil.toLocalDateTime(historicTaskInstance.getEndTime()),
            PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat())
            : null);
        this.setCreateTime(DateUtil.toString(DateUtil.toLocalDateTime(historicTaskInstance.getCreateTime()),
            PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat()));
    }

    public PEPTask(HistoricTaskInstance historicTaskInstance,
                   Map<String, Object> formData,
                   Map<String, Object> globalData) {
        if (formData == null) {
            formData = new HashMap<>(1);
        }
        if (globalData == null) {
            globalData = new HashMap<>(1);
        }
        this.setProcInstId(historicTaskInstance.getProcessInstanceId());
        this.setTaskId(historicTaskInstance.getId());
        String formKey = StringUtil.isEmpty(historicTaskInstance.getFormKey())
            ? (String) globalData.get(WorkFlowConstants.START_FORM_KEY)
            : historicTaskInstance.getFormKey();
        PEPExtForm pepExtForm = new PEPExtForm(formKey, formData);
        pepExtForm.setGlobalData(globalData);
        pepExtForm.setShowType(ShowType.SHOW);
        this.setForm(pepExtForm.convert());
        this.setAssignee(historicTaskInstance.getAssignee());
        this.setName(historicTaskInstance.getName());
        this.setEndTime(null != historicTaskInstance.getEndTime()
            ? DateUtil.toString(DateUtil.toLocalDateTime(historicTaskInstance.getEndTime()),
            PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat())
            : null);
        this.setCreateTime(DateUtil.toString(DateUtil.toLocalDateTime(historicTaskInstance.getCreateTime()),
            PEPPropertiesLoader.load(CoreProperties.class).getDefaultDatetimeFormat()));
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
     * 候选集合展示
     */
    private List<PEPTaskCandidate> candidates;

    /**
     * 候选集合拼接
     */
    private Map<String, PEPTaskCandidate> candidatesMap;

    /**
     * 任务表单
     */
    private PEPForm form;

    /**
     * 全局变量
     */
    private Map<String, Object> globalData;

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

    /**
     * 将candidatesMap 中的数据整合并排序
     *
     * @return 任务候选集合
     */
    public List<PEPTaskCandidate> getCandidates() {
        if (CollectionUtil.isEmpty(this.candidatesMap)) {
            return new ArrayList<>();
        }
        this.candidates = new ArrayList<>();
        Collection<WFIdmQueryConfEntity> wfIdmQueryConfEntities = WFIdmQueryConfUtil.findAll();
        for (WFIdmQueryConfEntity wfIdmQueryConfEntity : wfIdmQueryConfEntities) {
            PEPTaskCandidate pepTaskCandidateMap = candidatesMap.get(wfIdmQueryConfEntity.getType());
            if (null == pepTaskCandidateMap) {
                continue;
            }
            PEPTaskCandidate pepTaskCandidate = new PEPTaskCandidate(wfIdmQueryConfEntity.getType(),
                wfIdmQueryConfEntity.getName());
            pepTaskCandidate.setData(pepTaskCandidateMap.getData());
            candidates.add(pepTaskCandidate);
        }
        return candidates;
    }

    public void setCandidates(List<PEPTaskCandidate> candidates) {
        this.candidates = candidates;
    }

    public Map<String, PEPTaskCandidate> getCandidatesMap() {
        return candidatesMap;
    }

    public void setCandidatesMap(Map<String, PEPTaskCandidate> candidatesMap) {
        this.candidatesMap = candidatesMap;
    }

    public void setSameAssigneeSkip(Boolean sameAssigneeSkip) {
        this.sameAssigneeSkip = sameAssigneeSkip;
    }

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
        HistoricProcessInstance processInstance = PEPApplicationContext.getBean(HistoryService.class)
            .createHistoricProcessInstanceQuery()
            .includeProcessVariables()
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

    @SuppressWarnings("unchecked")
    public Boolean getSameAssigneeSkip() {
        if (null == this.getForm() || null == this.getForm().getGlobalData()) {
            this.sameAssigneeSkip = false;
            return this.sameAssigneeSkip;
        }
        Set<String> noSameAssigneeSkipActIds = (Set<String>) this.getForm()
            .getGlobalData()
            .get(WorkFlowConstants.NO_SAME_ASSIGNEE_SKIP_REMARK);
        if (null == noSameAssigneeSkipActIds) {
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

    public void addCandidate(PEPCandidateModel pepCandidateModel) {
        if (CollectionUtil.isEmpty(this.candidatesMap)) {
            this.candidatesMap = new HashMap<>(16);
        }
        if (null == this.candidatesMap.get(pepCandidateModel.getType())) {
            WFIdmQueryConfEntity wfIdmQueryConfEntity = WFIdmQueryConfUtil.findByType(pepCandidateModel.getType());
            if (null == wfIdmQueryConfEntity) {
                throw new ErrMsgException("can't find type:" + pepCandidateModel.getType() + ":wfIdmQueryConfEntity");
            }
            this.candidatesMap.put(pepCandidateModel.getType(),
                new PEPTaskCandidate(wfIdmQueryConfEntity.getType(), wfIdmQueryConfEntity.getName()));
        }
        this.candidatesMap.get(pepCandidateModel.getType()).getData().add(pepCandidateModel);
    }

    public Map<String, Object> getGlobalData() {
        if (null != this.form) {
            return this.form.getGlobalData();
        }
        return globalData;
    }

    public void setGlobalData(Map<String, Object> globalData) {
        this.globalData = globalData;
    }

    private static void buildIdentityMsg(PEPTask pepTask, List<? extends IdentityLinkInfo> identityLinkInfos) {
        if (CollectionUtil.isEmpty(identityLinkInfos)) {
            return;
        }
        for (IdentityLinkInfo identityLinkInfo : identityLinkInfos) {
            if ("candidate".equals(identityLinkInfo.getType())) {
                if (StringUtil.isNotEmpty(identityLinkInfo.getUserId())) {
                    pepTask.addCandidate(PEPCandidateExtQueryFactory
                        .product(PEPCandidateUserExtQueryImpl.USER_CONF_CODE)
                        .findCandidateById(identityLinkInfo.getUserId()));
                }
                if (StringUtil.isNotEmpty(identityLinkInfo.getGroupId())) {
                    CandidateIdUtil.CandidateId candidateId = CandidateIdUtil.decode(identityLinkInfo.getGroupId());
                    pepTask.addCandidate(PEPCandidateExtQueryFactory
                        .product(candidateId.getType())
                        .findCandidateById(candidateId.getId()));
                }
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
