package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.workflow.convert.TaskConvert;
import com.proper.enterprise.platform.workflow.convert.VariableConvert;
import com.proper.enterprise.platform.workflow.enums.FlowableExceptionEnum;
import com.proper.enterprise.platform.workflow.flowable.idm.service.impl.PEPGroupQueryImpl;
import com.proper.enterprise.platform.workflow.model.PEPExtForm;
import com.proper.enterprise.platform.workflow.model.PEPWorkflowPage;
import com.proper.enterprise.platform.workflow.predicate.VariableQueryPredicate;
import com.proper.enterprise.platform.workflow.repository.PEPHistoricVariableInstanceRepository;
import com.proper.enterprise.platform.workflow.service.PEPProcessService;
import com.proper.enterprise.platform.workflow.service.PEPTaskService;
import com.proper.enterprise.platform.workflow.util.VariableUtil;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPageVO;
import org.apache.commons.collections.MapUtils;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.identitylink.api.IdentityLinkInfo;
import org.flowable.idm.api.Group;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PEPTaskServiceImpl implements PEPTaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PEPTaskServiceImpl.class);

    private TaskService taskService;

    private HistoryService historyService;

    @Autowired
    private PEPProcessService pepProcessService;

    private UserDao userDao;

    @Autowired
    private PEPHistoricVariableInstanceRepository pepHistoricVariableInstanceRepository;

    @Autowired
    PEPTaskServiceImpl(TaskService taskService,
                       HistoryService historyService,
                       UserDao userDao) {
        this.taskService = taskService;
        this.historyService = historyService;
        this.userDao = userDao;
    }


    @Override
    public void complete(String taskId) {
        complete(taskId, null);
    }

    @Override
    public void complete(String taskId, Map<String, Object> variables) {
        Task task = getTask(taskId);
        validAssigneeIsCurrentUser(task);
        variables = VariableUtil.handleVariableSpecialType(variables);
        if (MapUtils.isEmpty(variables)) {
            variables = new HashMap<>(16);
        }
        taskService.setVariablesLocal(taskId, variables);
        Map<String, Object> globalVariables = taskService.getVariables(taskId);
        if (MapUtils.isEmpty(globalVariables)) {
            globalVariables = new HashMap<>(16);
        }
        globalVariables = addNoSameAssigneeSkipMark(task, globalVariables);
        globalVariables.putAll(variables);
        String formKey = StringUtil.isEmpty(task.getFormKey())
            ? (String) globalVariables.get(WorkFlowConstants.START_FORM_KEY)
            : task.getFormKey();
        if (StringUtil.isNotEmpty(formKey)) {
            globalVariables.put(formKey, variables);
        }
        if (null == task.getAssignee()) {
            taskService.claim(taskId, Authentication.getCurrentUserId());
        }
        try {
            taskService.complete(taskId, globalVariables);
        } catch (FlowableException flowableException) {
            LOGGER.error("workflow complete task error:taskId:{}", taskId, flowableException);
            throw new ErrMsgException(FlowableExceptionEnum.convertFlowableException(flowableException));
        } catch (Exception e) {
            LOGGER.error("workflow complete task error:taskId:{}", taskId, e);
            throw new ErrMsgException(I18NUtil.getMessage("workflow.task.complete.error"));
        }
    }

    @Override
    public long getTodoCount() {
        return taskService.createTaskQuery().taskCandidateOrAssigned(Authentication.getCurrentUserId()).count();
    }

    @Override
    public long getTodoCount(String userId) {
        return taskService.createTaskQuery().taskCandidateOrAssigned(userId).count();
    }

    private Task getTask(String taskId) {
        Task task = taskService.createTaskQuery()
            .includeIdentityLinks()
            .taskId(taskId).singleResult();
        if (null != task) {
            return task;
        }
        HistoricTaskInstance historicTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (null != historicTask) {
            throw new ErrMsgException(I18NUtil.getMessage("workflow.task.completed"));
        }
        throw new ErrMsgException(I18NUtil.getMessage("workflow.task.not.exist"));
    }

    private void validAssigneeIsCurrentUser(Task task) {
        if (StringUtil.isEmpty(Authentication.getCurrentUserId())) {
            throw new ErrMsgException(I18NUtil.getMessage("workflow.task.complete.no.permissions"));
        }
        if (StringUtil.isNotEmpty(task.getAssignee())
            && task.getAssignee().equals(Authentication.getCurrentUserId())) {
            return;
        }
        User user = userDao.findById(Authentication.getCurrentUserId());
        if (null == user) {
            throw new ErrMsgException(I18NUtil.getMessage("workflow.task.complete.no.permissions"));
        }
        if (CollectionUtil.isNotEmpty(task.getIdentityLinks())) {
            for (IdentityLinkInfo identityLinkInfo : task.getIdentityLinks()) {
                if ("candidate".equals(identityLinkInfo.getType())) {
                    if (StringUtil.isNotEmpty(identityLinkInfo.getUserId())
                        && identityLinkInfo.getUserId().equals(user.getId())) {
                        return;
                    }
                    if (StringUtil.isNotEmpty(identityLinkInfo.getGroupId())) {
                        List<Group> groups = new PEPGroupQueryImpl()
                            .groupMember(Authentication.getCurrentUserId())
                            .list();
                        for (Group group : groups) {
                            if (identityLinkInfo.getGroupId().equals(group.getId())) {
                                return;
                            }
                        }
                    }
                }
            }
        }
        throw new ErrMsgException(I18NUtil.getMessage("workflow.task.complete.no.permissions"));
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
        List<PEPTaskVO> taskVOs = new ArrayList<>(BeanUtil.convert(TaskConvert.convert(tasks), PEPTaskVO.class));
        DataTrunk<PEPTaskVO> dataTrunk = new DataTrunk<>();
        dataTrunk.setData(taskVOs);
        dataTrunk.setCount(taskQuery.count());
        return dataTrunk;
    }

    @Override
    public DataTrunk<PEPTaskVO> findTaskAssigneeIsMePagination(String processDefinitionName, PageRequest pageRequest) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
            .taskAssignee(Authentication.getCurrentUserId())
            .finished()
            .orderByHistoricTaskInstanceEndTime()
            .desc();
        if (StringUtil.isNotEmpty(processDefinitionName)) {
            historicTaskInstanceQuery.processDefinitionName(processDefinitionName);
        }
        List<HistoricTaskInstance> historicTasks = historicTaskInstanceQuery
            .listPage(pageRequest.getPageNumber() * pageRequest.getPageSize(),
                pageRequest.getPageSize());

        if (!CollectionUtil.isNotEmpty(historicTasks)) {
            return new DataTrunk<>(new ArrayList<>(), historicTaskInstanceQuery.count());
        }

        Set<String> taskIds = new HashSet<>();
        Set<String> processInstIds = new HashSet<>();
        // 使用 lambda 提供的 forEach 方法进行遍历出任务Id集合和流程实例Id集合
        historicTasks.forEach(
            historicTask -> {
                taskIds.add(historicTask.getId());
                processInstIds.add(historicTask.getProcessInstanceId());
            }
        );

        VariableQueryPredicate queryByTaskId = new VariableQueryPredicate();
        queryByTaskId.setTaskIds(taskIds);
        List<HistoricVariableInstance> historicTasksVariables =
            pepHistoricVariableInstanceRepository.findHistoricVariableByQueryPredicate(queryByTaskId);
        VariableQueryPredicate queryByProcInstId = new VariableQueryPredicate();
        queryByProcInstId.setProcInstIds(processInstIds);
        List<HistoricVariableInstance> historicProcessInstsVariables =
            pepHistoricVariableInstanceRepository.findHistoricVariableByQueryPredicate(queryByProcInstId);

        // 将任务节点变量通过任务Id进行分组
        Map<String, Map<String, Object>> formDataByTaskId =
            VariableConvert.groupVariables(historicTasksVariables, HistoricVariableInstance::getTaskId);

        // 将流程实例变量通过流程实例Id进行分组
        Map<String, Map<String, Object>> globalDataByProcessInstId =
            VariableConvert.groupVariables(historicProcessInstsVariables, HistoricVariableInstance::getProcessInstanceId);

        List<PEPTaskVO> taskVOs = new ArrayList<>(
            BeanUtil.convert(TaskConvert.convertHisTasks(historicTasks, formDataByTaskId, globalDataByProcessInstId), PEPTaskVO.class));

        DataTrunk<PEPTaskVO> dataTrunk = new DataTrunk<>();
        dataTrunk.setData(taskVOs);
        dataTrunk.setCount(historicTaskInstanceQuery.count());
        return dataTrunk;
    }

    @Override
    public PEPWorkflowPageVO buildPage(String taskId) {
        Task task = taskService.createTaskQuery().includeProcessVariables().taskId(taskId).singleResult();
        if (null == task) {
            throw new ErrMsgException("can't buildTaskPage with empty task");
        }
        PEPWorkflowPageVO hisWorkflowPageVO = pepProcessService.buildPage(pepProcessService.findTopMostProcInstId(task.getProcessInstanceId()));
        Map<String, PEPForm> pepExtFormMaps = new HashMap<>(16);
        for (PEPForm pepForm : hisWorkflowPageVO.getForms()) {
            pepExtFormMaps.put(pepForm.getFormKey(), pepForm);
        }
        PEPExtForm pepExtForm = new PEPExtForm(task);
        if (StringUtil.isNotEmpty(pepExtForm.getFormKey())) {
            pepExtFormMaps.put(pepExtForm.getFormKey(), pepExtForm.convert());
        }
        List<PEPForm> returnPEPForms = new ArrayList<>();
        for (Map.Entry<String, PEPForm> entry : pepExtFormMaps.entrySet()) {
            returnPEPForms.add(entry.getValue());
        }
        PEPWorkflowPage pepWorkflowPage = new PEPWorkflowPage();
        pepWorkflowPage.setForms(returnPEPForms);
        pepWorkflowPage.setGlobalVariables(task.getProcessVariables());
        return pepWorkflowPage.convert();
    }

    @Override
    public List<PEPTaskVO> findHisTasks(String procInstId) {
        List<HistoricTaskInstance> historicTaskInstances = historyService
            .createHistoricTaskInstanceQuery()
            .processInstanceIdIn(pepProcessService.findProcAndSubInstIds(procInstId))
            .includeProcessVariables()
            .includeTaskLocalVariables()
            .finished()
            .orderByHistoricTaskInstanceEndTime()
            .desc()
            .list();
        return new ArrayList<>(BeanUtil.convert(TaskConvert.convertHisTasks(historicTaskInstances), PEPTaskVO.class));
    }

    @Override
    public List<PEPTaskVO> findCurrentTasks(String procInstId) {
        TaskQuery taskQuery = taskService.createTaskQuery()
            .processInstanceIdIn(pepProcessService.findProcAndSubInstIds(procInstId))
            .includeIdentityLinks();
        List<Task> tasks = taskQuery.list();
        return new ArrayList<>(BeanUtil.convert(TaskConvert.convert(tasks), PEPTaskVO.class));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> addNoSameAssigneeSkipMark(Task task, Map<String, Object> variables) {
        Set<String> noSameAssigneeSkipActIds = (Set<String>) variables.get(WorkFlowConstants.NO_SAME_ASSIGNEE_SKIP_REMARK);
        if (null == noSameAssigneeSkipActIds) {
            noSameAssigneeSkipActIds = new HashSet<>();
        }
        noSameAssigneeSkipActIds.add(task.getId());
        variables.put(WorkFlowConstants.NO_SAME_ASSIGNEE_SKIP_REMARK, noSameAssigneeSkipActIds);
        return variables;
    }

}
