package com.proper.enterprise.platform.push.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.push.service.PushMsgStatisticService;
import com.proper.enterprise.platform.push.vo.PushMsgStatisticVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 推送消息统计
 *
 * @author guozhimin
 */
@RestController
@AuthcIgnore
@RequestMapping("/push")
public class PushStatisticController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushStatisticController.class);
    @Autowired
    PushMsgStatisticService pushMsgStatisticService;

    @GetMapping
    @JsonView(PushMsgStatisticVO.Single.class)
    @RequestMapping("/statistic")
    public ResponseEntity<?> get(String dateType, String appkey) {
        return new ResponseEntity<>(pushMsgStatisticService.findByDateTypeAndAppkey(dateType, appkey), null, HttpStatus.OK);
    }
}
