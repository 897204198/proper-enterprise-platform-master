package com.proper.enterprise.platform.integration.workflow.activiti

import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.runtime.ProcessInstanceQuery
import org.activiti.engine.task.Task
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ProcessIntegTest extends AbstractIntegTest {

    private static final String PROC_DEF_KEY = 'approve-process'
    private static final String TENANT_ID = 'pep'

    @Autowired RepositoryService repositoryService
    @Autowired RuntimeService runtimeService
    @Autowired TaskService taskService

    @Test
    public void executeAWorkflow() {
        deployProcess()
        assert countProcessInstance() == 0

        def procInst = startProcess()
        assert countProcessInstance() == 1

        Task task
        while (!isProcInstEnded(procInst.processInstanceId)) {
            task = getCurrentTask(procInst.processInstanceId)
            println task.name
            taskService.setVariableLocal(task.id, 'localApproveResult', '同意')
            taskService.setVariable(task.id, 'approveResult', '同意')
            taskService.complete(task.id)
        }
        println 'is ended'
    }

    private void deployProcess() {
        repositoryService.createDeployment()
                         .name('approve-deploy')
                         .category('integration-test')
                         .tenantId(TENANT_ID)
                         .addClasspathResource('approve-process.bpmn20.xml')
                         .deploy()
    }

    private long countProcessInstance() {
        getProcInstQuery().count()
    }

    private ProcessInstanceQuery getProcInstQuery() {
        runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(PROC_DEF_KEY)
                .processDefinitionName('approve-process-definition')
    }

    private ProcessInstance startProcess() {
        runtimeService.createProcessInstanceBuilder()
                      .tenantId(TENANT_ID)
                      .processDefinitionKey(PROC_DEF_KEY)
                      .addVariable('approveResult', '')
                      .start()
    }

    private Task getCurrentTask(String procInstId) {
        taskService.createTaskQuery()
                   .processInstanceId(procInstId)
                   .singleResult()
    }

    private boolean isProcInstEnded(String procInstId) {
        runtimeService.createProcessInstanceQuery()
                      .processInstanceId(procInstId)
                      .singleResult() == null
    }

}
