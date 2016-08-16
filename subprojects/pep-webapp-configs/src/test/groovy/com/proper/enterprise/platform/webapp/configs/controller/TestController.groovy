package com.proper.enterprise.platform.webapp.configs.controller

import com.proper.enterprise.platform.core.controller.BaseController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController extends BaseController {

    @GetMapping(path = "/test/get")
    public ResponseEntity<String> getString(@RequestParam String str) {
        return responseOfGet(str);
    }

}
