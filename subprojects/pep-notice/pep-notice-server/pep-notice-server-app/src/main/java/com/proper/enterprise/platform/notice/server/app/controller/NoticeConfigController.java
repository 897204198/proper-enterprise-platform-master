package com.proper.enterprise.platform.notice.server.app.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.factory.NoticeConfiguratorFactory;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(path = "/notice/server/config")
public class NoticeConfigController extends BaseController {

    private AccessTokenService accessTokenService;

    @Autowired
    public NoticeConfigController(@Qualifier("accessTokenService") AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @PostMapping("/{noticeType}")
    public ResponseEntity post(@PathVariable NoticeType noticeType,
                               @RequestParam(required = false, name = "access_token") String accessToken,
                               @RequestBody Map config, HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfPost(NoticeConfiguratorFactory.product(noticeType)
            .post(accessTokenService.getUserId(token).get(), config, request));
    }

    @DeleteMapping("/{noticeType}")
    public ResponseEntity delete(@PathVariable NoticeType noticeType,
                                 @RequestParam(required = false, name = "access_token") String accessToken,
                                 HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        NoticeConfiguratorFactory.product(noticeType).delete(accessTokenService.getUserId(token).get(), request);
        return responseOfDelete(true);
    }

    @PutMapping("/{noticeType}")
    public ResponseEntity put(@PathVariable NoticeType noticeType,
                              @RequestParam(required = false, name = "access_token") String accessToken,
                              @RequestBody Map config, HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfPut(NoticeConfiguratorFactory.product(noticeType).put(accessTokenService
            .getUserId(token).get(), config, request));
    }

    @GetMapping("/{noticeType}")
    public ResponseEntity get(@PathVariable NoticeType noticeType,
                              @RequestParam(required = false, name = "access_token") String accessToken,
                              HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfGet(NoticeConfiguratorFactory.product(noticeType).get(accessTokenService
            .getUserId(token).get(), request));
    }
}
