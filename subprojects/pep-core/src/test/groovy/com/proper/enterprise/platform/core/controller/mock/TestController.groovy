package com.proper.enterprise.platform.core.controller.mock

import com.proper.enterprise.platform.core.controller.BaseController
import com.proper.enterprise.platform.core.utils.DateUtil
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.ServletException

@RestController
@RequestMapping('/test')
class TestController extends BaseController {

    @GetMapping(path = "/trouble/1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String, String>> test1(@RequestParam String div) {
        return new ResponseEntity<String>(['result': 10/Integer.parseInt(div) + ""], HttpStatus.OK);
    }

    @GetMapping("/trouble/2")
    public ResponseEntity<String> test2() {
        throw new ServletException('异常啦')
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @GetMapping("/json/entity")
    public ResponseEntity<MockEntity> entity() {
        def e = new MockEntity('E1', 'E2')
        e.setId('E')
        e.setCreateUserId('EU')
        e.setCreateTime(DateUtil.timestamp)
        e.setLastModifyUserId('ELU')
        e.setLastModifyTime(DateUtil.timestamp)
        responseOfGet(e)
    }

    @GetMapping("/json/document")
    public ResponseEntity<MockDocument> document() {
        def d = new MockDocument('D1', 'D2')
        d.setId('D')
        d.setCreateUserId('DU')
        d.setCreateTime(DateUtil.timestamp)
        d.setLastModifyUserId('DLU')
        d.setLastModifyTime(DateUtil.timestamp)
        responseOfGet(d)
    }

}
