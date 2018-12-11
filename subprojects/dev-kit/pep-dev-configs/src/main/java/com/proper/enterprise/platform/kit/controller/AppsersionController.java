package com.proper.enterprise.platform.kit.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/versions")
@Api(tags = "/app/versions")
public class AppsersionController {

    @GetMapping("/latest")
    public String getLatestRelease() {
        return "";
    }

    @GetMapping("/{version}")
    @ApiOperation("‍APP端，用来获取指定版本的版本信息")
    public String getCertainVersion(@ApiParam(value = "‍版本号", required = true) @PathVariable String version) {
        return "";
    }

}
