package com.proper.enterprise.platform.core.jpa

import org.junit.Test
import org.springframework.transaction.annotation.Transactional

class TxTest extends CrudBaseTest {

    @Test
    void normal() {
        int before = repository.count()
        create()
        assert repository.count() > before
    }

    @Test
    @Transactional(readOnly=true)
    void readonly() {
        int before = repository.count()
        create()
        assert repository.count() == before
    }

}
