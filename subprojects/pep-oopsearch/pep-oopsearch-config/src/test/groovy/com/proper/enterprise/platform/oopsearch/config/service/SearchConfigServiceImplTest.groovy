package com.proper.enterprise.platform.oopsearch.config.service

import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs
import com.proper.enterprise.platform.oopsearch.api.repository.SearchConfigRepository
import com.proper.enterprise.platform.oopsearch.api.serivce.SearchConfigService
import com.proper.enterprise.platform.oopsearch.config.repository.DemoUserRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class SearchConfigServiceImplTest extends AbstractTest{

    @Autowired
    SearchConfigService searchConfigService

    @Autowired
    DemoUserRepository  demoUserRepository

    @Autowired
    SearchConfigRepository searchConfigRepository

    @Test
    @Sql("/sql/oopsearch/config/configData.sql")
    void testAll() {
        String moduleName = "demouser"
        Map<String, Object> searchConfigBeans = searchConfigService.getSearchConfigs()
        assert searchConfigBeans.size() == 1

        AbstractSearchConfigs searchConfigs = searchConfigService.getSearchConfig(moduleName)
        assert searchConfigs != null

    }
}
