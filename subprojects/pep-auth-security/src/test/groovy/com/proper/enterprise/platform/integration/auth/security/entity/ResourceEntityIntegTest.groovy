package com.proper.enterprise.platform.integration.auth.common.entity

import com.proper.enterprise.platform.auth.common.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository
import com.proper.enterprise.platform.core.enums.MOC
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.bind.annotation.RequestMethod

class ResourceEntityIntegTest extends AbstractIntegTest {

    @Autowired
    ResourceRepository repo

    @Before
    public void setUp() {
        push("""
INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('test1', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'test1', '默认根资源', null, 'SYSTEM', '/test1', 'GET', 0);
""")
        push("""
INSERT INTO pep_auth_resources
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('test2', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'test2', '首页', 'test1', 'SUBSYSTEM', '/test2', 'GET', 1);
""")
        executeSqls()
    }

    @After
    public void tearDown() {
        push("DELETE FROM pep_auth_resources WHERE id='test2'")
        push("DELETE FROM pep_auth_resources WHERE id = 'test1'")
        executeSqls()
    }

    @Test
    public void getParentResource() {
        def resource = repo.findOne('test2')
        def parent = resource.getParent()
        assert parent.getId() == 'test1'
        assert parent.getMoc() == MOC.SYSTEM
        assert parent.getMethod() == RequestMethod.GET

        assert parent.getParent() == null
    }

    @Test(expected = DataIntegrityViolationException)
    public void checkUniqueConstrains() {
        def entity = new ResourceEntity()
        entity.setUrl('/test2')
        entity.setMethod(RequestMethod.GET)
        repo.saveAndFlush(entity)
    }

}
