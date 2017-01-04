package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/menus")
public class MenusController extends BaseController {

    @Autowired
    private MenuService service;

    @GetMapping
    public ResponseEntity get() {
        return responseOfGet(service.getMenus());
    }

}
