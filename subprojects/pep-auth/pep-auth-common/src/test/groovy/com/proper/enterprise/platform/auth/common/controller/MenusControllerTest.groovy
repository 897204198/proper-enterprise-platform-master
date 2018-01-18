package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.auth.common.dictionary.MenuType
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql([
    "/com/proper/enterprise/platform/auth/common/resources.sql",
    "/com/proper/enterprise/platform/auth/common/menus.sql",
    "/com/proper/enterprise/platform/auth/common/menus-resources.sql",
    "/com/proper/enterprise/platform/auth/common/roles.sql",
    "/com/proper/enterprise/platform/auth/common/roles-menus.sql",
    "/com/proper/enterprise/platform/auth/common/users.sql",
    "/com/proper/enterprise/platform/auth/common/users-roles.sql",
    "/com/proper/enterprise/platform/auth/common/datadics.sql"
])
class MenusControllerTest extends AbstractTest {

    @Autowired
    MenuType menuType

    @Test
    void diffUsersGetDiffMenus() {
        // super user
        mockUser('test1', 't1', 'pwd', true)
        assert resOfGet('/auth/menus', HttpStatus.OK).size() == 14

        // super user with condition
        mockUser('test1', 't1', 'pwd', true)
        assert resOfGet('/auth/menus?name=1&description=des&route=a1&enable=Y', HttpStatus.OK).size() == 1

        // has role
        mockUser('test2', 't2', 'pwd', false)
        assert resOfGet('/auth/menus', HttpStatus.OK).size() == 8

        // without role
        mockUser('test3', 't3', 'pwd', false)
        assert resOfGet('/auth/menus', HttpStatus.OK) == []
    }

    @Test
    void getMenusWithOrder() {
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

    @Test
    void menuUnionTest() {
        def menu = [:]
        menu['icon'] = 'test_icon'
        menu['name'] = 'test_name1'
        menu['route'] = '/bbb'
        menu['enable'] = false
        menu['sequenceNumber'] = 55
        menu['menuCode'] = '1'
        def menuObj = JSONUtil.parse(post('/auth/menus', JSONUtil.toJSON(menu), HttpStatus.CREATED)
            .getResponse().getContentAsString(), Map.class)
        def id = menuObj.get('id')
        menuObj = JSONUtil.parse(get('/auth/menus/' + id,  HttpStatus.OK)
            .getResponse().getContentAsString(), Map.class)
        assert menuObj.get("icon") == 'test_icon'

        menu['icon'] = 'test_icon_change'
        put('/auth/menus/' + id, JSONUtil.toJSON(menu), HttpStatus.OK)
        menuObj = JSONUtil.parse(get('/auth/menus/' + id,  HttpStatus.OK)
            .getResponse().getContentAsString(), Map.class)
        assert menuObj.get("icon") == 'test_icon_change'

        delete('/auth/menus?ids=' + id, HttpStatus.NO_CONTENT)
        get('/auth/menus/' + id,  HttpStatus.OK).getResponse().getContentAsString() == ''

        def parents = JSONUtil.parse(get('/auth/menus/parents',  HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)
        assert parents.size() == 0 //TODO
    }

    @Sql("/com/proper/enterprise/platform/auth/common/datadics.sql")
    @Test
    void testMenuType() {
        menuType.getCatalog()
        menuType.page().getCode()
        menuType.app().getCode()
        menuType.function().getCode()
    }

}
