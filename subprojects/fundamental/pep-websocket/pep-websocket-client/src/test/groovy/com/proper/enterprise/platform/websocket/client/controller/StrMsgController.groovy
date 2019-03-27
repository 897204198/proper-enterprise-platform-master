package com.proper.enterprise.platform.websocket.client.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

import java.time.LocalDateTime

@Controller
class StrMsgController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StrMsgController.class)

    @MessageMapping("test.str")
    @SendTo("/topic/test.str")
    String handle(String greeting) {
        LOGGER.debug("Received {}", greeting)
        return "[" + LocalDateTime.now().toString() + ": " + greeting
    }

    @MessageMapping("test.str.add")
    @SendTo("/topic/test.str.add")
    String addhandle(String greeting) {
        LOGGER.debug("Received {}", greeting)
        return "[" + LocalDateTime.now().toString() + ": " + greeting
    }
}
