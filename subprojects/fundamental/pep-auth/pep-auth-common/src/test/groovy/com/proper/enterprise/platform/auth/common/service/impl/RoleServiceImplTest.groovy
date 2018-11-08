package com.proper.enterprise.platform.auth.common.service.impl

import com.proper.enterprise.platform.api.auth.dao.RoleDao
import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql("/com/proper/enterprise/platform/auth/common/service/impl/identity.sql")
class RoleServiceImplTest extends AbstractJPATest {

    @Autowired
    private RoleService roleService
    @Autowired
    private RoleDao roleDao

    @Test
    void testGet() {
        def role = roleService.get("role1")
        assert role.name == "testrole1"
    }

    @Test
    void testSave() {
        def role = new RoleEntity()
        role.setDescription("desc")
        try {
            roleService.save(role)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.role.name.empty")
        }
        role.setName("testrole1")
        try {
            roleService.save(role)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.role.name.duplicate")
        }

        role.setName("test")
        role.setParentId("role1")
        def saveUser = roleService.save(role)
        assert saveUser.name == "test"

        role.setParentId(null)
        def saveUser2 = roleService.save(role)
        assert saveUser2.name == "test"

        role.setParentId("role1")
        def saveUser3 = roleService.save(role)
        assert saveUser3.name == "test"

        role.setParentId(saveUser3.getId())
        try {
            roleService.save(role)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.role.circle.error")
        }

    }

    @Test
    void testDelete() {
        def role = new RoleEntity()
        role.setName("test")
        role.setDescription("desc")
        def fail = roleService.delete(role)
        assert false == fail

        def save = roleService.save(role)
        def success = roleService.delete(save)
        assert true == success
    }

    @Test
    void testDeleteByIds() {
        String ids = ""
        def fail = roleService.deleteByIds(ids)
        assert false == fail
        ids = "role1,role2"
        try {
            roleService.deleteByIds(ids)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.role.delete.relation.failed")
        }
        ids = "role6"
        def success = roleService.deleteByIds(ids)
        assert true == success

        try {
            def role = roleService.get("role5") as RoleEntity
            role.setParentId("role6")
            roleService.update(role)
            roleService.deleteByIds("role6")
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.role.has.role")
        }
    }

    @Test
    void testUpdate() {
        def role = roleService.get("role6") as RoleEntity
        role.setParentId("role5")
        def updateRole = roleService.update(role)
        assert "testrole6" == updateRole.name
    }

    @Test
    void testFindRolesPagination() {
        def dataTrunk = JSONUtil.parse(get("/auth/roles?pageNo=1&pageSize=2&name=testrole2", HttpStatus.OK).response.contentAsString, DataTrunk.class)
        assert dataTrunk.data[0].name == "testrole2"
    }

    @Test
    void testFindRoles() {
        def name = "testrole2"
        def roles = roleService.findRoles(name, EnableEnum.ENABLE)
        assert roles.size() == 1
    }

    @Test
    void testFindRolesLike() {
        def name = "testrole2"
        def roles = roleService.findRolesLike(name, EnableEnum.ENABLE)
        assert roles.size() == 1

        roles = roleService.findRolesLike(name, null, null, EnableEnum.ENABLE)
        assert roles.size() == 1
    }

    @Test
    void testFindRoleParents() {
        def emptyList = roleService.findRoleParents("ppp")
        assert 0 == emptyList.size()
        def role = roleService.get("role5") as RoleEntity
        role.setParentId("role6")
        roleService.update(role)
        def roles = roleService.findRoleParents(role.getId())
        assert 1 == roles.size()
    }

    @Test
    void testGetRoleResources() {
        def roleId = ""
        def empty = roleService.getRoleResources(roleId, EnableEnum.ENABLE)
        assert 0 == empty.size()

        roleId = "role3"
        def role = roleService.get(roleId) as RoleEntity
        role.setParentId("role4")
        roleService.update(role)
        def resource = roleService.getRoleResources(roleId, EnableEnum.ENABLE)
        assert resource.size() == 1

        def all = roleDao.findAll()
        def resources = roleService.getRoleResources(all, EnableEnum.ENABLE)
        assert resources.size() > 0
    }

    @Test
    void testFindParentRoles() {
        def roleId = "role2"
        def roles = roleService.findParentRoles(roleId)
        assert roles.size() == 0
    }

    @Test
    void testAddRoleResources() {
        try {
            roleService.addRoleResources("ppp", new ArrayList<String>())
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.role.get.failed")
        }

        def roleId = "role2"
        def list = new ArrayList()
        list.add("995")
        def role = roleService.addRoleResources(roleId, list)
        assert role.getResources().size() > 0

        try {
            roleService.deleteRoleResources("ppp", "")
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.role.get.failed")
        }
        def resourecIds = "995"
        def role2 = roleService.deleteRoleResources(roleId, resourecIds)
        assert role2.getResources().size() == 0
    }

    @Test
    void testGetRoleUsers() {
        def users = roleService.getRoleUsers("", EnableEnum.ENABLE, EnableEnum.ENABLE)
        assert 0 == users.size()

        def roleId = "role1"
        def users1 = roleService.getRoleUsers(roleId, EnableEnum.ENABLE, EnableEnum.ALL)
        assert users1.size() == 1
    }

    @Test
    void testGetRoleUserGroups() {
        try {
            roleService.getRoleUserGroups("", EnableEnum.ENABLE, EnableEnum.ENABLE)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.role.get.failed")
        }

        def roleId = "role3"
        def groups = roleService.getRoleUserGroups(roleId, EnableEnum.ENABLE, EnableEnum.ENABLE)
        assert 1 == groups.size()
    }

    @Test
    void testGetUserGroupRolesByUserId() {
        def userId = "user2"
        def map = roleService.getUserGroupRolesByUserId(userId)
        assert map.get("role4") != null
    }

    @Test
    void testGetFilterRoles() {
        def all = roleDao.findAll()
        def roles = roleService.getFilterRoles(all)
        assert roles.size() > 0
        roleService.getFilterRoles(all, EnableEnum.ALL)
        roleService.getFilterRoles(all, EnableEnum.ENABLE)
        roleService.getFilterRoles(all, EnableEnum.DISABLE)
    }

    @Test
    void testUpdateEnable() {
        def list = new ArrayList()
        def empty = roleService.updateEnable(list, true)
        assert empty.size() == 0
        list.add("role2")
        list.add("role3")
        def roles = roleService.updateEnable(list, true)
        assert 2 == roles.size()
    }

    @Test
    void testGetRoleMenus() {
        roleService.getRoleMenus("", EnableEnum.ENABLE)
        def roleId = "role8"
        def role = roleService.get(roleId) as RoleEntity
        role.setParentId("role4")
        roleService.update(role)
        def menus = roleService.getRoleMenus(roleId, EnableEnum.ENABLE)
        assert menus.size() > 0
    }

    @Test
    void testAddRoleMenus() {
        try {
            roleService.addRoleMenus("", new ArrayList<String>())
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.role.get.failed")
        }
        roleService.addRoleMenus("role2", new ArrayList<String>())
        def ids = new ArrayList<String>()
        ids.add("999")
        ids.add("998")
        def role = roleService.addRoleMenus("role8", ids)
        assert role.getMenus().size() == 2

        roleService.addRoleMenus("role4", ids)

        def role1 = roleService.deleteRoleMenus("role8", "999,998")
        assert role1.getMenus().size() == 0

        try {
            roleService.deleteRoleMenus("", "999,998")
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.role.get.failed")
        }


    }


}
