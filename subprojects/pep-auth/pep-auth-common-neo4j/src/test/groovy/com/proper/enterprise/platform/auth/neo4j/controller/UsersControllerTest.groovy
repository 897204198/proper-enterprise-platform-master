package com.proper.enterprise.platform.auth.neo4j.controller

import com.proper.enterprise.platform.auth.neo4j.entity.RoleNodeEntity
import com.proper.enterprise.platform.auth.neo4j.entity.UserGroupNodeEntity
import com.proper.enterprise.platform.auth.neo4j.entity.UserNodeEntity
import com.proper.enterprise.platform.auth.neo4j.repository.MenuNodeRepository
import com.proper.enterprise.platform.auth.neo4j.repository.ResourceNodeRepository
import com.proper.enterprise.platform.auth.neo4j.repository.RoleNodeRepository
import com.proper.enterprise.platform.auth.neo4j.repository.UserGroupNodeRepository
import com.proper.enterprise.platform.auth.neo4j.repository.UserNodeRepository
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class UsersControllerTest extends AbstractTest {

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

    @Before
    void loginUser() {
        tearDown()
        UserNodeEntity userNodeEntity = new UserNodeEntity()
        userNodeEntity.setName('admin')
        userNodeEntity.setPassword('123456')
        userNodeEntity.setPhone('15948525865')
        userNodeEntity.setUsername('admin')
        userNodeEntity.setSuperuser(true)
        userNodeEntity = userNodeRepository.save(userNodeEntity)
        mockUser(userNodeEntity.getId(), userNodeEntity.getUsername(), userNodeEntity.getPassword())
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

    @Test
    void testDeleteUser() {
        UserNodeEntity userNodeEntity = new UserNodeEntity()
        userNodeEntity.setName('testa')
        userNodeEntity = userNodeRepository.save(userNodeEntity)
        def result = delete('/auth/users/' + userNodeEntity.getId(), HttpStatus.NO_CONTENT).getResponse().getContentAsString()
        assert result == ''
    }

    @Test
    void testGetUserByCondiction() {
        UserNodeEntity userNodeEntity
        for (int i = 0; i < 20; i++) {
            userNodeEntity = new UserNodeEntity()
            userNodeEntity.setUsername('sun' + i + 's1')
            userNodeEntity.setName('sas' + i)
            userNodeEntity.setEmail('sses' + i + '@ww.com')
            userNodeEntity.setPhone('15978' + i + '48584')
            userNodeEntity.setEnable(true)
            userNodeRepository.save(userNodeEntity)
        }

        def resAll = JSONUtil.parse(get('/auth/users?username=sun&name=&phone=&email=&enable=y&pageNo=1&pageSize=20', HttpStatus.OK).getResponse()
            .getContentAsString(), DataTrunk.class)
        assert resAll.count == 20
        assert resAll.data[0].get("username") == 'sun0s1'
        assert resAll.data[0].get("name") == 'sas0'
        assert resAll.data[1].get("username") == 'sun1s1'
        assert resAll.data[1].get("name") == 'sas1'

        resAll = JSONUtil.parse(get('/auth/users?username=sun&name=&phone=&email=&enable=n&pageNo=1&pageSize=20', HttpStatus.OK).getResponse()
            .getContentAsString(), DataTrunk.class)
        assert resAll.count == 0

        resAll = JSONUtil.parse(get('/auth/users?username=sun&name=&phone=&email=&enable=&pageNo=3&pageSize=2', HttpStatus.OK).getResponse()
            .getContentAsString(), DataTrunk.class)
        assert resAll.count == 2
        assert resAll.data[0].get("username") == 'sun12s1'
        assert resAll.data[0].get("name") == 'sas12'
        assert resAll.data[1].get("username") == 'sun13s1'
        assert resAll.data[1].get("name") == 'sas13'

        resAll = JSONUtil.parse(get('/auth/users?username=&name=&phone=48585688&email=&enable=&pageNo=3&pageSize=2', HttpStatus.OK).getResponse()
            .getContentAsString(), DataTrunk.class)
        assert resAll.count == 0

    }

    @Test
    @NoTx
    void testGetUser() {
        UserNodeEntity userNodeEntity = new UserNodeEntity()
        userNodeEntity.setName('sunshuai')
        userNodeEntity.setPassword('123456')
        userNodeEntity.setPhone('15948525865')
        userNodeEntity.setUsername('sun')
        userNodeEntity.setId(null)

        def user = post('/auth/users', JSONUtil.toJSON(userNodeEntity), HttpStatus.CREATED).getResponse().getContentAsString()
        userNodeEntity = JSONUtil.parse(user, UserNodeEntity.class)

        def result = get('/auth/users/' + userNodeEntity.getId(), HttpStatus.OK).getResponse().getContentAsString()
        result = JSONUtil.parse(result, UserNodeEntity.class)
        assert result.getId() == userNodeEntity.getId()

        Map<String, Object> userMap = new HashMap<>()
        userMap.put('name', 'new name')
        userMap.put('email', 'email@po.com')
        userMap.put('password', '789456')
        userMap.put('enable', true)

        result = put('/auth/users/' + userNodeEntity.getId(), JSONUtil.toJSON(userMap), HttpStatus.OK).getResponse().getContentAsString()
        assert result == ''

        result = delete('/auth/users?ids=' + userNodeEntity.getId(), HttpStatus.NO_CONTENT).getResponse().getContentAsString()
        assert result == ''
    }

    @Test
    void userRoleTest() {
        mockUserData()

        RoleNodeEntity roleNodeEntity = new RoleNodeEntity()
        roleNodeEntity.setId('role1')
        roleNodeEntity.setName('testrole')
        roleNodeEntity.setDescription('des')
        roleNodeEntity = roleNodeRepository.save(roleNodeEntity)

        RoleNodeEntity roleNodeEntity1 = new RoleNodeEntity()
        roleNodeEntity1.setId('role2')
        roleNodeEntity1.setName('testrole')
        roleNodeEntity1 = roleNodeRepository.save(roleNodeEntity1)

        def resList = JSONUtil.parse(get('/auth/users/test1/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0
        post('/auth/users/test2/role/role1', '', HttpStatus.CREATED)
        resList = JSONUtil.parse(get('/auth/users/test2/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'role1'
        delete('/auth/users/test2/role/role1', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/users/test2/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0
    }

    @Test
    @NoTx
    void userGroupTest() {

        mockUserData()

        UserGroupNodeEntity userGroupNodeEntity = new UserGroupNodeEntity()
        userGroupNodeEntity.setId('group1')
        userGroupNodeEntity.setName('testgroup1')
        userGroupNodeEntity.setDescription('testgroup1')
        userGroupNodeEntity.setSeq(1)
        userGroupNodeRepository.save(userGroupNodeEntity)

        UserGroupNodeEntity userGroupNodeEntity1 = new UserGroupNodeEntity()
        userGroupNodeEntity1.setId('group2')
        userGroupNodeEntity1.setName('testgroup2')
        userGroupNodeEntity1.setDescription('testgroup2')
        userGroupNodeEntity1.setSeq(2)
        userGroupNodeRepository.save(userGroupNodeEntity1)

        post('/auth/user-groups/group1/user/test3', '', HttpStatus.CREATED)
        def resList = JSONUtil.parse(get('/auth/users/test3/user-groups', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'group1'
        delete('/auth/user-groups/group1/user/test3', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/users/test3/user-groups', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0
    }

    @Test
    void userQueryTest() {
        mockUserData()
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
        assert list.size() == 4
        assert list.get(0).username == 't3'
        assert list.get(1).username == 'admin'
        assert list.get(2).username == 't2'
        assert list.get(3).username == 't1'
    }


    @Test
    void updateEnableTest() {
        UserNodeEntity userNodeEntity = new UserNodeEntity()
        userNodeEntity.setName('sunshuai')
        userNodeEntity.setPassword('123456')
        userNodeEntity.setPhone('15948525865')
        userNodeEntity.setUsername('sun')
        userNodeEntity = userNodeRepository.save(userNodeEntity)

        def reqMap = [:]
        reqMap['ids'] = [userNodeEntity.getId()]
        reqMap['enable'] = false
        put('/auth/users', JSONUtil.toJSON(reqMap), HttpStatus.OK)
        def result = get('/auth/users/' + userNodeEntity.getId(), HttpStatus.OK).getResponse().getContentAsString()
        assert result == ''
    }

}
