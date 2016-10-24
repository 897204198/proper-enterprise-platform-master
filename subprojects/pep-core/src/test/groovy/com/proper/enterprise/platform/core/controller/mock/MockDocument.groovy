package com.proper.enterprise.platform.core.controller.mock

import com.fasterxml.jackson.annotation.JsonIgnore
import com.proper.enterprise.platform.core.document.BaseDocument

class MockDocument extends BaseDocument {

    public MockDocument(String c1, String c2) {
        docC1 = c1
        docC2 = c2
    }

    protected String createTime

    String docC2

    @JsonIgnore
    String docC1

}
