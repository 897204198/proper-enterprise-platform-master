package com.proper.enterprise.platform.app.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.app.document.AppVersionDocument;
import com.proper.enterprise.platform.app.service.AppVersionService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/versions")
@Api(tags = "/app/versions")
public class AppVersionController extends BaseController {

    private AppVersionService service;

    @Autowired
    public AppVersionController(AppVersionService service) {
        this.service = service;
    }

    @AuthcIgnore
    @GetMapping("/latest")
    @ApiOperation("‍根据当前版本号，获得是否有最新的发布版本,返回最新发布版；若有比当前版本更新的版本，返回更新的版本信息；若当前已是最新版，返回空；若当前版本号为空，直接返回最新发布版")
    public ResponseEntity<AppVersionDocument> getLatestRelease(String current) {
        AppVersionDocument latestVersion = service.getLatestRelease();
        if (StringUtil.isNotBlank(current)) {
            AppVersionDocument currentVersion = service.get(current);
            if (currentVersion != null && latestVersion != null && currentVersion.getCreateTime().equals(latestVersion.getCreateTime())) {
                latestVersion = null;
            }
        }
        return responseOfGet(latestVersion);
    }

    @AuthcIgnore
    @GetMapping("/{version}")
    @ApiOperation("‍APP端，用来获取指定版本的版本信息")
    public ResponseEntity<AppVersionDocument> getCertainVersion(@ApiParam(value = "‍版本号", required = true) @PathVariable String version) {
        return responseOfGet(service.get(version));
    }

}
