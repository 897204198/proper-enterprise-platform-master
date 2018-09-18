package com.proper.enterprise.platform.notice.server.app.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.model.Notice;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.notice.server.api.sender.NoticeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/notice/server/send")
public class ApiNoticeSendResource extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiNoticeSendResource.class);

    private NoticeSender noticeSendService;

    private AccessTokenService accessTokenService;

    @Autowired
    public ApiNoticeSendResource(NoticeSender noticeSendService, @Qualifier("accessTokenService") AccessTokenService accessTokenService) {
        this.noticeSendService = noticeSendService;
        this.accessTokenService = accessTokenService;
    }

    @PostMapping
    public ResponseEntity sendNotice(@RequestParam(required = false, name = "access_token") String accessToken,
                                     @RequestBody NoticeRequest noticeRequest,
                                     HttpServletRequest request) throws NoticeException {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        LOGGER.info("Receive client messages batchId:{}", noticeRequest.getBatchId());
        List<Notice> notices = noticeSendService
            .beforeSend(accessTokenService.getUserId(token).get(), noticeRequest);
        LOGGER.info("client messages check batchId:{}", noticeRequest.getBatchId());
        noticeSendService.sendAsync(notices);
        return responseOfPost(null);
    }

}
