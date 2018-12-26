package com.proper.enterprise.platform.workflow.httptask

import com.proper.enterprise.platform.core.PEPApplicationContext
import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import org.flowable.engine.cfg.HttpClientConfig
import org.flowable.spring.SpringProcessEngineConfiguration
import org.junit.Test
import org.springframework.test.context.jdbc.Sql

class WorkflowHttpClientConfigTest extends WorkflowAbstractTest {

    @Test
    void test() {
        HttpClientConfig httpClientConfig

        httpClientConfig = PEPApplicationContext.getBean(SpringProcessEngineConfiguration.class).getHttpClientConfig()
        assert 5000 == httpClientConfig.getConnectTimeout()
        assert 5000 == httpClientConfig.getSocketTimeout()
        assert 5000 == httpClientConfig.getConnectionRequestTimeout()
        assert 3 == httpClientConfig.getRequestRetryLimit()
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    void testProcess() {
        setCurrentUserId("user1")
        start("testProcess", [:])
    }
}
