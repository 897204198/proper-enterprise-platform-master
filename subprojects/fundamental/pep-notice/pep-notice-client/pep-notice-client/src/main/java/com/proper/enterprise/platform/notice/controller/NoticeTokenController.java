package com.proper.enterprise.platform.notice.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "/notice")
@RequestMapping("/notice")
public class NoticeTokenController extends BaseController {

    @Autowired
    DataDicService dataDicService;

    @PutMapping("/token")
    @ApiOperation("‍‍在字典表中修改Token")
    public ResponseEntity<String> updateToken(@ApiParam(value = "‍‍Token串", required = true)
                                              @RequestParam(required = true, name = "accessToken") String accessToken) {
        DataDic dataDic = dataDicService.get("NOTICE_SERVER", "TOKEN");
        if (dataDic == null) {
            dataDic = new DataDicEntity();
            dataDic.setCatalog("NOTICE_SERVER");
            dataDic.setCode("TOKEN");
            dataDic.setName(accessToken);
            dataDic.setOrder(1);
            dataDic.setDataDicType(DataDicTypeEnum.BUSINESS);
            dataDic.setEnable(true);
        }
        dataDic.setName(accessToken);
        dataDicService.save(dataDic);
        return responseOfPut(accessToken);
    }

    @ApiOperation("‍‍在字典表中获取Token")
    @GetMapping("/token")
    public ResponseEntity<String> getToken() {
        DataDic dataDic = dataDicService.get("NOTICE_SERVER", "TOKEN");
        if (dataDic == null) {
            throw new ErrMsgException("Not Configured");
        }
        return responseOfGet(dataDic.getName());
    }

    @GetMapping("/serverUrl")
    @ApiOperation("‍‍在字典表中获取URL")
    public ResponseEntity<String> getUrl() {
        DataDic dataDic = dataDicService.get("NOTICE_SERVER", "URL");
        if (dataDic == null) {
            throw new ErrMsgException("Not Configured");
        }
        return responseOfGet(dataDic.getName());
    }

}
