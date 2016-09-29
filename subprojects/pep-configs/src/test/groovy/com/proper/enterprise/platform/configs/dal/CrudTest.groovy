package com.proper.enterprise.platform.configs.dal

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
