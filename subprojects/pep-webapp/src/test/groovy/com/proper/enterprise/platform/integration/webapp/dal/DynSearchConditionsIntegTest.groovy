package com.proper.enterprise.platform.integration.webapp.dal

import com.proper.enterprise.platform.core.repository.SearchCondition
import com.proper.enterprise.platform.core.repository.SearchConditionBuilder
import com.proper.enterprise.platform.integration.webapp.dal.repository.TestRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class DynSearchConditionsIntegTest extends AbstractIntegTest {

    @Autowired
    TestRepository repo

    @Test
    public void test() {
        repo.findAll(SearchConditionBuilder.build(new SearchCondition('id', '1'), new SearchCondition('loginName', '2')))
    }

}
