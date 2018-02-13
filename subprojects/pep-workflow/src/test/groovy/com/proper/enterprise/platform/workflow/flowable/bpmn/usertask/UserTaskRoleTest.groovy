package com.proper.enterprise.platform.workflow.flowable.bpmn.usertask

import com.proper.enterprise.platform.test.AbstractTest
import org.flowable.engine.HistoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.test.Deployment
import org.flowable.engine.TaskService
import org.flowable.identitylink.api.IdentityLinkInfo
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class UserTaskRoleTest extends AbstractTest {
    @Autowired
    private RuntimeService runtimeService
    @Autowired
    private TaskService taskService
    @Autowired
    private HistoryService historyService

    @Test
    @Deployment(resources = 'bpmn/usertask/testUsertaskRole.bpmn20.xml')
    @Sql("/com/proper/enterprise/platform/workflow/identity.sql")
    void testUserTaskRole() {
        def processInstance = runtimeService.startProcessInstanceByKey("testUsertaskRole")
        //获取当前代理人  应为空  只有角色候选人没有代理人
        def taskAssignee1 = taskService.createTaskQuery().taskAssignee("user1").singleResult()
        assert null == taskAssignee1
        //获取当前候选人user1  应无用户组任务
        def taskGroup1 = taskService.createTaskQuery().taskCandidateGroup("group1").singleResult()
        assert null == taskGroup1
        //获取当前候选人user1有role1角色应有任务
        def taskUser1 = taskService.createTaskQuery().taskCandidateUser("user1").singleResult()
        assert null != taskUser1
        //获取当前候选人user2无role1角色 应查到task实例
        def taskUser2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult()
        assert null == taskUser2
        //获取当前角色候选 应查到task实例
        def taskRole1 = taskService.createTaskQuery().taskCandidateRole("role1").singleResult()
        assert null != taskRole1
        //获取当前角色候选 应查到task实例
        def taskRole1In = taskService.createTaskQuery().taskCandidateRoleIn(["role1"]).singleResult()
        assert null != taskRole1In
        //获取当前角色候选 应查到task实例
        def taskCanOrAss1 = taskService.createTaskQuery().ignoreAssigneeValue()
            .taskCandidateOrAssigned("user1").singleResult()
        assert null != taskCanOrAss1
        taskService.addCandidateRole(taskCanOrAss1.getId(), 'role2')
        taskService.deleteCandidateRole(taskCanOrAss1.getId(), 'role1')
        //动态删除候选角色 并验证role1候选任务为空
        def delTaskRole1 = taskService.createTaskQuery().taskCandidateRole("role1").singleResult()
        assert null == delTaskRole1
        //动态添加候选角色 并验证role2候选任务存在
        def addTaskRole2 = taskService.createTaskQuery().taskCandidateRole("role2").singleResult()
        assert null != addTaskRole2
        //动态添加候选角色 并验证user2,user3属于role2候选任务存在
        def addTaskUser2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult()
        assert null != addTaskUser2
        def addTaskUser3 = taskService.createTaskQuery().taskCandidateUser("user3").singleResult()
        assert null != addTaskUser3
        //user2签收任务  user3中任务为空
        taskService.claim(addTaskUser2.getId(), "user2")
        def claimTaskUser2 = taskService.createTaskQuery()
            .taskCandidateOrAssigned("user2").singleResult()
        assert null != claimTaskUser2
        def claimTaskUser3 = taskService.createTaskQuery().taskCandidateUser("user3").singleResult()
        assert null == claimTaskUser3
        taskService.complete(claimTaskUser2.getId())
        //验证历史
        def historicIdentityLinks = historyService.getHistoricIdentityLinksForTask(claimTaskUser2.getId())
        assert null != historicIdentityLinks && historicIdentityLinks.size() > 0
        assert haveRoleLink("role2", historicIdentityLinks)
        def hisIns = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(processInstance.processInstanceId)
            .ignoreAssigneeValue()
            .includeIdentityLinks().finished().singleResult()
        assert null != hisIns
        assert haveRoleLink('role2', hisIns.getIdentityLinks())
        //验证双角色候选
        def taskUser1Step2 = taskService.createTaskQuery().taskCandidateUser("user1").singleResult()
        assert null != taskUser1Step2
        def taskUser2Step2 = taskService.createTaskQuery().taskCandidateUser("user2").singleResult()
        assert null != taskUser2Step2
        taskService.complete(taskUser1Step2.getId())
        assert null == runtimeService.createProcessInstanceQuery()
            .processInstanceId(processInstance.getId()).singleResult()
    }

    private boolean haveRoleLink(String roleId, List<IdentityLinkInfo> links) {
        for (IdentityLinkInfo link : links) {
            if (roleId.equals(link.getRoleId())) {
                return true
            }
        }
        return false
    }
}
