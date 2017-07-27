package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public abstract class BaseBPMController extends BaseController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    /**
     * 根据流程定义 key，发起流程，并将流程运行结束后的流程变量返回
     *
     * @param procDefKey 流程定义 key
     * @param inputs     输入流程变量 map
     * @param output     输出流程变量名
     * @return 流程变量值
     */
    protected Object getVariableAfterProcessDone(String procDefKey, Map<String, Object> inputs, String output) {
        ProcessInstance procInst = startProcess(procDefKey, inputs);
        Object var = getVariableFromHistory(procInst.getId(), output);
        cleanHisProcInst(procInst.getId());
        return var;
    }

    private ProcessInstance startProcess(String procDefKey, Map<String, Object> vars) {
        ProcessInstanceBuilder builder = runtimeService.createProcessInstanceBuilder();
        builder.processDefinitionKey(procDefKey);
        if (!CollectionUtil.isEmpty(vars)) {
            for (Map.Entry<String, Object> entry : vars.entrySet()) {
                builder.addVariable(entry.getKey(), entry.getValue());
            }
        }
        return builder.start();
    }

    private boolean isProcInstEnd(String procInstId) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult() == null;
    }

    private Object getVariableFromHistory(String procInstId, String varName) {
        return historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(procInstId)
                    .variableName(varName)
                    .singleResult()
                    .getValue();
    }

    @Async
    private void cleanHisProcInst(String procInstId) {
        historyService.deleteHistoricProcessInstance(procInstId);
    }

    @Override
    protected String handleBody(Exception ex) {
        return ex instanceof ActivitiException ? findOriginalMessage(ex) : super.handleBody(ex);
    }

    private String findOriginalMessage(Throwable t) {
        return t.getCause() == null ? t.getMessage() : findOriginalMessage(t.getCause());
    }

}
