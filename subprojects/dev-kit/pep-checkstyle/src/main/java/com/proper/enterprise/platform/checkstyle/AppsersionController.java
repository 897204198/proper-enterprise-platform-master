package com.proper.enterprise.platform.checkstyle;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "/admin/app/applications")
@RequestMapping(value = "/admin/app/applications")
public class AppsersionController {

    @PutMapping("/{appId}")
    public String updateApplication(@ApiParam(value = "‍应用id", required = true) @PathVariable String appId) {
        return "";
    }

    @DeleteMapping
    @ApiOperation("‍删除应用‍")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiImplicitParam(name = "ids", value = "‍应用Id列表(使用\",\"分割)", required = true, dataType = "String", paramType = "query")
    public String deleteApplications(@RequestParam String ids) {
        return "";
    }


    @GetMapping("/{appId}")
    public String findApplication(@ApiParam(value = "‍应用id", required = true) @PathVariable String appId) {
        return "";
    }

}
