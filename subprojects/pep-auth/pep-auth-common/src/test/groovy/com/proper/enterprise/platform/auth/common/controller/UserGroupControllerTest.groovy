package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.api.auth.model.UserGroup
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity
import com.proper.enterprise.platform.auth.common.repository.MenuRepository
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository
import com.proper.enterprise.platform.auth.common.repository.RoleRepository
import com.proper.enterprise.platform.auth.common.repository.UserGroupRepository
import com.proper.enterprise.platform.auth.common.repository.UserRepository
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
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
    private RoleRepository roleRepo

    @Autowired
    MenuRepository menuRepository
    @Autowired
    ResourceRepository resourceRepository
    @Autowired
    UserGroupRepository userGroupRepository
    @Autowired
    DataDicRepository dataDicRepository

    @Test
    @Sql(["/com/proper/enterprise/platform/auth/common/users.sql",
          "/com/proper/enterprise/platform/auth/common/roles.sql"
        ])
    @NoTx
    void userGroupUnionTest() {

        mockUser('test1', 't1', 'pwd')

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
        assert single.size() == 5
        assert single.get("id") == id1
        assert single.get("name") == 'group-1'

        delete(URI + '/' + id1 + '/user/' + u1.id, HttpStatus.NO_CONTENT)
        delete(URI + '/' + id1 + '/role/role1', HttpStatus.NO_CONTENT)

        single = JSONUtil.parse(get(URI + '/' + id1, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert single.get('name') == 'group-1'
        assert single.get('description') == 'group-1-des'
        assert single.get('enable')

        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName("gourp-22221")
        userGroupEntity.setSeq(3)
        userGroupEntity.setDescription("ddddd")
        userGroupEntity = userGroupRepository.saveAndFlush(userGroupEntity)

        Map<String, Object> reqMap = new HashMap<>()
        reqMap.put("name", "group-212121212")
        reqMap.put("enable", true)
        reqMap.put("description", "group-2-des")
        reqMap.put("seq", 3)
        single = JSONUtil.parse(put(URI + '/' + userGroupEntity.getId(), JSONUtil.toJSON(reqMap), HttpStatus.OK).getResponse().getContentAsString(), Map
            .class)
        assert single.get("name") == 'group-212121212'

        assert delete(URI + '/' + userGroupEntity.getId(), HttpStatus.NO_CONTENT).getResponse().getContentAsString() == ''

        UserEntity userEntity = new UserEntity('u11', 'p11')
        userEntity = userService.save(userEntity)
        UserEntity [] userEntitys = new UserEntity[1]
        userEntitys[0] = userEntity

        UserGroupEntity userGroupEntity1 = new UserGroupEntity()
        userGroupEntity1.setName('lastgroup')
        userGroupEntity1.add(userEntitys)
        userGroupEntity1 = userGroupRepository.saveAndFlush(userGroupEntity1)
        userGroupEntity1.remove(userEntity)
        userGroupEntity1 = userGroupRepository.saveAndFlush(userGroupEntity1)

        assert delete(URI + '?ids=' + userGroupEntity1.getId(), HttpStatus.NO_CONTENT).getResponse().getContentAsString() == ''


    }

    @Test
    void testGetUsersAndRoles(){
        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('role11')
        roleEntity = roleRepo.saveAndFlush(roleEntity)

        UserEntity userEntity = new UserEntity('u11', 'p11')
        userEntity = userService.save(userEntity)

        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName('group11')
        userGroupEntity.add(roleEntity)
        userGroupEntity.add(userEntity)
        userGroupEntity = userGroupRepository.saveAndFlush(userGroupEntity)

        def result = get(URI + '/' + userGroupEntity.getId() + '/roles', HttpStatus.OK).getResponse().getContentAsString()
        List list = JSONUtil.parse(result, List.class)
        assert list.size() == 1
        assert list.get(0).get('id') == roleEntity.getId()

        result = get(URI + '/' + userGroupEntity.getId() + '/users', HttpStatus.OK).getResponse().getContentAsString()
        list = JSONUtil.parse(result, List.class)
        assert list.size() == 1
        assert list.get(0).get('id') == userEntity.getId()
    }

    @After
    void tearDown() {
        userGroupRepository.deleteAll()
        userRepo.deleteAll()
        roleRepo.deleteAll()
        menuRepository.deleteAll()
        resourceRepository.deleteAll()
        dataDicRepository.deleteAll()
    }
}
