package com.proper.enterprise.platform.core.controller.mock

import com.proper.enterprise.platform.core.controller.BaseController
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.core.utils.StringUtil
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.NoHandlerFoundException

import javax.servlet.ServletException

@RestController('coreTestController')
@RequestMapping('/core/test')
class TestController extends BaseController {

    @GetMapping(path = "/trouble/1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Map<String, String>> test1(@RequestParam String div) {
        return new ResponseEntity<String>(['result': 10 / Integer.parseInt(div) + ""], HttpStatus.OK);
    }

    @GetMapping("/trouble/2")
    ResponseEntity<String> test2() {
        throw new ServletException('异常啦')
    }

    @GetMapping("/trouble/3")
    ResponseEntity<String> test3() {
        throw new NoHandlerFoundException('handle by handler', '', null)
    }

    @GetMapping('/trouble/4')
    ResponseEntity test4() {
        throw new ErrMsgException('empty stack')
    }

    @GetMapping("/json/entity")
    ResponseEntity<MockEntity> entity() {
        def e = new MockEntity('E1', 'E2')
        e.setId('E')
        e.setCreateUserId('EU')
        e.setCreateTime(DateUtil.timestamp)
        e.setLastModifyUserId('ELU')
        e.setLastModifyTime(DateUtil.timestamp)
        responseOfGet(e)
    }

    @GetMapping("/datatrunk")
    ResponseEntity<DataTrunk<String>> datatrunk() {
        responseOfGet(['1', '2', '3'], 10)
    }

    @GetMapping('/emptylist')
    ResponseEntity<List> getAgreement() {
        return responseOfGet([])
    }

}
