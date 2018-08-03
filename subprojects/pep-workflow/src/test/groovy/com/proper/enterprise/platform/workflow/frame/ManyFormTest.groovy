package com.proper.enterprise.platform.workflow.frame

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
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
        List<Map> pages = buildPage(procInstId)
        assert pages.size() == 1
        assert pages.get(0).get("formData").a == "a"

        Map form1Step1 = getTask("form1step1")
        form1.put("a", "a2")
        completeStep(form1Step1, form1)
        List<Map> pages2 = buildPage(procInstId)
        assert pages2.size() == 1
        assert pages2.get(0).get("formData").a == "a2"

        Map form1Step2 = getTask("form1step2")
        form1.put("a", "a3")
        completeStep(form1Step2, form1)
        List<Map> pages3 = buildPage(procInstId)
        assert pages3.size() == 1
        assert pages3.get(0).get("formData").a == "a3"

        Map form2Step1 = getTask("form2step1")
        Map form2 = new HashMap()
        form2.put("b", "b")
        completeStep(form2Step1, form2)
        List<Map> pages4 = buildPage(procInstId)
        assert pages4.size() == 2
        assert pages4.get(0).get("formData").a == "a3"
        assert pages4.get(1).get("formData").b == "b"

        Map form2Step2 = getTask("form2step2")
        form2.put("b", "b2")
        completeStep(form2Step2, form2)
        List<Map> pages5 = buildPage(procInstId)
        assert pages5.size() == 2
        assert pages5.get(0).get("formData").a == "a3"
        assert pages5.get(1).get("formData").b == "b2"
    }

    @Test
    @Sql("/com/proper/enterprise/platform/workflow/datadics.sql")
    public void testTaskPage() {
        Authentication.setCurrentUserId("admin")
        identityService.setAuthenticatedUserId("admin")
        Map form1 = new HashMap()
        form1.put("a", "a")
        String procInstId = start(MANY_FORM_WORKFLOW_KEY, form1)

        Map form1Step1 = getTask("form1step1")
        List<Map> pages2 = buildPageTask(form1Step1.taskId)
        assert pages2.size() == 1
        assert pages2.get(0).get("formData").a == "a"
        assert pages2.get(0).get("showType") == "EDIT"
        form1.put("a", "a2")
        assert pages2.get(0).get("formProperties").size() == 2
        assert pages2.get(0).get("formProperties").get(0).name == 'name'
        assert pages2.get(0).get("formProperties").get(0).readable
        assert pages2.get(0).get("formProperties").get(0).writable
        assert !pages2.get(0).get("formProperties").get(0).required
        assert !pages2.get(0).get("formProperties").get(1).writable
        assert pages2.get(0).get("formProperties").get(1).value == PEPConstants.DEFAULT_OPERAOTR_ID
        completeStep(form1Step1, form1)


        Map form1Step2 = getTask("form1step2")
        List<Map> page3 = buildPageTask(form1Step2.taskId)
        assert page3.size() == 1
        assert page3.get(0).get("formData").a == "a2"
        assert page3.get(0).get("showType") == "EDIT"
        assert page3.get(0).get("formProperties").size() == 0
        form1.put("a", "a3")
        completeStep(form1Step2, form1)

        Map form2Step1 = getTask("form2step1")
        List<Map> page4 = buildPageTask(form2Step1.taskId)
        assert page4.size() == 2
        assert page4.get(0).get("formData").a == "a3"
        assert page4.get(0).get("showType") == "SHOW"
        assert page4.get(1).get("showType") == "EDIT"
        assert page4.get(1).get("formKey") == "form2"
        assert page4.get(1).get("formProperties").size() == 1
        assert page4.get(1).get("formProperties").get(0).name == 'name1'
        assert page4.get(1).get("formProperties").get(0).readable
        assert page4.get(1).get("formProperties").get(0).writable
        assert page4.get(1).get("formProperties").get(0).required
        Map form2 = new HashMap()
        form2.put("b", "b")
        form2.put("name1", "name1")
        completeStep(form2Step1, form2)


        Map form2Step2 = getTask("form2step2")
        List<Map> pages5 = buildPageTask(form2Step2.taskId)
        assert pages5.size() == 2
        assert pages5.get(0).get("formData").a == "a3"
        assert pages5.get(0).get("showType") == "SHOW"
        assert pages5.get(1).get("showType") == "EDIT"
        assert pages5.get(1).get("formKey") == "form2"
        assert pages5.get(1).get("formData").b == "b"
        assert pages5.get(1).get("formData").name1 == "name1"
        form2.put("b", "b2")
        completeStep(form2Step2, form2)
    }


    private String start(String key, Map<String, Object> form) {
        PEPProcInstVO pepProcInstVO = JSONUtil.parse(post('/workflow/process/' + key, JSONUtil.toJSON(form), HttpStatus.CREATED).getResponse().getContentAsString(), PEPProcInstVO.class)
        return pepProcInstVO.getProcInstId()
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

    private List<Map> buildPage(String procInstId) {
        List<Map> pages = JSONUtil.parse(get('/workflow/process/' + procInstId + '/page', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        return pages
    }

    private List<Map> buildPageTask(String taskId) {
        List<Map> pages = JSONUtil.parse(get('/workflow/task/' + taskId + '/page', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        return pages
    }

    private void completeStep(Map step, Map<String, Object> taskFormMap) {
        post('/workflow/task/' + step.taskId, JSONUtil.toJSON(taskFormMap), HttpStatus.CREATED)
    }
}
