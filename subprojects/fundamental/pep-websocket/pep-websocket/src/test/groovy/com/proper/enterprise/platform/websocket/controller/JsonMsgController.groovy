package com.proper.enterprise.platform.websocket.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class JsonMsgController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonMsgController.class)

    @MessageMapping("test.json")
    @SendTo("/topic/test.json")
    Greeting handle(Hello greeting) {
        LOGGER.debug("Received {}", greeting.getName())
        return new Greeting("Hello " + greeting.getName())
    }

    static class Hello {

        private String name

        String getName() {
            return name
        }

        void setName(String name) {
            this.name = name
        }

    }

    static class Greeting {

        private String content

        Greeting() { }

        Greeting(String content) {
            this.content = content
        }

        String getContent() {
            return content
        }

        void setContent(String content) {
            this.content = content
        }

    }

}
