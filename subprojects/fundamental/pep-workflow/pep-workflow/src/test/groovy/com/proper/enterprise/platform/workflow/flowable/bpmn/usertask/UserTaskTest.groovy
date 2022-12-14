package com.proper.enterprise.platform.workflow.flowable.bpmn.usertask

import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.workflow.flowable.service.AssigneeService
import com.proper.enterprise.platform.workflow.service.DeployService
import org.flowable.engine.HistoryService
import org.flowable.engine.IdentityService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.identitylink.api.IdentityLinkInfo
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class UserTaskTest extends AbstractJPATest {

    @Autowired
    RuntimeService runtimeService
    @Autowired
    TaskService taskService
    @Autowired
    AssigneeService assigneeService
    @Autowired
    DeployService deployService
    @Autowired
    IdentityService identityService
    @Autowired
    private HistoryService historyService

    @Test
    void validateInitiator() {
        identityService.setAuthenticatedUserId("zjl")
        def procInst = runtimeService.startProcessInstanceByKey("ValidateInitiator")
        def task = taskService.createTaskQuery().taskAssignee("zjl").singleResult()
        assert task.name == 'assign'
        taskService.complete(task.getId())
        task = taskService.createTaskQuery().taskAssignee("zjl").singleResult()
        assert task.name == 'exp'
        assert taskService.createTaskQuery().taskAssignee("test").singleResult() == null
        taskService.complete(task.id)
        assert isProcInstEnd(procInst.getId())
    }

    @Test
    void validateNamedUser() {
        def procInst = runtimeService.startProcessInstanceByKey("validateNamedUser")
        def task = taskService.createTaskQuery().taskAssignee("677036db-6ca6-47cf-915a-563ff8d58da4").singleResult()
        assert task.name == 'validateCommonUser'
        taskService.complete(task.getId())
        assert isProcInstEnd(procInst.getId())
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    void validateAssignGroup() {
        def procInst = runtimeService.startProcessInstanceByKey("validateAssignGroup")
        def task1 = taskService.createTaskQuery().taskCandidateGroup("group2_GROUP").singleResult()
        def task2 = taskService.createTaskQuery().taskCandidateOrAssigned("user2").singleResult()
        def task3 = taskService.createTaskQuery().taskCandidateOrAssigned("user3").singleResult()
        assert task1.id == task2.id
        assert task2.id == task3.id
        taskService.complete(task2.getId())
        assert isProcInstEnd(procInst.getId())
    }

    @Test
    void validateCandidatesUser() {
        def procInst = runtimeService.startProcessInstanceByKey("validateCandidatesUser")
        def task = taskService.createTaskQuery().taskCandidateOrAssigned("5d17010b-caf0-4613-bbb6-80b93380a7ca").singleResult()
        def task2 = taskService.createTaskQuery().taskCandidateOrAssigned("677036db-6ca6-47cf-915a-563ff8d58da4").singleResult()
        assert task.id == task2.id
        taskService.complete(task2.getId())
        assert isProcInstEnd(procInst.getId())
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    void testUserTaskRole() {
        def processInstance = runtimeService.startProcessInstanceByKey("testUsertaskRole")

        //?????????????????????  ?????????  ????????????????????????????????????
        def taskAssignee1 = taskService.createTaskQuery().taskAssignee("user1").singleResult()
        assert null == taskAssignee1
        //?????????????????????user1  ?????????????????????
        def taskGroup1 = taskService.createTaskQuery().taskCandidateGroup("group1_GROUP").singleResult()
        assert null == taskGroup1
        //?????????????????????user2???role1?????? ?????????task??????
        def taskUser2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult()
        assert null == taskUser2

        def task1Name = 'role1'

        //?????????????????????user1???role1??????????????????
        def taskUser1 = taskService.createTaskQuery().taskCandidateUser("user1").singleResult()
        assert taskUser1.name == task1Name
        //???????????????????????? ?????????task??????
        def taskRole1 = taskService.createTaskQuery().taskCandidateGroup("role1_ROLE").singleResult()
        assert taskRole1.name == task1Name
        //???????????????????????? ?????????task??????
        def taskRole1In = taskService.createTaskQuery().taskCandidateGroupIn(["role1_ROLE"]).singleResult()
        assert taskRole1In.name == task1Name
        //???????????????????????? ?????????task??????
        def taskCanOrAss1 = taskService.createTaskQuery().ignoreAssigneeValue()
            .taskCandidateOrAssigned("user1").singleResult()
        assert taskCanOrAss1.name == task1Name

        taskService.addCandidateGroup(taskCanOrAss1.getId(), 'role2_ROLE')
        taskService.deleteCandidateGroup(taskCanOrAss1.getId(), 'role1_ROLE')
        //???????????????????????? ?????????role1??????????????????
        def delTaskRole1 = taskService.createTaskQuery().taskCandidateGroup("role1_ROLE").singleResult()
        assert null == delTaskRole1
        //???????????????????????? ?????????role2??????????????????
        def addTaskRole2 = taskService.createTaskQuery().taskCandidateGroup("role2_ROLE").singleResult()
        assert addTaskRole2.name == task1Name
        //???????????????????????? ?????????user2,user3??????role2??????????????????
        def addTaskUser2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult()
        assert addTaskUser2.name == task1Name
        def addTaskUser3 = taskService.createTaskQuery().taskCandidateUser("user3").singleResult()
        assert addTaskUser3.name == task1Name
        //user2????????????  user3???????????????
        taskService.claim(addTaskUser2.getId(), "user2")

        def claimTaskUser2 = taskService.createTaskQuery()
            .taskCandidateOrAssigned("user2").singleResult()
        assert claimTaskUser2.name == task1Name
        def claimTaskUser3 = taskService.createTaskQuery().taskCandidateUser("user3").singleResult()
        assert null == claimTaskUser3
        taskService.complete(claimTaskUser2.getId())

        //????????????
        def historicIdentityLinks = historyService.getHistoricIdentityLinksForTask(claimTaskUser2.getId())
        assert null != historicIdentityLinks && historicIdentityLinks.size() > 0
        assert hasRoleLink("role2", historicIdentityLinks)
        assert !hasRoleLink('role1', historicIdentityLinks)

        def hisIns = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(processInstance.processInstanceId)
            .ignoreAssigneeValue()
            .includeIdentityLinks().finished().singleResult()
        assert null != hisIns
        assert hasRoleLink('role2', hisIns.getIdentityLinks())

        def task2Name = 'role1ORrole2'

        //?????????????????????
        def taskUser1Step2 = taskService.createTaskQuery().taskCandidateUser("user1").singleResult()
        assert taskUser1Step2.name == task2Name
        def taskUser2Step2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult()
        assert taskUser2Step2.name == task2Name
        taskService.complete(taskUser1Step2.getId())
        assert isProcInstEnd(processInstance.getId())
    }

    private static boolean hasRoleLink(String roleId, List<IdentityLinkInfo> links) {
        for (IdentityLinkInfo link : links) {
            if (roleId + "_ROLE" == link.getGroupId()) {
                return true
            }
        }
        return false
    }

    private boolean isProcInstEnd(String procInstId) {
        runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult() == null
    }

}
