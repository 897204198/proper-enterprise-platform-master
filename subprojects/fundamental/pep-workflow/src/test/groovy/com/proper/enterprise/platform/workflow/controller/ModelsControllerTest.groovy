package com.proper.enterprise.platform.workflow.controller

import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.vo.PEPModelVO
import org.flowable.ui.common.model.ResultListDataRepresentation
import org.flowable.ui.modeler.domain.AbstractModel
import org.flowable.ui.modeler.domain.Model
import org.flowable.ui.modeler.repository.ModelRepository
import org.flowable.ui.modeler.serviceapi.ModelService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql("/com/proper/enterprise/platform/workflow/wfCategory.sql")
class ModelsControllerTest extends AbstractJPATest {
    @Autowired
    ModelService modelService
    @Autowired
    ModelRepository modelRepository
    @Autowired
    private DataDicService dataDicService

    @Test
    void testDeployment() {
        mockUser("pep-sysadmin", "admin", "123456", true)
        List<AbstractModel> models = modelService.getModelsByModelType(AbstractModel.MODEL_TYPE_BPMN)
        Map modelVO1 = resOfPost('/repository/models/' + models.get(0).id + '/deployment', new PEPModelVO())
        Map modelVO2 = resOfPost('/repository/models/' + models.get(0).id + '/deployment', new PEPModelVO())
        assert modelVO1.processVersion < modelVO2.processVersion
        assert modelVO1.name == modelVO2.name
        assert modelVO1.workflowCategory.code == "http://www.flowable.org/processdef"
        assert modelVO1.workflowCategory.name == "默认Flowable类别"
    }


    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/datadics.sql",
        "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql",
        "/com/proper/enterprise/platform/workflow/wfCategory.sql"])
    void testGetModels() {
        def searchKey = "validateNamedUser"
        List<AbstractModel> models = modelRepository.findByModelTypeAndFilter(AbstractModel.MODEL_TYPE_BPMN, searchKey.toLowerCase(), null)
        resOfPost('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        resOfPost('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        resOfPost('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        resOfPost('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        resOfPost('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        resOfPost('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        resOfPost('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        resOfPost('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        resOfPost('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        resOfPost('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        resOfPost('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        Model model = new Model()
        model.setName("nodeploy")
        model.setKey("nodeploy")
        model.setDescription("nodeploy")
        model.setModelType(AbstractModel.MODEL_TYPE_BPMN)
        modelRepository.save(model)
        ResultListDataRepresentation representation = JSONUtil.parse(get('/repository/models/?filter=' + searchKey + '&modelType=0'
            , HttpStatus.OK).getResponse().getContentAsString(), ResultListDataRepresentation.class)
        assert 12 == representation.getData().get(0).processVersion
        assert PEPModelVO.ModelStatus.DEPLOYED.name() == representation.getData().get(0).status.code
        ResultListDataRepresentation nodeploy = JSONUtil.parse(get('/repository/models/?filter=nodeploy&modelType=0'
            , HttpStatus.OK).getResponse().getContentAsString(), ResultListDataRepresentation.class)
        assert PEPModelVO.ModelStatus.UN_DEPLOYED.name() == nodeploy.getData().get(0).status.code

        representation = JSONUtil.parse(get('/repository/models/?modelType=0&modelStatus=DEPLOYED'
            , HttpStatus.OK).getResponse().getContentAsString(), ResultListDataRepresentation.class)
        for (Map deployModelVO : representation.getData()) {
            assert PEPModelVO.ModelStatus.DEPLOYED.name() == deployModelVO.status.code
        }

        Map nodeployModelVO = nodeploy.getData().get(0)
        nodeployModelVO.put("name", "nodeploy2")
        nodeployModelVO.put("description", "nodeploy2")
        put('/repository/models/' + nodeployModelVO.id, JSONUtil.toJSON(nodeployModelVO)
            , HttpStatus.OK)
        ResultListDataRepresentation nodeploy2 = JSONUtil.parse(get('/repository/models/?filter=nodeploy2&modelType=0'
            , HttpStatus.OK).getResponse().getContentAsString(), ResultListDataRepresentation.class)
        assert "nodeploy2" == nodeploy2.getData().get(0).name
        assert "nodeploy2" == nodeploy2.getData().get(0).description

        ResultListDataRepresentation modelVOs = JSONUtil.parse(get('/repository/models/?filter=' + searchKey + '&modelType=0'
            , HttpStatus.OK).getResponse().getContentAsString(), ResultListDataRepresentation.class)
        put('/repository/models/' + models.get(0).id + '/wfCategory?workflowCategoryCode=http://www.activiti.org/processdef', JSONUtil.toJSON("")
            , HttpStatus.OK)
        ResultListDataRepresentation modelVOs2 = JSONUtil.parse(get('/repository/models/?filter=' + searchKey + '&modelType=0'
            , HttpStatus.OK).getResponse().getContentAsString(), ResultListDataRepresentation.class)
        assert "默认Activiti类别" == modelVOs2.getData().get(0).workflowCategory.name
    }
}
