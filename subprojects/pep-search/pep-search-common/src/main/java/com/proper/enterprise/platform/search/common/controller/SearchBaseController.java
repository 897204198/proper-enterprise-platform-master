package com.proper.enterprise.platform.search.common.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.search.api.serivce.MongoDataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthcIgnore
@RestController
@RequestMapping("/search")
public class SearchBaseController extends BaseController {

    @Autowired
    private MongoDataSyncService mongoDataSyncService;

    @GetMapping("/init")
    public ResponseEntity<String> syncMongoFromDB() {
        mongoDataSyncService.syncMongoFromDB();
        return responseOfGet("");
    }

}
