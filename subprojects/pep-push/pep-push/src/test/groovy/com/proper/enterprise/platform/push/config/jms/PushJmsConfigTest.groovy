package com.proper.enterprise.platform.push.config.jms

import com.proper.enterprise.platform.push.test.PushAbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class PushJmsConfigTest extends PushAbstractTest {

    @Autowired
    PushJmsConfig config

    @Test
    void innerTest() {
        assert config.jmsListenerAnnotationProcessor() instanceof PushJmsConfig.MyNoJmsBeanPostProcessor
        assert config.defaultJmsListenerEndpointRegistry() instanceof PushJmsConfig.MyNoJmsApplicationListener
    }
}
