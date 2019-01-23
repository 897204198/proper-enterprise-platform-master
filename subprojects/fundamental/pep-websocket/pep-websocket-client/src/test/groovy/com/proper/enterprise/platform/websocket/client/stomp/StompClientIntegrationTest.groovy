package com.proper.enterprise.platform.websocket.client.stomp

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.auth.common.vo.AccessTokenVO
import com.proper.enterprise.platform.test.AbstractIntegrationTest
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
        StompClient.connect(stompUser, url)

        subscribeAndSendMsgCheck(stompUser, '/topic/test/str', '/app/test/str', 'test1')

        //动态增加订阅
        subscribeAndSendMsgCheck(stompUser, '/topic/test/str/add', '/app/test/str/add', 'test2')
    }

    private static void subscribeAndSendMsgCheck(String stompUser, String topic, String sendDes, String sendPayload) {
        CountDownLatch latch = new CountDownLatch(1)
        StompClient.subscribe(stompUser, topic, new StompFrameStringHandler() {
            @Override
            void handleFrame(StompHeaders headers, @Nullable Object payload) {
                if (payload.endsWith(sendPayload)) {
                    latch.countDown()
                }
            }
        })
        StompClient.send(stompUser, sendDes, sendPayload)
        assert latch.await(1, TimeUnit.SECONDS)
    }

    @Test
    void multiClients() {
        def u1 = 'user1', u2 = 'user2'
        StompClient.connect(u1, url)
        StompClient.connect(u2, url)

        CountDownLatch latch = new CountDownLatch(2)
        def broadcast = '/topic/test/str'
        def msg = 'Do NOT response! Do NOT response! Do NOT response!'
        def handler = new StompFrameStringHandler() {
            @Override
            void handleFrame(StompHeaders headers, @Nullable Object payload) {
                if (payload.endsWith(msg)) {
                    latch.countDown()
                }
            }
        }
        StompClient.subscribe(u1, broadcast, handler)
        StompClient.subscribe(u2, broadcast, handler)
        StompClient.send(u1, '/app/test/str', msg)
        assert latch.await(1, TimeUnit.SECONDS)
    }

    @Test
    void unsubscribe() {
        def stompUser = 'unsub'
        def topic = '/topic/test/str'
        def des = '/app/test/str'
        def msg = 'could not receive msg after unsubscribe'
        StompClient.connect(stompUser, url)

        CountDownLatch latch = new CountDownLatch(2)
        StompClient.subscribe(stompUser, topic, new StompFrameStringHandler() {
            @Override
            void handleFrame(StompHeaders headers, @Nullable Object payload) {
                if (payload.endsWith(msg)) {
                    latch.countDown()
                }
            }
        })
        StompClient.send(stompUser, des, msg)
        assert !latch.await(200, TimeUnit.MILLISECONDS)
        StompClient.unsubscribe(stompUser, topic)
        StompClient.send(stompUser, des, msg)
        assert !latch.await(100, TimeUnit.MILLISECONDS)
        assert latch.getCount() == 1
    }

}
