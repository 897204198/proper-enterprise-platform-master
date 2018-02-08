package com.proper.enterprise.platform.search.demo.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.search.api.document.SearchColumn;
import com.proper.enterprise.platform.search.api.serivce.SearchService;
import com.proper.enterprise.platform.search.demo.DemoUserConfigs;
import com.proper.enterprise.platform.search.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AuthcIgnore
@RestController
@RequestMapping("/search")
public class DemoUserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoUserController.class);

    @Autowired
    private DemoService demoService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private DemoUserConfigs demoUserConfigs;

    @GetMapping("/user")
    public ResponseEntity<List<SearchColumn>> searchInfo(@RequestParam String data) {
        List<SearchColumn> docs = (List<SearchColumn>) searchService.getSearchInfo(data, demoUserConfigs);
        ResponseEntity<List<SearchColumn>> result = responseOfGet(docs);
        return result;
    }
}
