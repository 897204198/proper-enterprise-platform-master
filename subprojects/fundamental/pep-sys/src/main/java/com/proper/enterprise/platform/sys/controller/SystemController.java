package com.proper.enterprise.platform.sys.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.model.CurrentModel;
import com.proper.enterprise.platform.core.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Api(tags = "/sys")
@RequestMapping("/sys")
public class SystemController extends BaseController {

    @GetMapping("/current/date")
    @ApiOperation("‍获取当前时间")
    public ResponseEntity<CurrentModel> getCurrentDate() {
        return responseOfGet(new CurrentModel().setId(DateUtil.getTimestamp()).setData(new Date()));
    }
}
