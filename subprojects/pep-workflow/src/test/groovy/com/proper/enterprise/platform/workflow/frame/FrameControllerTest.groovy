package com.proper.enterprise.platform.workflow.frame

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.sys.i18n.I18NUtil
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants
import com.proper.enterprise.platform.workflow.util.VariableUtil
import com.proper.enterprise.platform.workflow.vo.CustomHandlerVO
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import com.proper.enterprise.platform.workflow.vo.enums.PEPProcInstStateEnum
import org.flowable.engine.HistoryService
import org.flowable.engine.IdentityService
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
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
    @Autowired
    TaskService taskService
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
        Authentication.setCurrentUserId("user1")
        assert null == getTask("第一步")
        assert null == findProcessStartByKey(FRAME_WORKFLOW_KEY)
        Map<String, Object> formTestVO = new HashMap<>()
        formTestVO.put("sex", "1")
        formTestVO.put("name", "zjl")
        formTestVO.put("passOrNot", 1)
        PEPProcInstVO pepProcInstVO = start(FRAME_WORKFLOW_KEY, formTestVO)
        String procInstId = pepProcInstVO.getProcInstId()
        assert findHis(procInstId).hisTasks.size() == 0
        assert findHis(procInstId).currentTasks.get(0).endTime == null
        assert findHis(procInstId).currentTasks.get(0).assignee == "user1"
        start(VALIDATE_ASSIGN_GROUP_KEY, formTestVO)
        assert "处理中" == findProcessStartByKey(FRAME_WORKFLOW_KEY).getStateValue()
        assert "user1" == findProcessStartByKey(FRAME_WORKFLOW_KEY).getStartUserId()
        assert "框架测试流程" == findProcessStartByKey(FRAME_WORKFLOW_KEY).getProcessDefinitionName()
        Map step1 = getTask("第一步")
        String processTitle = I18NUtil.getMessage("workflow.default.process.title")
        processTitle = processTitle.replace("\${initiatorName}", "c")
        processTitle = processTitle.replace("\${processDefinitionName}", "框架测试流程")
        assert processTitle == step1.pepProcInst.processTitle
        assertGlobalVariables(step1.taskId)
        Map<String, String> msgParam = VariableUtil.convertVariableToMsgParam(taskService.createTaskQuery()
            .taskId(step1.taskId)
            .includeProcessVariables()
            .singleResult()
            .getProcessVariables())
        assert msgParam.get(WorkFlowConstants.INITIATOR_NAME) == "c"
        //判断task内容
        assertTaskMsg(step1, "第一步")
        completeStep1(step1)
        //判断历史
        assert null != findHis(procInstId, "第一步").endTime
        assert "c" == findHis(procInstId, "第一步").assigneeName
        assert "test" == findHis(procInstId, "第一步").form.formData.test

        Map step2 = getTask("第二步")
        assertTaskMsg(step2, "第二步")
        completeStep2(step2)
        mockUser('user2', 'testuser2', '123456')
        Authentication.setCurrentUserId("user2")
        Map approve = getTask("审核")
        assertTaskMsg(approve, "审核")
        completeApprove(approve, false)
        assert "这不能通过将第二步好好填填" == findHis(procInstId, "审核").form.formData.approveDesc

        mockUser('user1', 'testuser1', '123456')
        Authentication.setCurrentUserId("user1")
        Map step2Again = getTask("第二步")
        completeStep2Again(step2Again)
        Map approveAgain = getTask("审核")
        assert findHisCurr(procInstId, "审核").candidateGroupNames.contains("testgroup1")
        completeApprove(approveAgain, false)
        assert "这不能通过将第二步好好填填" == findHis(procInstId, "审核").form.formData.approveDesc

        Map step2Again2 = getTask("第二步")
        completeStep2Again2(step2Again2)
        mockUser('user3', 'testuser3', '123456')
        Authentication.setCurrentUserId("user3")
        Map approveAgain2 = getTask("审核")
        completeApprove(approveAgain2, true)
        assert "这次OK" == findHis(procInstId, "审核").form.formData.approveDesc

        mockUser('user1', 'testuser1', '123456')
        Authentication.setCurrentUserId("user1")
        Map step3 = getTask("第三步")
        completeStep3(step3)
        Map step4 = getTask("第四步")
        complete(step4.taskId)
        assert "c" == findHis(procInstId, "第四步").assigneeName
        assert true == findHis(procInstId).ended
        assert "已完成" == findProcessStartByKey(FRAME_WORKFLOW_KEY).getStateValue()
        assert processTitle == findProcessStartByKey(FRAME_WORKFLOW_KEY).getProcessTitle()
        assert 2 == findProcessStartByMe().size()
        assert "处理中" == findProcessStartByKey(VALIDATE_ASSIGN_GROUP_KEY).getStateValue()
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql"])
    public void findStartByMeTest() {
        Authentication.setCurrentUserId("pep-sysadmin")
        identityService.setAuthenticatedUserId("pep-sysadmin")
        assert 0 == findProcessStartByMe().size()
        start(FRAME_WORKFLOW_KEY, new HashMap<String, Object>())
        start("autoFinish", new HashMap<String, Object>())
        assert 2 == findProcessStartByMe().size()
        assert "框架测试流程" == findProcessStartByMeParam("框架测试流程", PEPProcInstStateEnum.DOING, 1, 1).get(0).processDefinitionName
        assert 1 == findProcessStartByMeParam("autoFinish", PEPProcInstStateEnum.DOING, 1, 1).size()
        assert "autoFinish" == findProcessStartByMeParam("", null, 1, 1).get(0).processDefinitionName
        assert "autoFinish" == findProcessStartByMeParam("autoFinish", PEPProcInstStateEnum.DONE, 1, 1).get(0).processDefinitionName

        assert "框架测试流程" == findProcessStartByMeParam("", null, 2, 1).get(0).processDefinitionName
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql"])
    public void findTodoTest() {
        identityService.setAuthenticatedUserId('user1')
        Authentication.setCurrentUserId("user1")
        start(FRAME_WORKFLOW_KEY, new HashMap<String, Object>())
        start(FRAME_WORKFLOW_KEY, new HashMap<String, Object>())
        start(FRAME_WORKFLOW_KEY, new HashMap<String, Object>())
        start(FRAME_WORKFLOW_KEY, new HashMap<String, Object>())
        start(FRAME_WORKFLOW_KEY, new HashMap<String, Object>())
        start(FRAME_WORKFLOW_KEY, new HashMap<String, Object>())
        assert 6 == JSONUtil.parse(get('/workflow/task?pageNo=1&pageSize=10', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class).getCount()
        assert 0 == JSONUtil.parse(get('/workflow/task?processDefinitionName=123&pageNo=1&pageSize=10', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class).getCount()
        assert 6 == JSONUtil.parse(get('/workflow/task?pageNo=1&pageSize=2', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class).getCount()
        assert 2 == JSONUtil.parse(get('/workflow/task?pageNo=1&pageSize=2', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class).getData().size()
        assert 2 == JSONUtil.parse(get('/workflow/task?processDefinitionName=框架测试流程&pageNo=1&pageSize=2', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class).getData().size()
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql"])
    public void testStartUserName() {
        identityService.setAuthenticatedUserId("user1")
        Authentication.setCurrentUserId("user1")
        start(FRAME_WORKFLOW_KEY, new HashMap<String, Object>())
        Task task = taskService.createTaskQuery().includeProcessVariables().singleResult()
        assert "c" == task.getProcessVariables().get(WorkFlowConstants.INITIATOR_NAME)
    }

    private void assertTaskMsg(Map pepTaskVO, String name) {
        assert name == pepTaskVO.name
        assert "框架测试流程" == pepTaskVO.pepProcInst.processDefinitionName
        assert "user1" == pepTaskVO.pepProcInst.startUserId
        assert "c" == pepTaskVO.pepProcInst.startUserName
        assert "处理中" == pepTaskVO.pepProcInst.stateValue
    }

    private PEPProcInstVO start(String key, Map<String, Object> form) {
        PEPProcInstVO pepProcInstVO = JSONUtil.parse(post('/workflow/process/' + key, JSONUtil.toJSON(form), HttpStatus.CREATED).getResponse().getContentAsString(), PEPProcInstVO.class)
        return pepProcInstVO
    }

    private Map getTask(String taskName) {
        DataTrunk dataTrunk = JSONUtil.parse(get('/workflow/task?pageNo=1&pageSize=10', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        for (Map pepTaskVO : dataTrunk.getData()) {
            if (taskName.equals(pepTaskVO.name)) {
                return pepTaskVO
            }
        }
        return null
    }

    private void completeStep1(Map step1) {
        Map<String, Object> taskFormMap = new HashMap<>()
        taskFormMap.put("test", "test")
        post('/workflow/task/' + step1.taskId, JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }

    private void completeStep2(Map step2) {
        Map<String, Object> taskFormMap = new HashMap<>()
        taskFormMap.put("step2", "step2")
        CustomHandlerVO customHandlerVO = new CustomHandlerVO()
        customHandlerVO.setAssignee("user2")
        taskFormMap.put("customHandler", customHandlerVO)
        post('/workflow/task/' + step2.taskId, JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }

    private void completeStep3(Map step3) {
        Map<String, Object> taskFormMap = new HashMap<>()
        taskFormMap.put("step3", "step3")
        post('/workflow/task/' + step3.taskId, JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }

    private void completeStep2Again(Map step2) {
        Map<String, Object> taskFormMap = new HashMap<>()
        taskFormMap.put("step2", "好好填填")
        CustomHandlerVO customHandlerVO = new CustomHandlerVO()
        List<String> groupIds = new ArrayList<>()
        groupIds.add("group1")
        customHandlerVO.setCandidateGroups(groupIds)
        taskFormMap.put("customHandler", customHandlerVO)
        post('/workflow/task/' + step2.taskId, JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }

    private void completeStep2Again2(Map step2) {
        Map<String, Object> taskFormMap = new HashMap<>()
        taskFormMap.put("step2", "好好填填")
        CustomHandlerVO customHandlerVO = new CustomHandlerVO()
        List<String> roleIds = new ArrayList<>()
        roleIds.add("role2")
        customHandlerVO.setCandidateRoles(roleIds)
        taskFormMap.put("customHandler", customHandlerVO)
        post('/workflow/task/' + step2.taskId, JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }

    private void completeApprove(Map approve, boolean pass) {
        Map<String, Object> taskFormMap = new HashMap<>()
        taskFormMap.put("approveResult", pass ? "Y" : "N")
        taskFormMap.put("approveDesc", pass ? "这次OK" : "这不能通过将第二步好好填填")

        post('/workflow/task/' + approve.taskId, JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }

    private void complete(String taskId) {
        post('/workflow/task/' + taskId, JSONUtil.toJSON(new HashMap()), HttpStatus.CREATED)
    }

    private List<PEPProcInstVO> findProcessStartByMe() {
        DataTrunk<PEPProcInstVO> dataTrunk = JSONUtil.parse(get('/workflow/process?pageNo=1&pageSize=10', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        return dataTrunk.getData()
    }

    private List<PEPProcInstVO> findProcessStartByMeParam(String processDefinitionName, PEPProcInstStateEnum state, Integer pageNo, Integer pageSize) {
        String stateQuery = null == state ? '' : '&state=' + state
        DataTrunk<PEPProcInstVO> dataTrunk = JSONUtil.parse(get('/workflow/process?processDefinitionName=' + processDefinitionName + stateQuery + '&pageNo=' + pageNo + '&pageSize=' + pageSize, HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
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

    private Map findHis(String procInstId) {
        Map pepWorkflowPathVO = JSONUtil.parse(get('/workflow/process/' + procInstId + '/path', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        return pepWorkflowPathVO
    }

    private Map findHis(String procInstId, String name) {
        Map pepWorkflowPathVO = JSONUtil.parse(get('/workflow/process/' + procInstId + '/path', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        for (Map pepTaskVO : pepWorkflowPathVO.hisTasks) {
            if (name.equals(pepTaskVO.name)) {
                return pepTaskVO
            }
        }
        return null
    }

    private Map findHisCurr(String procInstId, String name) {
        Map pepWorkflowPathVO = JSONUtil.parse(get('/workflow/process/' + procInstId + '/path', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        for (Map pepTaskVO : pepWorkflowPathVO.currentTasks) {
            if (name.equals(pepTaskVO.name)) {
                return pepTaskVO
            }
        }
        return null
    }

    private void assertGlobalVariables(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).includeProcessVariables().singleResult()
        assert 1 == task.getProcessVariables().get("passOrNot")
        assert "workflowtest" == task.getProcessVariables().get("workflowtest")
    }
}
