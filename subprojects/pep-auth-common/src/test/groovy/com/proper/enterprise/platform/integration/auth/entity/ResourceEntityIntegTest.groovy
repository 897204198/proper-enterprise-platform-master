package com.proper.enterprise.platform.integration.auth.entity

import com.proper.enterprise.platform.auth.repository.ResourceRepository
import com.proper.enterprise.platform.core.enums.MOC
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import com.proper.enterprise.platform.test.integration.SqlWorker
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMethod

class ResourceEntityIntegTest extends AbstractIntegTest {

    @Autowired
    ResourceRepository repo

    @Before
    public void setUp() {
        SqlWorker worker = new SqlWorker()
        worker.push("""
INSERT INTO pep_auth_resource
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('1', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'test1', '默认根资源', null, 'SYSTEM', '/', 'GET', 0);
""")
        worker.push("""
INSERT INTO pep_auth_resource
(id, create_user_id, create_time, last_modify_user_id, last_modify_time, code, name, parent, moc, url, method, sequence_number)
VALUES
('2', 'sys', '2015-08-18 09:38:00', 'sys', '2015-08-18 09:38:00', 'test2', '首页', '1', 'SUBSYSTEM', '/index', 'GET', 1);
""")
        worker.work()
    }

    @Test
    public void getParentResource() {
        def resource = repo.findOne('2')
        def parent = resource.getParent()
        assert parent.getId() == '1'
        assert parent.getMoc() == MOC.SYSTEM
        assert parent.getMethod() == RequestMethod.GET

        assert parent.getParent() == null
    }

}
