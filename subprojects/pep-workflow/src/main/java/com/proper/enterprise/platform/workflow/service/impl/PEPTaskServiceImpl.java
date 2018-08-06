package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.workflow.convert.TaskConvert;
import com.proper.enterprise.platform.workflow.model.PEPExtForm;
import com.proper.enterprise.platform.workflow.service.PEPProcessService;
import com.proper.enterprise.platform.workflow.service.PEPTaskService;
import com.proper.enterprise.platform.workflow.util.GlobalVariableUtil;
import com.proper.enterprise.platform.workflow.util.VariableUtil;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import org.apache.commons.collections.MapUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PEPTaskServiceImpl implements PEPTaskService {

    @Value("${workflow.global.variables}")
    private List<String> globalVariableKeys;

    private TaskService taskService;

    private HistoryService historyService;

    @Autowired
    private PEPProcessService pepProcessService;

    @Autowired
    PEPTaskServiceImpl(TaskService taskService,
                       HistoryService historyService) {
        this.taskService = taskService;
        this.historyService = historyService;
    }


    @Override
    public void complete(String taskId) {
        complete(taskId, null);
    }

    @Override
    public void complete(String taskId, Map<String, Object> variables) {
        variables = VariableUtil.handleVariableSpecialType(variables);
        if (MapUtils.isEmpty(variables)) {
            variables = new HashMap<>(16);
        }
        taskService.setVariablesLocal(taskId, variables);
        Map<String, Object> globalVariables = taskService.getVariables(taskId);
        if (MapUtils.isEmpty(globalVariables)) {
            globalVariables = new HashMap<>(16);
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        globalVariables = addNoSameAssigneeSkipMark(task, globalVariables);
        globalVariables.putAll(variables);
        String formKey = task.getFormKey();
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
        List<PEPTaskVO> taskVOs = new ArrayList<>(BeanUtil.convert(TaskConvert.convert(tasks), PEPTaskVO.class));
        DataTrunk<PEPTaskVO> dataTrunk = new DataTrunk<>();
        dataTrunk.setData(taskVOs);
        dataTrunk.setCount(taskQuery.count());
        return dataTrunk;
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
        PEPExtForm pepExtForm = new PEPExtForm(task);
        if (StringUtil.isNotEmpty(pepExtForm.getFormKey())) {
            pepExtFormMaps.put(pepExtForm.getFormKey(), pepExtForm.convert());
        }
        List<PEPForm> returnPEPForms = new ArrayList<>();
        for (Map.Entry<String, PEPForm> entry : pepExtFormMaps.entrySet()) {
            returnPEPForms.add(entry.getValue());
        }
        return returnPEPForms;
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

    private Map<String, Object> addNoSameAssigneeSkipMark(Task task, Map<String, Object> variables) {
        Set<String> noSameAssigneeSkipActIds = (Set<String>) task
            .getProcessVariables()
            .get(WorkFlowConstants.NO_SAME_ASSIGNEE_SKIP_REMARK);
        if (null == noSameAssigneeSkipActIds) {
            noSameAssigneeSkipActIds = new HashSet<>();
        }
        noSameAssigneeSkipActIds.add(task.getId());
        variables.put(WorkFlowConstants.NO_SAME_ASSIGNEE_SKIP_REMARK, noSameAssigneeSkipActIds);
        return variables;
    }

}
