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
        def client = StompClient.connect(clientId, url)
        client.subscribe(topic, handler)

        sleep(10*60*1000)

        expect:
        1 == 1
        client.disconnect()
    }

    def handler = new StompFrameStringHandler() {
        @Override
        void handleFrame(StompHeaders headers, @Nullable Object payload) {
            println payload
        }
    }

}
