package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql(["/com/proper/enterprise/platform/auth/common/roles.sql",
      "/com/proper/enterprise/platform/auth/common/menus.sql"
    ])
class RolesControllerTest extends AbstractTest {

    @Test
    void rolesUnionTest() {
        def roles = JSONUtil.parse(get('/auth/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert roles.size() == 2
        assert roles.get(0).enable
        assert roles.get(1).enable

        def updateEnable = [:]
        updateEnable['ids'] = ['role1', 'role2']
        updateEnable['enable'] = false
        put('/auth/roles', JSONUtil.toJSON(updateEnable), HttpStatus.OK)
        roles = JSONUtil.parse(get('/auth/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert roles.size() == 2
        assert !roles.get(0).enable
        assert !roles.get(1).enable

        roles = JSONUtil.parse(get('/auth/roles?name=testrole&description=des&enable=N', HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)
        assert roles.size() == 1
        assert roles.get(0).id == 'role1'

        def req = [:]
        req['name'] = 'req_test_name'
        req['description'] = 'req_test_description'
        req['enable'] = true
//        req['parentId'] = 'req_test_parentId' TODO
        def role = JSONUtil.parse(post('/auth/roles', JSONUtil.toJSON(req), HttpStatus.CREATED).getResponse().getContentAsString(), Map.class)
        assert role != null
        def id = role.get('id')

        role = JSONUtil.parse(get('/auth/roles/' + id, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert role.get('description') == 'req_test_description'
        assert role.get('enable')
        assert role.get('name') == 'req_test_name'

        def updateReq = [:]
        updateReq['name'] = 'updateReq_test_name'
        updateReq['description'] = 'updateReq_test_description'
        updateReq['enable'] = false
//        req['parentId'] = 'updateReq_test_parentId' TODO
        put('/auth/roles/' + id, JSONUtil.toJSON(updateReq), HttpStatus.OK)
        def addReq = [:]
        addReq['ids'] = 'a1,a2'
        post('/auth/roles/' + id + '/menu', JSONUtil.toJSON(addReq), HttpStatus.CREATED)

        role = JSONUtil.parse(get('/auth/roles/' + id, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert role.get('description') == 'updateReq_test_description'
        assert !role.get('enable')
        assert role.get('name') == 'updateReq_test_name'
        assert role.get('menus').size == 2
        assert role.get('menus')[0].id == 'a1'
        assert role.get('menus')[1].id == 'a2'

        delete('/auth/roles/' + id + '/menu?ids=a1,a2', HttpStatus.NO_CONTENT)
        role = JSONUtil.parse(get('/auth/roles/' + id, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert role.get('menus').size == 0

        delete('/auth/roles?ids=' + id, HttpStatus.NO_CONTENT)
        get('/auth/roles/' + id,  HttpStatus.OK).getResponse().getContentAsString() == ''

        def parents = JSONUtil.parse(get('/auth/roles/parents',  HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)
        assert parents.size() == 0 //TODO

    }
}
