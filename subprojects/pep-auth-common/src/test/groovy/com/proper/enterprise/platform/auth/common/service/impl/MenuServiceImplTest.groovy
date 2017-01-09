package com.proper.enterprise.platform.auth.common.service.impl
import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
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
class MenuServiceImplTest extends AbstractTest {

    @Autowired
    MenuService service

    @Autowired
    ResourceService resourceService

    @Test
    public void getMenuTree() {
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
    public void accessible() {
        // anyone could access resource not be defined
        assert service.accessible(null)
        // anyone could access resource without menu
        assert service.accessible(resourceService.get('test'))

        mockUser('test3', 't3')
        assert service.accessible(resourceService.get('test'))
        // could not access resource without role
        assert !service.accessible(resourceService.get('test-d'))

        mockUser('test2', 't2')
        assert service.accessible(resourceService.get('test'))
        assert service.accessible(resourceService.get('test-d'))
        // normal user could not access resource without authorization
        assert !service.accessible(resourceService.get('test1'))

        // super user could access everything
        mockUser('test1', 't1', 'pwd', true)
        assert service.accessible(null)
        assert service.accessible(resourceService.get('test'))
        assert service.accessible(resourceService.get('test-d'))
        assert service.accessible(resourceService.get('test1'))
    }

}
