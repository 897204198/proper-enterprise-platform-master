package com.proper.enterprise.platform.auth.common.neo4j.controller

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.neo4j.entity.RoleNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserGroupNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.repository.MenuNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.ResourceNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.RoleNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserGroupNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserNodeRepository
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class UserGroupControllerTest extends AbstractNeo4jTest {

    private static final String URI = '/auth/user-groups'

    @Autowired
    UserService userService

    @Autowired
    UserNodeRepository userNodeRepository

    @Autowired
    RoleNodeRepository roleNodeRepository

    @Autowired
    UserGroupNodeRepository userGroupNodeRepository

    @Autowired
    MenuNodeRepository menuNodeRepository

    @Autowired
    ResourceNodeRepository resourceNodeRepository

    @Autowired
    private I18NService i18NService

    @Before
    void initData() {
        tearDown()
    }

    @After
    void clearAll() {
        tearDown()
    }

    void tearDown() {
        userNodeRepository.deleteAll()
        roleNodeRepository.deleteAll()
        userGroupNodeRepository.deleteAll()
        menuNodeRepository.deleteAll()
        resourceNodeRepository.deleteAll()
    }

    void mockUserData() {
        UserNodeEntity userNodeEntity = new UserNodeEntity()
        userNodeEntity.setId('test1')
        userNodeEntity.setUsername('t1')
        userNodeEntity.setPassword('pwd')
        userNodeEntity.setName('c')
        userNodeEntity.setPhone('12345678901')
        userNodeEntity.setSuperuser(true)
        userNodeEntity = userNodeRepository.save(userNodeEntity)

        UserNodeEntity userNodeEntity1 = new UserNodeEntity()
        userNodeEntity1.setId('test2')
        userNodeEntity1.setUsername('t2')
        userNodeEntity1.setPassword('pwd')
        userNodeEntity1.setName('b')
        userNodeEntity1.setPhone('12345678902')
        userNodeEntity1 = userNodeRepository.save(userNodeEntity1)

        UserNodeEntity userNodeEntity2 = new UserNodeEntity()
        userNodeEntity2.setId('test3')
        userNodeEntity2.setUsername('t3')
        userNodeEntity2.setPassword('pwd')
        userNodeEntity2.setName('a')
        userNodeEntity2.setPhone('12345678903')
        userNodeRepository.save(userNodeEntity2)

    }

    void mockRoleData() {
        RoleNodeEntity roleNodeEntity = new RoleNodeEntity()
        roleNodeEntity.setId('role1')
        roleNodeEntity.setName('testrole')
        roleNodeEntity.setDescription('des')
        roleNodeEntity = roleNodeRepository.save(roleNodeEntity)

        RoleNodeEntity roleNodeEntity1 = new RoleNodeEntity()
        roleNodeEntity1.setId('role2')
        roleNodeEntity1.setName('testrole')
        roleNodeEntity1 = roleNodeRepository.save(roleNodeEntity1)
    }

    @Test
    void userGroupUnionTest() {
        mockUserData()
        mockRoleData()

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

        assert post(URI, JSONUtil.toJSON(group1), HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService.getMessage("pep.auth.common.usergroup.name.duplicate")
        assert g1.get('id') != null
        assert g2.get('id') != null

        def id1 = g1.get('id')
        def id2 = g2.get('id')

        def query = JSONUtil.parse(get(URI + '?name=group-1&description=group-1&enable=Y', HttpStatus.OK)
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

        def queryPage = JSONUtil.parse(get(URI + "?pageNo=1&pageSize=1", HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert queryPage.getData().size() == 1
        assert queryPage.count == 2
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
        assert single.size() == 12
        assert single.get("id") == id1
        assert single.get("name") == 'group-1'

        delete(URI + '/' + id1 + '/user/' + u1.id, HttpStatus.NO_CONTENT)
        delete(URI + '/' + id1 + '/role/role1', HttpStatus.NO_CONTENT)

        single = JSONUtil.parse(get(URI + '/' + id1, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert single.get('name') == 'group-1'
        assert single.get('description') == 'group-1-des'
        assert single.get('enable')

        UserGroupNodeEntity userGroupEntity = new UserGroupNodeEntity()
        userGroupEntity.setName("gourp-22221")
        userGroupEntity.setSeq(3)
        userGroupEntity.setDescription("ddddd")
        userGroupEntity = userGroupNodeRepository.save(userGroupEntity)

        Map<String, Object> reqMap = new HashMap<>()
        reqMap.put("name", "group-212121212")
        reqMap.put("enable", true)
        reqMap.put("description", "group-2-des")
        reqMap.put("seq", 3)
        single = JSONUtil.parse(put(URI + '/' + userGroupEntity.getId(), JSONUtil.toJSON(reqMap), HttpStatus.OK).getResponse().getContentAsString(), Map
            .class)
        assert single.get("name") == 'group-212121212'

        assert delete(URI + '/' + userGroupEntity.getId(), HttpStatus.NO_CONTENT).getResponse().getContentAsString() == ''

        UserNodeEntity userEntity = new UserNodeEntity('u11', 'p11')
        userEntity = userService.save(userEntity)
        UserNodeEntity[] userEntitys = new UserNodeEntity[1]
        userEntitys[0] = userEntity

        UserGroupNodeEntity userGroupEntity1 = new UserGroupNodeEntity()
        userGroupEntity1.setName('lastgroup')
        userGroupEntity1.add(userEntitys)
        userGroupEntity1 = userGroupNodeRepository.save(userGroupEntity1)
        userGroupEntity1.remove(userEntity)
        userGroupEntity1 = userGroupNodeRepository.save(userGroupEntity1)

        assert delete(URI + '?ids=' + userGroupEntity1.getId(), HttpStatus.NO_CONTENT).getResponse().getContentAsString() == ''
    }

    @Test
    void testGetUsersAndRoles() {
        RoleNodeEntity roleEntity = new RoleNodeEntity()
        roleEntity.setName('role11')
        roleEntity = roleNodeRepository.save(roleEntity)

        UserNodeEntity userEntity = new UserNodeEntity('u11', 'p11')
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)

        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())

        UserGroupNodeEntity userGroupEntity = new UserGroupNodeEntity()
        userGroupEntity.setName('group11')
        userGroupEntity.add(roleEntity)
        userGroupEntity.add(userEntity)
        userGroupEntity = userGroupNodeRepository.save(userGroupEntity)

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
    @NoTx
    void testPutUsers() {
        UserNodeEntity userEntity = new UserNodeEntity('u11', 'p11')
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)
        UserNodeEntity userEntity2 = new UserNodeEntity('u12', 'p11')
        userEntity2 = userService.save(userEntity2)
        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())
        UserGroupNodeEntity userGroupEntity = new UserGroupNodeEntity()
        userGroupEntity.setName('group11')
        userGroupEntity.add(userEntity)
        userGroupEntity = userGroupNodeRepository.save(userGroupEntity)
        def result = get(URI + '/' + userGroupEntity.getId() + '/users', HttpStatus.OK).getResponse().getContentAsString()
        def list = JSONUtil.parse(result, List.class)
        assert list.size() == 1
        assert list.get(0).get('id') == userEntity.getId()
        def req = [:]
        req["ids"] = userEntity.getId() + "," + userEntity2.getId()
        put(URI + '/' + userGroupEntity.getId() + '/users', JSONUtil.toJSON(req), HttpStatus.OK)
        def result2 = JSONUtil.parse(get(URI + '/' + userGroupEntity.getId() + '/users', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result2.size() == 2
    }

}
