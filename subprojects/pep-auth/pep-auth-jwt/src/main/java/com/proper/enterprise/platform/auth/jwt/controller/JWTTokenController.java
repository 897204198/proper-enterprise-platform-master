package com.proper.enterprise.platform.auth.jwt.controller;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dev/jwt")
class JWTTokenController extends BaseController {

    @Autowired
    private UserService userService;

    @PostMapping("/encode/header")
    public ResponseEntity<String> encode(@RequestBody JWTHeader header) throws Exception {
        User user;
        if (StringUtil.isBlank(header.getId()) && StringUtil.isNotBlank(header.getName())) {
            user = userService.getByUsername(header.getName());
            if (user != null) {
                header.setId(user.getId());
            }
        } else if (StringUtil.isNotBlank(header.getId()) && StringUtil.isBlank(header.getName())) {
            user = userService.get(header.getId());
            if (user != null) {
                header.setName(user.getUsername());
            }
        }
        String res = StringUtil.isNotBlank(header.getId())
            && StringUtil.isNotBlank(header.getName()) ? JSONUtil.toJSON(header) : ("{\"id\":\"" + header.getId());
        return responseOfPost(Base64.encodeBase64URLSafeString(res.getBytes(PEPConstants.DEFAULT_CHARSET)));
    }

    @PostMapping("/decode")
    public ResponseEntity<String> decode(@RequestBody String token) throws Exception {
        return responseOfPost(StringUtil.toEncodedString(Base64.decodeBase64(token)));
    }

}
