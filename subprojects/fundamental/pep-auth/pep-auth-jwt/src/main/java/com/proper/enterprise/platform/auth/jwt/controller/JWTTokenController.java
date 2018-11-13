package com.proper.enterprise.platform.auth.jwt.controller;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;

@RestController
@Api(tags = "/admin/dev/jwt")
@RequestMapping("/admin/dev/jwt")
class JWTTokenController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private CoreProperties coreProperties;

    @PostMapping(value = "/encode/header", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍JWT header 信息转换成 token")
    public ResponseEntity<String> encodeHeader(@RequestBody JWTHeader header) throws Exception {
        User user;
        if (StringUtil.isBlank(header.getId()) && StringUtil.isNotBlank(header.getName())) {
            user = userService.getByUsername(header.getName(), EnableEnum.ENABLE);
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
        return responseOfPost(Base64.encodeBase64URLSafeString(res.getBytes(coreProperties.getCharset())));
    }

    @PostMapping(value = "/decode", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("JWT token decode")
    public ResponseEntity<String> decodeToken(@RequestBody String token) {
        StringBuilder sb = new StringBuilder();
        String escapeSymbol = "\\.";
        String tokenSeparator = ".";
        if (token.contains(tokenSeparator)) {
            for (String part : token.split(escapeSymbol)) {
                sb.append(tokenSeparator).append(decode(part));
            }
        } else {
            sb.append(tokenSeparator).append(decode(token));
        }
        return responseOfPost(sb.toString().replaceFirst(escapeSymbol, ""));
    }

    private String decode(String str) {
        return new String(Base64.decodeBase64(str), Charset.forName(coreProperties.getCharset()));
    }

}
