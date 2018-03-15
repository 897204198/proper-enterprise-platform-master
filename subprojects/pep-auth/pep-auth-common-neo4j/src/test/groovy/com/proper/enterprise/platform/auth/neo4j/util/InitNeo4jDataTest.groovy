package com.proper.enterprise.platform.auth.neo4j.util

import com.proper.enterprise.platform.auth.common.dictionary.MenuType
import com.proper.enterprise.platform.auth.neo4j.repository.*
import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class InitNeo4jDataTest extends AbstractNeo4jTest {

    @Autowired
    MenuNodeRepository menuNodeRepo

    @Autowired
    ResourceNodeRepository resourceNodeRepo

    @Autowired
    RoleNodeRepository roleNodeRepo

    @Autowired
    UserNodeRepository userNodeRepo

    @Autowired
    UserGroupNodeRepository userGroupNodeRepo

    @Autowired
    MenuType menuType

    @Autowired
    DataDicService dataDicService

    @Test
    void initNeo4jData() {
        new InitNeo4jData()
    }

    @Before
    void setUp() {
        clearNeo4jRepositories()
    }

    @After
    void tearDown() {
        clearNeo4jRepositories()
    }

    void clearNeo4jRepositories() {
        userGroupNodeRepo.deleteAll()
        userNodeRepo.deleteAll()
        roleNodeRepo.deleteAll()
        menuNodeRepo.deleteAll()
        resourceNodeRepo.deleteAll()
    }
}
