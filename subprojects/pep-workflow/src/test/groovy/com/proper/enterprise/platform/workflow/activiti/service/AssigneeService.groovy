package com.proper.enterprise.platform.workflow.activiti.service

import org.activiti.engine.HistoryService
import org.activiti.engine.delegate.DelegateExecution
import org.activiti.engine.delegate.DelegateTask
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AssigneeService {

    @Autowired HistoryService historyService

    private static final Logger LOGGER = LoggerFactory.getLogger(AssigneeService.class)

    String getDeptMaster(String applicant) {
        LOGGER.info("Invoke into getDeptMaster method with param: ${applicant}")
        return 'Alpha Hinex'
    }

    String getDeptMasters(String dept) {
        LOGGER.info("Invoke into getDeptMasters method with param: ${dept}")
        return ['hinex1', 'hinex2', 'hinex3']
    }

    List getCountersignAssigneeList() {
        LOGGER.info("Invoke into getCountersignAssigneeList method")
        return (0..9).collect { idx ->
            "cs-hinex$idx"
        }
    }

    void executionEnd(DelegateExecution execution) {
        def nrOfInstances = execution.getVariable('nrOfInstances')
        def loopCounter = execution.getVariable('loopCounter')
        if (loopCounter != null && nrOfInstances == loopCounter + 1) {
            def vars = historyService.createHistoricVariableInstanceQuery()
                                     .processInstanceId(execution.getProcessInstanceId())
                                     .variableName('csApproveResult')
                                     .list()
            def passCount = vars.count { it.value == '会签同意' }
            def notPassCount = vars.count { it.value == '会签不同意' }
            LOGGER.info("Countersign result: $passCount pass , $notPassCount not pass.")
            execution.setVariable('approveResult', passCount > notPassCount ? '同意' : '不同意')
        }
    }

    void taskEnd(DelegateTask task) {
        LOGGER.info("Invoke into taskEnd method and task is ${task.getVariables()} ${task.getAssignee()}")
    }

}
