package com.proper.enterprise.platform.notice.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "/notice/set")
@RequestMapping("/notice/set")
public class NoticeSetController extends BaseController {

    @Autowired
    NoticeSetService noticeSetService;

    @GetMapping
    @ApiOperation("‍查询当前用户通知设置列表")
    public ResponseEntity<List<NoticeSetDocument>> find() {
        String userId = Authentication.getCurrentUserId();
        return responseOfGet(noticeSetService.findByUserId(userId));
    }

    @PostMapping
    @ApiOperation("‍保存通知设置")
    public ResponseEntity<NoticeSetDocument> save(@RequestBody NoticeSetDocument noticeSetDocument) {
        return responseOfGet(noticeSetService.save(noticeSetDocument));
    }

}
