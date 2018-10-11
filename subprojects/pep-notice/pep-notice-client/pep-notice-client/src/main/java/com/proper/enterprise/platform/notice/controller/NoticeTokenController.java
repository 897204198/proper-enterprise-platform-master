package com.proper.enterprise.platform.notice.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
public class NoticeTokenController extends BaseController {

    @Autowired
    DataDicService dataDicService;

    @PutMapping("/token")
    public ResponseEntity<String> updateToken(@RequestParam(required = true, name = "accessToken") String accessToken) {
        DataDic dataDic = dataDicService.get("NOTICE_SERVER", "TOKEN");
        if (dataDic == null) {
            dataDic = new DataDicEntity();
            dataDic.setCatalog("NOTICE_SERVER");
            dataDic.setCode("TOKEN");
            dataDic.setName(accessToken);
            dataDic.setOrder(1);
            dataDic.setEnable(true);
        }
        dataDic.setName(accessToken);
        dataDicService.save(dataDic);
        return responseOfPut(accessToken);
    }

    @GetMapping("/token")
    public ResponseEntity<String> getToken() {
        DataDic dataDic = dataDicService.get("NOTICE_SERVER", "TOKEN");
        if (dataDic == null) {
            throw new ErrMsgException("Not Configured");
        }
        return responseOfGet(dataDic.getName());
    }

    @GetMapping("/serverUrl")
    public ResponseEntity<String> getUrl() {
        DataDic dataDic = dataDicService.get("NOTICE_SERVER", "URL");
        if (dataDic == null) {
            throw new ErrMsgException("Not Configured");
        }
        return responseOfGet(dataDic.getName());
    }

}
