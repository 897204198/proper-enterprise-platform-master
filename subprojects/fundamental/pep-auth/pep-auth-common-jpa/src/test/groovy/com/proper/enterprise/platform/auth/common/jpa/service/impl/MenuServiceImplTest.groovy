package com.proper.enterprise.platform.auth.common.jpa.service.impl

import com.proper.enterprise.platform.api.auth.dao.MenuDao
import com.proper.enterprise.platform.api.auth.dao.ResourceDao
import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql([
    "/com/proper/enterprise/platform/auth/common/jpa/resources.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/menus.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/menus-resources.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/roles.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/roles-menus.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/roles-resources.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/users.sql",
    "/com/proper/enterprise/platform/auth/common/jpa/users-roles.sql"
])
class MenuServiceImplTest extends AbstractJPATest {

    @Autowired
    MenuService service

    @Autowired
    MenuDao dao

    @Autowired
    ResourceService resourceService

    @Autowired
    ResourceDao resourceDao

    @Autowired
    MenuDao menuDao

    @Test
    void getMenuTree() {
        def menu = service.get('a2-m2-1')
        assert menu.getParent().getRoute() == '/a2/m2'
        assert menu.getApplication().getRoute() == '/a2'
        assert menu.getChildren().size() == 2

        def a1 = service.get('a1')
        assert a1.getApplication() == a1
        assert !a1.isLeaf()

        def leaf = service.get('a1-m1-3')
        assert leaf.isLeaf()
    }

    @Test
    void accessible() {
        // anyone could access resource not be defined
        assert service.accessible(null, null)
        // anyone could access resource without menu
        assert service.accessible(resourceService.get('test'), null)

        mockUser('test3', 't3')
        assert service.accessible(resourceService.get('test'), 'test3')
        // could not access resource without role
        assert !service.accessible(resourceService.get('test-d'), 'test3')

        mockUser('test2', 't2')
        assert service.accessible(resourceService.get('test'), 'test2')
        // could not access resource if role has no relationship with it
        assert !service.accessible(resourceService.get('test-d'), 'test2')
        // could access the resource with a role having it
        assert service.accessible(resourceService.get('test-u'), 'test2')
        // ??????????????????????????????????????????????????????
        assert !service.accessible(resourceService.get('test-enable'), 'test2')
        // ????????????????????????????????????????????????????????????
        //assert service.accessible(resourceService.get('test-valid'), 'test2')
        // normal user could not access resource without authorization
        assert !service.accessible(resourceService.get('test1'), 'test2')

        // super user could access everything
        mockUser('test1', 't1', 'pwd', true)
        assert service.accessible(null, 'test1')
        assert service.accessible(resourceService.get('test-menus'), 'test1')
        assert service.accessible(resourceService.get('test'), 'test1')
        assert service.accessible(resourceService.get('test-d'), 'test1')
        assert service.accessible(resourceService.get('test1'), 'test1')

        assert !service.accessible(resourceDao.get('test-enable'), 'test1')
        //assert !service.accessible(resourceDao.get('test-valid'), 'test1')
    }

}
