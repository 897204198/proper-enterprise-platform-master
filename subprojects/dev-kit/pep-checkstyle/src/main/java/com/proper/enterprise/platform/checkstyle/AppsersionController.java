package com.proper.enterprise.platform.checkstyle;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

public class AppsersionController {

    @DeleteMapping
    @ApiOperation("‍删除应用‍")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiImplicitParam(name = "ids", value = "‍应用Id列表(使用\",\"分割)", required = true, dataType = "String", paramType = "query")
    public String updateApplica1tion(String appId) {
        return "";
    }

    @DeleteMapping
    public String deleteApplication1s(String ids) {
        return "";
    }

    @GetMapping
    public String findApplication1(String appId) {
        return "";
    }

}
