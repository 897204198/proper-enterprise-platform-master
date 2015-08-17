package com.proper.enterprise.platform.auth.aop;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AOPConfiguration {

    @Autowired
    public HistoricalAdvice advice;

    @Bean
    public Advisor historicalAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public * org.springframework.data.repository.Repository+.save*(..))");
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
