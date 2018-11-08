package com.proper.enterprise.platform.workflow.frame.service;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("formPropertyService")
public class FormPropertyService {

    @Autowired
    private CoreProperties coreProperties;

    public String value() {
        return coreProperties.getDefaultOperatorId();
    }
}
