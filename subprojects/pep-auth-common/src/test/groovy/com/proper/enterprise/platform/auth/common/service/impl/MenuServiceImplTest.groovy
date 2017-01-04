package com.proper.enterprise.platform.auth.common.service.impl
import com.proper.enterprise.platform.api.auth.service.MenuService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@Sql("/com/proper/enterprise/platform/auth/common/menus.sql")
class MenuServiceImplTest extends AbstractTest {

    @Autowired
    MenuService service

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

}
