package com.proper.enterprise.platform.notice.server.push.controller;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.notice.server.api.model.App;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgStatisticService;
import com.proper.enterprise.platform.notice.server.push.enums.PushDataAnalysisDateRangeEnum;
import com.proper.enterprise.platform.notice.server.push.vo.PushMsgPieDataVO;
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgPieVO;
import com.proper.enterprise.platform.notice.server.push.vo.PushServiceDataAnalysisVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "/notice/server/push/statistic")
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
    @ApiOperation("‍根据基础日期获取推送统计数据")
    @RequestMapping("/dataAnalysis/{dateType}")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", paramType = "query", dataType = "int")
    })
    public ResponseEntity<?> get(@ApiParam(value = "‍推送数据分析枚举", required = true) @PathVariable PushDataAnalysisDateRangeEnum dateType,
                                 @ApiParam(value = "‍应用唯一标识", required = true) String appKey) {
        List<PushServiceDataAnalysisVO> dataAnalysisVOS = pushMsgStatisticService.findByDateTypeAndAppKey(new Date(), dateType, appKey);
        DataTrunk<PushServiceDataAnalysisVO> dataTrunk = new DataTrunk<>();
        dataTrunk.setCount(dataAnalysisVOS.size());
        dataTrunk.setData(dataAnalysisVOS);
        return new ResponseEntity<>(dataTrunk, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation("‍统计今天的推送数据")
    @RequestMapping("/init")
    public ResponseEntity<?> init() {
        pushMsgStatisticService.saveStatisticSomeday(DateUtil.toString(LocalDateTime.now(), coreProperties.getDefaultDateFormat()));
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "‍根据日期获取饼图数据", httpMethod = "GET")
    @RequestMapping("/pieDataAnalysis")
    public ResponseEntity<PushNoticeMsgPieVO> getPieDataAnalysis(String startDate, String endDate, String appKey) {
        return new ResponseEntity<>(pushMsgStatisticService.findPieDataByDateAndAppKey(startDate, endDate, appKey), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/pieDataItems")
    @ApiOperation("‍获取饼图左侧项目相关数据")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<PushMsgPieDataVO>> getPieDataItems(@ApiParam(value = "‍开始时间", required = true)
                                                                       @RequestParam("startDate") String startDate,
                                                                       @ApiParam(value = "‍结束时间", required = true)
                                                                       @RequestParam("endDate") String endDate) {
        List<PushMsgPieDataVO> pieItems = pushMsgStatisticService.findPieItems(startDate, endDate);
        DataTrunk<PushMsgPieDataVO> dataTrunk = new DataTrunk<>();
        dataTrunk.setData(pieItems);
        dataTrunk.setCount(pieItems.size());
        return new ResponseEntity<>(dataTrunk, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/app")
    @ApiOperation("‍分页查询app")
    public ResponseEntity<DataTrunk<App>> findApp(@ApiParam("‍应用唯一标识‍") String appKey, @ApiParam("‍应用名称‍")String appName,
                                                  @ApiParam("‍应用描述‍")String appDesc, @ApiParam("‍启用停用‍")Boolean enable) {
        return responseOfGet(pushMsgStatisticService.findApp(appKey, appName,
            appDesc, enable, getPageRequest(new Sort(Sort.Direction.DESC, "createTime"))));
    }

}
