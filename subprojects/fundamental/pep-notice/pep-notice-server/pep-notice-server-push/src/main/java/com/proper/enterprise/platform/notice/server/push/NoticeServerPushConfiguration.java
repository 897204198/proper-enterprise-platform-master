package com.proper.enterprise.platform.notice.server.push;

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
public class NoticeServerPushConfiguration {

    @Bean
    List<Trigger> pepJobListPush(@Qualifier("saveMsgStatisticInfoScheduleTrigger") Trigger saveMsgStatisticInfoScheduleTrigger) {
        List<Trigger> pepJobList = new ArrayList<>();
        pepJobList.add(saveMsgStatisticInfoScheduleTrigger);
        return pepJobList;
    }

    @Bean
    public CronTriggerFactoryBean saveMsgStatisticInfoScheduleTrigger(@Qualifier("pushStatisticTaskDetail")
                                                                          JobDetail pushStatisticTaskDetail) {
        CronTriggerFactoryBean noticeStatusSyncPending = new CronTriggerFactoryBean();
        noticeStatusSyncPending.setJobDetail(pushStatisticTaskDetail);
        noticeStatusSyncPending.setCronExpression("0 0 3 * * ?");
        return noticeStatusSyncPending;
    }

    @Bean
    public PEPMethodInvokingJobDetailFactoryBean pushStatisticTaskDetail() {
        PEPMethodInvokingJobDetailFactoryBean noticeStatusSyncScheduler = new PEPMethodInvokingJobDetailFactoryBean();
        noticeStatusSyncScheduler.setTargetBeanName("pushStatisticTask");
        noticeStatusSyncScheduler.setTargetMethod("saveYesterdayPushStatistic");
        noticeStatusSyncScheduler.setConcurrent(false);
        return noticeStatusSyncScheduler;
    }
}
