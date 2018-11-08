package com.proper.enterprise.platform.schedule

import com.proper.enterprise.platform.schedule.cluster.PEPMethodInvokingJobDetailFactoryBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.CronTriggerFactoryBean
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean

@Configuration
class ScheduleTestConfiguration {

    @Bean
    List pepJobListTest(@Qualifier("simpleTrigger1") SimpleTriggerFactoryBean simpleTrigger1,
                        @Qualifier("simpleTrigger2") SimpleTriggerFactoryBean simpleTrigger2,
                        @Qualifier("simpleTrigger3") CronTriggerFactoryBean simpleTrigger3) {
        List pepJobListTest = new ArrayList()
        pepJobListTest.add(simpleTrigger1.getObject())
        pepJobListTest.add(simpleTrigger2.getObject())
        pepJobListTest.add(simpleTrigger3.getObject())
        return pepJobListTest
    }

    @Bean
    SimpleTriggerFactoryBean simpleTrigger1(@Qualifier("simpleDetail1") PEPMethodInvokingJobDetailFactoryBean simp1Fb) {
        SimpleTriggerFactoryBean simp1Tfb = new SimpleTriggerFactoryBean()
        simp1Tfb.setJobDetail(simp1Fb.getObject())
        simp1Tfb.setStartDelay(100)
        simp1Tfb.setRepeatInterval(200)
        return simp1Tfb
    }

    @Bean
    PEPMethodInvokingJobDetailFactoryBean simpleDetail1() {
        PEPMethodInvokingJobDetailFactoryBean simp1Fb = new PEPMethodInvokingJobDetailFactoryBean()
        simp1Fb.setTargetBeanName("businessService")
        simp1Fb.setTargetMethod("firstBlood")
        simp1Fb.setConcurrent(false)
        return simp1Fb
    }


    @Bean
    SimpleTriggerFactoryBean simpleTrigger2(@Qualifier("simpleDetail2") PEPMethodInvokingJobDetailFactoryBean simp2Fb) {
        SimpleTriggerFactoryBean simp2Tfb = new SimpleTriggerFactoryBean()
        simp2Tfb.setJobDetail(simp2Fb.getObject())
        simp2Tfb.setStartDelay(0)
        simp2Tfb.setRepeatInterval(100)
        return simp2Tfb
    }

    @Bean
    PEPMethodInvokingJobDetailFactoryBean simpleDetail2() {
        PEPMethodInvokingJobDetailFactoryBean simp2Fb = new PEPMethodInvokingJobDetailFactoryBean()
        simp2Fb.setTargetBeanName("businessService")
        simp2Fb.setTargetMethod("doubleKill")
        simp2Fb.setConcurrent(false)
        return simp2Fb
    }


    @Bean
    CronTriggerFactoryBean simpleTrigger3(@Qualifier("simpleDetail3") PEPMethodInvokingJobDetailFactoryBean simp3Fb) {
        CronTriggerFactoryBean ctfb = new CronTriggerFactoryBean()
        ctfb.setJobDetail(simp3Fb.getObject())
        ctfb.setCronExpression("* * * * * ?")
        return ctfb
    }

    @Bean
    PEPMethodInvokingJobDetailFactoryBean simpleDetail3() {
        PEPMethodInvokingJobDetailFactoryBean simp3Fb = new PEPMethodInvokingJobDetailFactoryBean()
        simp3Fb.setTargetBeanName("businessService")
        simp3Fb.setTargetMethod("tripleKill")
        simp3Fb.setConcurrent(false)
        return simp3Fb
    }
}
