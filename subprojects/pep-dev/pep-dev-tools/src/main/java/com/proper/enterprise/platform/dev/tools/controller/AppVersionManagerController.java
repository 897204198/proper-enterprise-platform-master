package com.proper.enterprise.platform.dev.tools.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.app.document.AppVersionDocument;
import com.proper.enterprise.platform.app.service.AppVersionService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/app/versions")
public class AppVersionManagerController extends BaseController {

    @Autowired
    private AppVersionService appVersionService;

    /**
     * APP端，用来获取最新发布版信息
     *
     * @author sunshuai
     */
    @AuthcIgnore
    @GetMapping("/latest")
    public ResponseEntity<AppVersionDocument> getLatestReleaseVersionInfoByVer(@RequestParam(required = false) Long current) {
        AppVersionDocument latestVersion = appVersionService.getLatestReleaseVersionOnlyValid();
        long currentVersion = current == null ? -1 : current;
        return responseOfGet(latestVersion.getVer() > currentVersion ? latestVersion : null);
    }

    /**
     * APP端，用来获取指定版本的版本信息
     *
     * @author sunshuai
     */
    @GetMapping("/{version}")
    public ResponseEntity<AppVersionDocument> getCertainVersionInfo(@PathVariable long version) {
        AppVersionDocument doc = appVersionService.getCertainVersion(version);
        return responseOfGet(doc);
    }

    /**
     * 更新发布版，将当前版设定为发布版
     *
     * @author sunshuai
     */
    @PutMapping(path = "/latest")
    public ResponseEntity<AppVersionDocument> updateReleaseVersionInfo(@RequestBody AppVersionDocument appVersionDocument) {
        return responseOfPut(appVersionService.releaseAPP(appVersionDocument));
    }

    /**
     * 根据页数、每页显示数量获取版本信息
     *
     * @author sunshuai
     */
    @GetMapping
    public ResponseEntity<DataTrunk<AppVersionDocument>> getVersionInfosByPage(Long version, Integer pageNo, Integer pageSize) {
        if (version != null) {
            AppVersionDocument doc = appVersionService.getCertainVersion(version);
            List<AppVersionDocument> lists = new ArrayList<>();
            lists.add(doc);
            return responseOfGet(lists, lists.size());
        } else {
            return responseOfGet(appVersionService.getVersionInfosByPage(pageNo, pageSize));
        }
    }

    /**
     * 创建一条APP版本信息(不管数据库中是否包含了此条信息，都会增加一条新的)
     *
     * @author sunshuai
     */
    @PostMapping
    public ResponseEntity<AppVersionDocument> create(@RequestBody AppVersionDocument appVersionDocument) {
        return responseOfPost(appVersionService.save(appVersionDocument));
    }

    /**
     * 根据版本号，修改对应的版本信息，包括版本号、url、note
     *
     * @author sunshuai
     */
    @PutMapping("/{version}")
    public ResponseEntity<AppVersionDocument> update(@PathVariable long version, @RequestBody AppVersionDocument appVersionDocument) {
        return responseOfPut(appVersionService.updateVersionInfo(appVersionDocument));
    }

    /**
     * 设定当前版本为无效版本
     *
     * @author sunshuai
     */
    @DeleteMapping(path = "/{version}")
    public ResponseEntity invalid(@PathVariable long version) {
        AppVersionDocument releaseApp = appVersionService.getLatestReleaseVersionOnlyValid();
        if (releaseApp.getVer() == version) {
            return responseOfDelete(false);
        }
        AppVersionDocument app = appVersionService.inValidByVersion(version);
        boolean result = false;
        if (!app.isValid()) {
            result = true;
        }
        return responseOfDelete(result);
    }
}
