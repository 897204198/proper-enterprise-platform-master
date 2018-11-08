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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@ComponentScan(basePackages = "com.proper.enterprise.platform", lazyInit = true)
@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@EnableScheduling
/**
 * TODO to be clean
 */
@ImportResource("classpath*:spring/**/applicationContext-*.xml")
public class PEPConfiguration implements WebMvcConfigurer {

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

    @Autowired
    private ObjectMapper objectMapper;

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
    public TaskScheduler pepScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(schedulerProperties.getPoolSize());
        return scheduler;
    }

    @Bean
    public TaskExecutor pepExecutor() {
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

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        // https://github.com/propersoft-cn/proper-enterprise-platform/pull/68
        stringConverter.setWriteAcceptCharset(false);
        converters.add(stringConverter);

        // https://github.com/propersoft-cn/proper-enterprise-platform/pull/348
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }

    @Bean
    public List<String> ignorePatternsListOptions() {
        return Collections.singletonList("OPTIONS:/**");
    }

}
