package com.proper.enterprise.platform.workflow;

import com.proper.enterprise.platform.workflow.flowable.idm.service.impl.PEPIdmIdentityServiceImpl;
import com.proper.enterprise.platform.workflow.filter.WorkflowAuthFilter;
import org.flowable.idm.spring.SpringIdmEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;


@Configuration
@PropertySource("classpath:application-workflow.properties")
public class WorkflowConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/workflow/*.html").addResourceLocations("classpath:/META-INF/resources/designer/");
        registry.addResourceHandler("/workflow/editor-app/**").addResourceLocations("classpath:/META-INF/resources/designer/editor-app/");
        registry.addResourceHandler("/workflow/fonts/**").addResourceLocations("classpath:/META-INF/resources/designer/fonts/");
        registry.addResourceHandler("/workflow/i18n/**").addResourceLocations("classpath:/META-INF/resources/designer/i18n/");
        registry.addResourceHandler("/workflow/libs/**").addResourceLocations("classpath:/META-INF/resources/designer/libs/");
        registry.addResourceHandler("/workflow/scripts/**").addResourceLocations("classpath:/META-INF/resources/designer/scripts/");
        registry.addResourceHandler("/workflow/styles/**").addResourceLocations("classpath:/META-INF/resources/designer/styles/");
        registry.addResourceHandler("/workflow/views/**").addResourceLocations("classpath:/META-INF/resources/designer/views/");
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
