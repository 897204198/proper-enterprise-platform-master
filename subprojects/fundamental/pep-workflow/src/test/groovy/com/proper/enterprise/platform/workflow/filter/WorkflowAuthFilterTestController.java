package com.proper.enterprise.platform.workflow.filter;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import org.flowable.common.engine.impl.identity.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/workflow/")
public class WorkflowAuthFilterTestController extends BaseController {

    private static final String USER_KEY = "user1";

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity test() {
        if (USER_KEY.equals(Authentication.getAuthenticatedUserId())) {
            return responseOfGet(null);
        }
        throw new ErrMsgException("error");
    }
}
