package com.proper.enterprise.platform.websocket.test.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("testServiceSend")
public class TestServiceSendController extends BaseController {

    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping("/test/socket/sendAll")
    @ApiOperation("sendAll")
    public ResponseEntity<String> sendAll() {
        template.convertAndSend("/topic/all", "all");
        return responseOfGet(null);
    }

    @GetMapping("/test/socket/send/{userId}")
    @ApiOperation("send")
    public ResponseEntity<String> sendTo(@PathVariable String userId) {
        template.convertAndSendToUser(userId, "/topic/single", "single");
        return responseOfGet(null);
    }

}
