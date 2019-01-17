package com.proper.enterprise.platform.websocket.test.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Api("sendAndReceive")
public class SendAndReceiveController {


    @MessageMapping("/topic/sendAndReceive")
    @SendTo("/topic/sendAndReceive")
    @ApiOperation("sendAndReceive")
    public String sendAndReceive(String message) throws Exception {
        Thread.sleep(1000);
        return "sendAndReceive:" + message;
    }
}
