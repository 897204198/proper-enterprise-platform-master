package com.proper.enterprise.platform.notice.server.push.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.push.dao.service.PushNoticeConfigService;
import com.proper.enterprise.platform.notice.server.push.vo.PushConfigVO;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.sdk.enums.PushProfileEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Api(tags = "/notice/server/push/config")
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

    @ApiOperation("‍‍保存‍‍‍‍推送配置")
    @PostMapping(value = "/{appKey}")
    public ResponseEntity post(@ApiParam(value = "‍应用唯一标识", required = true) @PathVariable String appKey,
                               @RequestBody PushConfigVo pushConfigVo) {
        PushConfigVO pushConfigVO = convertVo(pushConfigVo);
        pushNoticeConfigService.save(appKey, pushConfigVO);
        return responseOfPost(null);
    }


    @PostMapping
    @ApiOperation("‍‍保存推送配置")
    public ResponseEntity post(@ApiParam(value = "‍Token字符串", required = true)
                               @RequestParam(required = false, name = "access_token") String accessToken,
                               @RequestBody PushConfigVo pushConfigVo,
                               HttpServletRequest request) {
        PushConfigVO pushConfigVO = convertVo(pushConfigVo);
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        pushNoticeConfigService.save(accessTokenService
            .getUserId(token).get(), pushConfigVO);
        return responseOfPost(null);
    }

    @SuppressWarnings("unchecked")
    private PushConfigVO convertVo(PushConfigVo pushConfigVo) {
        PushConfigItem huaweiConfItem = pushConfigVo.getHuaweiConf();
        PushConfigItem xiaomiConfItem = pushConfigVo.getXiaomiConf();
        PushConfigItem iosConfItem = pushConfigVo.getIosConf();
        PushConfigVO pushConfigVO = new PushConfigVO();
        if (null != huaweiConfItem) {
            String json = JSONUtil.toJSONIgnoreException(huaweiConfItem);
            Map huaweiConf = JSONUtil.parseIgnoreException(json, Map.class);
            pushConfigVO.setHuaweiConf(huaweiConf);
        }
        if (null != xiaomiConfItem) {
            String json = JSONUtil.toJSONIgnoreException(xiaomiConfItem);
            Map xiaomiConf = JSONUtil.parseIgnoreException(json, Map.class);
            pushConfigVO.setXiaomiConf(xiaomiConf);
        }
        if (null != iosConfItem) {
            String json = JSONUtil.toJSONIgnoreException(iosConfItem);
            Map iosConf = JSONUtil.parseIgnoreException(json, Map.class);
            pushConfigVO.setIosConf(iosConf);
        }
        return pushConfigVO;
    }

    @ApiOperation("‍‍根据appkey删除配置")
    @DeleteMapping(value = "/{appKey}")
    public ResponseEntity delete(@ApiParam(value = "‍应用唯一标识", required = true) @PathVariable String appKey) {
        pushNoticeConfigService.delete(appKey);
        return responseOfDelete(true);
    }

    @DeleteMapping
    @ApiOperation("‍‍根据Token删除配置")
    public ResponseEntity delete(@ApiParam(value = "‍Token字符串", required = true)
                                 @RequestParam(required = false, name = "access_token") String accessToken,
                                 HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        pushNoticeConfigService.delete(accessTokenService
            .getUserId(token).get());
        return responseOfDelete(true);
    }

    @PutMapping(value = "/{appKey}")
    @ApiOperation("‍‍根据appkey修改配置")
    public ResponseEntity put(@ApiParam(value = "‍应用唯一标识", required = true) @PathVariable String appKey,
                              @RequestBody PushConfigVo pushConfigVo) {
        PushConfigVO pushConfigVO = convertVo(pushConfigVo);
        pushNoticeConfigService.update(appKey, pushConfigVO);
        return responseOfPost(null);
    }

    @PutMapping
    @ApiOperation("‍‍根据appkey修改配置")
    public ResponseEntity put(@ApiParam(value = "‍token字符串", required = true)
                              @RequestParam(required = false, name = "access_token") String accessToken,
                              @RequestBody PushConfigVo pushConfigVo,
                              HttpServletRequest request) {
        PushConfigVO pushConfigVO = convertVo(pushConfigVo);
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        pushNoticeConfigService.update(accessTokenService
            .getUserId(token).get(), pushConfigVO);
        return responseOfPost(null);
    }

    @GetMapping(value = "/{appKey}")
    @ApiOperation("‍‍根据appkey查询配置")
    public ResponseEntity<PushConfigVO> get(@ApiParam(value = "‍应用唯一标识", required = true) @PathVariable String appKey) {
        return responseOfGet(pushNoticeConfigService.get(appKey));
    }

    @GetMapping
    @ApiOperation("‍‍根据token字符串查询配置")
    public ResponseEntity get(@ApiParam(value = "‍token字符串", required = true)
                              @RequestParam(required = false, name = "access_token") String accessToken,
                              HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfGet(pushNoticeConfigService.get(accessTokenService
            .getUserId(token).get()));
    }


    public static class PushConfigVo {
        @ApiModelProperty(name = "‍华为配置")
        private PushConfigItem huaweiConf;
        @ApiModelProperty(name = "‍小米配置")
        private PushConfigItem xiaomiConf;
        @ApiModelProperty(name = "‍iOS配置")
        private PushConfigItem iosConf;

        public PushConfigItem getHuaweiConf() {
            return huaweiConf;
        }

        public void setHuaweiConf(PushConfigItem huaweiConf) {
            this.huaweiConf = huaweiConf;
        }

        public PushConfigItem getXiaomiConf() {
            return xiaomiConf;
        }

        public void setXiaomiConf(PushConfigItem xiaomiConf) {
            this.xiaomiConf = xiaomiConf;
        }

        public PushConfigItem getIosConf() {
            return iosConf;
        }

        public void setIosConf(PushConfigItem iosConf) {
            this.iosConf = iosConf;
        }
    }

    public static class PushConfigItem {
        @ApiModelProperty(name = "‍唯一标识", required = true)
        private String appKey;

        @ApiModelProperty(name = "‍推送渠道", required = true)
        private PushChannelEnum pushChannel;

        @ApiModelProperty(name = "‍推送应用ID(华为)", required = true)
        private String appId;

        @ApiModelProperty(name = "‍推送密钥或IOS的证书密码", required = true)
        private String appSecret;

        @ApiModelProperty(name = "‍推送包名", required = true)
        private String pushPackage;

        @ApiModelProperty(name = "‍证书Id", required = true)
        private String certificateId;

        @ApiModelProperty(name = "‍证书密码", required = true)
        private String certPassword;

        @ApiModelProperty(name = "‍推送环境")
        private PushProfileEnum pushProfile;

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public PushChannelEnum getPushChannel() {
            return pushChannel;
        }

        public void setPushChannel(PushChannelEnum pushChannel) {
            this.pushChannel = pushChannel;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public void setAppSecret(String appSecret) {
            this.appSecret = appSecret;
        }

        public String getPushPackage() {
            return pushPackage;
        }

        public void setPushPackage(String pushPackage) {
            this.pushPackage = pushPackage;
        }

        public String getCertificateId() {
            return certificateId;
        }

        public void setCertificateId(String certificateId) {
            this.certificateId = certificateId;
        }

        public String getCertPassword() {
            return certPassword;
        }

        public void setCertPassword(String certPassword) {
            this.certPassword = certPassword;
        }

        public PushProfileEnum getPushProfile() {
            return pushProfile;
        }

        public void setPushProfile(PushProfileEnum pushProfile) {
            this.pushProfile = pushProfile;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }

}
