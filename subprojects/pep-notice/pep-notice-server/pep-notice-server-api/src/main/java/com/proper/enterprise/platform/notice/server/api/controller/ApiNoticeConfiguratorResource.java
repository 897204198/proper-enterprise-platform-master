package com.proper.enterprise.platform.notice.server.api.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.notice.server.api.factory.NoticeConfiguratorFactory;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/notice/server/config")
public class ApiNoticeConfiguratorResource extends BaseController {


    private AccessTokenService accessTokenService;

    @Autowired
    public ApiNoticeConfiguratorResource(@Qualifier("accessTokenService") AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @PostMapping("/{noticeType}")
    public ResponseEntity post(@PathVariable NoticeType noticeType, @RequestParam String accessToken,
                               @RequestBody Map config, HttpServletRequest request) {
        if (validAppKeyIsNull(accessToken)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return responseOfPost(NoticeConfiguratorFactory.product(noticeType)
            .post(accessTokenService.getUserId(accessToken).get(), config, request));
    }

    @DeleteMapping("/{noticeType}")
    public ResponseEntity delete(@PathVariable NoticeType noticeType, @RequestParam String accessToken, HttpServletRequest request) {
        if (validAppKeyIsNull(accessToken)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        NoticeConfiguratorFactory.product(noticeType).delete(accessTokenService
            .getUserId(accessToken)
            .get(), request);
        return responseOfDelete(true);
    }

    @PutMapping("/{noticeType}")
    public ResponseEntity put(@PathVariable NoticeType noticeType, @RequestParam String accessToken,
                              @RequestBody Map config, HttpServletRequest request) {
        if (validAppKeyIsNull(accessToken)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return responseOfPut(NoticeConfiguratorFactory.product(noticeType).put(accessTokenService
            .getUserId(accessToken)
            .get(), config, request));
    }

    @GetMapping("/{noticeType}")
    public ResponseEntity get(@PathVariable NoticeType noticeType, @RequestParam String accessToken, HttpServletRequest request) {
        if (validAppKeyIsNull(accessToken)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return responseOfGet(NoticeConfiguratorFactory.product(noticeType).get(accessTokenService
            .getUserId(accessToken)
            .get(), request));
    }

    private boolean validAppKeyIsNull(String accessToken) {
        Optional<String> appKey = accessTokenService.getUserId(accessToken);
        return !appKey.isPresent();
    }

}
