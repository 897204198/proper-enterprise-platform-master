package com.proper.enterprise.platform.core.neo4j.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.neo4j.service.Neo4jDemoNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/neo4j/test")
public class Neo4jDemoController extends BaseController {
    @Autowired
    private Neo4jDemoNodeService neo4jDemoNodeService;

    @GetMapping
    public ResponseEntity<?> getUser() {
        return responseOfGet(neo4jDemoNodeService.findData());
    }
}
