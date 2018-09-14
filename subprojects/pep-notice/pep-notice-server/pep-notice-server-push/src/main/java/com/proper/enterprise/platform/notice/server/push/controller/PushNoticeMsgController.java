package com.proper.enterprise.platform.notice.server.push.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeMsgService;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgVO;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 推送消息统计
 *
 * @author guozhimin
 */
@RestController
@RequestMapping("/notice/server/push")
public class PushNoticeMsgController extends BaseController {

    private PushNoticeMsgService pushNoticeMsgService;

    @Autowired
    public PushNoticeMsgController(PushNoticeMsgService pushNoticeMsgService) {
        this.pushNoticeMsgService = pushNoticeMsgService;
    }

    @GetMapping
    @RequestMapping
    public ResponseEntity<DataTrunk<PushNoticeMsgVO>> get(String content, NoticeStatus status,
                                                          String appKey, PushChannelEnum pushChannel) {
        return new ResponseEntity<>(pushNoticeMsgService.findPagination(content, status, appKey, pushChannel,
            getPageRequest(new Sort(Sort.Direction.DESC, "createTime"))), HttpStatus.OK);
    }

}
