package com.proper.enterprise.platform.push.common.config.jms;

import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.proper.enterprise.platform.core.utils.ConfCenter;

@Configuration
@EnableJms
public class PushJmsConfig {
    @Bean
    public JmsTemplate pushAppServerRequestJmsTemplate(ConnectionFactory pushJmsConnectionFactory) {
        // queue
        JmsTemplate tpl = new JmsTemplate();
        tpl.setConnectionFactory(pushJmsConnectionFactory);
        return tpl;
    }

    @Bean
    public ConnectionFactory pushJmsConnectionFactory() {
        String brockerUrl = ConfCenter.get("push_mq_activemq_brockerUrl", "tcp://localhost:61616");
        ConnectionFactory srcFactory = new org.apache.activemq.ActiveMQConnectionFactory(brockerUrl);
        org.springframework.jms.connection.CachingConnectionFactory cacheFactory = new org.springframework.jms.connection.CachingConnectionFactory();
        String sessionCacheSize = ConfCenter.get("push_mq_sessionCacheSize", "10");
        cacheFactory.setSessionCacheSize(Integer.parseInt(sessionCacheSize));
        cacheFactory.setTargetConnectionFactory(srcFactory);
        return cacheFactory;

    }

    @Bean
    public JmsListenerContainerFactory<?> pushAppServerRequestFactory(ConnectionFactory pushJmsConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(pushJmsConnectionFactory);
        // This provides all boot's default to this factory, including the
        // message converter
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter pushJacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
