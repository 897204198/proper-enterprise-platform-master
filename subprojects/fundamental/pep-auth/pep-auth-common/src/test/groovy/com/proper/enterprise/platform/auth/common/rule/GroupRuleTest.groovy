package com.proper.enterprise.platform.auth.common.rule

import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.model.Role
import org.junit.Test
import org.springframework.test.context.jdbc.Sql

class GroupRuleTest extends BaseRuleTest {

    @Test
    @Sql("/com/proper/enterprise/platform/auth/common/service/impl/identity.sql")
    void allGroupRuleTest() {
        String userId = createUser("testGroupAll")
        createRule("groupAll", "GROUP")
        createRole("allGroup", "groupAll", "ALL")
        createGroup("test", userId)
        Collection<Role> roles = userService.getUserRoles(userId, EnableEnum.ENABLE)
        assert roles.size() == 1
        assert roles[0].getName() == "allGroup"
    }

    @Test
    @Sql("/com/proper/enterprise/platform/auth/common/service/impl/identity.sql")
    void specifiedGroupRuleTest() {
        String userId = createUser("testGroup")
        createRule("group", "GROUP")
        String groupId = createGroup("test", userId)
        Collection<Role> roles = userService.getUserRoles(userId, EnableEnum.ENABLE)
        assert roles.size() == 0
        createRole("onlyUser2", "group", groupId)
        Collection<Role> roles2 = userService.getUserRoles(userId, EnableEnum.ENABLE)
        assert roles2.size() == 1
        assert roles2[0].getName() == "onlyUser2"
    }
}
