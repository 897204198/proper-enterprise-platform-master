package com.proper.enterprise.platform.notice.server.app;

import com.proper.enterprise.platform.schedule.cluster.PEPMethodInvokingJobDetailFactoryBean;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile({"dev", "production"})
public class NoticeServerAppConfiguration {

    @Bean
    List<Trigger> pepJobListNotice(@Qualifier("noticeStatusSyncPending")Trigger noticeStatusSyncPending,
                                   @Qualifier("noticeStatusSyncRetry")Trigger noticeStatusSyncRetry) {
        List<Trigger> pepJobList = new ArrayList<>();
        pepJobList.add(noticeStatusSyncPending);
        pepJobList.add(noticeStatusSyncRetry);
        return pepJobList;
    }

    @Bean
    public CronTriggerFactoryBean noticeStatusSyncPending(@Qualifier("noticeStatusSyncPendingJobDetail")
                                                                  JobDetail noticeStatusSyncScheduler) {
        CronTriggerFactoryBean noticeStatusSyncPending = new CronTriggerFactoryBean();
        noticeStatusSyncPending.setJobDetail(noticeStatusSyncScheduler);
        noticeStatusSyncPending.setCronExpression("0 */2 * * * ?");
        return noticeStatusSyncPending;
    }

    @Bean
    public PEPMethodInvokingJobDetailFactoryBean noticeStatusSyncPendingJobDetail() {
        PEPMethodInvokingJobDetailFactoryBean noticeStatusSyncScheduler = new PEPMethodInvokingJobDetailFactoryBean();
        noticeStatusSyncScheduler.setTargetBeanName("noticeStatusSyncScheduler");
        noticeStatusSyncScheduler.setTargetMethod("syncPending");
        noticeStatusSyncScheduler.setConcurrent(false);
        return noticeStatusSyncScheduler;
    }

    @Bean
    public CronTriggerFactoryBean noticeStatusSyncRetry(@Qualifier("noticeStatusSyncRetryJobDetail")
                                                                JobDetail noticeStatusSyncScheduler) {
        CronTriggerFactoryBean noticeRetry = new CronTriggerFactoryBean();
        noticeRetry.setJobDetail(noticeStatusSyncScheduler);
        noticeRetry.setCronExpression("0 */2 * * * ?");
        return noticeRetry;
    }

    @Bean
    public PEPMethodInvokingJobDetailFactoryBean noticeStatusSyncRetryJobDetail() {
        PEPMethodInvokingJobDetailFactoryBean noticeStatusSyncScheduler = new PEPMethodInvokingJobDetailFactoryBean();
        noticeStatusSyncScheduler.setTargetBeanName("noticeStatusSyncScheduler");
        noticeStatusSyncScheduler.setTargetMethod("syncRetry");
        noticeStatusSyncScheduler.setConcurrent(false);
        return noticeStatusSyncScheduler;
    }

}


