package com.proper.enterprise.platform.websocket.client.stomp

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.auth.common.vo.AccessTokenVO
import com.proper.enterprise.platform.test.AbstractIntegrationTest
import com.proper.enterprise.platform.websocket.util.StompMessageSendUtil
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.Nullable
import org.springframework.messaging.simp.stomp.StompHeaders

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
    void addSubscribeAfterConnect() {
        def stompUser = 'u1'
        def client = StompClient.connect(stompUser, url)

        subscribeAndSendMsgCheck(stompUser, '/topic/test.str', '/app/test.str', 'test1')

        //动态增加订阅
        subscribeAndSendMsgCheck(stompUser, '/topic/test.str.add', '/app/test.str.add', 'test2')

        client.disconnect()
    }

    private static void subscribeAndSendMsgCheck(String stompUser, String topic, String sendDes, String sendPayload) {
        CountDownLatch latch = new CountDownLatch(1)
        def client = StompClient.getInstance(stompUser)
        client.subscribe(topic, new StompFrameStringHandler() {
            @Override
            void handleFrame(StompHeaders headers, @Nullable Object payload) {
                if (payload.endsWith(sendPayload)) {
                    latch.countDown()
                }
            }
        })
        client.send(sendDes, sendPayload)
        assert latch.await(1, TimeUnit.SECONDS)
    }

    @Test
    void multiClients() {
        def u1 = 'user1', u2 = 'user2'
        def client1 = StompClient.connect(u1, url)
        def client2 = StompClient.connect(u2, url)

        CountDownLatch latch = new CountDownLatch(2)
        def broadcast = '/topic/test.str'
        def msg = 'Do NOT response! Do NOT response! Do NOT response!'
        def handler = new StompFrameStringHandler() {
            @Override
            void handleFrame(StompHeaders headers, @Nullable Object payload) {
                if (payload.endsWith(msg)) {
                    latch.countDown()
                }
            }
        }
        client1.subscribe(broadcast, handler)
        client2.subscribe(broadcast, handler)
        StompMessageSendUtil.send('/topic/test.str', msg)
//        client1.send('/topic/test.str', msg)
        assert latch.await(1, TimeUnit.SECONDS)

        client1.disconnect()
        client2.disconnect()
    }

    @Test
    void unsubscribe() {
        def stompUser = 'unsub'
        def topic = '/topic/test.str'
        def des = '/app/test.str'
        def msg = 'could not receive msg after unsubscribe'
        def client = StompClient.connect(stompUser, url)

        CountDownLatch latch = new CountDownLatch(2)
        client.subscribe(topic, new StompFrameStringHandler() {
            @Override
            void handleFrame(StompHeaders headers, @Nullable Object payload) {
                if (payload.endsWith(msg)) {
                    latch.countDown()
                }
            }
        })
        client.send(des, msg)
        assert !latch.await(200, TimeUnit.MILLISECONDS)
        client.unsubscribe(topic)
        client.send(des, msg)
        assert !latch.await(100, TimeUnit.MILLISECONDS)
        assert latch.getCount() == 1

        client.disconnect()
    }

}
