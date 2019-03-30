package com.proper.enterprise.platform.websocket

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.auth.common.vo.AccessTokenVO
import com.proper.enterprise.platform.test.AbstractIntegrationTest
import com.proper.enterprise.platform.websocket.client.stomp.StompClient
import com.proper.enterprise.platform.websocket.client.stomp.StompFrameStringHandler
import com.proper.enterprise.platform.websocket.controller.JsonMsgController
import com.proper.enterprise.platform.websocket.util.StompMessageSendUtil
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.Nullable
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders

import java.lang.reflect.Type
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class StompClientIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private AccessTokenService tokenService

    String url, token

    @Before
    void setUp() {
        token = tokenService.generate()
        tokenService.saveOrUpdate(new AccessTokenVO('test', 'for test using', token, 'GET:/stomp/**'))
        url = "ws://localhost:$port/stomp?access_token=$token"
    }

    @After
    void tearDown() {
        tokenService.deleteByToken(token)
    }

    @Test
    void testStringMessage() {
        CountDownLatch latch = new CountDownLatch(1)
        connect(latch, 'str', '/topic/test.str', '/app/test.str', 'hinex')
        assert latch.await(3, TimeUnit.SECONDS)
    }

    void connect(CountDownLatch latch, String user, String subscribe, String send = null, Object sendPayload = null) {
        def client = StompClient.connect(user, url)
        def handler = new EmbeddedHandler(latch)
        client.subscribe(subscribe, handler)
        if (!subscribe.startsWith('/user')) {
            client.subscribe("/user$subscribe", handler)
        }
        if (send > '' && sendPayload != null) {
            client.send(send, sendPayload)
        }
    }

    class EmbeddedHandler implements StompFrameStringHandler {

        CountDownLatch latch

        EmbeddedHandler(CountDownLatch latch) {
            this.latch = latch
        }

        @Override
        void handleFrame(StompHeaders headers, @Nullable Object payload) {
            println "[HINEX] $headers, $payload"
            if (payload != null) {
                latch.countDown()
            }
        }
    }

    @Test
    void testJsonMsg() {
        final CountDownLatch latch = new CountDownLatch(3)

        def client = StompClient.connect('testJsonMsg', url, new MappingJackson2MessageConverter())
        client.subscribe('/topic/test.json', new StompFrameHandler() {
            @Override
            Type getPayloadType(StompHeaders headers) {
                return JsonMsgController.Greeting.class
            }

            @Override
            void handleFrame(StompHeaders headers, @Nullable Object payload) {
                if (payload.getContent().startsWith('Hello Spring')) {
                    latch.countDown()
                }
            }
        })

        sendMessage(client, "Springxxx", latch)
        sendMessage(client, "Spring", latch)
        sendMessage(client, "Springzzzz", latch)

        assert latch.await(5, TimeUnit.SECONDS)
    }

    def sendMessage(StompClient client, String msg, CountDownLatch latch) {
        try {
            client.send('/app/test.json', [name: msg])
        } catch (Throwable t) {
            t.printStackTrace()
            latch.countDown()
        }
    }

    @Test
    void testServerBroadcast() {
        CountDownLatch latch1 = new CountDownLatch(1)
        CountDownLatch latch2 = new CountDownLatch(1)
        CountDownLatch latch3 = new CountDownLatch(1)
        def subscribe = '/topic/test.broadcast'
        connect(latch1, 'u1', subscribe)
        connect(latch2, 'u2', "${subscribe}1")
        connect(latch3, 'u3', subscribe)
        assert latch1.await(3, TimeUnit.SECONDS) && !latch2.await(3, TimeUnit.SECONDS) && latch3.await(3, TimeUnit.SECONDS)
    }

    @Test
    void testSendToUser() {
        CountDownLatch latch1 = new CountDownLatch(1)
        CountDownLatch latch2 = new CountDownLatch(1)
        connect(latch1, 'single_u1', '/user/topic/test.single')
        connect(latch2, 'single_u2', '/user/topic/test.single')
        assert !latch1.await(3, TimeUnit.SECONDS) && latch2.await(3, TimeUnit.SECONDS)
    }

    @Ignore
    @Test
    void autoSendChecker() {
        CountDownLatch latch1 = new CountDownLatch(10)
        CountDownLatch latch2 = new CountDownLatch(10)
        CountDownLatch latch3 = new CountDownLatch(10)
        connect(latch1, 'as_u1', '/topic/sync')
        connect(latch2, 'as_u2', '/topic/sync')
        connect(latch3, 'as_u3', '/topic/sync')
        assert latch1.await(20, TimeUnit.SECONDS)
    }

    @Test
    void stompMessageSendUtil() {
        CountDownLatch latch1 = new CountDownLatch(1)
        connect(latch1, 'sendUtil', '/user/topic/stompMessageSendUtil.client')
        sleep(500)
        StompMessageSendUtil.send('sendUtil', '/topic/stompMessageSendUtil.client', "test")
        assert latch1.await(3, TimeUnit.SECONDS)


        CountDownLatch latch2 = new CountDownLatch(1)
        connect(latch2, 'sendUtil', '/topic/stompMessageSendUtil')
        sleep(500)
        StompMessageSendUtil.send('sendUtil', '/topic/stompMessageSendUtil', "test")
        assert latch2.await(3, TimeUnit.SECONDS)
    }

}
