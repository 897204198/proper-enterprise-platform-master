package com.proper.enterprise.platform.integration.workflow.activiti

import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ProcessIntegTest extends AbstractIntegTest {

    private static final String PROC_DEF_KEY = 'approve-process'
    private static final String PROC_DEF_V1 = 'approve-process-v1.bpmn20.xml'
    private static final String PROC_DEF_V2 = 'approve-process-v2.bpmn20.xml'

    @Autowired RepositoryService repositoryService
    @Autowired RuntimeService runtimeService
    @Autowired TaskService taskService

    @Test
    public void noNeedMoreDevWhileProcDefChanged() {
        deployProcess(PROC_DEF_V1)
        assert startProcAndGetApprovePath(['同意', '同意']) == ['一级审批', '二级审批']
        assert startProcAndGetApprovePath(['不同意']) == ['一级审批']

        deployProcess(PROC_DEF_V2)
        assert startProcAndGetApprovePath(['同意', '同意', '同意']) == ['一级审批', '二级审批', '三级审批']
        assert startProcAndGetApprovePath(['不同意']) == ['一级审批', '不同意咋整']
        assert startProcAndGetApprovePath(['同意', '不同意']) == ['一级审批', '二级审批', '不同意咋整']
    }

    private void deployProcess(String procDefKey) {
        repositoryService.createDeployment()
                         .addClasspathResource(procDefKey)
                         .deploy()
    }

    private List startProcAndGetApprovePath(List approveOpinions) {
        def procInst = startProcess()
        def path = [], i = 0
        Task task
        while (!isProcInstEnded(procInst.processInstanceId)) {
            task = getCurrentTask(procInst.processInstanceId)
            path << task.name
            taskService.setVariableLocal(task.id, 'localApproveResult', approveOpinions[i])
            taskService.setVariable(task.id, 'approveResult', approveOpinions[i++])
            taskService.complete(task.id)
        }
        path
    }

    private ProcessInstance startProcess() {
        runtimeService.createProcessInstanceBuilder()
                      .processDefinitionKey(PROC_DEF_KEY)
                      .addVariable('approveResult', '')
                      .start()
    }

    private boolean isProcInstEnded(String procInstId) {
        runtimeService.createProcessInstanceQuery()
                      .processInstanceId(procInstId)
                      .singleResult() == null
    }

    private Task getCurrentTask(String procInstId) {
        taskService.createTaskQuery()
                   .processInstanceId(procInstId)
                   .singleResult()
    }

}
