package com.proper.enterprise.platform.core.controller.mock

import com.proper.enterprise.platform.core.PEPVersion

class MockEntityR {

    private static final long serialVersionUID = PEPVersion.VERSION

    MockEntityR(String name) {
        this.name = name
    }

    String id

    String name

}
