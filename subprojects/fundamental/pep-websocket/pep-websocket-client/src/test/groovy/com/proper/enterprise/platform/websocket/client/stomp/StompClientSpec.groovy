package com.proper.enterprise.platform.websocket.client.stomp

import org.springframework.lang.Nullable
import org.springframework.messaging.simp.stomp.StompHeaders
import spock.lang.Ignore
import spock.lang.Specification

@Ignore
class StompClientSpec extends Specification {

    def url = 'ws://localhost:8080/websocket-server/stomp'
    def clientId = 'rec'
    def topic = '/topic/all'

    def 'connect'() {
        StompClient.connect(clientId, url)
        StompClient.subscribe(clientId, topic, handler)

        sleep(10*60*1000)

        expect:
        1 == 1
    }

    def handler = new StompFrameStringHandler() {
        @Override
        void handleFrame(StompHeaders headers, @Nullable Object payload) {
            println payload
        }
    }

}
