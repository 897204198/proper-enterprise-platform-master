package com.proper.enterprise.platform.workflow.flowable.service

import org.flowable.engine.HistoryService
import org.flowable.engine.delegate.DelegateExecution
import org.flowable.task.service.delegate.DelegateTask
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
        def nrOfCompletedInstances = execution.getVariable('nrOfCompletedInstances')
        LOGGER.debug("Invoke into executionEnd method and execution is ${execution.getVariables()}")
        //通过日志判断出来了  在activiti中会签任务最后总任务结束时是包含已完成实例数和实例总数相关数据的
        // 但是flowable则不然，它会在会签总任务结束时清除相关数据
        //目前想到的方案是已完成会签数+1 在总任务前处理路由走向
        if (nrOfCompletedInstances != null && nrOfInstances == nrOfCompletedInstances+1) {
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
