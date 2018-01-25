package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserGroupService
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity
import com.proper.enterprise.platform.auth.common.repository.MenuRepository
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository
import com.proper.enterprise.platform.auth.common.repository.RoleRepository
import com.proper.enterprise.platform.auth.common.repository.UserGroupRepository
import com.proper.enterprise.platform.auth.common.repository.UserRepository
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class UsersControllerTest extends AbstractTest {

    private static final String URI = '/auth/users'
    private static UserEntity userEntity = new UserEntity('user1', 'pwd1')

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

    @Sql("/com/proper/enterprise/platform/auth/common/users.sql")
    @Test
    void newCheckCRUD() {
        mockUser('test1', 't1', 'pwd')
        UserEntity user = postAndReturn(URI, userEntity)
        assert user.getUsername() == 'user1'
        def resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 4
        assert resAll.data[0].get("username") == 'user1'

        user.setName('new value')
        put(URI + '/' + user.getId(), JSONUtil.toJSON(user), HttpStatus.OK).getResponse().getContentAsString()

        //添加用户组
        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName('group1')
        userGroupEntity.setSeq(1)
        userGroupEntity.setEnable(true)
        userGroupEntity.add(user)
        userGroupEntity = userGroupService.save(userGroupEntity)

        def result = get(URI + '/' + user.getId(), HttpStatus.OK).getResponse().getContentAsString()
        result = (Map)JSONUtil.parse(result, java.lang.Object.class)
        assert result.get("name") == 'new value'


        UserEntity userEntity1 = postAndReturn(URI, new UserEntity('user2', 'pwd2'))
        result = delete(URI + '/' + userEntity1.getId(), HttpStatus.NO_CONTENT).getResponse().getContentAsString()
        assert result == ''

        result = delete('/auth/users?ids=' + user.id, HttpStatus.NO_CONTENT).getResponse().getContentAsString()
        assert result == ''

        resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 3

    }

    @Sql("/com/proper/enterprise/platform/auth/common/users.sql")
    @Test
    void userDetailTest() {
        mockUser('test1', 't1', 'pwd')
        def userReq = [:]
        userReq['username'] = 'td_username'
        userReq['name'] = 'td_name'
        userReq['password'] = 'td_password'
        userReq['email'] = 'td_email'
        userReq['phone'] = '12345678901'
        userReq['enable'] = true
        userReq['superuser'] = true

        post(URI, JSONUtil.toJSON(userReq), HttpStatus.CREATED)

        def res = JSONUtil.parse(get('/auth/users?userNametd_username=&name=td_name&phone=12345678901&email=&enable=&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)

        assert res.count == 1
        assert res.data[0].get('username') == 'td_username'

        def id = res.data[0].get('id')

        def user = JSONUtil.parse(get(URI + '/' + id, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert user.get('phone') == '12345678901'
        assert user.get('name') == 'td_name'

        def upUser = [:]
        upUser['name'] = 'up_name'
        upUser['password'] = 'up_password'
        upUser['email'] = 'up_email'
        upUser['phone'] = '12345678902'
        put(URI + '/' + id, JSONUtil.toJSON(upUser), HttpStatus.OK)

        user = JSONUtil.parse(get(URI + '/' + id, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert user.get('phone') == '12345678902'
        assert user.get('email') == 'up_email'

        delete(URI + '/' + id, HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService.getMessage(
            "pep.auth.common.user.delete.role.super.failed")
        get(URI + '/' + id, HttpStatus.OK).getResponse().getContentAsString() == ''
    }

    @Sql("/com/proper/enterprise/platform/auth/common/users.sql")
    @Test
    void getUserTest() {
        mockUser('test1', 't1', 'pwd')
        def resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 3
        assert resAll.data[0].get("username") == 't3'
        assert resAll.data[0].get("name") == 'a'
        assert resAll.data[1].get("username") == 't2'
        assert resAll.data[1].get("name") == 'b'

        def resFullCondictions = JSONUtil.parse(get('/auth/users?userName=t1&name=c&phone=12345678901&email=test1@test.com&enable=Y&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resFullCondictions.count == 1
        assert resFullCondictions.data[0].get("username") == 't1'
        assert resFullCondictions.data[0].get("name") == 'c'

        def resEnableFalseQuery = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=N&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resEnableFalseQuery.count == 0

        def resNone = JSONUtil.parse(get('/auth/users?userName=1212341&name=23423&phone=234234&email=234234&enable=N&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resNone.count == 0
    }

    @Sql("/com/proper/enterprise/platform/auth/common/users.sql")
    @Test
    void updateEnableTest(){
        mockUser('test1', 't1', 'pwd')
        def user1 = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        def user2 = JSONUtil.parse(get('/auth/users/test3', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        assert user1.enable
        assert user2.enable
        def reqMap = [:]
        reqMap['ids'] = ['test3']
        reqMap['enable'] = false
        put('/auth/users', JSONUtil.toJSONIgnoreException(reqMap), HttpStatus.OK)
        user1 = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        assert user1.enable
    }

    @Sql("/com/proper/enterprise/platform/auth/common/users.sql")
    @Test
    void deleteTest(){
        mockUser('test1', 't1', 'pwd')
        def resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 3
        assert delete('/auth/users?ids=test1', HttpStatus.BAD_REQUEST).getResponse()
            .getContentAsString() == i18NService.getMessage("pep.auth.common.user.delete.role.super.failed")
        delete('/auth/users?ids=test2,test3', HttpStatus.NO_CONTENT)
        resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 1
    }

    @Sql([
        "/com/proper/enterprise/platform/auth/common/roles.sql",
        "/com/proper/enterprise/platform/auth/common/users.sql"
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

    @Test
    void testNoPermission() {
        def deletefailed = i18NService.getMessage("pep.auth.common.user.permission.failed")
        def result = null
        Map<String, Object> map = new HashMap<>()
        String mapstr = JSONUtil.toJSON(map)

        result = get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=10',
            HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == deletefailed

        result = put('/auth/users', mapstr, HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == deletefailed

        result = post('/auth/users', JSONUtil.toJSON(new UserEntity('s', 's')), HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == deletefailed

        result = delete('/auth/users?ids=', HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == deletefailed

        result = get('/auth/users/1', HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == deletefailed

        result = put('/auth/users/1', mapstr, HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == deletefailed

        result = delete('/auth/users/1', HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == deletefailed

        result = post('/auth/users/1/role/1', '', HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == deletefailed

        result = delete('/auth/users/1/role/1', HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == deletefailed

    }

    @Sql([
        "/com/proper/enterprise/platform/auth/common/usergroups.sql",
        "/com/proper/enterprise/platform/auth/common/users.sql"
    ])
    @Test
    @NoTx
    void userGroupTest() {
        post('/auth/user-groups/group1/user/test3', '', HttpStatus.CREATED)
        def resList = JSONUtil.parse(get('/auth/users/test3/user-groups', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'group1'
        delete('/auth/user-groups/group1/user/test3', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/users/test3/user-groups', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0
    }

    @Sql("/com/proper/enterprise/platform/auth/common/users.sql")
    @Test
    void userQueryTest() {
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
        assert list.get(0).username == 't3'
        assert list.get(1).username == 't2'
        assert list.get(2).username == 't1'
    }

    @After
    void tearDown() {
        userGroupRepository.deleteAll()
        userRepository.deleteAll()
        roleRepository.deleteAll()
        menuRepository.deleteAll()
        resourceRepository.deleteAll()
        dataDicRepository.deleteAll()
    }
}
