package com.proper.enterprise.platform.notice.server.push.controller;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgStatisticService;
import com.proper.enterprise.platform.notice.server.push.enums.PushDataAnalysisDateRangeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 推送消息统计
 *
 * @author guozhimin
 */
@RestController
@RequestMapping("/notice/server/push/statistic")
public class PushNoticeStatisticController extends BaseController {

    private PushNoticeMsgStatisticService pushMsgStatisticService;

    @Autowired
    public PushNoticeStatisticController(PushNoticeMsgStatisticService pushMsgStatisticService) {
        this.pushMsgStatisticService = pushMsgStatisticService;
    }

    @GetMapping
    @RequestMapping("/dataAnalysis/{dateType}")
    public ResponseEntity<?> get(@PathVariable PushDataAnalysisDateRangeEnum dateType, String appKey) {
        return new ResponseEntity<>(pushMsgStatisticService.findByDateTypeAndAppKey(new Date(), dateType, appKey), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/init")
    public ResponseEntity<?> init() {
        Date date = new Date();
        pushMsgStatisticService.saveStatisticSomeday(DateUtil.toString(date, PEPConstants.DEFAULT_DATE_FORMAT));
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }

}
