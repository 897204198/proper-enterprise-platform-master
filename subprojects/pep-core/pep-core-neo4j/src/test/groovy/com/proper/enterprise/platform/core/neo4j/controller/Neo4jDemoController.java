package com.proper.enterprise.platform.core.neo4j.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.neo4j.entity.Neo4jDemoNode;
import com.proper.enterprise.platform.core.neo4j.service.Neo4jDemoNodeService;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.SortOrder;
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
        return responseOfGet(neo4jDemoNodeService.findPage());
    }

    @GetMapping(path = "/sort")
    public ResponseEntity<DataTrunk<Neo4jDemoNode>> getUserBySort() {
        return responseOfGet(neo4jDemoNodeService.findPage(Neo4jDemoNode.class, new Filters(),
            new SortOrder().add(SortOrder.Direction.DESC, "createTime")));
    }
}
