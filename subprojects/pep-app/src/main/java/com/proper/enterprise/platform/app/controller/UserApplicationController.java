package com.proper.enterprise.platform.app.controller;

import com.proper.enterprise.platform.app.service.UserApplicationService;
import com.proper.enterprise.platform.app.vo.AppCatalogVO;
import com.proper.enterprise.platform.app.vo.ApplicationVO;
import com.proper.enterprise.platform.app.vo.UserApplicationVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/app/applications")
public class UserApplicationController extends BaseController {

    private UserApplicationService userApplicationService;

    @Autowired
    public UserApplicationController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<ApplicationVO>> findUserApplications() {
        return responseOfGet(userApplicationService.findUserApplications());
    }

    @PutMapping
    public ResponseEntity<UserApplicationVO> putUserApplications(@RequestBody Map<String, String> reqMap) {
        String ids = reqMap.get("ids");
        return responseOfPut(userApplicationService.saveOrUpdateUserApplications(ids));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AppCatalogVO>> findCatalogAndApplications() {
        return responseOfGet(userApplicationService.findCatalogAndApplications());
    }

}
