package com.proper.enterprise.platform.notice.server.app.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.service.NoticeDaoService;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/notice/server/msg")
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
    public ResponseEntity<?> get(String id,
                                 String appKey,
                                 String batchId,
                                 String targetTo,
                                 String content,
                                 NoticeType noticeType,
                                 NoticeStatus status) {
        return isPageSearch() ? responseOfGet(noticeDaoService.findAll(id, appKey, batchId,
            targetTo, content, noticeType, null, status, getPageRequest()))
            : responseOfGet(noticeDaoService.findAll(id, appKey, batchId,
            targetTo, content, noticeType, null, status));
    }

    @GetMapping("/app")
    public ResponseEntity<?> getByClient(@RequestParam(required = false, name = "access_token") String accessToken,
                                         String batchId,
                                         String targetTo,
                                         NoticeType noticeType,
                                         String errorCode,
                                         NoticeStatus status,
                                         HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        String appKey = accessTokenService.getUserId(token).get();
        return isPageSearch() ? responseOfGet(noticeDaoService.findAll(null, appKey, batchId,
            targetTo, null, noticeType, errorCode, status, getPageRequest()))
            : responseOfGet(noticeDaoService.findAll(null, appKey, batchId,
            targetTo, null, noticeType, errorCode, status));
    }
}
