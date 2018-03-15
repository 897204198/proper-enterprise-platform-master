package com.proper.enterprise.platform.workflow.controller

import com.proper.enterprise.platform.test.AbstractTest
import org.flowable.app.domain.editor.AbstractModel
import org.flowable.app.service.api.ModelService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ModelsControllerTest extends AbstractTest {
    @Autowired
    ModelService modelService

    @Test
    void test() {
        mockUser("pep-sysadmin", "admin", "123456", true)
        List<AbstractModel> models = modelService.getModelsByModelType(AbstractModel.MODEL_TYPE_BPMN)
        Map<String,String> deploy1 = postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        Map<String,String> deploy2 = postAndReturn('/repository/models/' + models.get(0).id + '/deployment', new HashMap())
        assert deploy2.get('id') > deploy1.get('id')
        assert deploy1.get('name') == deploy2.get('name')
    }
}
