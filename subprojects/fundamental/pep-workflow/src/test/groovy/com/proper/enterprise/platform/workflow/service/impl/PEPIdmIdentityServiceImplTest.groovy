package com.proper.enterprise.platform.workflow.service.impl

import com.proper.enterprise.platform.test.AbstractJPATest
import org.flowable.idm.api.IdmIdentityService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class PEPIdmIdentityServiceImplTest extends AbstractJPATest {
    @Autowired
    private IdmIdentityService identityService

    @Test
    @Sql("/com/proper/enterprise/platform/workflow/identity.sql")
    void test() {
        def groups = identityService.queryGroupByUserId("user1")
        def roles = identityService.queryRoleByUserId("user1")
        except:
        assert 1 == groups.size() && "testgroup1" == groups.get(0).name
        assert 1 == roles.size() && "testrole1" == roles.get(0).name
        //接口方法未实现
        assert !identityService.checkPassword(null,null)
        assert null==identityService.getGroupsWithPrivilege()
        assert null==identityService.getPrivilegeMappingsByPrivilegeId()
        assert null==identityService.getUserInfo(null,null)
        assert null==identityService.getUserInfoKeys(null)
        assert null==identityService.getUserPicture(null)
        assert null==identityService.getUsersWithPrivilege(null)
        assert null==identityService.createNativeGroupQuery()
        assert null==identityService.createGroupQuery()
        assert null==identityService.createNativeTokenQuery()
        assert null==identityService.createNativeUserQuery()
        assert null==identityService.createPrivilege()
        assert null==identityService.createPrivilegeQuery()
        assert null==identityService.createTokenQuery()
        assert null==identityService.createUserQuery()
        identityService.saveGroup()
        identityService.saveToken()
        identityService.saveUser()
        identityService.saveGroup()
        identityService.setUserInfo(null,null,null)
        identityService.setUserPicture(null,null)
        identityService.updateUserPassword()
        identityService.deleteGroup()
        identityService.deleteGroupPrivilegeMapping(null,null)
        identityService.deleteMembership(null,null)
        identityService.deletePrivilege()
        identityService.deleteToken()
        identityService.deleteUser()
        identityService.deleteUserInfo(null,null)
        identityService.deleteUserPrivilegeMapping(null,null)
    }
}
