package com.proper.enterprise.platform.workflow.servlet

import com.proper.enterprise.platform.auth.common.jpa.repository.UserRepository
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.test.AbstractIntegrationTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.repository.WFIdmQueryConfRepository
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.flowable.ui.modeler.domain.AbstractModel
import org.flowable.ui.modeler.domain.Model
import org.flowable.ui.modeler.repository.ModelRepository
import org.flowable.ui.modeler.repository.ModelSort
import org.junit.After
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

/**
 * 暂时无法解决集成测试跟单元测试一起跑的时候 hibernate数据混淆问题 ignore
 */
@Ignore
@Sql(["/com/proper/enterprise/platform/workflow/adminUsers.sql",
    "/com/proper/enterprise/platform/workflow/datadics.sql",
    "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
class WorkflowRestServletTest extends AbstractIntegrationTest {

    @Autowired
    ModelRepository modelRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    DataDicRepository dataDicRepository

    @Autowired
    WFIdmQueryConfRepository wfIdmQueryConfRepository


    @After
    public void after() {
        userRepository.deleteAll()
        dataDicRepository.deleteAll()
        wfIdmQueryConfRepository.deleteAll()
    }

    @Test
    public void appServletTest() {
        def token = post('/auth/login', """{"username":"admin","pwd":"123456"}""", HttpStatus.OK).getInputStream().getText()
        get("/workflow/service/app/rest/models?access_token=" + token, HttpStatus.OK)
    }

    @Test
    public void apiServletTest() {
        def token = post('/auth/login', """{"username":"admin","pwd":"123456"}""", HttpStatus.OK).getInputStream().getText()
        List<Model> models = modelRepository.findByModelType(AbstractModel.MODEL_TYPE_BPMN, ModelSort.MODIFIED_DESC)
        resOfGet("/workflow/service/api/editor/models/" + models.get(0).getId() + "?access_token=" + token, HttpStatus.OK)
    }

    @Test
    public void processServletTest() {
        def token = post('/auth/login', """{"username":"admin","pwd":"123456"}""", HttpStatus.OK).getInputStream().getText()
        def pepProcInstVO = JSONUtil.parse(post('/workflow/process/workflowAutoTaskError?access_token=' + token, JSONUtil.toJSON(new HashMap()), HttpStatus.CREATED).getInputStream().getText(), PEPProcInstVO.class)
        resOfGet("/workflow/service/process/runtime/process-instances/" + pepProcInstVO.getProcInstId() + "/diagram?access_token=" + token, HttpStatus.OK)
    }
}
