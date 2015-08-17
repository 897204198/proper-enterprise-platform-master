package com.proper.enterprise.platform.auth.aop;

import org.springframework.aop.Advisor;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
public class AOPConfiguration {

    @Bean
    public MethodBeforeAdvice interceptor() {

        MethodBeforeAdvice interceptor = new MethodBeforeAdvice() {

            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                System.out.println(" ==== HINEX in advice under AOPConfiguration ==== ");
                System.out.println(method);
                System.out.println(args);
                System.out.println(target);
            }

        };
        return interceptor;
    }

    @Bean
    public Advisor historicalAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public * org.springframework.data.repository.Repository+.save*(..))");
        return new DefaultPointcutAdvisor(pointcut, interceptor());
    }

}
