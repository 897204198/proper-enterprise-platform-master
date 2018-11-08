package com.proper.enterprise.platform.workflow.service.impl

import com.proper.enterprise.platform.test.AbstractJPATest
import org.flowable.app.service.idm.RemoteIdmService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

public class PEPRemoteIdmServiceImplTest extends AbstractJPATest {

    @Autowired
    private RemoteIdmService remoteIdmService

    @Test
    @Sql("/com/proper/enterprise/platform/workflow/identity.sql")
    void test() {
        def user = remoteIdmService.getUser("user1")
        def users = remoteIdmService.findUsersByNameFilter("testuser2")
        def groups = remoteIdmService.findGroupsByNameFilter("testgroup2")
        def noFilterRoles = remoteIdmService.getRolesByNameFilter(null)
        def remoteRoles = remoteIdmService.getRolesByNameFilter("testrole2")
        //接口方法未实现
        def authenticate = remoteIdmService.authenticateUser(null, null)
        def getToken = remoteIdmService.getToken(null)
        def groupUsers = remoteIdmService.findUsersByGroup("group2")
        def group = remoteIdmService.getGroup("group1")
        expect:
        assert "testuser1" == user.firstName
        assert "user2" == users.get(0).id
        assert 1 == groups.size()&&"group2"==groups.get(0).id
        assert 2 == noFilterRoles.size()
        assert 1 == remoteRoles.size() && "role2" == remoteRoles.get(0).id
        assert null == authenticate
        assert null == getToken
        assert null == groupUsers
        assert null == group
    }
}
