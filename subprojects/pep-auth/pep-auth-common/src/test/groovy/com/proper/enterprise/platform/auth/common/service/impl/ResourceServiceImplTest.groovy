package com.proper.enterprise.platform.auth.common.service.impl

import com.proper.enterprise.platform.api.auth.dao.ResourceDao
import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity
import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.sys.i18n.I18NUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.bind.annotation.RequestMethod

@Sql("/com/proper/enterprise/platform/auth/common/service/impl/identity.sql")
class ResourceServiceImplTest extends AbstractTest {

    @Autowired
    private ResourceService resourceService
    @Autowired
    private ResourceDao resourceDao

    @Test
    void testSave() {
        def resource = new ResourceEntity()
        resource.setUrl("test")
        resource.setMethod(RequestMethod.GET)
        try {
            resourceService.save(resource)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.resource.name.empty")
        }
        resource.setName("resource1")
        try {
            resourceService.save(resource)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.resource.name.duplicate")
        }
        resource.setName("/resource")
        def save = resourceService.save(resource)
        assert save.getName() == "/resource"
    }

    @Test
    void testUpdate() {
        def resource = resourceService.get("996")
        resource.setResourceCode("2")
        def update = resourceService.update(resource)
        assert update.getName() == "resources1"
    }

    @Test
    void testGet() {
        def resource = resourceService.get("/resource1", RequestMethod.GET)
        assert resource.getName() == "resources1"
        resourceService.get("/auth/resources/aaa/roles", RequestMethod.GET)
    }

    @Test
    void testFindAll() {
        def ids = new ArrayList()
        ids.add("996")
        ids.add("995")
        ids.add("994")
        ids.add("993")
        resourceService.findAll(ids, EnableEnum.ENABLE)
        resourceService.findAll(ids, EnableEnum.DISABLE)
        resourceService.findAll(ids, EnableEnum.ALL)
    }

    @Test
    void testDelete() {
        def resource = resourceService.get("992")
        try {
            resourceService.delete(resource)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.resource.delete.relation.menu")
        }
        try {
            resourceService.delete(resourceService.get("996"))
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.resource.delete.relation.role")
        }
        resourceService.delete(resourceService.get("995"))

        assert true == resourceService.deleteByIds("994")
        assert false == resourceService.deleteByIds("")
    }

    @Test
    void testGetFilterResources() {
        def resources = resourceService.getFilterResources(resourceDao.findAll(), EnableEnum.ENABLE)
        assert resources.size() > 0
        def resources1 = resourceService.getFilterResources(resourceDao.findAll(), EnableEnum.DISABLE)
        assert resources1.size() == 1


    }

    @Test
    void testUpdateEnable() {
        def ids = new ArrayList()
        ids.add("995")
        ids.add("994")
        def enable = resourceService.updateEnable(ids, true)
        assert enable.size() == 2
    }

    @Test
    void testGetResourceMenus() {
        resourceService.getResourceMenus("", EnableEnum.ENABLE)
        def menus = resourceService.getResourceMenus("992", EnableEnum.ENABLE)
        assert menus.size() == 1
    }

    @Test
    void testGetResourceRoles() {
        resourceService.getResourceRoles("", EnableEnum.ENABLE)
        def roles = resourceService.getResourceRoles("996", EnableEnum.ENABLE)
        assert roles.size() == 1

    }


}
