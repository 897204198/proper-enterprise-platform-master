package com.proper.enterprise.platform.notice.server.push.controller;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.notice.server.api.model.App;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgStatisticService;
import com.proper.enterprise.platform.notice.server.push.enums.PushDataAnalysisDateRangeEnum;
import com.proper.enterprise.platform.notice.server.push.vo.PushMsgPieDataVO;
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgPieVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    @GetMapping
    @RequestMapping("/app")
    public ResponseEntity<DataTrunk<App>> findApp(String appKey, String appName, String appDesc, Boolean enable) {
        return responseOfGet(pushMsgStatisticService.findApp(appKey, appName,
            appDesc, enable, getPageRequest(new Sort(Sort.Direction.DESC, "createTime"))));
    }
}
