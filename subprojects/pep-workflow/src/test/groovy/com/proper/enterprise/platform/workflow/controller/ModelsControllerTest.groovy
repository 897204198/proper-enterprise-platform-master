package com.proper.enterprise.platform.workflow.controller

import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.vo.PEPModelVO
import org.flowable.app.domain.editor.AbstractModel
import org.flowable.app.domain.editor.Model
import org.flowable.app.model.common.ResultListDataRepresentation
import org.flowable.app.repository.editor.ModelRepository
import org.flowable.app.service.api.ModelService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class ModelsControllerTest extends AbstractTest {
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
        Map<String, String> deploy1 = postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        Map<String, String> deploy2 = postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        assert deploy2.get('id') > deploy1.get('id')
        assert deploy1.get('name') == deploy2.get('name')
    }


    @Test
    @Sql("/com/proper/enterprise/platform/workflow/datadics.sql")
    void testGetModels() {
        def searchKey = "validatenameduser"
        List<AbstractModel> models = modelRepository.findByModelTypeAndFilter(AbstractModel.MODEL_TYPE_BPMN, searchKey, null)
        postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
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
    }
}
