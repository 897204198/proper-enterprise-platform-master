package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserGroupService
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class UsersControllerTest extends AbstractTest {

    private static final String URI = '/auth/users'

    @Autowired
    RoleService roleService

    @Autowired
    UserGroupService userGroupService

    @Sql("/com/proper/enterprise/platform/auth/common/users.sql")
    @Test
    void userDetailTest() {
        mockUser('td_id', 'td_username', 'td_password', true)
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
        upUser['enable'] = false
        put(URI + '/' + id, JSONUtil.toJSON(upUser), HttpStatus.OK)

        user = JSONUtil.parse(get(URI + '/' + id, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert user.get('phone') == '12345678902'
        assert user.get('email') == 'up_email'

        delete(URI + '/' + id, HttpStatus.NO_CONTENT)
        get(URI + '/' + id, HttpStatus.OK).getResponse().getContentAsString() == ''
    }

    @Sql("/com/proper/enterprise/platform/auth/common/users.sql")
    @Test
    void getUserTest() {
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
        assert resEnableFalseQuery.count == 1
        assert resEnableFalseQuery.data[0].get("username") == 't2'
        assert resEnableFalseQuery.data[0].get("name") == 'b'

        def resNone = JSONUtil.parse(get('/auth/users?userName=1212341&name=23423&phone=234234&email=234234&enable=N&pageNo=1&pageSize=2',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resNone.count == 0
    }

    @Sql("/com/proper/enterprise/platform/auth/common/users.sql")
    @Test
    void updateEnableTest(){
        def user1 = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        def user2 = JSONUtil.parse(get('/auth/users/test3', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        assert user1.enable
        assert user2.enable
        def reqMap = [:]
        reqMap['ids'] = ['test1', 'test3']
        reqMap['enable'] = false
        put('/auth/users', JSONUtil.toJSONIgnoreException(reqMap), HttpStatus.OK)
        user1 = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        user2 = JSONUtil.parse(get('/auth/users/test3', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        assert !user1.enable
        assert !user2.enable
    }

    @Sql("/com/proper/enterprise/platform/auth/common/users.sql")
    @Test
    void deleteTest(){
        def resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 3
        delete('/auth/users?ids=test1,test2,test3', HttpStatus.NO_CONTENT)
        resAll = JSONUtil.parse(get('/auth/users?userName=&name=&phone=&email=&enable=&pageNo=1&pageSize=10',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 0
    }

    @Sql([
        "/com/proper/enterprise/platform/auth/common/roles.sql",
        "/com/proper/enterprise/platform/auth/common/users.sql"
    ])
    @Test
    void userRoleTest() {
        def user = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert user.get('roles').size() == 0
        post('/auth/users/' + user.id + '/role/' + 'role1', '', HttpStatus.CREATED)
        user = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert user.get('roles').size() == 1
        assert user.get('roles')[0].id == 'role1'
        delete('/auth/users/' + user.id + '/role/' + 'role1', HttpStatus.NO_CONTENT)
        user = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert user.get('roles').size() == 0
    }

}
