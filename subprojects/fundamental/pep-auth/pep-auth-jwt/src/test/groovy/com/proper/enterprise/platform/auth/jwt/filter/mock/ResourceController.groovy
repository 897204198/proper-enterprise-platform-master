package com.proper.enterprise.platform.auth.jwt.filter.mock

import com.proper.enterprise.platform.core.controller.BaseController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/jwt/filter/res")
class ResourceController extends BaseController {

    @GetMapping("/nores")
    public ResponseEntity<String> noRes() {
        return responseOfGet("SUCCESS")
    }

    @GetMapping("/nomenu")
    public ResponseEntity<String> nomenu() {
        return responseOfGet("SUCCESS")
    }

    @GetMapping("/menures")
    public ResponseEntity<String> menures() {
        return responseOfGet("SUCCESS")
    }

}
