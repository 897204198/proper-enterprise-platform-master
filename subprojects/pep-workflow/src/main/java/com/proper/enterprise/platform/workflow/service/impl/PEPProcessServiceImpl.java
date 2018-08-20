package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.workflow.convert.ProcInstConvert;
import com.proper.enterprise.platform.workflow.decorator.GlobalVariableInitDecorator;
import com.proper.enterprise.platform.workflow.handler.GlobalVariableInitHandler;
import com.proper.enterprise.platform.workflow.model.PEPExtForm;
import com.proper.enterprise.platform.workflow.model.PEPProcInst;
import com.proper.enterprise.platform.workflow.model.PEPWorkflowPage;
import com.proper.enterprise.platform.workflow.service.PEPProcessService;
import com.proper.enterprise.platform.workflow.service.PEPTaskService;
import com.proper.enterprise.platform.workflow.util.GlobalVariableUtil;
import com.proper.enterprise.platform.workflow.util.VariableUtil;
import com.proper.enterprise.platform.workflow.vo.*;
import com.proper.enterprise.platform.workflow.vo.enums.PEPProcInstStateEnum;
import com.proper.enterprise.platform.workflow.vo.enums.ShowType;
import org.apache.commons.collections.MapUtils;
import org.flowable.engine.FormService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.proper.enterprise.platform.core.PEPConstants.DEFAULT_DATETIME_FORMAT;

@Service
public class PEPProcessServiceImpl implements PEPProcessService {

    @Value("#{'${workflow.global.variables}'.split(',')}")
    private List<String> globalVariableKeys;

    private RuntimeService runtimeService;

    private FormService formService;

    private RepositoryService repositoryService;

    private HistoryService historyService;

    @Autowired
    private PEPTaskService pepTaskService;

    private UserDao userDao;

    private GlobalVariableInitHandler customHandler;

    private GlobalVariableInitDecorator globalVariableInitDecorator;

    @Autowired(required = false)
    PEPProcessServiceImpl(RuntimeService runtimeService,
                          FormService formService,
                          RepositoryService repositoryService,
                          GlobalVariableInitHandler customHandler,
                          HistoryService historyService,
                          UserDao userDao) {
        this.runtimeService = runtimeService;
        this.formService = formService;
        this.repositoryService = repositoryService;
        this.historyService = historyService;
        this.userDao = userDao;
        this.customHandler = customHandler;
        this.globalVariableInitDecorator = new GlobalVariableInitDecorator(repositoryService, this.customHandler, userDao);
    }

