package com.proper.enterprise.platform.notice.server.app.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.model.App;
import com.proper.enterprise.platform.notice.server.api.service.AppDaoService;
import com.proper.enterprise.platform.notice.server.app.vo.AppVO;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@Api(tags = "/notice/server/app")
@RequestMapping("/notice/server/app")
public class AppController extends BaseController {

    private AppDaoService appDaoService;

    private AccessTokenService accessTokenService;

    @Autowired
    public AppController(AppDaoService appDaoService, @Qualifier("accessTokenService") AccessTokenService accessTokenService) {
        this.appDaoService = appDaoService;
        this.accessTokenService = accessTokenService;
    }

    @GetMapping
    @ApiOperation("‍分页查询app")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk> get(@ApiParam("‍‍App唯一标识") String appKey,
                                         @ApiParam("‍‍App名称") String appName,
                                         @ApiParam("‍‍‍‍App描述") String appDesc,
                                         @ApiParam("‍‍‍‍‍‍‍‍‍‍‍‍是否启用 ") Boolean enable) {


        return responseOfGet(appDaoService.findAll(appKey, appName,
            appDesc, enable, getPageRequest(new Sort(Sort.Direction.DESC, "createTime"))));
    }

    @GetMapping(value = "/appKey/{appKey}")
    @ApiOperation("‍根据应用唯一标识获取应用")
    public ResponseEntity<App> get(@ApiParam(value = "‍‍‍‍‍‍‍‍‍‍‍‍‍‍App唯一标识", required = true) @PathVariable String appKey) {
        return responseOfGet(appDaoService.get(appKey));
    }

    @GetMapping(value = "/appKey")
    @ApiOperation("‍根据token获取应用")
    public ResponseEntity<App> get(@ApiParam("‍‍‍‍‍‍‍‍‍‍‍‍‍‍token") @RequestParam(required = false, name = "access_token") String accessToken,
                                   HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfGet(appDaoService.get(accessTokenService
            .getUserId(token).get()));
    }

    @GetMapping(value = "/appKey/init")
    @ApiOperation("‍生成一个随机的appKey")
    public ResponseEntity<String> getAppKey() {
        return responseOfGet(UUID.randomUUID().toString());
    }

    @GetMapping(value = "/token/init")
    @ApiOperation("‍以UUID的形式生成一个随机token")
    public ResponseEntity<String> getToken() {
        return responseOfGet(accessTokenService.generate());
    }

    @PostMapping
    @ApiOperation("‍保存App")
    public ResponseEntity<App> post(@RequestBody AppVo appVo) {
        App app = new AppVO();
        BeanUtils.copyProperties(appVo, app);
        return responseOfPost(appDaoService.save(app));
    }

    @DeleteMapping
    @ApiOperation("‍根据appId批量删除App")
    public ResponseEntity delete(@ApiParam(value = "‍‍‍‍‍‍‍‍‍‍‍‍‍‍Appid用逗号(,)拼接成的字符串", required = true) @RequestParam String appIds) {
        return responseOfDelete(appDaoService.delete(appIds));
    }

    @PutMapping(value = "/{appId}")
    @ApiOperation("‍更新APP")
    public ResponseEntity<App> put(@ApiParam(value = "‍‍‍‍‍‍‍‍‍‍‍‍‍‍AppId", required = true) @PathVariable String appId,
                                   @RequestBody AppVo appVo) {
        App app = new AppVO();
        BeanUtils.copyProperties(appVo, app);
        app.setId(appId);
        return responseOfPut(appDaoService.updateApp(app));
    }

    @PutMapping
    @ApiOperation("‍根据appIds批量启用停用App")
    public ResponseEntity updateAppsEnable(@ApiParam(value = "‍‍‍‍‍‍‍‍‍‍‍‍‍‍Appid用逗号(,)拼接成的字符串", required = true) @RequestParam String appIds,
                                           @ApiParam(value = "‍‍启用OR停用标识", required = true) @RequestParam boolean enable) {
        appDaoService.updateAppsEnable(appIds, enable);
        return responseOfPut(null);
    }

    public static class AppVo {

        @ApiModelProperty(name = "‍应用名称", required = true)
        private String appName;

        @ApiModelProperty(name = "‍应用唯一标识", required = true)
        private String appKey;

        @ApiModelProperty(name = "‍应用token", required = true)
        private String appToken;

        @ApiModelProperty(name = "‍描述", required = true)
        private String appDesc;

        @ApiModelProperty(name = "‍app颜色", required = true)
        private String color;

        @ApiModelProperty(name = "‍是否包含email配置", required = true)
        private Boolean haveEmailConf;

        @ApiModelProperty(name = "‍是否包含短信配置", required = true)
        private Boolean haveSMSConf;

        @ApiModelProperty(name = "‍是否包含推送配置", required = true)
        private Boolean havePushConf;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getAppToken() {
            return appToken;
        }

        public void setAppToken(String appToken) {
            this.appToken = appToken;
        }

        public String getAppDesc() {
            return appDesc;
        }

        public void setAppDesc(String appDesc) {
            this.appDesc = appDesc;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Boolean getHaveEmailConf() {
            return haveEmailConf;
        }

        public void setHaveEmailConf(Boolean haveEmailConf) {
            this.haveEmailConf = haveEmailConf;
        }

        public Boolean getHaveSMSConf() {
            return haveSMSConf;
        }

        public void setHaveSMSConf(Boolean haveSMSConf) {
            this.haveSMSConf = haveSMSConf;
        }

        public Boolean getHavePushConf() {
            return havePushConf;
        }

        public void setHavePushConf(Boolean havePushConf) {
            this.havePushConf = havePushConf;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }


}
