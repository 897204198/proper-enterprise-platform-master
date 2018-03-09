package com.proper.enterprise.platform.sys.rule

import com.proper.enterprise.platform.sys.bizrule.rule.CommonRule
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class CommonRuleTest extends AbstractTest {
    @Autowired
    CommonRule commonRule

    @Test
    void testCommonRule() {
        assert commonRule.in("test", "test", "test2")
    }
}
