package com.proper.enterprise.platform.workflow.decorator;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.workflow.handler.GlobalVariableInitHandler;
import com.proper.enterprise.platform.workflow.model.PEPProcInst;
import org.flowable.bpmn.model.ValuedDataObject;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;

import java.util.*;

public class GlobalVariableInitDecorator implements GlobalVariableInitHandler {

    private GlobalVariableInitHandler customHandler;

    private RepositoryService repositoryService;

    private UserDao userDao;

    public GlobalVariableInitDecorator(RepositoryService repositoryService,
                                       GlobalVariableInitHandler customHandler,
                                       UserDao userDao) {
        this.repositoryService = repositoryService;
        this.customHandler = customHandler;
        this.userDao = userDao;
    }

    @Override
    public Map<String, Object> init(Map<String, Object> globalVars, ProcessDefinition processDefinition) {
        Map<String, Object> customGlobalVars = new HashMap<>(16);
        if (null != customHandler) {
            customGlobalVars = customHandler.init(globalVars, processDefinition);
        }
        if (CollectionUtil.isEmpty(customGlobalVars)) {
            customGlobalVars = new HashMap<>(16);
        }
        customGlobalVars.putAll(setDefaultVariables(globalVars, processDefinition));
        return customGlobalVars;
    }

    private Map<String, Object> setDefaultVariables(Map<String, Object> globalVariables, ProcessDefinition processDefinition) {
        globalVariables = setStartUserName(globalVariables);
        globalVariables = setDefNoSameAssigneeSkipMark(globalVariables);
        return setProcessDefinition(globalVariables, processDefinition);
    }

    private Map<String, Object> setProcessDefinition(Map<String, Object> globalVariables, ProcessDefinition processDefinition) {
        globalVariables.put(WorkFlowConstants.PROCESS_DEFINITION_NAME, processDefinition.getName());
        globalVariables.put(WorkFlowConstants.PROCESS_CREATE_TIME, new Date());
        List<ValuedDataObject> datas = repositoryService.getBpmnModel(processDefinition.getId()).getMainProcess().getDataObjects();
        globalVariables.put(WorkFlowConstants.PROCESS_TITLE, PEPProcInst.buildProcessTitle(datas, globalVariables));
        return globalVariables;
    }

    private Map<String, Object> setStartUserName(Map<String, Object> globalVariables) {
        if (StringUtil.isNotEmpty(Authentication.getCurrentUserId())) {
            globalVariables.put(WorkFlowConstants.INITIATOR, Authentication.getCurrentUserId());
            User user = userDao.findOne(Authentication.getCurrentUserId());
            if (null != user) {
                //设置默认全局变量
                globalVariables.put(WorkFlowConstants.INITIATOR_NAME, user.getName());
            }
        }
        return globalVariables;
    }

    private Map<String, Object> setDefNoSameAssigneeSkipMark(Map<String, Object> globalVariables) {
        globalVariables.put(WorkFlowConstants.NO_SAME_ASSIGNEE_SKIP_REMARK, new HashSet<>());
        return globalVariables;
    }
}
