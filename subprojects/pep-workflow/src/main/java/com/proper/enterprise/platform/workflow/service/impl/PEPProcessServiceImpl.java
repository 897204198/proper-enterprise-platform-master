package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.api.PEPForm;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.workflow.convert.ProcInstConvert;
import com.proper.enterprise.platform.workflow.service.PEPProcessService;
import com.proper.enterprise.platform.workflow.util.GlobalVariableUtil;
import com.proper.enterprise.platform.workflow.util.VariableUtil;
import com.proper.enterprise.platform.workflow.vo.PEPExtFormVO;
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO;
import com.proper.enterprise.platform.workflow.vo.enums.PEPProcInstStateEnum;
import com.proper.enterprise.platform.workflow.vo.enums.ShowType;
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

@Service
public class PEPProcessServiceImpl implements PEPProcessService {

    @Value("#{'${workflow.global.variables}'.split(',')}")
    private List<String> globalVariableKeys;

    private RuntimeService runtimeService;

    private FormService formService;

    private RepositoryService repositoryService;

    private HistoryService historyService;

    private UserDao userDao;

    @Autowired
    PEPProcessServiceImpl(RuntimeService runtimeService,
                          FormService formService,
                          RepositoryService repositoryService,
                          HistoryService historyService,
                          UserDao userDao) {
        this.runtimeService = runtimeService;
        this.formService = formService;
        this.repositoryService = repositoryService;
        this.historyService = historyService;
        this.userDao = userDao;
    }

    @Override
    public PEPProcInstVO startProcess(String procDefKey, Map<String, Object> variables) {
        variables = VariableUtil.handleVariableDateType(variables);
        ProcessDefinition processDefinition = repositoryService
            .createProcessDefinitionQuery()
            .processDefinitionKey(procDefKey)
            .latestVersion()
            .singleResult();
        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        Map<String, Object> globalVariables = new HashMap<>(16);
        if (StringUtil.isNotEmpty(startFormKey)) {
            globalVariables.put(startFormKey, variables);
            globalVariables.put(WorkFlowConstants.START_FORM_DATA, variables);
        }
        //设置允许是skip
        globalVariables.put(WorkFlowConstants.SKIP_EXPRESSION_ENABLED, true);
        globalVariables = setDefaultVariables(globalVariables);
        globalVariables = GlobalVariableUtil.setGlobalVariable(globalVariables, variables, globalVariableKeys);
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), globalVariables);
        return ProcInstConvert.convert(processInstance);
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
        dataTrunk.setData(ProcInstConvert.convert(historicProcessInstances));
        dataTrunk.setCount(historicProcessInstanceQuery.count());
        return dataTrunk;
    }

    public List<PEPForm> buildPage(String procInstId) {
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
            PEPExtFormVO pepExtFormVO = new PEPExtFormVO(historicTaskInstance.getFormKey(),
                getFormData(historicTaskInstance.getProcessVariables(), historicTaskInstance.getFormKey()));
            if (StringUtil.isEmpty(pepExtFormVO.getFormKey())) {
                continue;
            }
            pepExtFormVO.setShowType(ShowType.SHOW);
            pepExtFormMaps.put(pepExtFormVO.getFormKey(), pepExtFormVO);
        }
        List<PEPForm> pepForms = new ArrayList<>();
        for (Map.Entry<String, PEPForm> entry : pepExtFormMaps.entrySet()) {
            pepForms.add(entry.getValue());
        }
        return pepForms;
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

    private Map<String, Object> setDefaultVariables(Map<String, Object> globalVariables) {
        return setStartUserName(globalVariables);
    }

    private Map<String, Object> setStartUserName(Map<String, Object> globalVariables) {
        if (StringUtil.isNotEmpty(Authentication.getCurrentUserId())) {
            User user = userDao.findOne(Authentication.getCurrentUserId());
            if (null != user) {
                //设置默认全局变量
                globalVariables.put(WorkFlowConstants.INITIATOR_NAME, user.getName());
            }
        }
        return globalVariables;
    }

    private Map<String, Object> getFormData(Map<String, Object> processVariables, String formKey) {
        if (StringUtil.isEmpty(formKey)) {
            return null;
        }
        return (Map<String, Object>) processVariables.get(formKey);
    }
}
