package com.proper.enterprise.platform.webapp.configs.controller

import com.proper.enterprise.platform.core.controller.BaseController
import com.proper.enterprise.platform.webapp.configs.dal.entity.AEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController extends BaseController {

    @GetMapping(path = "/test/get")
    public ResponseEntity<String> getString(@RequestParam String str) {
        responseOfGet(str)
    }

    @GetMapping(path = "/test/list")
    public ResponseEntity<List<AEntity>> getList() {
        List<AEntity> list = new ArrayList<>()
        list.add(new AEntity('u1','p1'))
        list.add(new AEntity('u2','p2'))
        list.add(new AEntity('u3','p3'))
        responseOfGet(list)
    }

}
