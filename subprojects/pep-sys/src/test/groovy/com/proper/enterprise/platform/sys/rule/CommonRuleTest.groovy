package com.proper.enterprise.platform.sys.rule

import com.proper.enterprise.platform.sys.bizrule.entity.RuleEntity
import com.proper.enterprise.platform.sys.bizrule.repository.RuleRepository
import com.proper.enterprise.platform.sys.bizrule.rule.CommonRule
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class CommonRuleTest extends AbstractTest {
    @Autowired
    CommonRule commonRule

    @Autowired
    RuleRepository repository

    @Test
    void testCommonRule() {
        assert commonRule.in("test", "test", "test2")
    }

    @Test
    void testEntity(){
        coverBean(new RuleEntity())
    }
}
