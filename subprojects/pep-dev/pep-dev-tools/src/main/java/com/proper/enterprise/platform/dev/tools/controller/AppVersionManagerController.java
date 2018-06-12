package com.proper.enterprise.platform.dev.tools.controller;

import com.proper.enterprise.platform.app.document.AppVersionDocument;
import com.proper.enterprise.platform.app.service.AppVersionService;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/app/versions")
public class AppVersionManagerController extends BaseController {

    private AppVersionService appVersionService;

    @Autowired
    public AppVersionManagerController(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    /**
     * 添加新的版本，版本号需唯一
     *
     * @param  appVersionDocument 版本信息
     * @return 返回添加后的版本信息
     */
    @PostMapping
    public ResponseEntity<AppVersionDocument> create(@RequestBody AppVersionDocument appVersionDocument) {
        return responseOfPost(appVersionService.saveOrUpdate(appVersionDocument));
    }

    /**
     * 更新版本信息
     *
     * @param  appVersionDocument 版本信息
     * @return 更新后的版本信息
     */
    @PutMapping
    public ResponseEntity<AppVersionDocument> update(@RequestBody AppVersionDocument appVersionDocument) {
        return responseOfPut(appVersionService.saveOrUpdate(appVersionDocument));
    }

    @GetMapping
    public ResponseEntity<List<AppVersionDocument>> list() {
        return responseOfGet(appVersionService.list());
    }

    /**
     * 删除某版本
     *
     * @param  version 版本号
     * @return 删除响应
     */
    @DeleteMapping(path = "/{version}")
    public ResponseEntity delete(@PathVariable String version) {
        AppVersionDocument ver = appVersionService.get(version);
        if (ver == null) {
            return responseOfDelete(false);
        }
        appVersionService.delete(ver);
        return responseOfDelete(true);
    }

    /**
     * 保存并发布版本
     *
     * @param  appVersionDocument 版本信息
     * @return 发布的版本信息
     */
    @PostMapping(path = "/latest")
    public ResponseEntity<AppVersionDocument> saveAndRelease(@RequestBody AppVersionDocument appVersionDocument) {
        return responseOfPost(appVersionService.release(appVersionDocument));
    }

}
