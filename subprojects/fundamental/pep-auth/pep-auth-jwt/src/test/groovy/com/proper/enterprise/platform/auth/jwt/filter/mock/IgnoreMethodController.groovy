package com.proper.enterprise.platform.auth.jwt.filter.mock
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/jwt/filter/ignore/method")
class IgnoreMethodController {

    @GetMapping
    @AuthcIgnore
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
