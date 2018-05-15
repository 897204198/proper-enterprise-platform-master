package com.proper.enterprise.platform.core.jpa

import org.junit.Test

class CrudTest extends CrudBaseTest {

    @Test
    void crud() {
        create()
        retrieve()
        update()
        updateCheck()
        delete()
    }

}
