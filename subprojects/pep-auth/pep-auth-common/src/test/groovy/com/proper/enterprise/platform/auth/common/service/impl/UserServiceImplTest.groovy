package com.proper.enterprise.platform.auth.common.service.impl

import com.proper.enterprise.platform.api.auth.dao.UserDao
import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.service.ResourceService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.sys.i18n.I18NUtil
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.bind.annotation.RequestMethod

@Sql("/com/proper/enterprise/platform/auth/common/service/impl/identity.sql")
class UserServiceImplTest extends AbstractTest {

    @Autowired
    private UserService userService
    @Autowired
    private UserDao userDao

    @Autowired
    private ResourceService resourceService

    private userInit = { ->
        def user = new UserEntity()
        user.setUsername('test')
        user.setPassword('test')
        user.setEmail('12345678@qq.com')
        user.setPhone('13333333333')
        user.setName('test')
        user.setSuperuser(true)
        user = userService.save(user)
        user as UserEntity
    }


    @Test
    void testSave() {

        def user = new UserEntity()
        user.setUsername('test')
        user.setPassword('test')
        user.setEmail('12345678@qq.com')
        user.setPhone('13333333333')
        user.setName('test')
        user = userService.save(user)
        assert 'test' == user.username

        def user2 = new UserEntity()
        user2.setId("40fb7973-abfd-4d13-b8d0-0f6d68825ff8")
        user2.setUsername('test2')
        user2.setPassword('test')
        user2.setEmail('12345678@qq.com')
        user2.setPhone('13333333333')
        user2.setName('test')
        user2 = userService.save(user2)
        assert "40fb7973-abfd-4d13-b8d0-0f6d68825ff8" != user2.getId()
    }

    @Test
    void test_SaveUsers() {
        def user = new UserEntity()
        user.setId('001')
        user.setUsername('test2')
        user.setPassword('test2')
        user.setEmail('12345678@qq.com')
        user.setPhone('13333333331')
        user.setName('test')

        def user2 = new UserEntity()
        user2.setId('002')
        user2.setUsername('test3')
        user2.setPassword('test4')
        user2.setEmail('12345678@qq.com')
        user2.setPhone('13333333332')
        user2.setName('test2')

        userService.save(user, user2)
    }


    @Test
    void test_Update() {
        def userEntity = userInit()
        def user = new UserEntity()
        user.setId(userEntity.getId())
        user.setName('test3')
        user.setPassword("ZL001")
        def update = userService.update(user)
        assert 'test3' == update.name

        user.setUsername("testuser2")
        try {
            userService.update(user)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.user.username.duplicated")
        }

    }

    @Test
    void testUpdateChangePassword() {
        def userEntity = userDao.findById("user3")
        def oldPassword = userEntity.getPassword()
        def changePasswordUser = userService.updateChangePassword(userEntity.id, "123456", 'test3')
        def newPassword = changePasswordUser.getPassword()
        assert oldPassword != newPassword
    }

    @Test
    void testGet() {
        def userEntity = userDao.findById("user3")
        def user = userService.get(userEntity.id)
        assert 'testuser3' == user.username
    }

    @Test
    void testGetByUsername() {
        def userEntity = userDao.findById("user3")
        def user = userService.getByUsername(userEntity.username, EnableEnum.ENABLE)
        assert 'testuser3' == user.username
    }

    @Test
    void testDelete() {
        def userEntity = userDao.findById("user3")
        assert userService.delete(userEntity.id)
    }

    @Test
    void testDeleteByIds() {
        assert userService.deleteByIds('user2,user3')
    }

    @Test
    void testGetUserMenus() {
        def userEntity = userDao.findById("user3")
        userEntity.setSuperuser(true)
        def menus = userService.getUserMenus(userEntity.id, EnableEnum.ENABLE)
        assert menus.size() > 0
    }

    @Test
    void testGetUserResources() {
        def resources1 = userService.getUserResources("userId", EnableEnum.ENABLE)
        assert null != resources1 && resources1.size() == 0
        def user = userDao.getByUsername("testuser2", EnableEnum.ENABLE)
        userService.getUserResources(user.getId(), EnableEnum.ENABLE)
        addResources()
        def userEntity = userInit()
        def resources = userService.getUserResources(userEntity.id, EnableEnum.ENABLE)
        assert resources.size() > 0
    }


    private void addResources() {
        def resource1 = new ResourceEntity()
        resource1.setName('resource1')
        resource1.setUrl('resource1')
        resource1.setMethod(RequestMethod.GET)
        resourceService.save(resource1)

        def resource2 = new ResourceEntity()
        resource2.setName('resource2')
        resource2.setUrl('resource2')
        resource2.setMethod(RequestMethod.GET)
        resourceService.save(resource2)
    }

    @Test
    void testGetUsersByIds() {
        def users = userService.getUsersByIds()
        assert users.size() == 0

        def ids = new ArrayList()
        ids.add("user2")
        ids.add("user3")
        def ids1 = userService.getUsersByIds(ids)
        assert ids1.size() == ids.size()
    }

    @Test
    void testGetCurrentUserByUserId() {
        String condition = "testuser2"
        def users = userService.getUsersByOrCondition(condition, EnableEnum.ENABLE)
        assert users.size() > 0
    }

    @Test
    void testGetUsersByAndCondition() {
        String userName = "testuser2"
        def users = userService.getUsersByAndCondition(userName, null, null, null, EnableEnum.ENABLE)
        assert users.size() > 0
    }

    @Test
    void testFindUsersPagination() {
        def dataTrunk = JSONUtil.parse(get("/auth/users?pageNo=1&pageSize=2&username=testuser2", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert dataTrunk.count > 0
    }

    @Test
    void testUpdateEnable() {
        def users = userService.updateEnable(null, false)
        assert users.size() == 0
        def ids = new ArrayList()
        ids.add("user2")
        ids.add("user3")
        def users1 = userService.updateEnable(ids, true)
        assert users1.size() == ids.size()
    }

    @Test
    void testAddUserRole() {
        def user = userService.addUserRole("user2", "role1")
        assert user.username == "testuser2"
    }

    @Test
    void testDeleteUserRole() {
        def user = userService.deleteUserRole("user2", "role2")
        assert user.username == "testuser2"
    }

    @Test
    void testGetFilterUsers() {
        def users = userDao.findAll()
        def users1 = userService.getFilterUsers(users, EnableEnum.ALL)
        assert users.size() == users1.size()

        def users2 = userService.getFilterUsers(users, EnableEnum.ENABLE)
        assert users2.size() == users.size()

        def users3 = userService.getFilterUsers(users, EnableEnum.DISABLE)
        assert 0 == users3.size()
    }

    @Test
    void testGetUserGroups() {
        def userGroups = userService.getUserGroups("user2")
        assert userGroups.size() > 0

        def userGroups1 = userService.getUserGroups("ppp", EnableEnum.ENABLE)
        assert 0 == userGroups1.size()

        def userGroups2 = userService.getUserGroups("user2", EnableEnum.ENABLE)
        assert userGroups2.size() > 0
    }

    @Test
    void testGetUserRoles() {
        def roles = userService.getUserRoles("user2")
        assert roles.size() > 0

        try {
            userService.getUserRoles("ppp", EnableEnum.ENABLE)
        } catch (ErrMsgException e) {
            assert e.getMessage() == I18NUtil.getMessage("pep.auth.common.user.get.failed")
        }

        def roles1 = userService.getUserRoles("user2", EnableEnum.ENABLE)
        assert roles1.size() > 0

    }


}
