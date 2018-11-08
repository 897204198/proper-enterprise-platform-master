package com.proper.enterprise.platform.auth.common.jpa.controller

import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.model.UserGroup
import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserGroupService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserGroupEntity
import com.proper.enterprise.platform.auth.common.jpa.repository.*
import com.proper.enterprise.platform.auth.common.vo.UserGroupVO
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.core.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class UserGroupControllerTest extends AbstractJPATest {

    private static final String URI = '/auth/user-groups'

    @Autowired
    private UserService userService

    @Autowired
    private I18NService i18NService

    @Autowired
    private UserRepository userRepo

    @Autowired
    private RoleRepository roleRepo

    @Autowired
    private RoleService roleService

    @Autowired
    MenuRepository menuRepository
    @Autowired
    ResourceRepository resourceRepository
    @Autowired
    UserGroupRepository userGroupRepository
    @Autowired
    DataDicRepository dataDicRepository
    @Autowired
    UserGroupService userGroupService

    @Before
    void setUp() {
        userGroupRepository.deleteAllInBatch()
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/auth/common/jpa/users.sql",
        "/com/proper/enterprise/platform/auth/common/jpa/roles.sql"
    ])
    @NoTx
    void userGroupUnionTest() {

        mockUser('test1', 't1', 'pwd')
        Authentication.setCurrentUserId('test1')

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

        assert post(URI, JSONUtil.toJSON(group1), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() == i18NService.getMessage("pep.auth.common.usergroup.name.duplicate")
        assert g1.get('id') != null
        assert g2.get('id') != null

        def id1 = g1.get('id')
        def id2 = g2.get('id')

        def query = JSONUtil.parse(get(URI + '?name=group-1&description=group-1-des&enable=Y', HttpStatus.OK)
            .getResponse().getContentAsString(), DataTrunk.class)

        assert query.count == 1
        assert query.data.get(0).id == id1
        assert query.data.get(0).name == 'group-1'
        assert query.data.get(0).enable

        query = JSONUtil.parse(get(URI, HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)

        assert query.count == 2
        assert query.data.get(1).id == id2
        assert query.data.get(1).name == 'group-2'
        assert query.data.get(1).enable
        def queryPage = JSONUtil.parse(get(URI + "?pageNo=1&pageSize=1", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert queryPage.getData().size() == 1
        assert queryPage.count == 2
        def updateReq = [:]
        updateReq['ids'] = [id1, id2]
        updateReq['enable'] = false
        put(URI, JSONUtil.toJSON(updateReq), HttpStatus.OK)
        query = JSONUtil.parse(get(URI + "?userGroupEnable=DISABLE", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert query.count == 2
        assert !query.data.get(0).enable
        assert !query.data.get(1).enable

        updateReq['enable'] = true
        put(URI, JSONUtil.toJSON(updateReq), HttpStatus.OK)

        def single = JSONUtil.parse(get(URI + '/' + id1, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert single != null
        assert single.get('name') == 'group-1'
        assert single.get('description') == 'group-1-des'
        assert single.get('enable')
        assert single.get('seq') == 1

        def u1 = userService.getByUsername('t1', EnableEnum.ALL)
        def u2 = userService.getByUsername('t2', EnableEnum.ALL)

        post(URI + '/' + id1 + '/user/' + u1.id, '', HttpStatus.CREATED)
        post(URI + '/' + id1 + '/user/' + u2.id, '', HttpStatus.CREATED)

        // add role to group and check could find the group from role
        UserGroupVO groupVO = JSONUtil.parse(post(URI + '/' + id1 + '/role/role1', '', HttpStatus.CREATED).getResponse().getContentAsString(), UserGroupVO.class)
        def groups = resOfGet('/auth/roles/role1/user-groups', HttpStatus.OK)
        assert groups.size() > 0
        assert groups.get(0).name == 'group-1'
        def mm = get(URI + '/' + id1, HttpStatus.OK).getResponse().getContentAsString()
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
        userGroupEntity = userGroupService.save(userGroupEntity)

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
        UserEntity[] userEntitys = new UserEntity[1]
        userEntitys[0] = userEntity

        UserGroupEntity userGroupEntity1 = new UserGroupEntity()
        userGroupEntity1.setName('lastgroup')
        userGroupEntity1.add(userEntitys)
        userGroupEntity1 = userGroupService.save(userGroupEntity1)
        userGroupEntity1.remove(userEntity)
        userGroupEntity1 = userGroupService.save(userGroupEntity1)

        assert delete(URI + '?ids=' + userGroupEntity1.getId(), HttpStatus.NO_CONTENT).getResponse().getContentAsString() == ''
    }

    @NoTx
    @Test
    void testDeleteUsersOfUserGroup() {
        mockUser('test1', 't1', 'pwd')

        UserEntity userEntity = new UserEntity('u11', 'p11')
        userEntity.setEnable(true)
        userEntity.setSuperuser(true)
        userEntity = userRepo.save(userEntity)

        UserEntity userEntity2 = new UserEntity('u22', 'p11')
        userEntity2.setEnable(true)
        userEntity2.setSuperuser(true)
        userEntity2 = userRepo.save(userEntity2)

        UserGroup userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName("group1")
        userGroupEntity.setDescription("des")
        userGroupEntity.setEnable(true)
        userGroupEntity.setSeq(2)
        userGroupEntity.add(userEntity)
        userGroupEntity.add(userEntity2)
        userGroupEntity = userGroupService.save(userGroupEntity)

        delete(URI + '/' + userGroupEntity.getId() + '/users?ids=' + userEntity.getId() + ',' + userEntity2.getId(), HttpStatus.NO_CONTENT)
            .getResponse().getContentAsString() == ''
    }

    @Test
    void testGetUsersAndRoles() {
        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('role11')
        roleEntity = roleService.save(roleEntity)

        UserEntity userEntity = new UserEntity('u11', 'p11')
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)

        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())

        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName('group11')
        userGroupEntity.add(roleEntity)
        userGroupEntity.add(userEntity)
        userGroupEntity = userGroupService.save(userGroupEntity)

        def result = get(URI + '/' + userGroupEntity.getId() + '/roles', HttpStatus.OK).getResponse().getContentAsString()
        List list = JSONUtil.parse(result, List.class)
        assert list.size() == 1
        assert list.get(0).get('id') == roleEntity.getId()

        result = get(URI + '/' + userGroupEntity.getId() + '/users', HttpStatus.OK).getResponse().getContentAsString()
        list = JSONUtil.parse(result, List.class)
        assert list.size() == 1
        assert list.get(0).get('id') == userEntity.getId()
    }

    @Test
    void testGetGroupRolesAndUsers() {
        UserEntity userEntity = new UserEntity('u', 'p')
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)

        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())

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

        RoleEntity roleEntity3 = new RoleEntity()
        roleEntity3.setName('role3')
        roleEntity3 = roleService.save(roleEntity3)

        UserEntity userEntity1 = new UserEntity('u1', 'p1')
        userEntity1 = userService.save(userEntity1)

        UserEntity userEntity2 = new UserEntity('u2', 'p2')
        userEntity2.setEnable(false)
        userEntity2 = userService.save(userEntity2)

        UserEntity userEntity3 = new UserEntity('u3', 'p3')
        userEntity3 = userService.save(userEntity3)

        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName('group')
        userGroupEntity.add(roleEntity)
        userGroupEntity.add(roleEntity1)
        userGroupEntity.add(roleEntity2)
        userGroupEntity.add(roleEntity3)
        userGroupEntity.add(userEntity1)
        userGroupEntity.add(userEntity2)
        userGroupEntity.add(userEntity3)
        userGroupEntity = userGroupService.save(userGroupEntity)

        def result = JSONUtil.parse(get('/auth/user-groups/' + userGroupEntity.getId() + '/roles', HttpStatus.OK).getResponse().getContentAsString
            (), List.class)
        assert result.size() == 3

        result = get('/auth/user-groups/isdfsdlfsj/roles', HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
        assert result == i18NService.getMessage("pep.auth.common.usergroup.get.failed")

        result = JSONUtil.parse(get('/auth/user-groups/' + userGroupEntity.getId() + '/users', HttpStatus.OK).getResponse().getContentAsString
            (), List.class)
        assert result.size() == 2

    }

    @Test
    void testPutUsers() {
        UserEntity userEntity = new UserEntity('u11', 'p11')
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)
        UserEntity userEntity2 = new UserEntity('u12', 'p11')
        userEntity2 = userService.save(userEntity2)
        UserEntity userEntity3 = new UserEntity('u13', 'p11')
        userEntity3 = userService.save(userEntity3)
        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())
        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName('group11')
        userGroupEntity.add(userEntity)
        userGroupEntity = userGroupService.save(userGroupEntity)
        def result = get(URI + '/' + userGroupEntity.getId() + '/users', HttpStatus.OK).getResponse().getContentAsString()
        def list = JSONUtil.parse(result, List.class)
        assert list.size() == 1
        assert list.get(0).get('id') == userEntity.getId()
        def req = [:]
        req["ids"] = userEntity3.getId() + "," + userEntity2.getId()
        put(URI + '/' + userGroupEntity.getId() + '/users', JSONUtil.toJSON(req), HttpStatus.OK)
        def result2 = JSONUtil.parse(get(URI + '/' + userGroupEntity.getId() + '/users', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result2.size() == 3
    }

    @After
    void tearDown() {
        userRepo.deleteAll()
        userGroupRepository.deleteAll()
        roleRepo.deleteAll()
        menuRepository.deleteAll()
        resourceRepository.deleteAll()
        dataDicRepository.deleteAll()
    }
}
