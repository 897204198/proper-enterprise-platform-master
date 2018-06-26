package com.proper.enterprise.platform.dev.tools.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.jpa.converter.AESStringConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dev/aes")
public class AesController extends BaseController {

    private AESStringConverter converter = new AESStringConverter();

    @PostMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestBody String content) {
        return responseOfPost(converter.convertToDatabaseColumn(content));
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestBody String content) {
        return responseOfPost(converter.convertToEntityAttribute(content));
    }

}
