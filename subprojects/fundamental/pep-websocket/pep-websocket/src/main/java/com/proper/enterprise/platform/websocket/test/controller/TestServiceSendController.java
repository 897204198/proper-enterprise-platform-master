package com.proper.enterprise.platform.websocket.test.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api("testServiceSend")
public class TestServiceSendController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestServiceSendController.class);

    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping("/test/socket/sendAll")
    @ApiOperation("sendAll")
    public String sendAll(String payload, HttpServletRequest request) {
        LOGGER.info("Received {}", request.getRequestURI());
        template.convertAndSend("/topic/all", StringUtil.isBlank(payload) ? "all" : payload);
        return "Broadcast at " + DateUtil.getTimestamp();
    }

    @GetMapping("/test/socket/send/{userId}")
    @ApiOperation("send")
    public ResponseEntity<String> sendTo(@PathVariable String userId) {
        template.convertAndSendToUser(userId, "/topic/single", "single");
        return responseOfGet(null);
    }

}
