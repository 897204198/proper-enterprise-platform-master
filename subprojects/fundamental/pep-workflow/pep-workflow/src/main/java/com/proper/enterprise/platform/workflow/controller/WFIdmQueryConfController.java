package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.workflow.entity.WFIdmQueryConfEntity;
import com.proper.enterprise.platform.workflow.plugin.service.WFIdmQueryConfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/workflow/ext/idm/conf")
@Api(tags = "/workflow/ext/idm/conf")
public class WFIdmQueryConfController extends BaseController {

    private WFIdmQueryConfService wfIdmQueryConfService;

    @Autowired
    public WFIdmQueryConfController(WFIdmQueryConfService wfIdmQueryConfService) {
        this.wfIdmQueryConfService = wfIdmQueryConfService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation("‍获取流程IDM配置")
    public ResponseEntity<Collection<WFIdmQueryConfEntity>> get() {
        return responseOfGet(wfIdmQueryConfService.findAll());
    }
}
