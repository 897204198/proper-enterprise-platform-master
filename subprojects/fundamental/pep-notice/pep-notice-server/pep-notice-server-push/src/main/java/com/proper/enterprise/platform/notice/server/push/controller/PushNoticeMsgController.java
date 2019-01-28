package com.proper.enterprise.platform.notice.server.push.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgService;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgVO;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 推送消息统计
 */
@RestController
@Api(tags = "/notice/server/push")
@RequestMapping("/notice/server/push")
public class PushNoticeMsgController extends BaseController {

    private PushNoticeMsgService pushNoticeMsgService;

    @Autowired
    public PushNoticeMsgController(PushNoticeMsgService pushNoticeMsgService) {
        this.pushNoticeMsgService = pushNoticeMsgService;
    }

    @GetMapping
    @RequestMapping
    @ApiOperation(value = "‍分页查询推送消息", httpMethod = "GET")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<PushNoticeMsgVO>> get(@ApiParam(value = "‍消息内容") String content,
                                                          @ApiParam(value = "‍消息状态") NoticeStatus status,
                                                          @ApiParam(value = "‍应用唯一标识") String appKey,
                                                          @ApiParam(value = "‍推送渠道") PushChannelEnum pushChannel) {
        return new ResponseEntity<>(pushNoticeMsgService.findPagination(content, status, appKey, pushChannel,
            getPageRequest(new Sort(Sort.Direction.DESC, "createTime"))), HttpStatus.OK);
    }

}
