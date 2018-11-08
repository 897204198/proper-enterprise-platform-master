package com.proper.enterprise.platform.workflow.handler;

import com.proper.enterprise.platform.core.security.Authentication;
import org.springframework.stereotype.Service;

@Service("variableTimeHandlerTestListener")
public class VariableTimeHandlerTestListener {

    public void test(String data) {
        Authentication.setCurrentUserId(data);
    }
}
