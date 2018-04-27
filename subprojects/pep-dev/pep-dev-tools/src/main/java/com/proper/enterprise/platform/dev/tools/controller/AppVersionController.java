package com.proper.enterprise.platform.dev.tools.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.dev.tools.document.AppVersionDocument;
import com.proper.enterprise.platform.dev.tools.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/app/versions")
public class AppVersionController extends BaseController {

    @Autowired
    private AppVersionService appVersionService;

    /**
     * APP端，用来获取最新发布版信息
     *
     * @author sunshuai
     */
    @AuthcIgnore
    @GetMapping("/latest")
    public ResponseEntity<AppVersionDocument> getLatestReleaseVersionInfoByVer(Long current) {
        AppVersionDocument latestVersion = appVersionService.getLatestReleaseVersionOnlyValid();
        long currentVersion = current == null ? -1 : current;
        return responseOfGet(latestVersion != null ? (latestVersion.getVer() > currentVersion ? latestVersion : null) : null);
    }

    /**
     * APP端，用来获取指定版本的版本信息
     *
     * @author sunshuai
     */
    @AuthcIgnore
    @GetMapping("/{version}")
    public ResponseEntity<AppVersionDocument> getCertainVersionInfo(@PathVariable long version) {
        AppVersionDocument doc = appVersionService.getCertainVersion(version);
        return responseOfGet(doc);
    }

}
