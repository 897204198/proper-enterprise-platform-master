package com.proper.enterprise.platform.oopsearch;

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
public class OopSearchConfiguration {

    @Bean
    List<Trigger> pepJobListOopSearchMysql(@Qualifier("mongoSyncJobTrigger") Trigger mongoSyncJobTrigger) {
        List<Trigger> pepJobListTest = new ArrayList<>();
        pepJobListTest.add(mongoSyncJobTrigger);
        return pepJobListTest;
    }

    @Bean
    public CronTriggerFactoryBean mongoSyncJobTrigger(@Qualifier("mongoSyncJobDetail")
                                                              JobDetail mongoSyncJobDetail) {
        CronTriggerFactoryBean mongoSyncJobTrigger = new CronTriggerFactoryBean();
        mongoSyncJobTrigger.setJobDetail(mongoSyncJobDetail);
        mongoSyncJobTrigger.setCronExpression("* * * * * ?");
        return mongoSyncJobTrigger;
    }

    @Bean
    public PEPMethodInvokingJobDetailFactoryBean mongoSyncJobDetail() {
        PEPMethodInvokingJobDetailFactoryBean mongoSyncJobDetail = new PEPMethodInvokingJobDetailFactoryBean();
        mongoSyncJobDetail.setTargetBeanName("syncMongo");
        mongoSyncJobDetail.setTargetMethod("syncMongoInsert");
        mongoSyncJobDetail.setConcurrent(false);
        return mongoSyncJobDetail;
    }
}
