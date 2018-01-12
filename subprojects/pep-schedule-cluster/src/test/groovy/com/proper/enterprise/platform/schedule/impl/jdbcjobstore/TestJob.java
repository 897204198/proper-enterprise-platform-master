package com.proper.enterprise.platform.schedule.impl.jdbcjobstore;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;

public class TestJob implements Job {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TestJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        logger.info("Schedule Success");
    }
}
