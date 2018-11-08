package com.proper.enterprise.platform.workflow.frame.service;

import com.proper.enterprise.platform.core.PEPConstants;
import org.springframework.stereotype.Service;

@Service("formPropertyService")
public class FormPropertyService {

    public String value() {
        return PEPConstants.DEFAULT_OPERAOTR_ID;
    }
}
