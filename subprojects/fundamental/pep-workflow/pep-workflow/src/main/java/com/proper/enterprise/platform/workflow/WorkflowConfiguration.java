package com.proper.enterprise.platform.workflow;

import com.proper.enterprise.platform.workflow.flowable.idm.service.impl.PEPIdmIdentityServiceImpl;
import com.proper.enterprise.platform.workflow.filter.WorkflowAuthFilter;
import org.flowable.engine.cfg.HttpClientConfig;
import org.flowable.idm.spring.SpringIdmEngineConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;


@Configuration
@PropertySource("classpath:application-workflow.properties")
public class WorkflowConfiguration implements WebMvcConfigurer {

    @Autowired
    public WorkflowConfiguration(SpringProcessEngineConfiguration springProcessEngineConfiguration,
                                 WorkflowProperties workflowProperties) {
        HttpClientConfig httpClientConfig = new HttpClientConfig();
        httpClientConfig.setSocketTimeout(workflowProperties.getHttp().getSocketTimeout());
        httpClientConfig.setConnectTimeout(workflowProperties.getHttp().getConnectTimeout());
        httpClientConfig.setConnectionRequestTimeout(workflowProperties.getHttp().getConnectionRequestTimeout());
        httpClientConfig.setRequestRetryLimit(workflowProperties.getHttp().getRequestRetryLimit());
        springProcessEngineConfiguration.setHttpClientConfig(httpClientConfig);
    }

    @Bean
    public EngineConfigurationConfigurer<SpringIdmEngineConfiguration> pepIdmEngineConfigurer() {
        return idmEngineConfiguration -> idmEngineConfiguration
            .setIdmIdentityService(new PEPIdmIdentityServiceImpl());
    }

    @Bean
    @Order
    public Filter wfAuthFilter() {
        return new WorkflowAuthFilter();
    }

}
