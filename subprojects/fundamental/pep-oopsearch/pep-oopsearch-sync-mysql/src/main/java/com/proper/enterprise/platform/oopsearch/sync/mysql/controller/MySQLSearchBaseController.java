package com.proper.enterprise.platform.oopsearch.sync.mysql.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.oopsearch.api.serivce.MongoDataSyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthcIgnore
@RestController
@Api(tags = "/search")
@RequestMapping("/search")
public class MySQLSearchBaseController extends BaseController {

    @Autowired
    @Qualifier("mySQLMongoDataSync")
    private MongoDataSyncService mongoDataSyncService;

    @GetMapping("/init")
    @ApiOperation("‍同步数据")
    public ResponseEntity<String> fullSynchronization() {
        mongoDataSyncService.fullSynchronization();
        return responseOfGet("");
    }

}
