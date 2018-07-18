package com.proper.enterprise.platform.workflow.api;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sameAssigneeSkip")
public class SameAssigneeSkip {

    private ProcessEngineConfigurationImpl processEngineConfiguration;

    @Autowired
    SameAssigneeSkip(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    private HistoricTaskInstance findLastTask(String executionId) {
        HistoricTaskInstance lastHisTask = processEngineConfiguration.getHistoryService()
            .createHistoricTaskInstanceQuery()
            .executionId(executionId)
            .orderByHistoricTaskInstanceEndTime()
            .asc()
            .singleResult();
        if (null == lastHisTask) {
            return null;
        }
        return lastHisTask;
    }

    public boolean skip(ExecutionEntity execution) {
        String assignee = ((UserTask) execution.getCurrentFlowElement()).getAssignee();
        String initiator = (String) execution.getVariable(WorkFlowConstants.INITIATOR);
        if (StringUtil.isEmpty(assignee)) {
            return false;
        }
        assignee = (String) processEngineConfiguration.getExpressionManager().createExpression(assignee).getValue(execution);
        HistoricTaskInstance lastTask = findLastTask(execution.getId());
        if (null == lastTask) {
            if (StringUtil.isEmpty(initiator)) {
                return false;
            }
            if (!initiator.equals(assignee)) {
                return false;
            }
            addVariablePass(execution.getId());
            return true;
        }

        if (StringUtil.isEmpty(lastTask.getAssignee())) {
            return false;
        }
        if (!lastTask.getAssignee().equals(assignee)) {
            return false;
        }
        addVariablePass(execution.getId());
        return true;
    }

    private void addVariablePass(String executionId) {
        processEngineConfiguration.getRuntimeService().setVariableLocal(executionId,
            WorkFlowConstants.APPROVE_RESULT, WorkFlowConstants.APPROVE_PASS);
    }
}
