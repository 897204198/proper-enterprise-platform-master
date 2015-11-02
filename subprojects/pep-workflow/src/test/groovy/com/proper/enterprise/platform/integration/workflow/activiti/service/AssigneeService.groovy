package com.proper.enterprise.platform.integration.workflow.activiti.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AssigneeService {

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

    public void executionEnd(def execution) {
        LOGGER.info("Invoke into executionEnd method.")
        LOGGER.info("execution type is ${execution.class}")
    }

    public void taskEnd(def task) {
        LOGGER.info("Invoke into taskEnd method.")
        LOGGER.info("task type is ${task.class}")
    }

}
