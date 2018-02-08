package com.proper.enterprise.platform.search.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.search.api.document.SearchColumn;
import com.proper.enterprise.platform.search.api.serivce.QueryResultService;
import com.proper.enterprise.platform.search.api.serivce.SearchService;
import com.proper.enterprise.platform.search.demo.DemoDeptConfigs;
import com.proper.enterprise.platform.search.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@AuthcIgnore
@RestController
@RequestMapping("/search")
public class DemoDeptController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoDeptController.class);

    @Autowired
    private DemoService demoService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private QueryResultService queryResultService;

    @Autowired
    private DemoDeptConfigs demoDeptConfigs;

    @GetMapping("/dept")
    public ResponseEntity<List<SearchColumn>> searchInfo(@RequestParam String data) {
        List<SearchColumn> docs = (List<SearchColumn>) searchService.getSearchInfo(data, demoDeptConfigs);
        ResponseEntity<List<SearchColumn>> result = responseOfGet(docs);
        return result;
    }

    @GetMapping("/dept/query")
    public ResponseEntity deptResult(String req, String tableName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            req = URLDecoder.decode(req, PEPConstants.DEFAULT_CHARSET.toString());
            JsonNode jn = objectMapper.readValue(req, JsonNode.class);
            return responseOfGet(queryResultService.assemble(demoDeptConfigs, jn, tableName));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return responseOfGet(new ArrayList<>());
    }

}
