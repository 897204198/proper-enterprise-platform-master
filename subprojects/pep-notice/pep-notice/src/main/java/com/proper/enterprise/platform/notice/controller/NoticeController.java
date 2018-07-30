package com.proper.enterprise.platform.notice.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.model.NoticeModel;
import com.proper.enterprise.platform.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/notice")
public class NoticeController extends BaseController {

    @Autowired
    NoticeService noticeService;

    @AuthcIgnore
    @PostMapping
    public ResponseEntity<Boolean> sendNotice(@RequestBody NoticeModel noticeModel) {
        return responseOfPost(noticeService.sendNotice(noticeModel));
    }

    @GetMapping("/{noticeChannelName}")
    public ResponseEntity<List<NoticeDocument>> findNoticesByNoticeChannelName(@PathVariable String noticeChannelName) {
        return responseOfGet(noticeService.findByNoticeChannel(noticeChannelName));
    }
}
