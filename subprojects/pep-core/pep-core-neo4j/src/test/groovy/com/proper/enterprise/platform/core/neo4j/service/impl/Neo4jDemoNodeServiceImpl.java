package com.proper.enterprise.platform.core.neo4j.service.impl;

import com.proper.enterprise.platform.core.neo4j.entity.Neo4jDemoNode;
import com.proper.enterprise.platform.core.neo4j.repository.Neo4jDemoNodeRepository;
import com.proper.enterprise.platform.core.neo4j.service.Neo4jDemoNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Neo4jDemoNodeServiceImpl extends Neo4jServiceSupport<Neo4jDemoNode, Neo4jDemoNodeRepository, String> implements Neo4jDemoNodeService {

    @Autowired
    private Neo4jDemoNodeRepository neo4jDemoNodeRepository;

    @Override
    public Neo4jDemoNodeRepository getRepository() {
        return neo4jDemoNodeRepository;
    }

}
