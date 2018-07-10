package com.proper.enterprise.platform.workflow.frame

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.vo.CustomHandlerVO
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO
import org.flowable.engine.HistoryService
import org.flowable.engine.IdentityService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class FrameControllerTest extends AbstractTest {

    private static final String FRAME_WORKFLOW_KEY = 'flowableFrame'
    private static final String VALIDATE_ASSIGN_GROUP_KEY = 'validateAssignGroup'

    @Autowired
    HistoryService historyService
    @Autowired
    IdentityService identityService
    /**
     * 公共信息
     * state 状态
     *
     * 表单信息
     * name 姓名
     * sex 性别
     */
    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql"])
    public void flowableTest() {
        mockUser('user1', 'testuser1', '123456')
        identityService.setAuthenticatedUserId('user1')
        assert null == findProcessStartByKey(FRAME_WORKFLOW_KEY)
        Map<String, Object> formTestVO = new HashMap<>()
        formTestVO.put("sex", "1")
        formTestVO.put("name", "zjl")
        String procInstId = start(FRAME_WORKFLOW_KEY, formTestVO)
        assert findHis(procInstId).size() == 0
        start(VALIDATE_ASSIGN_GROUP_KEY, formTestVO)
        assert "处理中" == findProcessStartByKey(FRAME_WORKFLOW_KEY).getStateValue()
        assert "user1" == findProcessStartByKey(FRAME_WORKFLOW_KEY).getStartUserId()
        assert "框架测试流程" == findProcessStartByKey(FRAME_WORKFLOW_KEY).getProcessDefinitionName()
        PEPTaskVO step1 = getTask("第一步")
        //判断task内容
        assertTaskMsg(step1, "第一步")
        completeStep1(step1)
        //判断历史
        assert null != findHis(procInstId).get(0).endTime
        assert "c" == findHis(procInstId).get(0).assigneeName
        assert "test" == findHis(procInstId).get(0).variables.get("test")

        PEPTaskVO step2 = getTask("第二步")
        assertTaskMsg(step2, "第二步")
        completeStep2(step2)
        mockUser('user2', 'testuser2', '123456')
        PEPTaskVO approve = getTask("审核")
        assertTaskMsg(approve, "审核")
        completeApprove(approve, false)
        assert "这不能通过将第二步好好填填" == findHis(procInstId).last().variables.get("approveDesc")

        mockUser('user1', 'testuser1', '123456')
        PEPTaskVO step2Again = getTask("第二步")
        completeStep2Again(step2Again)
        PEPTaskVO approveAgain = getTask("审核")
        completeApprove(approveAgain, false)
        assert "这不能通过将第二步好好填填" == findHis(procInstId).last().variables.get("approveDesc")

        PEPTaskVO step2Again2 = getTask("第二步")
        completeStep2Again2(step2Again2)
        mockUser('user3', 'testuser3', '123456')
        PEPTaskVO approveAgain2 = getTask("审核")
        completeApprove(approveAgain2, true)
        assert "这次OK" == findHis(procInstId).last().variables.get("approveDesc")

        mockUser('user1', 'testuser1', '123456')
        PEPTaskVO step3 = getTask("第三步")
        completeStep3(step3)
        PEPTaskVO step4 = getTask("第四步")
        complete(step4.getTaskId())
        assert "c" == findHis(procInstId).last().assigneeName
        assert "已完成" == findProcessStartByKey(FRAME_WORKFLOW_KEY).getStateValue()
        assert 2 == findProcessStartByMe().size()
        assert "处理中" == findProcessStartByKey(VALIDATE_ASSIGN_GROUP_KEY).getStateValue()
    }

    private void assertTaskMsg(PEPTaskVO pepTaskVO, String name) {
        assert name == pepTaskVO.getName()
        assert "框架测试流程" == pepTaskVO.getPepProcInstVO().getProcessDefinitionName()
        assert "user1" == pepTaskVO.getPepProcInstVO().getStartUserId()
        assert "c" == pepTaskVO.getPepProcInstVO().getStartUserName()
        assert "处理中" == pepTaskVO.getPepProcInstVO().getStateValue()
    }

    private String start(String key, Map<String, Object> form) {
        PEPProcInstVO pepProcInstVO = JSONUtil.parse(post('/workflow/process/' + key, JSONUtil.toJSON(form), HttpStatus.CREATED).getResponse().getContentAsString(), PEPProcInstVO.class)
        return pepProcInstVO.getProcInstId()
    }

    private PEPTaskVO getTask(String taskName) {
        DataTrunk<PEPTaskVO> dataTrunk = JSONUtil.parse(get('/workflow/task?pageNo=1&pageSize=10', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        for (PEPTaskVO pepTaskVO : dataTrunk.getData()) {
            if (taskName.equals(pepTaskVO.getName())) {
                return pepTaskVO
            }
        }
        return null
    }

    private void completeStep1(PEPTaskVO step1) {
        Map<String, Object> taskFormMap = new HashMap<>()
        taskFormMap.put("test", "test")
        post('/workflow/task/' + step1.getTaskId(), JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }

    private void completeStep2(PEPTaskVO step2) {
        Map<String, Object> taskFormMap = new HashMap<>()
        taskFormMap.put("step2", "step2")
        CustomHandlerVO customHandlerVO = new CustomHandlerVO()
        customHandlerVO.setAssignee("user2")
        taskFormMap.put("customHandler", customHandlerVO)
        post('/workflow/task/' + step2.getTaskId(), JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }

    private void completeStep3(PEPTaskVO step3) {
        Map<String, Object> taskFormMap = new HashMap<>()
        taskFormMap.put("step3", "step3")
        post('/workflow/task/' + step3.getTaskId(), JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }

    private void completeStep2Again(PEPTaskVO step2) {
        Map<String, Object> taskFormMap = new HashMap<>()
        taskFormMap.put("step2", "好好填填")
        CustomHandlerVO customHandlerVO = new CustomHandlerVO()
        List<String> groupIds = new ArrayList<>()
        groupIds.add("group1")
        customHandlerVO.setCandidateGroups(groupIds)
        taskFormMap.put("customHandler", customHandlerVO)
        post('/workflow/task/' + step2.getTaskId(), JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }

    private void completeStep2Again2(PEPTaskVO step2) {
        Map<String, Object> taskFormMap = new HashMap<>()
        taskFormMap.put("step2", "好好填填")
        CustomHandlerVO customHandlerVO = new CustomHandlerVO()
        List<String> roleIds = new ArrayList<>()
        roleIds.add("role2")
        customHandlerVO.setCandidateRoles(roleIds)
        taskFormMap.put("customHandler", customHandlerVO)
        post('/workflow/task/' + step2.getTaskId(), JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }

    private void completeApprove(PEPTaskVO approve, boolean pass) {
        Map<String, Object> taskFormMap = new HashMap<>()
        taskFormMap.put("approveResult", pass ? "Y" : "N")
        taskFormMap.put("approveDesc", pass ? "这次OK" : "这不能通过将第二步好好填填")

        post('/workflow/task/' + approve.getTaskId(), JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }

    private void complete(String taskId) {
        post('/workflow/task/' + taskId, JSONUtil.toJSON(new HashMap()), HttpStatus.CREATED)
    }

    private List<PEPProcInstVO> findProcessStartByMe() {
        DataTrunk<PEPProcInstVO> dataTrunk = JSONUtil.parse(get('/workflow/process', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        return dataTrunk.getData()
    }

    private PEPProcInstVO findProcessStartByKey(String processDefinitionKey) {
        List<PEPProcInstVO> pepProcInstVOs = findProcessStartByMe()
        for (PEPProcInstVO pepProcInstVO : pepProcInstVOs) {
            if (processDefinitionKey == pepProcInstVO.getProcessDefinitionKey()) {
                return pepProcInstVO
            }
        }
        return null
    }

    private List<PEPTaskVO> findHis(String procInstId) {
        List<PEPTaskVO> list = JSONUtil.parse(get('/workflow/task/' + procInstId, HttpStatus.OK).getResponse().getContentAsString(), List.class)
        return list
    }
}
