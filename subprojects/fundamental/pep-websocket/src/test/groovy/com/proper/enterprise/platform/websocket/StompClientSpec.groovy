package com.proper.enterprise.platform.websocket

import com.proper.enterprise.platform.websocket.controller.JsonMsgController
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.lang.Nullable
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.converter.StringMessageConverter
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.client.WebSocketClient
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.Transport
import org.springframework.web.socket.sockjs.client.WebSocketTransport

import java.lang.reflect.Type
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class StompClientSpec {

    private static final Logger LOGGER = LoggerFactory.getLogger(StompClientSpec.class)

    static void main(args) {
        strMsg(getClient(new StringMessageConverter()))
        jsonMsg(getClient(new MappingJackson2MessageConverter()))
    }

    static def getClient(msgConverter) {
        List<Transport> transports = new ArrayList<>()
        transports.add(new WebSocketTransport(new StandardWebSocketClient()))
        WebSocketClient webSocketClient = new SockJsClient(transports)
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient)
        stompClient.setMessageConverter(msgConverter)
        stompClient
    }

    static def strMsg(stompClient) {
        CountDownLatch latch = new CountDownLatch(1)

        def url = 'ws://localhost:8080/pep/stomp'
        stompClient.connect(url, new StompSessionHandlerAdapter() {
            @Override
            void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                session.subscribe('/topic/test/str', new StompFrameHandler() {
                    @Override
                    Type getPayloadType(StompHeaders headers) {
                        return String.class
                    }

                    @Override
                    void handleFrame(StompHeaders headers, @Nullable Object payload) {
                        LOGGER.debug("Received payload {} at {}", payload, LocalDateTime.now())
                        println "Received payload $payload at ${LocalDateTime.now()}"
                        latch.countDown()
                    }
                })
                session.send('/app/test/str', 'hinex')
            }
        })

        latch.await(1, TimeUnit.SECONDS)
    }

    static def jsonMsg(stompClient) {
        final CountDownLatch latch = new CountDownLatch(3)

        def url = 'ws://localhost:8080/pep/stomp'
        stompClient.connect(url, new StompSessionHandlerAdapter() {
            @Override
            void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                session.subscribe('/topic/test/json', new StompFrameHandler() {
                    @Override
                    Type getPayloadType(StompHeaders headers) {
                        return JsonMsgController.Greeting.class
                    }

                    @Override
                    void handleFrame(StompHeaders headers, @Nullable Object payload) {
                        LOGGER.debug("Received payload {} at {}", payload, LocalDateTime.now())
                        println "Received " + ((JsonMsgController.Greeting)payload).getContent()
                        latch.countDown()
                    }
                })
                sendMessage(session, "Springxxx", latch)
                sendMessage(session, "Spring", latch)
                sendMessage(session, "Springzzzz", latch)
            }
        })

        latch.await(3, TimeUnit.SECONDS)
    }

    static def sendMessage(StompSession session, String msg, CountDownLatch latch) {
        try {
            session.send("/app/test/json", [name: msg])
        } catch (Throwable t) {
            t.printStackTrace()
            latch.countDown()
        }
    }

}
