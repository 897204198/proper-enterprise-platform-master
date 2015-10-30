package com.proper.enterprise.platform.integration.workflow.activiti

import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ProcessIntegTest extends AbstractIntegTest {

    private static final String DEPLOY_NAME = 'process-integ-test'
    private static final String PROC_DEF_KEY = 'approve-process'
    private static final String PROC_DEF_V1 = 'approve-process-v1.bpmn20.xml'
    private static final String PROC_DEF_V2 = 'approve-process-v2.bpmn20.xml'

    @Autowired RepositoryService repositoryService
    @Autowired RuntimeService runtimeService
    @Autowired TaskService taskService

    @After
    public void tearDown() {
        repositoryService.createDeploymentQuery().list().each {
            if (it.name == DEPLOY_NAME) {
                repositoryService.deleteDeployment(it.id)
            }
        }
    }

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

    @Test
    public void deployNewVerProcDefWhileStillHavingProcInstRunning() {
        deployProcess(PROC_DEF_V1)
        def procInst1 = startProcess()

        deployProcess(PROC_DEF_V2)
        def procInst2 = startProcess()

        assert getApprovePath(procInst1.processInstanceId, ['同意', '不同意']) == ['一级审批', '二级审批']
        assert getApprovePath(procInst2.processInstanceId, ['同意', '不同意']) == ['一级审批', '二级审批', '不同意咋整']
    }

    private void deployProcess(String procDefKey) {
        repositoryService.createDeployment()
                         .name(DEPLOY_NAME)
                         .addClasspathResource(procDefKey)
                         .deploy()
    }

    private List startProcAndGetApprovePath(List approveOpinions) {
        def procInst = startProcess()
        getApprovePath(procInst.processInstanceId, approveOpinions)
    }

    private ProcessInstance startProcess() {
        runtimeService.createProcessInstanceBuilder()
                      .processDefinitionKey(PROC_DEF_KEY)
                      .addVariable('approveResult', '')
                      .addVariable('applicant', 'hinex')
                      .start()
    }

    private List getApprovePath(String procInstId, List approveOpinions) {
        def path = [], i = 0
        Task task
        while (!isProcInstEnded(procInstId)) {
            task = getCurrentTask(procInstId)
            path << task.name
            taskService.setVariableLocal(task.id, 'localApproveResult', approveOpinions[i])
            taskService.setVariable(task.id, 'approveResult', approveOpinions[i++])
            taskService.complete(task.id)
        }
        path
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
