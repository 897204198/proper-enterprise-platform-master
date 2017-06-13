package com.proper.enterprise.platform.push.common.config.jms;

import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
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
import org.springframework.jms.annotation.JmsListenerAnnotationBeanPostProcessor;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerConfigUtils;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@Configuration
public class PushJmsConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushJmsConfig.class);

    /**
     * 是否不使用jms,直接调用target service
     *
     * @return
     */
    public boolean isNoJms() {
        return StringUtil.equals("inner", ConfCenter.get("push_mq_vendor"));
    }

    @Bean
    public JmsTemplate pushAppServerRequestJmsTemplate(ConnectionFactory pushJmsConnectionFactory) {
        if (isNoJms()) {
            return null;
        }
        // queue
        JmsTemplate tpl = new JmsTemplate();
        tpl.setConnectionFactory(pushJmsConnectionFactory);
        return tpl;
    }

    @Bean
    public ConnectionFactory pushJmsConnectionFactory() {
        if (isNoJms()) {
            LOGGER.info("jms push_mq_vendor is inner");
            return null;
        }
        String brockerUrl = ConfCenter.get("push_mq_activemq_brockerUrl", "tcp://localhost:61616");
        ConnectionFactory srcFactory = new ActiveMQConnectionFactory(brockerUrl);

        String sessionCacheSize = ConfCenter.get("push_mq_sessionCacheSize");
        LOGGER.info("jms use brockerUrl:{}", brockerUrl);
        ConnectionFactory rtnFactory;
        //如果配置文件中配置了push_mq_sessionCacheSize，则使用CachingConnectionFactory
        if (StringUtil.isNotEmpty(sessionCacheSize)) {
            CachingConnectionFactory cacheFactory = new CachingConnectionFactory();
            cacheFactory.setSessionCacheSize(Integer.parseInt(sessionCacheSize));
            cacheFactory.setTargetConnectionFactory(srcFactory);
            rtnFactory = cacheFactory;
            LOGGER.info("jms use Spring CachingConnectionFactory of sessionCacheSize:{}", sessionCacheSize);
        } else {
            //否则使用activemq默认的ConnectionFactory
            rtnFactory = srcFactory;
            LOGGER.info("jms use ActiveMQConnectionFactory");
        }

        return rtnFactory;

    }

    @Bean
    public JmsListenerContainerFactory<?> pushAppServerRequestFactory(ConnectionFactory pushJmsConnectionFactory) {
        if (isNoJms()) {
            return null;
        }
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(pushJmsConnectionFactory);
        // This provides all boot's default to this factory, including the
        // message converter
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter pushJacksonJmsMessageConverter() {
        if (isNoJms()) {
            return null;
        }
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean(name = JmsListenerConfigUtils.JMS_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanPostProcessor jmsListenerAnnotationProcessor() {
        if (isNoJms()) {
            return new MyNoJmsBeanPostProcessor();
        }
        return new JmsListenerAnnotationBeanPostProcessor();
    }

    @Bean(name = JmsListenerConfigUtils.JMS_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME)
    public ApplicationListener defaultJmsListenerEndpointRegistry() {
        if (isNoJms()) {
            return new MyNoJmsApplicationListener();
        }
        return new JmsListenerEndpointRegistry();
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
