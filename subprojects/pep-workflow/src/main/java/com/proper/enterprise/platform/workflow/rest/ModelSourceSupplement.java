package com.proper.enterprise.platform.workflow.rest;

import com.proper.enterprise.platform.core.PEPConstants;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * According to activiti-webapp-explorer2, an initial editor source is needed when
 * creating a new model. The RESTFul API in activiti-rest module only support a PUT
 * method with multipart/form-data content type request to set editor source for a
 * model, and that API is not easy to call in front-end. This class supply a POST
 * API with nothing else to initial the editor source for an existing model.
 */
@RestController
public class ModelSourceSupplement {

    private static final String INIT_EDITOR_SOURCE =
            "{\"id\":\"canvas\",\"resourceId\":\"canvas\",\"stencilset\":{\"namespace\":\"http://b3mn.org/stencilset/bpmn2.0#\"}}";

    @Autowired
    RepositoryService repositoryService;

    @RequestMapping(value="/repository/models/{modelId}/source", method = RequestMethod.POST)
    public void test(@PathVariable String modelId, HttpServletResponse response) {
        repositoryService.addModelEditorSource(modelId, INIT_EDITOR_SOURCE.getBytes(PEPConstants.DEFAULT_CHARSET));
        response.setStatus(HttpStatus.OK.value());
    }

}