    @Override
    public PEPProcInstVO startProcess(String procDefKey, Map<String, Object> variables) {
        variables = VariableUtil.handleVariableSpecialType(variables);
        ProcessDefinition processDefinition = repositoryService
            .createProcessDefinitionQuery()
            .processDefinitionKey(procDefKey)
            .latestVersion()
            .singleResult();

        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        Map<String, Object> globalVariables = new HashMap<>(16);
        if (MapUtils.isNotEmpty(variables)) {
            globalVariables.putAll(variables);
        }
        if (StringUtil.isNotEmpty(startFormKey)) {
            globalVariables.put(startFormKey, variables);
            globalVariables.put(WorkFlowConstants.START_FORM_DATA, variables);
        }
        //设置允许是skip
        globalVariables.put(WorkFlowConstants.SKIP_EXPRESSION_ENABLED, true);
        globalVariables = globalVariableInitDecorator.init(globalVariables, processDefinition);
        globalVariables = GlobalVariableUtil.setGlobalVariable(globalVariables, variables, globalVariableKeys);
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), globalVariables);
        return new PEPProcInst(processInstance).convert();
    }

    @Override
    public DataTrunk<PEPProcInstVO> findProcessStartByMePagination(String processDefinitionName,
                                                                   PEPProcInstStateEnum state, PageRequest pageRequest) {
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService
            .createHistoricProcessInstanceQuery()
            .startedBy(Authentication.getCurrentUserId());
        if (StringUtil.isNotEmpty(processDefinitionName)) {
            historicProcessInstanceQuery.processDefinitionName(processDefinitionName);
        }
        if (null != state && PEPProcInstStateEnum.DONE == state) {
            historicProcessInstanceQuery.finished();
        }
        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery
            .includeProcessVariables()
            .orderByProcessInstanceStartTime()
            .desc()
            .listPage(pageRequest.getPageNumber() * pageRequest.getPageSize(), pageRequest.getPageSize());
        DataTrunk<PEPProcInstVO> dataTrunk = new DataTrunk<>();
        dataTrunk.setData(BeanUtil.convert(ProcInstConvert.convert(historicProcessInstances), PEPProcInstVO.class));
        dataTrunk.setCount(historicProcessInstanceQuery.count());
        return dataTrunk;
    }

    public PEPWorkflowPageVO buildPage(String procInstId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(procInstId)
            .includeProcessVariables()
            .singleResult();
        Map<String, PEPForm> pepExtFormMaps = new HashMap<>(16);
        String startFormKey = formService.getStartFormKey(historicProcessInstance.getProcessDefinitionId());
        if (StringUtil.isNotEmpty(startFormKey)) {
            PEPExtFormVO startForm = new PEPExtFormVO(startFormKey,
                getFormData(historicProcessInstance.getProcessVariables(), startFormKey));
            startForm.setShowType(ShowType.SHOW);
            pepExtFormMaps.put(startFormKey, startForm);
        }
        List<HistoricTaskInstance> historicTaskInstances = historyService
            .createHistoricTaskInstanceQuery()
            .processInstanceIdIn(findProcAndSubInstIds(procInstId))
            .includeProcessVariables()
            .finished()
            .orderByHistoricTaskInstanceEndTime()
            .desc()
            .list();
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            PEPExtForm pepExtForm = new PEPExtForm(historicTaskInstance.getFormKey(),
                getFormData(historicTaskInstance.getProcessVariables(), historicTaskInstance.getFormKey()));
            if (StringUtil.isEmpty(pepExtForm.getFormKey())) {
                continue;
            }
            pepExtForm.setShowType(ShowType.SHOW);
            pepExtFormMaps.put(pepExtForm.getFormKey(), pepExtForm.convert());
        }
        List<PEPForm> pepForms = new ArrayList<>();
        for (Map.Entry<String, PEPForm> entry : pepExtFormMaps.entrySet()) {
            pepForms.add(entry.getValue());
        }
        PEPWorkflowPage pepWorkflowPage = new PEPWorkflowPage();
        pepWorkflowPage.setForms(pepForms);
        pepWorkflowPage.setGlobalVariables(historicProcessInstance.getProcessVariables());
        return pepWorkflowPage.convert();
    }

    @Override
    public PEPWorkflowPathVO findWorkflowPath(String procInstId) {
        PEPWorkflowPathVO pepWorkflowPathVO = new PEPWorkflowPathVO();
        pepWorkflowPathVO.setHisTasks(pepTaskService.findHisTasks(procInstId));
        pepWorkflowPathVO.setCurrentTasks(pepTaskService.findCurrentTasks(procInstId));
        pepWorkflowPathVO.setStart(findStart(procInstId));
        pepWorkflowPathVO.setEnded(isEnded(procInstId));
        return pepWorkflowPathVO;
    }

    public List<String> findProcAndSubInstIds(String procInstId) {
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
            .includeProcessVariables()
            .or()
            .processInstanceId(procInstId)
            .superProcessInstanceId(procInstId)
            .endOr()
            .list();
        List<String> procInstIds = new ArrayList<>();
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            procInstIds.add(historicProcessInstance.getId());
        }
        return procInstIds;
    }

    @Override
    public String findTopMostProcInstId(String procInstId) {
        HistoricProcessInstance historicProcessInstance = historyService
            .createHistoricProcessInstanceQuery()
            .includeProcessVariables()
            .processInstanceId(procInstId)
            .singleResult();
        if (StringUtil.isEmpty(historicProcessInstance.getSuperProcessInstanceId())) {
            return historicProcessInstance.getId();
        }
        return findTopMostProcInstId(historicProcessInstance.getSuperProcessInstanceId());
    }


    private Map<String, Object> getFormData(Map<String, Object> processVariables, String formKey) {
        if (StringUtil.isEmpty(formKey)) {
            return null;
        }
        return (Map<String, Object>) processVariables.get(formKey);
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
        User user = userDao.findOne(processInstance.getStartUserId());
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
}
