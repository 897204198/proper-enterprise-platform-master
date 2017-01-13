package com.proper.enterprise.platform.auth.jwt.filter.mock
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore
import com.proper.enterprise.platform.core.controller.BaseController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/jwt/filter/ignore/type")
@AuthcIgnore
class IgnoreTypeController extends BaseController {

    @GetMapping
    public String get() {
        'success'
    }

    @PostMapping
    public ResponseEntity<String> post() {
        responseOfPost('success')
    }

    @PutMapping
    public ResponseEntity<String> put() {
        responseOfPut('success')
    }

    @DeleteMapping
    public ResponseEntity<String> delete() {
        responseOfDelete(true)
    }

}
