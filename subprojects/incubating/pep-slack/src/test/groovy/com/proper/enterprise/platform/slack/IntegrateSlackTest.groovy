package com.proper.enterprise.platform.slack

import com.proper.enterprise.platform.core.utils.http.HttpClient
import com.proper.enterprise.platform.test.AbstractSpringTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.websocket.client.WebSocketClient
import com.proper.enterprise.platform.websocket.client.WebSocketClientEndpoint
import groovy.json.JsonSlurper
import org.junit.Ignore
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@Ignore
class IntegrateSlackTest extends AbstractSpringTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrateSlackTest.class)


    @Value('${pep.slack.token}')
    private String token

    def static final SLACK_API_POST_MSG = 'https://slack.com/api/chat.postMessage'
    def static final SLACK_API_RTM_CONNECT = 'https://slack.com/api/rtm.connect'
    def static final CHANNEL_TEST = 'C15C8EKPB'

    @Test
    void directMsg() {
        // https://api.slack.com/methods/chat.postMessage/test
        def resp = HttpClient.post(SLACK_API_POST_MSG,
                                   ['Authorization': "Bearer $token".toString()],
                                   MediaType.APPLICATION_JSON,
                                   JSONUtil.toJSON([channel: CHANNEL_TEST, text: 'why me?']))
                             .getBody()
        def body = new String(resp)
        println body
        assert body != ''
    }

    @Test
    void rtm() {
        def resp = HttpClient.get(SLACK_API_RTM_CONNECT,
                                  ['Authorization': "Bearer $token".toString(),
                                   'Content-Type': 'application/x-www-form-urlencoded'])
                             .getBody()
        def jsonObj = new JsonSlurper().parseText(new String(resp))
        String wss = ''
        if (jsonObj.ok) {
            wss = jsonObj.url
            println wss
        } else {
            println jsonObj
        }

        CountDownLatch latch = new CountDownLatch(1)
        def clientId = 'rtm-bot'
        def client = WebSocketClient.connect(clientId, wss, new WebSocketClientEndpoint.EventHandler() {
            @Override
            void onMessage(String message) {
                LOGGER.debug("message: {}", message)
                latch.countDown()
            }
        })

        // Send msg to slack will cause abnormally connection close, do NOT use ws client to send msg to slack
//        client.send("{'id':1, 'type':'message', 'channel':'$CHANNEL_TEST', 'text':'rtm msg'}")

        assert latch.await(5, TimeUnit.SECONDS)
        client.disconnect()
    }

}
