package com.proper.enterprise.platform.integration.workflow.activiti.service

import org.activiti.engine.TaskService
import org.activiti.engine.delegate.DelegateTask
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AssigneeService {

    @Autowired TaskService taskService

    private static final Logger LOGGER = LoggerFactory.getLogger(AssigneeService.class)

    public String getDeptMaster(String applicant) {
        LOGGER.info("Invoke into getDeptMaster method with param: ${applicant}")
        return 'Alpha Hinex'
    }

    public String getDeptMasters(String dept) {
        LOGGER.info("Invoke into getDeptMasters method with param: ${dept}")
        return ['hinex1', 'hinex2', 'hinex3']
    }

    public List getCountersignAssigneeList() {
        LOGGER.info("Invoke into getCountersignAssigneeList method")
        return (0..9).collect { idx ->
            "cs-hinex$idx"
        }
    }

    public void executionEnd(ExecutionListenerExecution execution) {
        LOGGER.info("Invoke into executionEnd method")
        def nrOfInstances = execution.getVariable('nrOfInstances')
        def nrOfCompletedInstances = execution.getVariable('nrOfCompletedInstances')
        if (nrOfInstances == nrOfCompletedInstances) {
            execution.setVariable('approveResult', '同意')
        }
        execution.getVariables().each {
            println it
        }
    }

    public void taskEnd(DelegateTask task) {
        LOGGER.info("Invoke into taskEnd method and task is ${task.getVariables()} ${task.getAssignee()}")
    }

}
