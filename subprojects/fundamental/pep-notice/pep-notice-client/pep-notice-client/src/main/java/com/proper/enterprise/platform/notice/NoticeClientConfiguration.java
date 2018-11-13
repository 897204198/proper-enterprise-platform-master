package com.proper.enterprise.platform.notice;

import com.proper.enterprise.platform.schedule.cluster.PEPMethodInvokingJobDetailFactoryBean;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

import java.util.Collections;
import java.util.List;

@Configuration
@Profile({"dev", "production"})
public class NoticeClientConfiguration {

    @Bean
    List<Trigger> pepJobListNoticeClient(@Qualifier("noticeClientCleanInvalidToken")Trigger trigger) {
        return Collections.singletonList(trigger);
    }

    @Bean
    public CronTriggerFactoryBean noticeClientCleanInvalidToken(@Qualifier("noticeClientCleanInvalidTokenJobDetail")
                                                                JobDetail noticeStatusSyncScheduler) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(noticeStatusSyncScheduler);
        // 每十分钟执行一次
        cronTriggerFactoryBean.setCronExpression("0 */10 * * * ?");
        return cronTriggerFactoryBean;
    }

    @Bean
    public PEPMethodInvokingJobDetailFactoryBean noticeClientCleanInvalidTokenJobDetail() {
        PEPMethodInvokingJobDetailFactoryBean jobDetail = new PEPMethodInvokingJobDetailFactoryBean();
        jobDetail.setTargetBeanName("delPushTokenTask");
        jobDetail.setTargetMethod("getErrorToken");
        jobDetail.setConcurrent(false);
        return jobDetail;
    }

}
