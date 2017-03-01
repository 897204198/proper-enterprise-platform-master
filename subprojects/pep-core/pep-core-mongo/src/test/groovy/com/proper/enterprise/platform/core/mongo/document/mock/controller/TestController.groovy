package com.proper.enterprise.platform.core.mongo.document.mock.controller
import com.proper.enterprise.platform.core.controller.BaseController
import com.proper.enterprise.platform.core.mongo.document.mock.document.MockDocument
import com.proper.enterprise.platform.core.utils.DateUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController extends BaseController {

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
