package com.proper.enterprise.platform.notice.server.app.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.model.Notice;
import com.proper.enterprise.platform.notice.server.api.service.NoticeDaoService;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/notice/server/msg")
@Api(tags = "/notice/server/msg")
public class NoticeMsgController extends BaseController {

    private NoticeDaoService noticeDaoService;

    private AccessTokenService accessTokenService;

    @Autowired
    public NoticeMsgController(NoticeDaoService noticeDaoService,
                               @Qualifier("accessTokenService") AccessTokenService accessTokenService) {
        this.noticeDaoService = noticeDaoService;
        this.accessTokenService = accessTokenService;
    }


    @GetMapping
    @ApiOperation("‍根据条件查询消息")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", paramType = "query", dataType = "int")
    })
    public ResponseEntity<?> get(@ApiParam(value = "‍消息id") String id,
                                 @ApiParam(value = "‍应用唯一标识") String appKey,
                                 @ApiParam(value = "‍批次号") String batchId,
                                 @ApiParam(value = "‍发送目标") String targetTo,
                                 @ApiParam(value = "‍消息内容") String content,
                                 @ApiParam(value = "‍消息类型") NoticeType noticeType,
                                 @ApiParam(value = "‍消息状态") NoticeStatus status) {

        if (isPageSearch()) {
            return responseOfGet(noticeDaoService.findAll(id, appKey, batchId,
                targetTo, content, noticeType, null, status, getPageRequest()));
        } else {
            List<Notice> notices = noticeDaoService.findAll(id, appKey, batchId, targetTo, content, noticeType, null, status);
            DataTrunk dataTrunk = new DataTrunk();
            dataTrunk.setData(notices);
            dataTrunk.setCount(notices.size());
            return responseOfGet(dataTrunk);
        }
    }

    @GetMapping("/app")
    @ApiOperation("‍‍根据条件查询消息")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", paramType = "query", dataType = "int")
    })
    public ResponseEntity<?> getByClient(@ApiParam(value = "‍‍token") @RequestParam(required = false, name = "access_token") String accessToken,
                                         @ApiParam(value = "‍批次号") String batchId,
                                         @ApiParam(value = "‍发送目标") String targetTo,
                                         @ApiParam(value = "‍消息类型") NoticeType noticeType,
                                         @ApiParam(value = "‍异常编码") String errorCode,
                                         @ApiParam(value = "‍消息状态") NoticeStatus status,
                                         HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        String appKey = accessTokenService.getUserId(token).get();
        if (isPageSearch()) {
            return responseOfGet(noticeDaoService.findAll(null, appKey, batchId,
                targetTo, null, noticeType, errorCode, status, getPageRequest()));
        } else {
            List<Notice> notices = noticeDaoService.findAll(null, appKey, batchId, targetTo, null, noticeType, errorCode, status);
            DataTrunk dataTrunk = new DataTrunk();
            dataTrunk.setData(notices);
            dataTrunk.setCount(notices.size());
            return responseOfGet(dataTrunk);
        }
    }
}
