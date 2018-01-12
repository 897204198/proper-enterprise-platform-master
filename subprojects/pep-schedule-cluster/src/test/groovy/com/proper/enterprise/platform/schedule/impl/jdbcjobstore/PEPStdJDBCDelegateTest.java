package com.proper.enterprise.platform.schedule.impl.jdbcjobstore;

import com.proper.enterprise.platform.test.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.quartz.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


public class PEPStdJDBCDelegateTest extends AbstractTest {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PEPStdJDBCDelegateTest.class);
    private static final String JOB_NAME = "testJob";
    private static final String JOB_CRON = "0 0/1 * * * ?";
    @Autowired
    SchedulerFactoryBean pepSchedulerFactory;

    @Test
    public void test() {
        if (addSchedule()) {
            pepSchedulerFactory.start();
            return;
        }
        logger.error("=====PEPStdJDBCDelegateTest test error========");
    }


    private Boolean addSchedule() {
        JobDetail jobDetail = JobBuilder
            .newJob(TestJob.class)
            .requestRecovery(false)
            .withIdentity(JOB_NAME, JOB_NAME)
            .build();
        jobDetail.getJobDataMap().put(JOB_NAME, JOB_NAME);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(JOB_CRON);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(JOB_NAME, JOB_NAME).withSchedule(scheduleBuilder).build();
        try {
            pepSchedulerFactory.getScheduler().scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            logger.error("======test Scheduler Exception======", e);
            return false;
        }
        return true;
    }


}
