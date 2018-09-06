package com.proper.enterprise.platform.push.config.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.jms.config.JmsListenerConfigUtils;

@Configuration
public class PushJmsConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushJmsConfig.class);

    @Bean(name = JmsListenerConfigUtils.JMS_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanPostProcessor jmsListenerAnnotationProcessor() {
        return new MyNoJmsBeanPostProcessor();
    }

    @Bean(name = JmsListenerConfigUtils.JMS_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME)
    public ApplicationListener defaultJmsListenerEndpointRegistry() {
        return new MyNoJmsApplicationListener();
    }

    /**
     * 当不使用jms时对应的Mock BeanPostProcessor,否则程序会报错
     */
    public static class MyNoJmsBeanPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }
    }

    /**
     * 当不使用jms时对应的Mock ApplicationListener,否则程序会报错
     */
    public static class MyNoJmsApplicationListener implements ApplicationListener {

        @Override
        public void onApplicationEvent(ApplicationEvent event) {

        }
    }
}
