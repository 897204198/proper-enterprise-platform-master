package com.proper.enterprise.platform.workflow.frame

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.vo.PEPExtFormVO
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import com.proper.enterprise.platform.workflow.vo.PEPPropertyVO
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPageVO
import org.flowable.app.model.common.ResultListDataRepresentation
import org.flowable.engine.IdentityService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class ManyFormTest extends AbstractTest {

    private static final String MANY_FORM_WORKFLOW_KEY = 'manyForm'

    @Autowired
    IdentityService identityService

    @Test
    @Sql("/com/proper/enterprise/platform/workflow/datadics.sql")
    public void testProcessPage() {
        Authentication.setCurrentUserId("admin")
        identityService.setAuthenticatedUserId("admin")
        Map form1 = new HashMap()
        form1.put("a", "a")
        String procInstId = start(MANY_FORM_WORKFLOW_KEY, form1)
        List<PEPExtFormVO> pages = buildPage(procInstId)
        assert pages.size() == 1
        assert pages.get(0).getFormData().get("a") == "a"

        Map form1Step1 = getTask("form1step1")
        form1.put("a", "a2")
        completeStep(form1Step1, form1)
        List<PEPExtFormVO> pages2 = buildPage(procInstId)
        assert pages2.size() == 1
        assert pages2.get(0).formData.a == "a2"

        Map form1Step2 = getTask("form1step2")
        form1.put("a", "a3")
        completeStep(form1Step2, form1)
        List<PEPExtFormVO> pages3 = buildPage(procInstId)
        assert pages3.size() == 1
        assert pages3.get(0).formData.a == "a3"

        Map form2Step1 = getTask("form2step1")
        Map form2 = new HashMap()
        form2.put("b", "b")
        completeStep(form2Step1, form2)
        List<PEPExtFormVO> pages4 = buildPage(procInstId)
        assert pages4.size() == 2
        assert pages4.get(0).formData.a == "a3"
        assert pages4.get(1).formData.b == "b"

        Map form2Step2 = getTask("form2step2")
        form2.put("b", "b2")
        completeStep(form2Step2, form2)
        List<PEPExtFormVO> pages5 = buildPage(procInstId)
        assert pages5.size() == 2
        assert pages5.get(0).formData.a == "a3"
        assert pages5.get(1).formData.b == "b2"
    }

    @Test
    @Sql("/com/proper/enterprise/platform/workflow/datadics.sql")
    public void testTaskPage() {
        Authentication.setCurrentUserId("admin")
        identityService.setAuthenticatedUserId("admin")
        Map form1 = new HashMap()
        form1.put("a", "a")
        ResultListDataRepresentation representation = JSONUtil.parse(get('/repository/models/?filter=' + MANY_FORM_WORKFLOW_KEY + '&modelType=0'
            , HttpStatus.OK).getResponse().getContentAsString(), ResultListDataRepresentation.class)
        assert representation.data.get(0).formProperties.size() == 2
        Map readAndWriteStart = representation.data.get(0).formProperties.readAndWrite
        assert readAndWriteStart.label == 'name'
        assert readAndWriteStart.writable
        assert !readAndWriteStart.required
        Map write = representation.data.get(0).formProperties.write
        assert write.label == 'name3'
        assert write.writable
        assert !write.required
        PEPProcInstVO pepProcInstVO = startReturnVO(MANY_FORM_WORKFLOW_KEY, form1)

        Map form1Step1 = getTask("form1step1")
        assert "admin发起的manyForm流程" == form1Step1.pepProcInst.processTitle
        List<PEPExtFormVO> pages2 = buildPageTask(form1Step1.taskId)
        assert pages2.size() == 1
        assert pages2.get(0).getFormData().get("a") == "a"
        assert pages2.get(0).getShowType().name() == "EDIT"
        form1.put("a", "a2")
        assert pages2.get(0).getFormProperties().size() == 2
        PEPPropertyVO readAndWrite = pages2.get(0).getFormProperties().get("readAndWrite")
        assert readAndWrite.label == 'name'
        assert readAndWrite.writable
        assert !readAndWrite.require
        PEPPropertyVO read = pages2.get(0).getFormProperties().get("read")
        assert !read.writable
        completeStep(form1Step1, form1)


        Map form1Step2 = getTask("form1step2")
        List<PEPExtFormVO> page3 = buildPageTask(form1Step2.taskId)
        assert page3.size() == 1
        assert page3.get(0).getFormData().get("a") == "a2"
        assert page3.get(0).getShowType().name() == "EDIT"
        assert page3.get(0).getFormProperties() == null
        form1.put("a", "a3")
        completeStep(form1Step2, form1)

        Map form2Step1 = getTask("form2step1")
        List<PEPExtFormVO> page4 = buildPageTask(form2Step1.taskId)
        assert page4.size() == 2
        assert page4.get(0).getFormData().get("a") == "a3"
        assert page4.get(0).getShowType().name() == "SHOW"
        assert page4.get(1).getShowType().name() == "EDIT"
        assert page4.get(1).getFormKey() == "form2"
        PEPPropertyVO readAndWrite2 = page4.get(1).getFormProperties().get("readAndWrite")
        assert page4.get(1).getFormProperties().size() == 1
        assert readAndWrite2.label == 'name1'
        assert readAndWrite2.writable
        assert readAndWrite2.require
        Map form2 = new HashMap()
        form2.put("b", "b")
        form2.put("name1", "name1")
        completeStep(form2Step1, form2)


        Map form2Step2 = getTask("form2step2")
        List<PEPExtFormVO> pages5 = buildPageTask(form2Step2.taskId)
        assert pages5.size() == 2
        assert pages5.get(0).getFormData().get("a") == "a3"
        assert pages5.get(0).getShowType().name() == "SHOW"
        assert pages5.get(1).getShowType().name() == "EDIT"
        assert pages5.get(1).getFormKey() == "form2"
        assert pages5.get(1).getFormData().get("b") == "b"
        assert pages5.get(1).getFormData().get("name1") == "name1"
        form2.put("b", "b2")
        completeStep(form2Step2, form2)
        assert "admin发起的manyForm流程" == findProcessStartByKey(MANY_FORM_WORKFLOW_KEY).getProcessTitle()
    }


    private String start(String key, Map<String, Object> form) {
        PEPProcInstVO pepProcInstVO = JSONUtil.parse(post('/workflow/process/' + key, JSONUtil.toJSON(form), HttpStatus.CREATED).getResponse().getContentAsString(), PEPProcInstVO.class)
        return pepProcInstVO.getProcInstId()
    }

    private PEPProcInstVO startReturnVO(String key, Map<String, Object> form) {
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

    private List<PEPExtFormVO> buildPage(String procInstId) {
        PEPWorkflowPageVO pages = JSONUtil.parse(get('/workflow/process/' + procInstId + '/page', HttpStatus.OK).getResponse().getContentAsString(), PEPWorkflowPageVO.class)
        return pages.getForms()
    }

    private List<PEPExtFormVO> buildPageTask(String taskId) {
        PEPWorkflowPageVO pages = JSONUtil.parse(get('/workflow/task/' + taskId + '/page', HttpStatus.OK).getResponse().getContentAsString(), PEPWorkflowPageVO.class)
        return pages.getForms()
    }

    private void completeStep(Map step, Map<String, Object> taskFormMap) {
        post('/workflow/task/' + step.taskId, JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
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

    private List<PEPProcInstVO> findProcessStartByMe() {
        DataTrunk<PEPProcInstVO> dataTrunk = JSONUtil.parse(get('/workflow/process?pageNo=1&pageSize=10', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        return dataTrunk.getData()
    }
}
