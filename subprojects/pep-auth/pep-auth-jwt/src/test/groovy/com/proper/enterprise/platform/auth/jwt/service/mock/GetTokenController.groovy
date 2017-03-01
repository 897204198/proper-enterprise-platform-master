package com.proper.enterprise.platform.auth.jwt.service.mock

import com.proper.enterprise.platform.auth.jwt.service.JWTService
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
    public ResponseEntity<String> get() {
        String token = jwtService.getTokenFromHeader(RequestUtil.getCurrentRequest())
        responseOfGet(token)
    }

}
