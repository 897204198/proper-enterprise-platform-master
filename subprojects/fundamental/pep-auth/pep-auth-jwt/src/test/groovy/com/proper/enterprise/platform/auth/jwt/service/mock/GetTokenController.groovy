package com.proper.enterprise.platform.auth.jwt.service.mock

import com.proper.enterprise.platform.auth.service.JWTService
import com.proper.enterprise.platform.core.controller.BaseController
import com.proper.enterprise.platform.core.utils.RequestUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GetTokenController extends BaseController {

    @Autowired
    JWTService jwtService

    @GetMapping('/token/get')
    ResponseEntity<String> get() {
        String token = jwtService.getToken(RequestUtil.getCurrentRequest()).orElse(null)
        responseOfGet(token)
    }

}
