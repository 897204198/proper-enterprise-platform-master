package com.proper.enterprise.platform.auth.common.service.impl

import com.proper.enterprise.platform.api.auth.dao.UserGroupDao
import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.model.UserGroup
import com.proper.enterprise.platform.api.auth.service.UserGroupService
import com.proper.enterprise.platform.auth.common.jpa.entity.UserGroupEntity
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
class UserGroupServiceImplTest extends AbstractJPATest {

    @Autowired
    private UserGroupService userGroupService
    @Autowired
    private UserGroupDao userGroupDao

    @Test
    void testGet() {
        def id = "group1"
        def userGroup = userGroupService.get(id)
        assert userGroup.getName() == "testgroup1"

        def userGroup1 = userGroupService.get(id, EnableEnum.ENABLE)
        assert userGroup1.getName() == "testgroup1"
    }

    @Test
    void testSave() {
        def userGroup = new UserGroupEntity()
        userGroup.setName("testgroup1")
        userGroup.setDescription("test1")
        userGroup.setSeq(2)
        try {
            userGroupService.save(userGroup)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.usergroup.name.duplicate")
        }
        userGroup.setName("testgroup9999")
        def userGroup1 = userGroupService.save(userGroup)
        assert userGroup1.getName() == "testgroup9999"
        assert true == userGroupService.delete(userGroup)
        assert false == userGroupService.deleteByIds("")
        def ids = "group1,group2"
        try {
            userGroupService.deleteByIds(ids)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.usergroup.delete.relation.user")
        }
        assert true == userGroupService.deleteByIds("group3,ppp")
    }

    @Test
    void testUpdate() {
        def userGroup2 = userGroupService.get("group3")
        userGroup2.setName("testgroup8")
        def update = userGroupService.update(userGroup2)
        assert update.getName() == "testgroup8"

        userGroupService.updateEnable(new ArrayList<String>(), true)
        def list = new ArrayList()
        list.add("group1")
        list.add("group2")
        def userGroups = userGroupService.updateEnable(list, true)
        assert userGroups.size() == 2
    }

    @Test
    void testGetGroups() {
        def groups = userGroupService.getGroups("testgroup1", null, null)
        assert groups.size() == 1
        DataTrunk dataTrunk = JSONUtil.parse(get("/auth/user-groups?pageNo=1&pageSize=2&name=testgroup1", HttpStatus.OK).response.contentAsString, DataTrunk.class)
        assert dataTrunk.count == 1
    }

    @Test
    void testGetFilterGroups() {
        def userGroups = userGroupDao.findAll()
        def groups = userGroupService.getFilterGroups(userGroups)
        assert groups.size() > 0

        userGroupService.getFilterGroups(userGroups, EnableEnum.ALL)
        userGroupService.getFilterGroups(userGroups, EnableEnum.ENABLE)
        def group = userGroupService.get("group3")
        group.setEnable(false)
        userGroupService.update(group)
        def list = new ArrayList()
        list.add(group)
        userGroupService.getFilterGroups(list, EnableEnum.DISABLE)
    }

    @Test
    void testSaveUserGroupRole() {
        try {
            userGroupService.saveUserGroupRole("", "")
        } catch (ErrMsgException e) {
            assert e.getMessage() == "can't save because userGropu not find"
        }
        try {
            userGroupService.saveUserGroupRole("group1", "")
        } catch (ErrMsgException e) {
            assert e.getMessage() == "can't save because role not find"
        }
        def userGroup = userGroupService.saveUserGroupRole("group3", "role8")
        assert userGroup.getRoles().size() == 1

        try {
            userGroupService.deleteUserGroupRole("", "")
        } catch (ErrMsgException e) {
            assert e.getMessage() == "can't save because userGropu not find"
        }

        try {
            userGroupService.deleteUserGroupRole("group1", "")
        } catch (ErrMsgException e) {
            assert e.getMessage() == "can't save because role not find"
        }
        def userGroup1 = userGroupService.deleteUserGroupRole("group3", "role8")
        assert userGroup1.getRoles().size() == 0
    }

    @Test
    void testGetGroupRoles() {
        try {
            userGroupService.getGroupRoles("", EnableEnum.ENABLE, EnableEnum.ENABLE)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.usergroup.get.failed")
        }
        def roles = userGroupService.getGroupRoles("group2", EnableEnum.ENABLE, EnableEnum.ENABLE)
        assert roles.size() == 1
    }


    @Test
    void testAddGroupUser() {
        try {
            userGroupService.addGroupUser("", "")
        } catch (ErrMsgException e) {
            assert e.getMessage() == "can't save because userGropu not find"
        }

        try {
            userGroupService.addGroupUser("group1", "")
        } catch (ErrMsgException e) {
            assert e.getMessage() == "can't save because user not find"
        }
        userGroupService.addGroupUser("group3", "user3")
        try {
            userGroupService.deleteGroupUser("", "")
        } catch (ErrMsgException e) {
            assert e.getMessage() == "can't save because userGropu not find"
        }

        try {
            userGroupService.deleteGroupUser("group2", "")
        } catch (ErrMsgException e) {
            assert e.getMessage() == "can't save because user not find"
        }
        userGroupService.deleteGroupUser("group3", "user3")
        def userGroup = userGroupService.deleteGroupUsers("group2", "user2,ppp")
        assert userGroup.getUsers().size() == 1
    }

    @Test
    void testAddGroupUserByUserIds() {
        try {
            userGroupService.addGroupUserByUserIds("", new ArrayList<String>())
        } catch (ErrMsgException e) {
            assert e.getMessage() == "can't save because userGropu not find"
        }

        def ids = new ArrayList()
        ids.add("user3")
        def userGroup = userGroupService.addGroupUserByUserIds("group3", ids)
        assert userGroup.getUsers().size() == 1
    }

    @Test
    void testGetGroupUsers() {
        try {
            userGroupService.getGroupUsers("", EnableEnum.ENABLE, EnableEnum.ENABLE)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.usergroup.get.failed")
        }
        def users = userGroupService.getGroupUsers("group2", EnableEnum.ENABLE, EnableEnum.ENABLE)
        assert users.size() == 2
    }

    @Test
    void testGetGroupResources() {
        def groups = userGroupService.getGroups("testgroup1", "testgroup1", EnableEnum.ENABLE)
        userGroupService.getGroupResources(new ArrayList<UserGroup>(), EnableEnum.ENABLE)
        def resources = userGroupService.getGroupResources(groups, EnableEnum.ENABLE)
        assert resources.size() == 1

        def group = userGroupService.get("group1")
        def resources1 = userGroupService.getGroupResources(group, EnableEnum.ENABLE)
        assert resources1.size() == 1
    }

    @Test
    void testCreateUserGroup() {
        def group = new UserGroupEntity()
        group.setName("testgroup1")
        try {
            userGroupService.createUserGroup(group)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.usergroup.name.duplicate")
        }
        group.setName("testgroup99")
        def group1 = userGroupService.createUserGroup(group)
        assert group1.getName() == "testgroup99"

    }


}
