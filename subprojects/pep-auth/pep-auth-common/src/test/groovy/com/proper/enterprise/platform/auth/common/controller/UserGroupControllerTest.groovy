package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.api.auth.model.UserGroup
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity
import com.proper.enterprise.platform.auth.common.repository.RoleRepository
import com.proper.enterprise.platform.auth.common.repository.UserGroupRepository
import com.proper.enterprise.platform.auth.common.repository.UserRepository
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class UserGroupControllerTest extends AbstractTest {

    private static final String URI = '/auth/user-groups'
    private static UserGroup group = new UserGroupEntity('oopstrom')

    @Autowired
    private UserService userService

    @Autowired
    private UserRepository userRepo

    @Autowired
    private UserGroupRepository userGroupRepo

    @Autowired
    private RoleRepository roleRepo

    @Test
    void checkCRUD() {
        checkBaseCRUD(URI, group)
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/auth/common/users.sql",
          "/com/proper/enterprise/platform/auth/common/roles.sql"
        ])
    void userGroupUnionTest() {
        def group1 = [:]
        group1['name'] = 'group-1'
        group1['description'] = 'group-1-des'
        group1['enable'] = true
        group1['seq'] = 1

        def group2 = [:]
        group2['name'] = 'group-2'
        group2['description'] = 'group-2-des'
        group2['enable'] = true
        group2['seq'] = 2

        def g2 = JSONUtil.parse(post(URI,
            JSONUtil.toJSON(group2), HttpStatus.CREATED).getResponse().getContentAsString(), Map.class)
        def g1 = JSONUtil.parse(post(URI,
            JSONUtil.toJSON(group1), HttpStatus.CREATED).getResponse().getContentAsString(), Map.class)

        assert g1.get('id') != null
        assert g2.get('id') != null

        def id1 = g1.get('id')
        def id2 = g2.get('id')

        def query = JSONUtil.parse(get(URI + '?name=group-1&description=group-1-des&enable=Y', HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)

        assert query.size() == 1
        assert query.get(0).id == id1
        assert query.get(0).name == 'group-1'
        assert query.get(0).enable

        query = JSONUtil.parse(get(URI, HttpStatus.OK).getResponse().getContentAsString(), List.class)

        assert query.size() == 2
        assert query.get(1).id == id2
        assert query.get(1).name == 'group-2'
        assert query.get(1).enable

        def updateReq = [:]
        updateReq['ids'] = [id1, id2]
        updateReq['enable'] = false
        put(URI, JSONUtil.toJSON(updateReq), HttpStatus.OK)
        query = JSONUtil.parse(get(URI, HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert query.size() == 2
        assert !query.get(0).enable
        assert !query.get(1).enable

        updateReq['enable'] = true
        put(URI, JSONUtil.toJSON(updateReq), HttpStatus.OK)

        def single = JSONUtil.parse(get(URI + '/' + id1, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert single != null
        assert single.get('name') == 'group-1'
        assert single.get('description') == 'group-1-des'
        assert single.get('enable')
        assert single.get('seq') == 1

        def u1 = userService.getByUsername('t1')
        def u2 = userService.getByUsername('t2')

        post(URI + '/' + id1 + '/user/' + u1.id, '', HttpStatus.CREATED)
        post(URI + '/' + id1 + '/user/' + u2.id, '', HttpStatus.CREATED)
        post(URI + '/' + id1 + '/role/role1', '', HttpStatus.CREATED)

        single = JSONUtil.parse(get(URI + '/' + id1, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert single.get('users').size == 2
        assert single.get('users')[0].id == 'test1'
        assert single.get('users')[1].id == 'test2'

        assert single.get('roles').size == 1
        assert single.get('roles')[0].id == 'role1'

        delete(URI + '/' + id1 + '/user/' + u1.id, HttpStatus.NO_CONTENT)
        delete(URI + '/' + id1 + '/role/role1', HttpStatus.NO_CONTENT)

        single = JSONUtil.parse(get(URI + '/' + id1, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert single.get('users').size == 1
        assert single.get('users')[0].id == 'test2'

        assert single.get('roles').size == 0
    }
}
