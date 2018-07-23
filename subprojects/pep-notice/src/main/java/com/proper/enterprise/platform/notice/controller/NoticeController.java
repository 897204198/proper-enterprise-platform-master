package com.proper.enterprise.platform.notice.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/notice")
public class NoticeController extends BaseController {

    @Autowired
    NoticeService noticeService;

    @GetMapping("/{noticeChannelName}")
    public ResponseEntity findNoticesByNoticeChannelName(@PathVariable String noticeChannelName) {
        return responseOfGet(noticeService.findByNoticeChannelName(noticeChannelName));
    }

}
