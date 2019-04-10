package com.proper.enterprise.platform.websocket.client

import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class WebSocketClientSpec extends Specification {

    def clientId = 'wscs'
    def ws = 'ws://echo.websocket.org'

    def echo() {
        CountDownLatch latch = new CountDownLatch(1)
        def client = WebSocketClient.connect(clientId, ws, new WebSocketClientEndpoint.EventHandler() {
            @Override
            void onMessage(String message) {
                println "Echo $message"
                latch.countDown()
            }
        })
        client.send('Hello')

        expect:
        latch.await(1, TimeUnit.SECONDS)
        client.disconnect()
        WebSocketClient.getInstance(clientId) == null
    }

}
