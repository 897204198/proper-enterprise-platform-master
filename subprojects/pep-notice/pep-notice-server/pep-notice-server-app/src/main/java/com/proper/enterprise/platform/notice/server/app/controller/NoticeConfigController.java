package com.proper.enterprise.platform.notice.server.app.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.notice.server.api.factory.NoticeConfiguratorFactory;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/notice/server/config")
public class NoticeConfigController extends BaseController {

    private AccessTokenService accessTokenService;

    private FileService fileService;

    @Autowired
    public NoticeConfigController(@Qualifier("accessTokenService") AccessTokenService accessTokenService, FileService fileService) {
        this.accessTokenService = accessTokenService;
        this.fileService = fileService;
    }

    @PostMapping("/{noticeType}")
    public ResponseEntity post(@PathVariable NoticeType noticeType,
                               @RequestParam(required = false, name = "access_token") String accessToken,
                               @RequestBody Map config, HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfPost(NoticeConfiguratorFactory.product(noticeType)
            .post(accessTokenService.getUserId(token).get(), config, buildRequestMap(request)));
    }

    @PostMapping("/{noticeType}/{appKey}")
    public ResponseEntity postAppConfig(@PathVariable NoticeType noticeType,
                                        @PathVariable String appKey,
                                        @RequestBody Map config, HttpServletRequest request) {
        return responseOfPost(NoticeConfiguratorFactory.product(noticeType)
            .post(appKey, config, buildRequestMap(request)));
    }

    @DeleteMapping("/{noticeType}")
    public ResponseEntity delete(@PathVariable NoticeType noticeType,
                                 @RequestParam(required = false, name = "access_token") String accessToken,
                                 HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        NoticeConfiguratorFactory.product(noticeType).delete(accessTokenService.getUserId(token).get(), buildRequestMap(request));
        return responseOfDelete(true);
    }

    @DeleteMapping("/{noticeType}/{appKey}")
    public ResponseEntity deleteAppConfig(@PathVariable NoticeType noticeType,
                                          @PathVariable String appKey,
                                          HttpServletRequest request) {
        NoticeConfiguratorFactory.product(noticeType).delete(appKey, buildRequestMap(request));
        return responseOfDelete(true);
    }

    @PutMapping("/{noticeType}")
    public ResponseEntity put(@PathVariable NoticeType noticeType,
                              @RequestParam(required = false, name = "access_token") String accessToken,
                              @RequestBody Map config, HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfPut(NoticeConfiguratorFactory.product(noticeType).put(accessTokenService
            .getUserId(token).get(), config, buildRequestMap(request)));
    }

    @PutMapping("/{noticeType}/{appKey}")
    public ResponseEntity putAppConfig(@PathVariable NoticeType noticeType,
                                       @PathVariable String appKey,
                                       @RequestBody Map config, HttpServletRequest request) {
        return responseOfPut(NoticeConfiguratorFactory.product(noticeType).put(appKey, config, buildRequestMap(request)));
    }


    @GetMapping("/{noticeType}")
    public ResponseEntity get(@PathVariable NoticeType noticeType,
                              @RequestParam(required = false, name = "access_token") String accessToken,
                              HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfGet(NoticeConfiguratorFactory.product(noticeType).get(accessTokenService
            .getUserId(token).get(), buildRequestMap(request)));
    }

    @GetMapping("/{noticeType}/{appKey}")
    public ResponseEntity getAppConfig(@PathVariable NoticeType noticeType,
                                       @PathVariable String appKey,
                                       HttpServletRequest request) {
        return responseOfGet(NoticeConfiguratorFactory.product(noticeType).get(appKey, buildRequestMap(request)));
    }

    @PostMapping(value = "/file")
    public ResponseEntity<String> uploadFile(MultipartFile file) throws IOException {
        return responseOfPost(fileService.save(file).getId());
    }

    @PostMapping(path = "/file/{id}")
    public ResponseEntity<String> updateFile(@PathVariable String id, MultipartFile file) throws IOException {
        return responseOfPost(fileService.update(id, file).getId());
    }

    @DeleteMapping(value = "/file")
    public ResponseEntity deleteFile(@RequestParam String ids) throws IOException {
        return responseOfDelete(fileService.deleteByIds(ids));
    }

    private Map<String, Object> buildRequestMap(HttpServletRequest request) {
        Map<String, Object> requestParams = new HashMap<>(16);
        Enumeration em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            requestParams.put(name, request.getParameter(name));
        }
        return requestParams;
    }
}
