package com.proper.enterprise.platform.oopsearch.sync.h2.service.impl;

import com.proper.enterprise.platform.schedule.cluster.PEPMethodInvokingJobDetailFactoryBean;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OopSearchSyncH2Configuration {

    @Bean
    List<Trigger> pepJobListOopSearchH2(@Qualifier("h2SyncJobTrigger")Trigger h2SyncJobTrigger) {
        List<Trigger> pepJobListTest = new ArrayList<>();
        pepJobListTest.add(h2SyncJobTrigger);
        return pepJobListTest;
    }

    @Bean
    public CronTriggerFactoryBean h2SyncJobTrigger(@Qualifier("h2SyncJobDetail")
                                                          JobDetail h2SyncJobDetail) {
        CronTriggerFactoryBean mongoSyncJobTrigger = new CronTriggerFactoryBean();
        mongoSyncJobTrigger.setJobDetail(h2SyncJobDetail);
        mongoSyncJobTrigger.setCronExpression("1/5 * * * * ?");
        return mongoSyncJobTrigger;
    }

    @Bean
    public PEPMethodInvokingJobDetailFactoryBean h2SyncJobDetail() {
        PEPMethodInvokingJobDetailFactoryBean h2SyncJobDetail = new PEPMethodInvokingJobDetailFactoryBean();
        h2SyncJobDetail.setTargetBeanName("h2SyncJobService");
        h2SyncJobDetail.setTargetMethod("fullSyncMongo");
        h2SyncJobDetail.setConcurrent(false);
        return h2SyncJobDetail;
    }
}
