package com.proper.enterprise.platform.push.config.jms

import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.push.test.PushAbstractTest
import org.apache.activemq.ActiveMQConnectionFactory
import org.junit.Before
import org.junit.Test
import org.springframework.jms.annotation.JmsListenerAnnotationBeanPostProcessor
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerEndpointRegistry
import org.springframework.jms.connection.CachingConnectionFactory
import org.springframework.jms.core.JmsTemplate

class PushJmsConfigTest extends PushAbstractTest {
    PushJmsConfig config

    @Before
    void init() {
        config = new PushJmsConfig()
        System.clearProperty('push_mq_vendor')
        System.clearProperty('push_mq_sessionCacheSize')
        System.clearProperty('push_mq_activemq_brockerUrl')
    }

    @Test
    void activeMqTest() {
        System.setProperty('push_mq_vendor', 'activemq')
        ConfCenter.reload()

        assert !config.isNoJms()
        assert config.pushJmsConnectionFactory() instanceof ActiveMQConnectionFactory
        assert config.pushAppServerRequestFactory(config.pushJmsConnectionFactory()) instanceof DefaultJmsListenerContainerFactory
        assert config.pushJacksonJmsMessageConverter() != null
        assert config.jmsListenerAnnotationProcessor() instanceof JmsListenerAnnotationBeanPostProcessor
        assert config.defaultJmsListenerEndpointRegistry() instanceof JmsListenerEndpointRegistry
        assert config.pushAppServerRequestJmsTemplate() instanceof JmsTemplate

        System.setProperty('push_mq_sessionCacheSize', '10')
        ConfCenter.reload()
        assert config.pushJmsConnectionFactory() instanceof CachingConnectionFactory
        CachingConnectionFactory cacheFactory = config.pushJmsConnectionFactory()
        assert cacheFactory.getSessionCacheSize() == 10

    }

    @Test
    void innerTest() {

        System.setProperty('push_mq_vendor', 'inner')
        ConfCenter.reload()
        assert config.isNoJms()
        assert config.pushJmsConnectionFactory() == null
        assert config.pushAppServerRequestFactory(null) == null
        assert config.pushJacksonJmsMessageConverter() == null
        assert config.jmsListenerAnnotationProcessor() instanceof PushJmsConfig.MyNoJmsBeanPostProcessor
        assert config.defaultJmsListenerEndpointRegistry() instanceof PushJmsConfig.MyNoJmsApplicationListener
        assert config.pushAppServerRequestJmsTemplate() == null

    }
}
