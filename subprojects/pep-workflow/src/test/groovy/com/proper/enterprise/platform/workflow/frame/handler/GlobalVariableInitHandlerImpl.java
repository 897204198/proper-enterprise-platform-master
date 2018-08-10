package com.proper.enterprise.platform.workflow.frame.handler;

import com.proper.enterprise.platform.workflow.handler.GlobalVariableInitHandler;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GlobalVariableInitHandlerImpl implements GlobalVariableInitHandler {
    @Override
    public Map<String, Object> init(Map<String, Object> globalVars, ProcessDefinition processDefinition) {
        globalVars.put("workflowtest", "workflowtest");
        return globalVars;
    }
}
