package com.proper.enterprise.platform.notice.server.api.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.notice.server.api.model.App;
import com.proper.enterprise.platform.notice.server.api.service.AppDaoService;
import com.proper.enterprise.platform.notice.server.api.vo.AppVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notice/server/app")
public class AppController extends BaseController {

    private AppDaoService appDaoService;

    private AccessTokenService accessTokenService;

    @Autowired
    public AppController(AppDaoService appDaoService, AccessTokenService accessTokenService) {
        this.appDaoService = appDaoService;
        this.accessTokenService = accessTokenService;
    }


    @GetMapping
    public ResponseEntity<DataTrunk<App>> get(String appKey, String appName, String describe, Boolean enable) {
        return responseOfGet(appDaoService.findAll(appKey, appName, describe, enable, getPageRequest()));
    }

    @GetMapping(value = "/appKey")
    public ResponseEntity<String> getAppKey() {
        return responseOfGet(UUID.randomUUID().toString());
    }

    @GetMapping(value = "/token")
    public ResponseEntity<String> getToken() {
        return responseOfGet(accessTokenService.generate());
    }

    @GetMapping(value = "/appId/{appId}")
    public ResponseEntity<App> get(@PathVariable String appId) {
        return responseOfGet(appDaoService.get(appId));
    }

    @PostMapping
    public ResponseEntity<App> post(@RequestBody AppVO app) {
        return responseOfPost(appDaoService.save(app));
    }

    @DeleteMapping(value = "{appId}")
    public ResponseEntity delete(@PathVariable String appId) {
        return responseOfDelete(appDaoService.delete(appId));
    }

    @PutMapping(value = "/{appId}")
    public ResponseEntity<App> put(@PathVariable String appId, @RequestBody AppVO app) {
        app.setId(appId);
        return responseOfPut(appDaoService.updateApp(app));
    }

    @PutMapping
    public ResponseEntity updateAppsEnable(@RequestParam String appIds, @RequestParam boolean enable) {
        appDaoService.updateAppsEnable(appIds, enable);
        return responseOfPut(null);
    }


}
