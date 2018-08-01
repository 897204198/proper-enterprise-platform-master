package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.dao.RoleDao;
import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.workflow.convert.ProcInstConvert;
import com.proper.enterprise.platform.workflow.convert.TaskConvert;
import com.proper.enterprise.platform.workflow.service.PEPProcessService;
import com.proper.enterprise.platform.workflow.service.PEPTaskService;
import com.proper.enterprise.platform.workflow.util.GlobalVariableUtil;
import com.proper.enterprise.platform.workflow.util.VariableUtil;
import com.proper.enterprise.platform.workflow.vo.PEPExtFormVO;
import com.proper.enterprise.platform.workflow.vo.PEPStartVO;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPathVO;
import com.proper.enterprise.platform.workflow.vo.enums.ShowType;
import org.apache.commons.collections.MapUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.proper.enterprise.platform.core.PEPConstants.DEFAULT_DATETIME_FORMAT;

@Service
public class PEPTaskServiceImpl implements PEPTaskService {

    @Value("${workflow.global.variables}")
    private List<String> globalVariableKeys;

    private TaskService taskService;

    private RuntimeService runtimeService;

    private HistoryService historyService;

    private UserDao userDao;

    private RoleDao roleDao;

    private UserGroupDao userGroupDao;

    private PEPProcessService pepProcessService;

    @Autowired
    PEPTaskServiceImpl(PEPProcessService pepProcessService,
                       TaskService taskService,
                       RuntimeService runtimeService,
                       UserDao userDao,
                       RoleDao roleDao,
                       UserGroupDao userGroupDao,
                       HistoryService historyService) {
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.userGroupDao = userGroupDao;
        this.historyService = historyService;
        this.pepProcessService = pepProcessService;
    }


    @Override
    public void complete(String taskId) {
        complete(taskId, null);
    }

    @Override
    public void complete(String taskId, Map<String, Object> variables) {
        variables = VariableUtil.handleVariableSpecialType(variables);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (MapUtils.isEmpty(variables)) {
            if (null == task.getAssignee()) {
                taskService.claim(taskId, Authentication.getCurrentUserId());
            }
            taskService.complete(taskId);
            return;
        }
        taskService.setVariablesLocal(taskId, variables);
        String formKey = task.getFormKey();
        Map<String, Object> globalVariables = taskService.getVariables(taskId);
        if (MapUtils.isNotEmpty(variables)) {
            globalVariables.putAll(variables);
        }
        if (StringUtil.isNotEmpty(formKey)) {
            globalVariables.put(formKey, variables);
        }
        globalVariables = GlobalVariableUtil.setGlobalVariable(globalVariables, variables, globalVariableKeys);
        if (null == task.getAssignee()) {
            taskService.claim(taskId, Authentication.getCurrentUserId());
        }
        taskService.complete(taskId, globalVariables);
    }

    @Override
    public DataTrunk<PEPTaskVO> findTodoPagination(String processDefinitionName, PageRequest pageRequest) {
        TaskQuery taskQuery = taskService.createTaskQuery()
            .includeProcessVariables()
            .taskCandidateOrAssigned(Authentication.getCurrentUserId())
            .orderByTaskCreateTime()
            .desc();
        if (StringUtil.isNotEmpty(processDefinitionName)) {
            taskQuery.processDefinitionName(processDefinitionName);
        }
        List<Task> tasks = taskQuery.listPage(pageRequest.getPageNumber() * pageRequest.getPageSize(),
            pageRequest.getPageSize());
        List<PEPTaskVO> taskVOs = TaskConvert.convert(tasks);
        taskVOs = buildTaskProsInstMsg(taskVOs);
        taskVOs = buildTaskUserName(taskVOs, WorkFlowConstants.INITIATOR);
        taskVOs = buildTaskUserName(taskVOs, WorkFlowConstants.ASSIGNEE);
        DataTrunk<PEPTaskVO> dataTrunk = new DataTrunk<>();
        dataTrunk.setData(taskVOs);
        dataTrunk.setCount(taskQuery.count());
        return dataTrunk;
    }

    @Override
    public PEPWorkflowPathVO findWorkflowPath(String procInstId) {
        PEPWorkflowPathVO pepWorkflowPathVO = new PEPWorkflowPathVO();
        pepWorkflowPathVO.setHisTasks(findHisTasks(procInstId));
        pepWorkflowPathVO.setCurrentTasks(findCurrentTasks(procInstId));
        pepWorkflowPathVO.setStart(findStart(procInstId));
        pepWorkflowPathVO.setEnded(isEnded(procInstId));
        return pepWorkflowPathVO;
    }

