package com.proper.enterprise.platform.oopsearch.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testOopController")
public class TestController extends BaseController {

    @GetMapping(path = "/{myName}/istest/{id}")
    public ResponseEntity<String> updateMenuDetail(@PathVariable String myName, @PathVariable String id, String createTime) {
        return responseOfGet(myName + id + createTime);
    }
}
