package com.proper.enterprise.platform.notice.server.app.controller;

import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.server.api.model.App;
import com.proper.enterprise.platform.notice.server.api.service.AppDaoService;
import com.proper.enterprise.platform.notice.server.app.vo.AppVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
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
    public ResponseEntity<DataTrunk<App>> get(String appKey, String appName, String appDesc, Boolean enable) {
        return responseOfGet(appDaoService.findAll(appKey, appName,
            appDesc, enable, getPageRequest(new Sort(Sort.Direction.DESC, "createTime"))));
    }

    @GetMapping(value = "/appKey/{appKey}")
    public ResponseEntity<App> get(@PathVariable String appKey) {
        return responseOfGet(appDaoService.get(appKey));
    }

    @GetMapping(value = "/appKey")
    public ResponseEntity<App> get(@RequestParam(required = false, name = "access_token") String accessToken,
                                   HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(AccessTokenService.TOKEN_FLAG_HEADER);
        String token = StringUtil.isEmpty(accessTokenHeader) ? accessToken : accessTokenHeader;
        return responseOfGet(appDaoService.get(accessTokenService
            .getUserId(token).get()));
    }

    @GetMapping(value = "/appKey/init")
    public ResponseEntity<String> getAppKey() {
        return responseOfGet(UUID.randomUUID().toString());
    }

    @GetMapping(value = "/token/init")
    public ResponseEntity<String> getToken() {
        return responseOfGet(accessTokenService.generate());
    }

    @PostMapping
    public ResponseEntity<App> post(@RequestBody AppVO app) {
        return responseOfPost(appDaoService.save(app));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String appIds) {
        return responseOfDelete(appDaoService.delete(appIds));
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
