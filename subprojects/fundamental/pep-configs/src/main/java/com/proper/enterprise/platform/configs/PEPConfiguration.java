package com.proper.enterprise.platform.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proper.enterprise.platform.configs.filter.AllowCrossOriginFilter;
import com.proper.enterprise.platform.configs.properties.AccessControlProperties;
import com.proper.enterprise.platform.configs.properties.ExecutorProperties;
import com.proper.enterprise.platform.configs.properties.SchedulerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@ComponentScan(basePackages = "com.proper.enterprise.platform")
@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@EnableScheduling
@PropertySource({"classpath:/application-tomcat.properties", "classpath:/application-access.properties"})
public class PEPConfiguration {

    private AccessControlProperties accessControlProperties;
    private SchedulerProperties schedulerProperties;
    private ExecutorProperties executorProperties;

    @Autowired
    public PEPConfiguration(AccessControlProperties accessControlProperties, SchedulerProperties schedulerProperties,
                            ExecutorProperties executorProperties) {
        this.accessControlProperties = accessControlProperties;
        this.schedulerProperties = schedulerProperties;
        this.executorProperties = executorProperties;
    }

    @Bean
    public Filter encodingFilter() {
        return new CharacterEncodingFilter(StandardCharsets.UTF_8.name());
    }

    @Bean
    public Filter allowCrossOriginFilter() {
        return new AllowCrossOriginFilter(accessControlProperties.getAllowCredentials(),
            accessControlProperties.getAllowHeaders(), accessControlProperties.getAllowMethods(),
            accessControlProperties.getAllowOrigin(), accessControlProperties.getExposeHeaders(),
            accessControlProperties.getMaxAge());
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(schedulerProperties.getPoolSize());
        return scheduler;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(executorProperties.getCorePoolSize());
        executor.setMaxPoolSize(executorProperties.getMaxPoolSize());
        executor.setQueueCapacity(executorProperties.getQueueCapacity());
        return executor;
    }

    @Bean
    public ObjectMapper objectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        return builder.build();
    }

    @Bean
    public List<String> ignorePatternsListOptions() {
        return Collections.singletonList("OPTIONS:/**");
    }

}
