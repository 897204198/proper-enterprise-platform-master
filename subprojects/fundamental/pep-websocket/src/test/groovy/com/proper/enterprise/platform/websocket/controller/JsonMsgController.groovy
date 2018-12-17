package com.proper.enterprise.platform.websocket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
class JsonMsgController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonMsgController.class);

    @MessageMapping("/test/json")
    @SendTo("/topic/test/json")
    public Greeting handle(Hello greeting) {
        LOGGER.debug("Received {}", greeting.getName());
        return new Greeting("Hello " + greeting.getName());
    }

    static class Hello {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class Greeting {

        private String content;

        public Greeting() {
        }

        public Greeting(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

}
