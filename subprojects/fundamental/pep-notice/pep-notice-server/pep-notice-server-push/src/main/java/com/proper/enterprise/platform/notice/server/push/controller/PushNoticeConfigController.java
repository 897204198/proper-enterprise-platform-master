package com.proper.enterprise.platform.notice.server.push.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeConfigService;
import com.proper.enterprise.platform.notice.server.push.vo.PushConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/notice/server/push/config")
public class PushNoticeConfigController extends BaseController {

    private PushNoticeConfigService pushNoticeConfigService;

    private AccessTokenService accessTokenService;

    @Autowired
    public PushNoticeConfigController(PushNoticeConfigService pushNoticeConfigService,
                                      @Qualifier("accessTokenService") AccessTokenService accessTokenService) {
        this.pushNoticeConfigService = pushNoticeConfigService;
        this.accessTokenService = accessTokenService;
    }

    @PostMapping(value = "/{appKey}")
    public ResponseEntity post(@PathVariable String appKey, @RequestBody PushConfigVO pushConfigVO) {
        pushNoticeConfigService.save(appKey, pushConfigVO);
        return responseOfPost(null);
    }

    @PostMapping
    public ResponseEntity post(@RequestParam(required = false, name = "access_token") String accessToken,
                               @RequestBody PushConfigVO pushConfigVO,
                               HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        pushNoticeConfigService.save(accessTokenService
            .getUserId(token).get(), pushConfigVO);
        return responseOfPost(null);
    }

    @DeleteMapping(value = "/{appKey}")
    public ResponseEntity delete(@PathVariable String appKey) {
        pushNoticeConfigService.delete(appKey);
        return responseOfDelete(true);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam(required = false, name = "access_token") String accessToken,
                                 HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        pushNoticeConfigService.delete(accessTokenService
            .getUserId(token).get());
        return responseOfDelete(true);
    }

    @PutMapping(value = "/{appKey}")
    public ResponseEntity put(@PathVariable String appKey, @RequestBody PushConfigVO pushConfigVO) {
        pushNoticeConfigService.update(appKey, pushConfigVO);
        return responseOfPost(null);
    }

    @PutMapping
    public ResponseEntity put(@RequestParam(required = false, name = "access_token") String accessToken,
                              @RequestBody PushConfigVO pushConfigVO,
                              HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        pushNoticeConfigService.update(accessTokenService
            .getUserId(token).get(), pushConfigVO);
        return responseOfPost(null);
    }

    @GetMapping(value = "/{appKey}")
    public ResponseEntity<PushConfigVO> get(@PathVariable String appKey) {
        return responseOfGet(pushNoticeConfigService.get(appKey));
    }

    @GetMapping
    public ResponseEntity get(@RequestParam(required = false, name = "access_token") String accessToken,
                              HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfGet(pushNoticeConfigService.get(accessTokenService
            .getUserId(token).get()));
    }

}
