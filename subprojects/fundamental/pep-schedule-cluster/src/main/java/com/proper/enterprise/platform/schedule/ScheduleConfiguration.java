package com.proper.enterprise.platform.schedule;

import com.proper.enterprise.platform.core.factory.ComposeListFactoryBean;
import org.quartz.Trigger;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties(QuartzProperties.class)
@PropertySource("classpath:application-schedule-cluster.properties")
public class ScheduleConfiguration {

    @Bean
    public ComposeListFactoryBean pepJobList() {
        return new ComposeListFactoryBean("pepJobList.+");
    }

    @Bean
    @DependsOn("liquibase")
    @Lazy(false)
    public SchedulerFactoryBean pepSchedulerFactory(QuartzProperties quartzProperties,
                                                    DataSource dataSource,
                                                    JpaTransactionManager jpaTransactionManager,
                                                    List<Trigger> pepJobList) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setTransactionManager(jpaTransactionManager);
        schedulerFactoryBean.setTriggers(pepJobList.toArray(new Trigger[0]));
        schedulerFactoryBean.setQuartzProperties(asProperties(quartzProperties.getProperties()));
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setJobFactory(new SpringBeanJobFactory());
        return schedulerFactoryBean;
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }
}
