package com.proper.enterprise.platform.auth.jwt.filter.mock

import com.proper.enterprise.platform.auth.jwt.annotation.JWTIgnore
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/jwt/filter/ignore/method")
class IgnoreMethodController {

    @GetMapping
    @JWTIgnore
    public String get() {
        'success'
    }

    @PostMapping
    public String post() {
        'success'
    }

    @PutMapping
    public String put() {
        'success'
    }

}
