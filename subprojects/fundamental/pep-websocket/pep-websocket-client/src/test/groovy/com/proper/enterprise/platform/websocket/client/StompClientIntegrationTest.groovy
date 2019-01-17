package com.proper.enterprise.platform.websocket.client

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.auth.common.vo.AccessTokenVO
import com.proper.enterprise.platform.test.AbstractIntegrationTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.stomp.StompSession

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
    public void testClient() {
        CountDownLatch countDownLatch = new CountDownLatch(1)
        TestBaseStompSessionAdapter sessionAdapter = new TestBaseStompSessionAdapter(countDownLatch)
        StompClient stompClient = new StompClient(url, new StompSubscribe("/topic/test/str", sessionAdapter))
        stompClient.connect()
        StompSession session = stompClient.getStompSession()
        session.send("/app/test/str", "test")
        assert countDownLatch.await(1, TimeUnit.SECONDS)
        //动态增加订阅
        CountDownLatch countDownLatch2 = new CountDownLatch(1)
        TestBaseStompSessionAdapter sessionAdapter2 = new TestBaseStompSessionAdapter(countDownLatch2)
        stompClient.addSubscribe(new StompSubscribe("/topic/test/str/add", sessionAdapter2))
        session.send("/app/test/str/add", "test")
        assert countDownLatch2.await(1, TimeUnit.SECONDS)
    }
}
