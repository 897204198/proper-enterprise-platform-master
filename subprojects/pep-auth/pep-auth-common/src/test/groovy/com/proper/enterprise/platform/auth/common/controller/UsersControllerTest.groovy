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
    private static UserEntity userEntity = new UserEntity('user1', 'pwd1')

    @Test
    void checkCRUD() {
        checkBaseCRUD(URI, userEntity)
    }

    @Autowired
    RoleService roleService

    @Autowired
    UserGroupService userGroupService

    @Sql("/com/proper/enterprise/platform/auth/common/roles.sql")
    @Test
    void addRolesToUserAndThenRemove() {
        UserEntity user = postAndReturn(URI, userEntity)
        def roles = roleService.getByName('testrole')
        assert !roles.isEmpty() && roles.size()==2
        user.add(roles[0])
        user.add(roles[1])
        user = putAndReturn(URI, user, HttpStatus.OK)
        assert user.roles.containsAll(roles)

        def role = roleService.get('role1')
        user.remove(role)
        assert putAndReturn(URI, user, HttpStatus.OK).roles.first().id == 'role2'
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
        def user = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        assert user.roleEntities.size() == 0
        post('/auth/users/' + user.id + '/role/' + 'role1', '', HttpStatus.CREATED)
        user = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        assert user.roleEntities.size() == 1
        assert user.roleEntities[0].id == 'role1'
        delete('/auth/users/' + user.id + '/role/' + 'role1', HttpStatus.NO_CONTENT)
        user = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        assert user.roleEntities.size() == 0
    }

    @Sql([
        "/com/proper/enterprise/platform/auth/common/usergroups.sql",
        "/com/proper/enterprise/platform/auth/common/users.sql"
    ])
    @Test
    void userGroupTest() {
        def user = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        assert user.userGroups.size() == 0
        def userGroup = userGroupService.get('group1')
        assert userGroup.users.size() == 0
        post('/auth/users/' + user.id + '/group/' + 'group1', '', HttpStatus.CREATED)
        user = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
//        assert user.userGroups.size() == 1 TODO 被控表字段未被更新
//        assert user.userGroups[0].id == 'group1'
        userGroup = userGroupService.get('group1')
        assert userGroup.users.size() == 1
        assert userGroup.users[0].id == 'test1'
        delete('/auth/users/' + user.id + '/group/' + 'group1', HttpStatus.NO_CONTENT)
        user = JSONUtil.parse(get('/auth/users/test1', HttpStatus.OK).getResponse().getContentAsString(), UserEntity.class)
        assert user.userGroups.size() == 0
        userGroup = userGroupService.get('group1')
        assert userGroup.users.size() == 0
    }

}
