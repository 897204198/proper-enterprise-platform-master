package com.proper.enterprise.platform.core.controller.mock

import com.fasterxml.jackson.annotation.JsonIgnore
import com.proper.enterprise.platform.core.PEPConstants

class MockEntity extends DO {
    private static final long serialVersionUID = PEPConstants.VERSION;
    public MockEntity(String c1, String c2) {
        entityC1 = c1
        entityC2 = c2
    }

    protected String lastModifyTime

    @JsonIgnore
    String entityC1

    String entityC2

}
