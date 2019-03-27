package com.proper.enterprise.platform.websocket.controller

import org.apache.commons.lang3.RandomStringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller

import java.time.LocalDateTime

@Controller
class StrMsgController {

    @Autowired
    SimpMessagingTemplate template

    private static final Logger LOGGER = LoggerFactory.getLogger(StrMsgController.class)

    @MessageMapping("test.str")
    @SendTo("/topic/test.str")
    String handle(String greeting) {
        LOGGER.debug("Received {}", greeting)
        return "[" + LocalDateTime.now().toString() + ": " + greeting
    }

    @Scheduled(fixedDelay = 100L)
    String broadcast() {
        template.convertAndSend('/topic/test.broadcast', RandomStringUtils.randomAlphanumeric(5))
    }

    @Scheduled(fixedDelay = 100L)
    String sendToUser() {
        template.convertAndSendToUser('single_u2', '/topic/test.single', RandomStringUtils.randomAlphanumeric(5))
    }

}
