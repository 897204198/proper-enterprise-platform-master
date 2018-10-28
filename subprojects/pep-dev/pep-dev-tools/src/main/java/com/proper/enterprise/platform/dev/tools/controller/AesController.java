package com.proper.enterprise.platform.dev.tools.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.jpa.converter.AESStringConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "/admin/dev/aes")
@RequestMapping("/admin/dev/aes")
public class AesController extends BaseController {

    private AESStringConverter converter = new AESStringConverter();

    @PostMapping(value = "/encrypt", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍AES 加密")
    public ResponseEntity<String> encrypt(@RequestBody String content) {
        return responseOfPost(converter.convertToDatabaseColumn(content));
    }

    @PostMapping(value = "/decrypt", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍AES 解密")
    public ResponseEntity<String> decrypt(@RequestBody String content) {
        return responseOfPost(converter.convertToEntityAttribute(content));
    }

}
