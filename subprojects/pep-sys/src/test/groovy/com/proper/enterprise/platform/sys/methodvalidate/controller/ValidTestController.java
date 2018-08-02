package com.proper.enterprise.platform.sys.methodvalidate.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.sys.methodvalidate.TestBean;
import com.proper.enterprise.platform.sys.methodvalidate.service.ValidTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validtest")
public class ValidTestController extends BaseController {

    @Autowired
    private ValidTestService validTestService;

    @PostMapping
    public ResponseEntity post(String a, TestBean testBean) {
        validTestService.test(a, testBean);
        return responseOfGet(null);
    }

}
