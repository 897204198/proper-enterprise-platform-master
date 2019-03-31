package com.proper.enterprise.platform.auth.common.jpa.controller

import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.model.RetrievePasswordParam
import com.proper.enterprise.platform.api.auth.service.PasswordEncryptService
import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserGroupService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.api.auth.service.ValidCodeService
import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserGroupEntity
import com.proper.enterprise.platform.auth.common.jpa.repository.*
import com.proper.enterprise.platform.auth.common.vo.UserVO
import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.core.i18n.I18NService
import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.annotation.NoTx
import groovy.json.JsonSlurper
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class UsersControllerTest extends AbstractJPATest {

    private static final String URI = '/auth/users'
    private static UserVO userEntity = new UserVO('user1', 'pwd1')

    @Autowired
    RoleService roleService

    @Autowired
    UserGroupService userGroupService

    @Autowired
    UserRepository userRepository

    @Autowired
    MenuRepository menuRepository
    @Autowired
    RoleRepository roleRepository
    @Autowired
    ResourceRepository resourceRepository
    @Autowired
    UserGroupRepository userGroupRepository
    @Autowired
    DataDicRepository dataDicRepository
    @Autowired
    I18NService i18NService
    @Autowired
    PasswordEncryptService pwdService
    @Autowired
    UserService userService
    @Autowired
    ValidCodeService validCodeService

    @Sql("/com/proper/enterprise/platform/auth/common/jpa/users.sql")
    @Test
    void newCheckCRUD() {
        mockUser('test1', 't1', 'pwd')
        UserVO user = resOfPost(URI, userEntity)
        assert user.getUsername() == 'user1'
        def resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 4
        assert resAll.data[0].get("username") == 'user1'
        UserVO userDisable = new UserVO('user3', 'pwd1')
        userDisable.setEnable(false)
        resOfPost(URI, userDisable)
        def resAllDisEnable = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&userEnable=DISABLE&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAllDisEnable.count == 1
        assert resAllDisEnable.data.size() == 1
        def resAllDisAndEnable = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&userEnable=ALL&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAllDisAndEnable.count == 5
        assert resAllDisAndEnable.data.size() == 5

        def resAllPage = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAllPage.count == 4
        assert resAllPage.data.size() == 2
        def resAllCollect = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAllCollect.count == 4
        user.setName('new value')

        user.setPassword('w1')
        def value = put(URI + '/' + user.getId(), JSONUtil.toJSON(user), HttpStatus.OK).getResponse().getContentAsString()
        assert value.contains(pwdService.encrypt('w1'))

        def result = get(URI + '/' + user.getId(), HttpStatus.OK).getResponse().getContentAsString()
        result = (Map) JSONUtil.parse(result, java.lang.Object.class)
        assert result.get("name") == 'new value'


        UserVO userEntity1 = resOfPost(URI, new UserVO('user2', 'pwd2'))
        result = delete(URI + '/' + userEntity1.getId(), HttpStatus.NO_CONTENT).getResponse().getContentAsString()
        assert result == ''

        result = delete('/auth/users?ids=' + user.id, HttpStatus.NO_CONTENT).getResponse().getContentAsString()
        assert result == ''

        resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 3

    }

    @Sql("/com/proper/enterprise/platform/auth/common/jpa/users.sql")
    @Test
    @NoTx
    void userDetailTest() {
        mockUser('test1', 't1', 'pwd')
        def userReq = [:]
        userReq['username'] = 'td_username'
        userReq['name'] = 'td_name'
        userReq['password'] = 'td_password'
        userReq['email'] = 'td_email'
        userReq['phone'] = '12345678901'
        userReq['enable'] = true
        userReq['avatar'] = 'avatar'
        post(URI, JSONUtil.toJSON(userReq), HttpStatus.CREATED)

        def res = JSONUtil.parse(get('/auth/users?userNametd_username=&name=td_name&phone=12345678901&email=&enable=&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)

        assert res.count == 1
        assert res.data[0].get('username') == 'td_username'


        def id = res.data[0].get('id')

        def user = JSONUtil.parse(get(URI + '/' + id, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert user.get('phone') == '12345678901'
        assert user.get('name') == 'td_name'
        assert user.get('avatar') == 'avatar'
        def upUser = [:]
        upUser['name'] = 'up_name'
        upUser['password'] = 'up_password'
        upUser['email'] = 'up_email'
        upUser['phone'] = '12345678902'
        put(URI + '/' + id, JSONUtil.toJSON(upUser), HttpStatus.OK)

        user = JSONUtil.parse(get(URI + '/' + id, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert user.get('phone') == '12345678902'
        assert user.get('email') == 'up_email'

        delete(URI + '/' + id, HttpStatus.NO_CONTENT).getResponse().getContentAsString() == ''
    }

    @Sql("/com/proper/enterprise/platform/auth/common/jpa/users.sql")
    @Test
    void getUserTest() {
        mockUser('test1', 't1', 'pwd')
        def resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 3
        def resFullCondictions = JSONUtil.parse(get('/auth/users?userName=t1&name=c&phone=12345678901&email=test1@test.com&enable=Y&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resFullCondictions.count == 1
        assert resFullCondictions.data[0].get("username") == 't1'
        assert resFullCondictions.data[0].get("name") == 'c'

        def resEnableFalseQuery = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&userEnable=DISABLE&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resEnableFalseQuery.count == 0

        def resNone = JSONUtil.parse(get('/auth/users?userName=1212341&name=23423&phone=234234&email=234234&userEnable=DISABLE&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resNone.count == 0
    }

    @Sql("/com/proper/enterprise/platform/auth/common/jpa/users.sql")
    @Test
    void updateEnableTest() {
        mockUser('test1', 't1', 'pwd')
        def user1 = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        def user2 = JSONUtil.parse(get('/auth/users/test3', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        assert user1.enable
        assert user2.enable
        def reqMap = [:]
        reqMap['ids'] = ['test3']
        reqMap['enable'] = false
        put('/auth/users', JSONUtil.toJSON(reqMap), HttpStatus.OK)
        def value = JSONUtil.parse(get('/auth/users/' + user2.id, HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        assert !value.enable

        def reqMap1 = [:]
        reqMap1['ids'] = ['test3']
        reqMap1['enable'] = true
        put('/auth/users', JSONUtil.toJSON(reqMap1), HttpStatus.OK)
        def value1 = JSONUtil.parse(get('/auth/users/' + user2.id, HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        assert value1.enable

        def res = JSONUtil.parse(get('/auth/users?userName=t3&name=a&phone=12345678903&email=test3@test.com&enable=Y&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert res.count == 1

    }

    @Sql("/com/proper/enterprise/platform/auth/common/jpa/users.sql")
    @Test
    void deleteTest() {
        mockUser('test1', 't1', 'pwd')
        def resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 3
        assert delete('/auth/users?ids=test1', HttpStatus.INTERNAL_SERVER_ERROR).getResponse()
            .getContentAsString() == i18NService.getMessage("pep.auth.common.user.delete.role.super.failed")
        delete('/auth/users?ids=test2,test3', HttpStatus.NO_CONTENT)
        resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 1
    }

    @Sql([
        "/com/proper/enterprise/platform/auth/common/jpa/roles.sql",
        "/com/proper/enterprise/platform/auth/common/jpa/users.sql"
    ])
    @Test
    @NoTx
    void userRoleTest() {
        mockUser('test1', 't1', 'pwd')
        def resList = JSONUtil.parse(get('/auth/users/test1/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0
        post('/auth/users/test2/role/role1', '', HttpStatus.CREATED)
        resList = JSONUtil.parse(get('/auth/users/test2/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'role1'
        delete('/auth/users/test2/role/role1', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/users/test2/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0
        tearDown()
    }

    @Sql([
        "/com/proper/enterprise/platform/auth/common/jpa/usergroups.sql",
        "/com/proper/enterprise/platform/auth/common/jpa/users.sql"
    ])
    @Test
    @NoTx
    void userGroupTest() {
        mockUser('test1', 't1', 'pwd')
        post('/auth/user-groups/group1/user/test3', '', HttpStatus.CREATED)
        def resList = JSONUtil.parse(get('/auth/users/test3/user-groups', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'group1'
        delete('/auth/user-groups/group1/user/test3', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/users/test3/user-groups', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0
    }

    @Sql("/com/proper/enterprise/platform/auth/common/jpa/users.sql")
    @Test
    void userQueryTest() {
        mockUser('test1', 't1', 'pwd')
        // certain query
        def list = JSONUtil.parse(get('/auth/users/query?condition=t2', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert list.size() == 1
        assert list.get(0).username == 't2'
        assert list.get(0).name == 'b'
        assert list.get(0).phone == '12345678902'
        // part query
        list = JSONUtil.parse(get('/auth/users/query?condition=8901', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert list.size() == 1
        assert list.get(0).username == 't1'
        assert list.get(0).name == 'c'
        assert list.get(0).phone == '12345678901'
        // all query
        list = JSONUtil.parse(get('/auth/users/query?condition=', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert list.size() == 3
    }

    @Test
    void testGetUserRoles() {
        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('role1')
        roleEntity = roleService.save(roleEntity)
        RoleEntity roleEntity2 = new RoleEntity()
        roleEntity2.setName('role2')
        roleEntity = roleService.save(roleEntity2)

    }

    @Test
    @NoTx
    void testGetUserGroups() {

        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('role')
        roleEntity = roleService.save(roleEntity)

        RoleEntity roleEntity1 = new RoleEntity()
        roleEntity1.setName('role1')
        roleEntity1.setEnable(false)
        roleEntity1 = roleService.save(roleEntity1)

        RoleEntity roleEntity2 = new RoleEntity()
        roleEntity2.setName('role2')
        roleEntity2 = roleService.save(roleEntity2)


        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName('group')
        userGroupEntity = userGroupService.save(userGroupEntity)

        UserGroupEntity userGroupEntity1 = new UserGroupEntity()
        userGroupEntity1.setName('group1')
        userGroupEntity1.setEnable(false)
        userGroupEntity1 = userGroupService.save(userGroupEntity1)

        UserGroupEntity userGroupEntity2 = new UserGroupEntity()
        userGroupEntity2.setName('group2')
        userGroupEntity2 = userGroupService.save(userGroupEntity2)

        UserEntity userEntity1 = new UserEntity('u11', 'p11')
        userEntity1.setSuperuser(true)
        userEntity1.add(roleEntity)
        userEntity1.add(roleEntity1)
        userEntity1.add(roleEntity2)
        userEntity1.add(userGroupEntity)
        userEntity1.add(userGroupEntity1)
        userEntity1.add(userGroupEntity2)
        userEntity1 = userService.save(userEntity1)

        mockUser(userEntity1.getId(), userEntity1.getUsername(), userEntity1.getPassword())

        UserEntity userEntity2 = new UserEntity('user2', 'pwd2')
        userEntity2.setName('name2')
        userEntity2.setPhone('15439392930')
        userEntity2 = userService.save(userEntity2)

        UserEntity userEntity3 = new UserEntity('user3', 'pwd3')
        userEntity3.setName('name3')
        userEntity3.setPhone('15443935852')
        userEntity3 = userService.save(userEntity3)

        UserEntity userEntity4 = new UserEntity('user4', 'pwd4')
        userEntity4.setName('name4')
        userEntity4.setPhone('15897535483')
        userEntity4 = userService.save(userEntity4)
        def resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)


        def result = JSONUtil.parse(get('/auth/users/' + userEntity1.getId() + '/user-groups', HttpStatus.OK).getResponse().getContentAsString(),
            List.class)
        assert result.size() == 2
        result = get('/auth/users/safjsldfj/user-groups', HttpStatus.OK).getResponse().getContentAsString()
        assert result == '[]'

        UserGroupEntity userGroupEntity3 = userGroupService.get(userGroupEntity1.getId())
        assert userGroupEntity3.getId() == userGroupEntity1.getId()

        result = JSONUtil.parse(get('/auth/users/' + userEntity1.getId() + '/roles', HttpStatus.OK).getResponse().getContentAsString(),
            List.class)
        assert result.size() == 2
        assert get('/auth/users/safjsldfj/roles', HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() == i18NService.getMessage("pep.auth.common.user.get.failed")

        String condition = 'name'
        result = JSONUtil.parse(get('/auth/users/query?condition=' + condition, HttpStatus.OK).getResponse().getContentAsString(),
            List.class)
        assert result.size() == 3

        condition = '154'
        result = JSONUtil.parse(get('/auth/users/query?condition=' + condition, HttpStatus.OK).getResponse().getContentAsString(),
            List.class)
        assert result.size() == 2

        condition = 'user3'
        result = JSONUtil.parse(get('/auth/users/query?condition=' + condition, HttpStatus.OK).getResponse().getContentAsString(),
            List.class)
        assert result.size() == 1
    }

    @Test
    @NoTx
    void couldAddSameUsernameAfterDelete() {
        def userReq = [:]
        userReq['username'] = 'user'
        userReq['name'] = 'name'
        userReq['password'] = 'password'
        userReq['email'] = 'email'
        userReq['phone'] = '123'
        userReq['enable'] = true
        def newUser = new JsonSlurper().parseText(post(URI, JSONUtil.toJSON(userReq), HttpStatus.CREATED).response.contentAsString)

        def id = newUser.id
        delete("$URI?ids=$id", HttpStatus.NO_CONTENT)

        post(URI, JSONUtil.toJSON(userReq), HttpStatus.CREATED)
    }

    @Test
    @NoTx
    void addSameUsernameWillGetErrAfterDisable() {
        def userReq = [:]
        userReq['username'] = 'user_dup'
        userReq['name'] = 'name'
        userReq['password'] = 'password'
        userReq['email'] = 'email'
        userReq['phone'] = '123'
        userReq['enable'] = true
        def newUser = new JsonSlurper().parseText(post(URI, JSONUtil.toJSON(userReq), HttpStatus.CREATED).response.contentAsString)

        def id = newUser.id
        userReq['id'] = id
        userReq['enable'] = false
        put("$URI/$id", JSONUtil.toJSON(userReq), HttpStatus.OK)

        userReq.remove('id')
        userReq['enable'] = true
        def res = post(URI, JSONUtil.toJSON(userReq), HttpStatus.INTERNAL_SERVER_ERROR).response
        assert res.getHeader(PEPConstants.RESPONSE_HEADER_ERROR_TYPE) == PEPConstants.RESPONSE_BUSINESS_ERROR
    }

    @Test
    void changePassword() {

        def userReq = [:]
        userReq['username'] = 'user_dup'
        userReq['name'] = 'name'
        userReq['password'] = 'password'
        userReq['email'] = 'email'
        userReq['phone'] = '123'
        userReq['enable'] = true
        UserVO userVO = JSONUtil.parse(post(URI, JSONUtil.toJSON(userReq), HttpStatus.CREATED).response.contentAsString, UserVO.class)
        assert userVO.getPassword() == pwdService.encrypt('password')
        mockUser(userVO.getId(), userVO.getUsername())
        def changePassword = [:]
        changePassword['oldPassword'] = "123"
        changePassword['password'] = "456"
        put(URI + "/password", JSONUtil.toJSON(changePassword), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() == I18NUtil.getMessage("pep.auth.common.user.change.password.oldpassword.error")
        changePassword['oldPassword'] = "password"
        changePassword['password'] = "456"
        Authentication.setCurrentUserId(userVO.getId())
        UserVO changeVO = JSONUtil.parse(put(URI + "/password/", JSONUtil.toJSON(changePassword), HttpStatus.OK).getResponse().getContentAsString(), UserVO.class)
        assert changeVO.getPassword() == pwdService.encrypt('456')
    }


    @Test
    public void resetPassword() {
        def userReq = [:]
        userReq['username'] = 'user_dup'
        userReq['name'] = 'name'
        userReq['password'] = 'password'
        userReq['email'] = 'email'
        userReq['phone'] = '123'
        userReq['enable'] = true
        UserVO userVO = JSONUtil.parse(post(URI, JSONUtil.toJSON(userReq), HttpStatus.CREATED).response.contentAsString, UserVO.class)
        assert userVO.getPassword() == pwdService.encrypt('password')
        String validCode = validCodeService.getPasswordValidCode('user_dup')
        RetrievePasswordParam retrievePasswordParam = new RetrievePasswordParam()
        retrievePasswordParam.setUsername('user_dup')
        retrievePasswordParam.setPassword("456")
        retrievePasswordParam.setValidCode("weqe")
        assert I18NUtil.getMessage("pep.auth.common.password.retrieve.validCode.not.match") == put(URI + "/password/reset", JSONUtil.toJSON(retrievePasswordParam), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
        retrievePasswordParam.setValidCode(validCode)
        put(URI + "/password/reset", JSONUtil.toJSON(retrievePasswordParam), HttpStatus.OK)
        assert userService.getByUsername('user_dup', EnableEnum.ALL).getPassword() == pwdService.encrypt('456')
    }

    @Test
    void updateCurrent() {
        def userReq = [:]
        userReq['username'] = 'user_dup'
        userReq['name'] = 'name'
        userReq['password'] = 'password'
        userReq['email'] = 'email'
        userReq['phone'] = '123'
        userReq['enable'] = true
        UserVO userVO = JSONUtil.parse(post(URI, JSONUtil.toJSON(userReq), HttpStatus.CREATED).response.contentAsString, UserVO.class)
        assert userVO.getUsername() == 'user_dup'
        Authentication.setCurrentUserId(userVO.getId())

        def updateReq = [:]
        updateReq['username'] = 'user_update'
        UserVO userAfterVO = JSONUtil.parse(put(URI + "/current", JSONUtil.toJSON(updateReq), HttpStatus.OK).response.contentAsString, UserVO.class)
        assert userAfterVO.getUsername() == 'user_update'
    }

    @Test
    void getUserByUserNames() {
        def userReq = [:]
        userReq['username'] = 'user_dup'
        userReq['name'] = 'name'
        userReq['password'] = 'password'
        userReq['email'] = 'email'
        userReq['phone'] = '123'
        userReq['enable'] = true
        UserVO userVO = JSONUtil.parse(post(URI, JSONUtil.toJSON(userReq), HttpStatus.CREATED).response.contentAsString, UserVO.class)
        def userReq2 = [:]
        userReq2['username'] = 'user_dup2'
        userReq2['name'] = 'name'
        userReq2['password'] = 'password'
        userReq2['email'] = 'email'
        userReq2['phone'] = '123'
        userReq2['enable'] = true
        UserVO userVO2 = JSONUtil.parse(post(URI, JSONUtil.toJSON(userReq2), HttpStatus.CREATED).response.contentAsString, UserVO.class)
        List<UserVO> userVOS = JSONUtil.parse(get('/auth/users/usernames?usernames=user_dup,user_dup2', HttpStatus.OK).getResponse().getContentAsString(),
            List.class)
        assert userVOS.size() == 2;

    }

    @Test
    void getUserByIds() {
        def userReq = [:]
        userReq['username'] = 'user_dup'
        userReq['name'] = 'name'
        userReq['password'] = 'password'
        userReq['email'] = 'email'
        userReq['phone'] = '123'
        userReq['enable'] = true
        UserVO userVO = JSONUtil.parse(post(URI, JSONUtil.toJSON(userReq), HttpStatus.CREATED).response.contentAsString, UserVO.class)
        def userReq2 = [:]
        userReq2['username'] = 'user_dup2'
        userReq2['name'] = 'name'
        userReq2['password'] = 'password'
        userReq2['email'] = 'email'
        userReq2['phone'] = '123'
        userReq2['enable'] = true
        UserVO userVO2 = JSONUtil.parse(post(URI, JSONUtil.toJSON(userReq2), HttpStatus.CREATED).response.contentAsString, UserVO.class)
        List<UserVO> userVOS = JSONUtil.parse(get('/auth/users/ids?ids=' + userVO.getId() + ',' + userVO2.getId(), HttpStatus.OK).getResponse().getContentAsString(),
            List.class)
        assert userVOS.size() == 2;

    }

    @After
    void tearDown() {
        userRepository.deleteAll()
        userGroupRepository.deleteAll()
        roleRepository.deleteAll()
        menuRepository.deleteAll()
        resourceRepository.deleteAll()
        dataDicRepository.deleteAll()
    }
}
