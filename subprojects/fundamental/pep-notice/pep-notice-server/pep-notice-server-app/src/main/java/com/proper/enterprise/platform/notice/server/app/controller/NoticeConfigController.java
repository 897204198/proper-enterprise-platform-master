package com.proper.enterprise.platform.notice.server.app.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.notice.server.api.factory.NoticeConfiguratorFactory;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "/notice/server/config")
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
    @ApiOperation("‍新增配置")
    @SuppressWarnings("unchecked")
    public ResponseEntity post(@ApiParam(value = "‍消息类型", required = true) @PathVariable NoticeType noticeType,
                               @ApiParam(value = "‍Token") @RequestParam(required = false, name = "access_token") String accessToken,
                               @ApiParam(value = "‍‍‍需要的参数配置", required = true) @RequestBody Map config, HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfPost(NoticeConfiguratorFactory.product(noticeType)
            .post(accessTokenService.getUserId(token).get(), config, buildRequestMap(request)));
    }

    @PostMapping("/{noticeType}/{appKey}")
    @ApiOperation("‍新增配置")
    @SuppressWarnings("unchecked")
    public ResponseEntity postAppConfig(@ApiParam(value = "‍消息类型", required = true) @PathVariable NoticeType noticeType,
                                        @ApiParam(value = "‍App唯一标识", required = true) @PathVariable String appKey,
                                        @ApiParam(value = "‍‍‍需要的参数配置", required = true) @RequestBody Map config,
                                        HttpServletRequest request) {
        return responseOfPost(NoticeConfiguratorFactory.product(noticeType)
            .post(appKey, config, buildRequestMap(request)));
    }

    @DeleteMapping("/{noticeType}")
    @ApiOperation("‍删除配置")
    public ResponseEntity delete(@ApiParam(value = "‍消息类型", required = true) @PathVariable NoticeType noticeType,
                                 @ApiParam(value = "‍Token") @RequestParam(required = false, name = "access_token") String accessToken,
                                 HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        NoticeConfiguratorFactory.product(noticeType).delete(accessTokenService.getUserId(token).get(), buildRequestMap(request));
        return responseOfDelete(true);
    }

    @DeleteMapping("/{noticeType}/{appKey}")
    @ApiOperation("‍删除配置")
    public ResponseEntity deleteAppConfig(@ApiParam(value = "‍消息类型", required = true) @PathVariable NoticeType noticeType,
                                          @ApiParam(value = "‍App唯一标识", required = true) @PathVariable String appKey,
                                          HttpServletRequest request) {
        NoticeConfiguratorFactory.product(noticeType).delete(appKey, buildRequestMap(request));
        return responseOfDelete(true);
    }

    @PutMapping("/{noticeType}")
    @ApiOperation("‍修改配置")
    @SuppressWarnings("unchecked")
    public ResponseEntity put(@ApiParam(value = "‍消息类型", required = true) @PathVariable NoticeType noticeType,
                              @ApiParam(value = "‍Token") @RequestParam(required = false, name = "access_token") String accessToken,
                              @ApiParam(value = "‍‍‍需要的参数信息", required = true) @RequestBody Map config,
                              HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfPut(NoticeConfiguratorFactory.product(noticeType).put(accessTokenService
            .getUserId(token).get(), config, buildRequestMap(request)));
    }

    @PutMapping("/{noticeType}/{appKey}")
    @ApiOperation("‍修改配置")
    @SuppressWarnings("unchecked")
    public ResponseEntity putAppConfig(@ApiParam(value = "‍消息类型", required = true) @PathVariable NoticeType noticeType,
                                       @ApiParam(value = "‍App唯一标识", required = true) @PathVariable String appKey,
                                       @ApiParam(value = "‍‍‍需要的参数信息", required = true) @RequestBody Map config,
                                       HttpServletRequest request) {
        return responseOfPut(NoticeConfiguratorFactory.product(noticeType).put(appKey, config, buildRequestMap(request)));
    }


    @GetMapping("/{noticeType}")
    @ApiOperation("‍根据token获取配置")
    public ResponseEntity get(@ApiParam(value = "‍消息类型", required = true) @PathVariable NoticeType noticeType,
                              @ApiParam(value = "‍Token") @RequestParam(required = false, name = "access_token") String accessToken,
                              HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfGet(NoticeConfiguratorFactory.product(noticeType).get(accessTokenService
            .getUserId(token).get(), buildRequestMap(request)));
    }

    @GetMapping("/{noticeType}/{appKey}")
    @ApiOperation("‍根据消息类型和appKey获取配置")
    public ResponseEntity getAppConfig(@ApiParam(value = "‍消息类型", required = true) @PathVariable NoticeType noticeType,
                                       @ApiParam(value = "‍App唯一标识", required = true) @PathVariable String appKey,
                                       HttpServletRequest request) {
        return responseOfGet(NoticeConfiguratorFactory.product(noticeType).get(appKey, buildRequestMap(request)));
    }

    @PostMapping(value = "/file")
    @ApiOperation("‍保存文件")
    public ResponseEntity<String> uploadFile(@ApiParam(value = "‍上传的文件内容", required = true) MultipartFile file) throws IOException {
        return responseOfPost(fileService.save(file).getId());
    }

    @PostMapping(path = "/file/{id}")
    @ApiOperation("‍修改文件")
    public ResponseEntity<String> updateFile(@ApiParam(value = "‍文件唯一标识", required = true) @PathVariable String id,
                                             @ApiParam(value = "‍上传的文件内容", required = true) MultipartFile file) throws IOException {
        return responseOfPost(fileService.update(id, file).getId());
    }

    @DeleteMapping(value = "/file")
    @ApiOperation("‍批量删除文件")
    public ResponseEntity deleteFile(@ApiParam(value = "‍文件id通过逗号(,)拼接成的字符串", required = true) @RequestParam String ids) throws IOException {
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
