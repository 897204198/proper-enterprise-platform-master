package com.proper.enterprise.platform.notice.server.app.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.notice.server.api.service.NoticeDaoService;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice/server")
public class NoticeServerController extends BaseController {

    private NoticeDaoService noticeDaoService;

    @Autowired
    public NoticeServerController(NoticeDaoService noticeDaoService) {
        this.noticeDaoService = noticeDaoService;
    }


    @GetMapping
    public ResponseEntity<?> get(String id,
                                 String appKey,
                                 String batchId,
                                 String targetTo,
                                 String content,
                                 NoticeType noticeType,
                                 NoticeStatus status) {
        return isPageSearch() ? responseOfGet(noticeDaoService.findAll(id, appKey, batchId,
            targetTo, content, noticeType, status, getPageRequest()))
            : responseOfGet(noticeDaoService.findAll(id, appKey, batchId,
            targetTo, content, noticeType, status));
    }
}