    private PEPStartVO findStart(String procInstId) {
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
            .includeProcessVariables().processInstanceId(procInstId).singleResult();
        PEPStartVO pepStartVO = new PEPStartVO();
        pepStartVO.setCreateTime(DateUtil.toString(processInstance.getStartTime(), DEFAULT_DATETIME_FORMAT));
        pepStartVO.setStartUserId(processInstance.getStartUserId());
        pepStartVO.setProcessDefinitionName(processInstance.getProcessDefinitionName());
        Object startFormData = processInstance.getProcessVariables().get(WorkFlowConstants.START_FORM_DATA);
        if (null != startFormData) {
            pepStartVO.setStartForm((Map<String, Object>) startFormData);
        }
        User user = userDao.findById(processInstance.getStartUserId());
        if (null != user) {
            pepStartVO.setStartUserName(user.getName());
        }
        return pepStartVO;
    }

    private boolean isEnded(String procInstId) {
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(procInstId).singleResult();
        return null != processInstance && null != processInstance.getEndTime();
    }

    public List<PEPTaskVO> findHisTasks(String procInstId) {
        List<HistoricTaskInstance> historicTaskInstances = historyService
            .createHistoricTaskInstanceQuery()
            .processInstanceIdIn(pepProcessService.findProcAndSubInstIds(procInstId))
            .includeTaskLocalVariables()
            .finished()
            .orderByHistoricTaskInstanceEndTime()
            .desc()
            .list();
        List<PEPTaskVO> hisTasks = TaskConvert.convertHisTasks(historicTaskInstances);
        hisTasks = buildTaskUserName(hisTasks, WorkFlowConstants.ASSIGNEE);
        return hisTasks;
    }

    @Override
    public List<PEPForm> buildPage(String taskId) {
        Task task = taskService.createTaskQuery().includeProcessVariables().taskId(taskId).singleResult();
        if (null == task) {
            throw new ErrMsgException("can't buildTaskPage with empty task");
        }
        List<PEPForm> pepForms = pepProcessService.buildPage(pepProcessService.findTopMostProcInstId(task.getProcessInstanceId()));
        Map<String, PEPForm> pepExtFormMaps = new HashMap<>(16);
        for (PEPForm pepForm : pepForms) {
            pepExtFormMaps.put(pepForm.getFormKey(), pepForm);
        }
        PEPExtFormVO pepExtFormVO = new PEPExtFormVO(task);
        pepExtFormVO.setShowType(ShowType.EDIT);
        if (StringUtil.isNotEmpty(pepExtFormVO.getFormKey())) {
            pepExtFormMaps.put(pepExtFormVO.getFormKey(), pepExtFormVO);
        }
        List<PEPForm> returnPEPForms = new ArrayList<>();
        for (Map.Entry<String, PEPForm> entry : pepExtFormMaps.entrySet()) {
            returnPEPForms.add(entry.getValue());
        }
        return returnPEPForms;
    }


    private List<PEPTaskVO> findCurrentTasks(String procInstId) {
        TaskQuery taskQuery = taskService.createTaskQuery()
            .processInstanceIdIn(pepProcessService.findProcAndSubInstIds(procInstId))
            .includeIdentityLinks();
        List<Task> tasks = taskQuery.list();
        List<PEPTaskVO> taskVOs = TaskConvert.convert(tasks);
        taskVOs = buildTaskUserName(taskVOs, WorkFlowConstants.ASSIGNEE);
        taskVOs = buildTaskCandidateUserName(taskVOs);
        taskVOs = buildTaskRoleName(taskVOs);
        taskVOs = buildTaskGroupName(taskVOs);
        return taskVOs;
    }

