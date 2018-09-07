package com.proper.enterprise.platform.notice.server.app.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.model.Notice;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.notice.server.api.sender.NoticeSender;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/notice/server/send")
public class ApiNoticeSendResource extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiNoticeSendResource.class);

    private NoticeSender noticeSendService;

    private AccessTokenService accessTokenService;

    @Autowired
    public ApiNoticeSendResource(NoticeSender noticeSendService, AccessTokenService accessTokenService) {
        this.noticeSendService = noticeSendService;
        this.accessTokenService = accessTokenService;
    }

    @PostMapping
    public ResponseEntity sendNotice(@RequestParam String accessToken, @RequestBody NoticeRequest noticeRequest) throws NoticeException {
        if (validAppKeyIsNull(accessToken)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        if (null == noticeRequest) {
            throw new ErrMsgException(I18NUtil.getMessage("notice.server.param.request.cantBeEmpty"));
        }
        LOGGER.info("Receive client messages batchId:{}", noticeRequest.getBatchId());
        List<Notice> notices = noticeSendService
            .beforeSend(accessTokenService.getUserId(accessToken).get(), noticeRequest);
        LOGGER.info("client messages check batchId:{}", noticeRequest.getBatchId());
        noticeSendService.sendAsync(notices);
        return responseOfPost(null);
    }

    private boolean validAppKeyIsNull(String accessToken) {
        Optional<String> appKey = accessTokenService.getUserId(accessToken);
        return !appKey.isPresent();
    }

}
