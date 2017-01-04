package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql([
    "/com/proper/enterprise/platform/auth/common/resources.sql",
    "/com/proper/enterprise/platform/auth/common/menus.sql",
    "/com/proper/enterprise/platform/auth/common/menus-resources.sql",
    "/com/proper/enterprise/platform/auth/common/roles.sql",
    "/com/proper/enterprise/platform/auth/common/roles-menus.sql",
    "/com/proper/enterprise/platform/auth/common/users.sql",
    "/com/proper/enterprise/platform/auth/common/users-roles.sql"
])
class MenusControllerTest extends AbstractTest {

    @Test
    public void diffUsersGetDiffMenus() {
        // super user
        mockUser('test1', 't1', 'pwd', true)
        assert resOfGet('/auth/menus', HttpStatus.OK).size() == 14

        // has role
        mockUser('test2', 't2', 'pwd', false)
        assert resOfGet('/auth/menus', HttpStatus.OK).size() == 8

        // without role
        mockUser('test3', 't3', 'pwd', false)
        assert resOfGet('/auth/menus', HttpStatus.OK) == ''
    }

    @Test
    public void getMenusWithOrder() {
        mockUser('test2', 't2', 'pwd', false)
        def res = resOfGet('/auth/menus', HttpStatus.OK)
        assert res[0].root
        1..3.each { idx ->
            res[idx].with {
                assert id == "a2-m$idx"
                assert parentId != null
            }
        }
    }

}