    /**
     * 为task封装流程实例信息
     *
     * @param tasks taskVO集合
     * @return 包含流程实例信息的taskVO集合
     */
    private List<PEPTaskVO> buildTaskProsInstMsg(List<PEPTaskVO> tasks) {
        if (CollectionUtil.isEmpty(tasks)) {
            return tasks;
        }
        Set<String> processInstanceIds = new HashSet<>();
        //key:procInstId  value:vos
        Map<String, List<PEPTaskVO>> matchMap = new HashMap<>(16);
        for (PEPTaskVO taskVO : tasks) {
            processInstanceIds.add(taskVO.getProcInstId());
            List<PEPTaskVO> matchTaskVOs = matchMap.get(taskVO.getProcInstId());
            if (null == matchTaskVOs) {
                matchTaskVOs = new ArrayList<>();
            }
            matchTaskVOs.add(taskVO);
            matchMap.put(taskVO.getProcInstId(), matchTaskVOs);
        }
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIds).list();
        for (ProcessInstance processInstance : processInstances) {
            List<PEPTaskVO> matchTaskVOs = matchMap.get(processInstance.getId());
            if (CollectionUtil.isEmpty(matchTaskVOs)) {
                continue;
            }
            for (PEPTaskVO pepTaskVO : matchTaskVOs) {
                pepTaskVO.setPepProcInstVO(ProcInstConvert.convert(processInstance));
            }
        }
        return tasks;
    }

    /**
     * 为task集合封装人员名称
     *
     * @param tasks 任务集合
     * @param type  流程启动者或者经办人
     * @return taskVO集合
     */
    private List<PEPTaskVO> buildTaskUserName(List<PEPTaskVO> tasks, String type) {
        if (CollectionUtil.isEmpty(tasks)) {
            return tasks;
        }
        Set<String> userIds = new HashSet<>();
        //key:userId  value:vos
        Map<String, List<PEPTaskVO>> matchMap = new HashMap<>(16);
        for (PEPTaskVO taskVO : tasks) {
            String userId = getUserId(taskVO, type);
            if (StringUtil.isEmpty(userId)) {
                continue;
            }
            userIds.add(userId);
            matchMap = buildMatchUserMap(matchMap, userId, taskVO);
        }
        if (CollectionUtil.isEmpty(userIds)) {
            return tasks;
        }
        List<User> users = new ArrayList<>(userDao.findAll(userIds));
        for (User user : users) {
            List<PEPTaskVO> matchTaskVOs = matchMap.get(user.getId());
            if (CollectionUtil.isEmpty(matchTaskVOs)) {
                continue;
            }
            for (PEPTaskVO pepTaskVO : matchTaskVOs) {
                if (WorkFlowConstants.ASSIGNEE.equals(type)) {
                    pepTaskVO.setAssigneeName(user.getName());
                    continue;
                }
                pepTaskVO.getPepProcInstVO().setStartUserName(user.getName());
            }
        }
        return tasks;
    }

    private List<PEPTaskVO> buildTaskRoleName(List<PEPTaskVO> tasks) {
        if (CollectionUtil.isEmpty(tasks)) {
            return tasks;
        }
        for (PEPTaskVO pepTaskVO : tasks) {
            if (CollectionUtil.isEmpty(pepTaskVO.getCandidateRoles())) {
                continue;
            }
            List<Role> roles = new ArrayList<>(roleDao.findAllById(pepTaskVO.getCandidateRoles()));
            Set<String> roleNames = new HashSet<>();
            for (Role role : roles) {
                roleNames.add(role.getName());
            }
            pepTaskVO.setCandidateRoleNames(roleNames);
        }
        return tasks;
    }

    private List<PEPTaskVO> buildTaskGroupName(List<PEPTaskVO> tasks) {
        if (CollectionUtil.isEmpty(tasks)) {
            return tasks;
        }
        for (PEPTaskVO pepTaskVO : tasks) {
            if (CollectionUtil.isEmpty(pepTaskVO.getCandidateGroups())) {
                continue;
            }
            List<UserGroup> groups = new ArrayList<>(userGroupDao.findAll(pepTaskVO.getCandidateGroups()));
            Set<String> groupNames = new HashSet<>();
            for (UserGroup userGroup : groups) {
                groupNames.add(userGroup.getName());
            }
            pepTaskVO.setCandidateGroupNames(groupNames);
        }
        return tasks;
    }

    private List<PEPTaskVO> buildTaskCandidateUserName(List<PEPTaskVO> tasks) {
        if (CollectionUtil.isEmpty(tasks)) {
            return tasks;
        }
        for (PEPTaskVO pepTaskVO : tasks) {
            if (CollectionUtil.isEmpty(pepTaskVO.getCandidateUsers())) {
                continue;
            }
            List<User> users = new ArrayList<>(userDao.findAll(pepTaskVO.getCandidateUsers()));
            Set<String> userNames = new HashSet<>();
            for (User user : users) {
                userNames.add(user.getName());
            }
            pepTaskVO.setCandidateUserNames(userNames);
        }
        return tasks;
    }

    private String getUserId(PEPTaskVO taskVO, String type) {
        if (WorkFlowConstants.ASSIGNEE.equals(type)) {
            return taskVO.getAssignee();
        }
        if (null == taskVO.getPepProcInstVO()) {
            return null;
        }
        return taskVO.getPepProcInstVO().getStartUserId();
    }

    private Map<String, List<PEPTaskVO>> buildMatchUserMap(Map<String, List<PEPTaskVO>> matchMap,
                                                           String userId, PEPTaskVO pepTaskVO) {
        List<PEPTaskVO> matchTaskVOs = matchMap.get(userId);
        if (null == matchTaskVOs) {
            matchTaskVOs = new ArrayList<>();
        }
        matchTaskVOs.add(pepTaskVO);
        matchMap.put(userId, matchTaskVOs);
        return matchMap;
    }
}
