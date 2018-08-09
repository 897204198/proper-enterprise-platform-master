package com.proper.enterprise.platform.core

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class PEPConstantsTest extends AbstractTest {

    @Test
    void defaultMonthFormat() {
        assert "yyyy-MM" == PEPConstants.DEFAULT_MONTH_FORMAT
    }
}
