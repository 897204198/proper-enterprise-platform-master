package com.proper.enterprise.platform.workflow;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("classpath:application-workflow.properties")
@ImportResource("classpath:applicationContext-workflow-engine.xml")
public class WorkflowConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/workflow/*.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/workflow/editor-app/**").addResourceLocations("classpath:/META-INF/resources/editor-app/");
        registry.addResourceHandler("/workflow/fonts/**").addResourceLocations("classpath:/META-INF/resources/fonts/");
        registry.addResourceHandler("/workflow/i18n/**").addResourceLocations("classpath:/META-INF/resources/i18n/");
        registry.addResourceHandler("/workflow/libs/**").addResourceLocations("classpath:/META-INF/resources/libs/");
        registry.addResourceHandler("/workflow/scripts/**").addResourceLocations("classpath:/META-INF/resources/scripts/");
        registry.addResourceHandler("/workflow/styles/**").addResourceLocations("classpath:/META-INF/resources/styles/");
        registry.addResourceHandler("/workflow/views/**").addResourceLocations("classpath:/META-INF/resources/views/");
    }

}
