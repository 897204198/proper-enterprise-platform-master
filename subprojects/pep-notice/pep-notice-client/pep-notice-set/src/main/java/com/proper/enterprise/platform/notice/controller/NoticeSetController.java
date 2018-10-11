package com.proper.enterprise.platform.notice.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice/set")
public class NoticeSetController extends BaseController {

    @Autowired
    NoticeSetService noticeSetService;

    @GetMapping
    public ResponseEntity<List<NoticeSetDocument>> find() {
        String userId = Authentication.getCurrentUserId();
        return responseOfGet(noticeSetService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<NoticeSetDocument> save(@RequestBody NoticeSetDocument noticeSetDocument) {
        return responseOfGet(noticeSetService.save(noticeSetDocument));
    }

}
