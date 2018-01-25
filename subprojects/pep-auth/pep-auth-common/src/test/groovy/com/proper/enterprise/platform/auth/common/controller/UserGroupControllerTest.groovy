package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.api.auth.model.UserGroup
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.DataRestrainEntity
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity
import com.proper.enterprise.platform.auth.common.repository.MenuRepository
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository
import com.proper.enterprise.platform.auth.common.repository.RoleRepository
import com.proper.enterprise.platform.auth.common.repository.UserGroupRepository
import com.proper.enterprise.platform.auth.common.repository.UserRepository
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
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

        // group add user
        post(URI + '/' + id1 + '/user/' + u1.id, '', HttpStatus.CREATED)
        post(URI + '/' + id1 + '/user/' + u2.id, '', HttpStatus.CREATED)
        def resList = JSONUtil.parse(get(URI + '/' + id1 + '/users', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 2
        assert resList.get(0).id == 'test1'
        assert resList.get(1).id == 'test2'
        // delete group's user
        delete(URI + '/' + id1 + '/user/' + u1.id, HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get(URI + '/' + id1 + '/users', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'test2'

        // group add role
        post(URI + '/' + id1 + '/role/role1', '', HttpStatus.CREATED)
        resList = JSONUtil.parse(get(URI + '/' + id1 + '/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'role1'
        // delete group's role
        delete(URI + '/' + id1 + '/role/role1', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get(URI + '/' + id1 + '/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        delete(URI + '/' + id1, HttpStatus.NO_CONTENT)
        delete(URI + '?ids=' + id2, HttpStatus.NO_CONTENT)
    }

    @Test
    void testPut(){
        def group1 = [:]
        group1['name'] = 'group-1'
        group1['description'] = 'group-1-des'
        group1['enable'] = true
        group1['seq'] = 1

        def g1 = JSONUtil.parse(post(URI,
            JSONUtil.toJSON(group1), HttpStatus.CREATED).getResponse().getContentAsString(), Map.class)
        assert g1.get('id') != null

        Map<String, Object> reqMap = new HashMap<>()
        reqMap.put("name",'name1')
        reqMap.put("enable", true)
        reqMap.put("description", 'description1')
        reqMap.put("seq",1)

        put('/auth/user-groups/'+g1.get('id'), JSONUtil.toJSON(reqMap), HttpStatus.OK)
        def single = JSONUtil.parse(get(URI + '/' + g1.get('id'), HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert single.get("id") == g1.get('id')
        assert single.get('name') == 'name1'

        DataRestrainEntity dataRestrainEntity = new DataRestrainEntity()
        dataRestrainEntity.setName('name1')
        dataRestrainEntity.setTableName('tablename')
        dataRestrainEntity.setSqlStr('select * from a')
        dataRestrainEntity.setFilterName('filter')
        assert dataRestrainEntity.getName() == 'name1'
        assert dataRestrainEntity.getTableName() == 'tablename'
        assert dataRestrainEntity.getSqlStr() == 'select * from a'
        assert dataRestrainEntity.getFilterName() == 'filter'
    }

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
