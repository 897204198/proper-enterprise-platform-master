package com.proper.enterprise.platform.push.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.push.api.openapi.service.PushChannelService;
import com.proper.enterprise.platform.push.vo.PushChannelVO;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/push/channels")
@Api(tags = "/push/channels")
public class PushChannelController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushChannelController.class);

    private PushChannelService pushChannelService;

    @Autowired
    public PushChannelController(PushChannelService pushChannelService) {
        this.pushChannelService = pushChannelService;
    }


    @PostMapping
    @ApiOperation("‍增加推送渠道")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PushChannelVO> add(@RequestBody PushChannelVO pushChannelVO) {
        LOGGER.info(pushChannelVO.toString());
        return responseOfPost(pushChannelService.addChannel(pushChannelVO));
    }

    @PutMapping
    @ApiOperation("‍更新推送渠道")
    public ResponseEntity<PushChannelVO> update(@RequestBody PushChannelVO pushChannelVO) {
        LOGGER.info(pushChannelVO.toString());
        return responseOfPut(pushChannelService.updateChannel(pushChannelVO));
    }

    @DeleteMapping
    @ApiOperation("‍删除推送渠道")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@ApiParam(value = "‍信息Id列表(使用\",\"分割)", required = true) @RequestParam String ids) {
        return responseOfDelete(pushChannelService.deleteChannel(ids));
    }

    @GetMapping
    @ApiOperation("‍推送渠道列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<PushChannelVO>> get() {
        return responseOfGet(
            pushChannelService.findAll());
    }

    @GetMapping("/enable")
    @ApiOperation("‍推送渠道列表")
    public ResponseEntity<DataTrunk<PushChannelVO>> getEnable(@ApiParam("‍‍开始时间") String startDate, @ApiParam("‍‍结束时间") String endDate) {
        return responseOfGet(pushChannelService.findByEnable(startDate, endDate));
    }

}
