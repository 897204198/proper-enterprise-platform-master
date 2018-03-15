package com.proper.enterprise.platform.test;

import org.springframework.transaction.annotation.Transactional;

@Transactional(transactionManager = "neo4jTransactionManager")
public class AbstractNeo4jTest extends AbstractTest {
}
