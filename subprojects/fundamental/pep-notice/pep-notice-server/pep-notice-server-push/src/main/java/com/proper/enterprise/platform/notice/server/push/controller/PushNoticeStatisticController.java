package com.proper.enterprise.platform.notice.server.push.controller;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgStatisticService;
import com.proper.enterprise.platform.notice.server.push.enums.PushDataAnalysisDateRangeEnum;
import com.proper.enterprise.platform.notice.server.push.vo.PushMsgPieDataVO;
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgPieVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 推送消息统计
 *
 * @author guozhimin
 */
@RestController
@RequestMapping("/notice/server/push/statistic")
public class PushNoticeStatisticController extends BaseController {

    private PushNoticeMsgStatisticService pushMsgStatisticService;

    private CoreProperties coreProperties;

    @Autowired
    public PushNoticeStatisticController(PushNoticeMsgStatisticService pushMsgStatisticService,
                                         CoreProperties coreProperties) {
        this.pushMsgStatisticService = pushMsgStatisticService;
        this.coreProperties = coreProperties;
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
        pushMsgStatisticService.saveStatisticSomeday(DateUtil.toString(date, coreProperties.getDefaultDateFormat()));
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/pieDataAnalysis")
    public ResponseEntity<PushNoticeMsgPieVO> getPieDataAnalysis(String startDate, String endDate, String appKey) {
        return new ResponseEntity<>(pushMsgStatisticService.findPieDataByDateAndAppKey(startDate, endDate, appKey), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/pieDataItems")
    public ResponseEntity<List<PushMsgPieDataVO>> getPieDataItems(@RequestParam("startDate") String startDate,
                                                                  @RequestParam("endDate") String endDate) {
        return new ResponseEntity<>(pushMsgStatisticService.findPieItems(startDate, endDate), HttpStatus.OK);
    }
}
