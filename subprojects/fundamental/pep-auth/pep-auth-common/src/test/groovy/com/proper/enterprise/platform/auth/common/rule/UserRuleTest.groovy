package com.proper.enterprise.platform.auth.common.rule

import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.model.Role
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class UserRuleTest extends BaseRuleTest {

    @Test
    @Sql("/com/proper/enterprise/platform/auth/common/service/impl/identity.sql")
    void allUserRuleTest() {
        String userId = createUser("testRoleAll")
        createRule("userAll",  "USER")
        createRole("allUser", "userAll", "ALL")
        createRole("allUserDisable", "userAll", "ALL", false)
        Collection<Role> roles = userService.getUserRoles(userId, EnableEnum.ENABLE)
        assert roles.size() == 1

        Collection<Role> allRoles = resOfGet("/auth/users/" + userId + "/roles?roleEnable=ALL", HttpStatus.OK)
        assert allRoles.size() == 2
        allRoles.each {role -> assert role.origin == 'RULE'}

        Collection<Role> allotmentRoles = resOfGet("/auth/users/" + userId + "/roles?roleEnable=ALL&origin=ALLOTMENT", HttpStatus.OK)
        assert allotmentRoles.size() == 0

        Collection<Role> ruleRoles = resOfGet("/auth/users/" + userId + "/roles?roleEnable=ALL&origin=RULE", HttpStatus.OK)
        assert ruleRoles.size() == 2
        ruleRoles.each {role -> assert role.origin == 'RULE'}
    }

    @Test
    @Sql("/com/proper/enterprise/platform/auth/common/service/impl/identity.sql")
    void specifiedUserRuleTest() {
        String userId = createUser("testRole")
        createRule("user",  "USER")
        Collection<Role> roles = userService.getUserRoles(userId, EnableEnum.ENABLE)
        assert roles.size() == 0
        createRole("onlyUser2", "user", userId)
        Collection<Role> roles2 = userService.getUserRoles(userId, EnableEnum.ENABLE)
        assert roles2.size() == 1
        assert roles2[0].getName() == "onlyUser2"
    }

}
