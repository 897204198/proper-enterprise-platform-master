package com.proper.enterprise.platform.auth.neo4j.controller

import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.auth.neo4j.entity.*
import com.proper.enterprise.platform.auth.neo4j.repository.*
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMethod

class RolesControllerTest extends AbstractTest {

    @Autowired
    RoleService roleService
    @Autowired
    RoleNodeRepository roleRepository
    @Autowired
    UserNodeRepository userRepository
    @Autowired
    MenuNodeRepository menuRepository
    @Autowired
    ResourceNodeRepository resourceRepository
    @Autowired
    UserGroupNodeRepository userGroupRepository
    @Autowired
    DataDicRepository dataDicRepository
    @Autowired
    I18NService i18NService

    @Before
    void initData() {
        tearDown()
    }

    @After
    void cleanAll() {
        tearDown()
    }

    void tearDown() {
        userGroupRepository.deleteAll()
        userRepository.deleteAll()
        roleRepository.deleteAll()
        menuRepository.deleteAll()
        resourceRepository.deleteAll()
        dataDicRepository.deleteAll()
    }

    void loginUser() {
        UserNodeEntity userNodeEntity = new UserNodeEntity()
        userNodeEntity.setName('admin')
        userNodeEntity.setPassword('123456')
        userNodeEntity.setPhone('15948525865')
        userNodeEntity.setUsername('admin')
        userNodeEntity.setSuperuser(true)
        userNodeEntity = userRepository.save(userNodeEntity)
        mockUser(userNodeEntity.getId(), userNodeEntity.getUsername(), userNodeEntity.getPassword())
    }

    @NoTx
    @Test
    void rolesUnionTest() {
        loginUser()

        MenuNodeEntity menu = new MenuNodeEntity()
        menu.setId("a1")
        menu.setName("应用1")
        menu.setRoute("/a1")
        menu.setDescription("desc")
        menuRepository.save(menu)
        MenuNodeEntity menuEntity = new MenuNodeEntity()
        menuEntity.setId("a2")
        menuEntity.setName("CRUD")
        menuEntity.setRoute("/a2")
        menuEntity.setDescription("1")
        menuRepository.save(menuEntity)
        Set list1 = new HashSet()
        list1.add(menu)
        list1.add(menuEntity)

        ResourceNodeEntity resource = new ResourceNodeEntity()
        resource.setId("test-g")
        resource.setName("获得资源")
        resource.setURL("/auth/resources/*")
        resource.setMethod(RequestMethod.GET)
        resourceRepository.save(resource)
        ResourceNodeEntity resourceNodeEntity = new ResourceNodeEntity()
        resourceNodeEntity.setId("test-d")
        resourceNodeEntity.setName("删除资源")
        resourceNodeEntity.setURL("/auth/resources/*")
        resourceNodeEntity.setMethod(RequestMethod.DELETE)
        resourceRepository.save(resourceNodeEntity)
        Set list2 = new HashSet()
        list2.add(resource)
        list2.add(resourceNodeEntity)

        UserGroupNodeEntity userGroupEntity = new UserGroupNodeEntity()
        userGroupEntity.setId("group1")
        userGroupEntity.setName("gourp-21")
        userGroupEntity.setSeq(1)
        userGroupEntity.setDescription("ddddd")
        userGroupRepository.save(userGroupEntity)

        RoleNodeEntity roleNodeEntity = new RoleNodeEntity()
        roleNodeEntity.setId("role1")
        roleNodeEntity.setName("testrole1")
        roleNodeEntity.setDescription("desc")
        roleNodeEntity.setParentId("1")
        roleNodeEntity.setMenus(list1)
        roleNodeEntity.setResources(list2)

        RoleNodeEntity roleNodeEntity1 = new RoleNodeEntity()
        roleNodeEntity1.setId("role2")
        roleNodeEntity1.setName("testrole")
        roleNodeEntity1.setParentId("2")
        roleNodeEntity1.setEnable(true)
        roleNodeEntity1.setMenus(list1)
        roleNodeEntity1.add(userGroupEntity)
        List list = new ArrayList()
        list.add(roleNodeEntity)
        list.add(roleNodeEntity1)
        roleRepository.save(list)

        def roles = JSONUtil.parse(get('/auth/roles', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert roles.size() == 2
        assert roles.get(0).enable
        assert roles.get(1).enable

        def requ = [:]
        requ['ids'] = ['role1', 'role2']
        requ['enable'] = false
        put('/auth/roles', JSONUtil.toJSON(requ), HttpStatus.OK)
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
        req['parentId'] = 'req_test_parentId'
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
        updateReq['enable'] = true
        req['parentId'] = 'updateReq_test_parentId'
        put('/auth/roles/' + id, JSONUtil.toJSON(updateReq), HttpStatus.OK)
        role = JSONUtil.parse(get('/auth/roles/' + id, HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        assert role.get('description') == 'updateReq_test_description'
        assert role.get('name') == 'updateReq_test_name'

        // role add menu
        def addReq = [:]
        addReq['ids'] = 'a1,a2'
        post('/auth/roles/' + id + '/menus', JSONUtil.toJSON(addReq), HttpStatus.CREATED)
        def resList = JSONUtil.parse(get('/auth/roles/' + id + '/menus', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 2
        assert resList.get(0).id == 'a1'
        assert resList.get(1).id == 'a2'
        // delete role's menu
        delete('/auth/roles/' + id + '/menus?ids=a1,a2', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/roles/' + id + '/menus', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        // role add resource
        def addResourceReq = [:]
        addResourceReq['ids'] = 'test-g,test-d'
        post('/auth/roles/' + id + '/resources', JSONUtil.toJSON(addResourceReq), HttpStatus.CREATED)
        resList = JSONUtil.parse(get('/auth/roles/' + id + '/resources', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 2
        // delete role's resources
        delete('/auth/roles/' + id + '/resources?ids=test-g,test-d', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/roles/' + id + '/resources', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        // userGroup add role
        post('/auth/user-groups/' + userGroupEntity.getId() + '/role/' + id, '', HttpStatus.CREATED)
        // delete userGroup's role
        delete('/auth/user-groups/' + userGroupEntity.getId() + '/role/' + id, HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/roles/' + id + '/user-groups', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        def parents = JSONUtil.parse(get('/auth/roles/' + id + '/parents', HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)
        assert parents.size() == 0
    }

    @Test
    void testGetMenuParents() {
        loginUser()
        RoleNodeEntity roleEntity1 = new RoleNodeEntity()
        roleEntity1.setName("role1")
        roleEntity1 = roleService.save(roleEntity1)

        RoleNodeEntity roleEntity2 = new RoleNodeEntity()
        roleEntity2.setName("role2")
        roleEntity2.setParent(roleEntity1)
        roleEntity2 = roleService.save(roleEntity2)

        RoleNodeEntity roleEntity3 = new RoleNodeEntity()
        roleEntity3.setName("role3")
        roleEntity3.setParent(roleEntity2)
        roleService.save(roleEntity3)

        def req = [:]
        req['name'] = 'req_test_name'
        req['description'] = 'req_test_description'
        req['enable'] = true
        def role = JSONUtil.parse(post('/auth/roles', JSONUtil.toJSON(req), HttpStatus.CREATED).getResponse().getContentAsString(), Map.class)
        def id = role.get('id')

        def parents = JSONUtil.parse(get('/auth/roles/' + id + '/parents', HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)
        assert parents.size() == 2

    }

    @Test
    void addOrDeleteRoleMenusErro() {
        loginUser()
        def id = "id"
        def addReq = [:]
        addReq['ids'] = 'a4,a5'
        assert post('/auth/roles/' + id + '/menus', JSONUtil.toJSON(addReq), HttpStatus.BAD_REQUEST).getResponse().getContentAsString() ==
            i18NService.getMessage("pep.auth.common.role.get.failed")
        assert delete('/auth/roles/' + id + '/menus?ids=a1,a2', HttpStatus.BAD_REQUEST).getResponse().getContentAsString() ==
            i18NService.getMessage("pep.auth.common.role.get.failed")

        get('/auth/roles/' + id + '/menus', HttpStatus.BAD_REQUEST).getResponse().getContentAsString() ==
            i18NService.getMessage("pep.auth.common.role.get.failed")
    }


    @Test
    void testNoPermission() {
        def str = i18NService.getMessage("pep.auth.common.user.permission.failed")
        def result = get('/auth/roles', HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == str

        Map<String, Object> map = new HashMap<>()
        result = put('/auth/roles', JSONUtil.toJSON(map), HttpStatus.BAD_REQUEST).getResponse()
            .getContentAsString()
        assert result == str

        result = post('/auth/roles', JSONUtil.toJSON(map), HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == str

        result = delete('/auth/roles?ids=', HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == str

        result = get('/auth/roles/1', HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == str

        result = put('/auth/roles/1', JSONUtil.toJSON(map), HttpStatus.BAD_REQUEST).getResponse()
            .getContentAsString()
        assert result == str

        result = post('/auth/roles/1/menus', JSONUtil.toJSON(map), HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == str

        result = delete('/auth/roles/1/menus?ids=', HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == str

        result = get('/auth/roles/parents', HttpStatus.BAD_REQUEST).getResponse().getContentAsString()
        assert result == str
    }

    @Test
    @NoTx
    void testGetRoleUsersAndGroups() {
        loginUser()

        RoleNodeEntity roleEntity = new RoleNodeEntity()
        roleEntity.setName('role')
        roleEntity.setEnable(true)
        roleEntity.setId("id")
        roleEntity = roleService.save(roleEntity)

        UserNodeEntity userEntity = new UserNodeEntity('u11', 'p11')
        userEntity.add(roleEntity)
        userEntity.setSuperuser(true)
        userEntity = userRepository.save(userEntity)

        UserNodeEntity userEntity1 = new UserNodeEntity('u22', 'p22')
        userEntity1.add(roleEntity)
        userEntity1.setSuperuser(true)
        userEntity1 = userRepository.save(userEntity1)

        UserNodeEntity userEntity2 = new UserNodeEntity('u33', 'p33')
        userEntity2.add(roleEntity)
        userEntity2.setSuperuser(true)
        userEntity2 = userRepository.save(userEntity2)

        UserGroupNodeEntity userGroupEntity = new UserGroupNodeEntity()
        userGroupEntity.setName('tempgroup')
        userGroupEntity.add(userEntity)
        userGroupEntity.add(userEntity1)
        userGroupRepository.save(userGroupEntity)

        UserGroupNodeEntity userGroupEntity1 = new UserGroupNodeEntity()
        userGroupEntity1.setName('tempgroup1')
        userGroupEntity1.add(userEntity1)
        userGroupEntity1.add(userEntity2)
        userGroupRepository.save(userGroupEntity1)

        def result = JSONUtil.parse(get('/auth/roles/' + roleEntity.getId() + '/users', HttpStatus.OK).getResponse().getContentAsString(),
            List.class)
        assert result.size() == 3
        assert get('/auth/roles/sdlfjsdf/users', HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService.getMessage("pep.auth.common" +
            ".role.get.failed")

        result = JSONUtil.parse(get('/auth/roles/' + roleEntity.getId() + '/user-groups', HttpStatus.OK).getResponse().getContentAsString(),
            List.class)
        assert result.size() == 2

    }

    @Test
    void testGetResources() {
        loginUser()

        ResourceNodeEntity resourceEntity = new ResourceNodeEntity()
        resourceEntity.setName('res11')
        resourceEntity.setURL('/auto/**')
        resourceEntity.setMethod(RequestMethod.GET)
        resourceEntity = resourceRepository.save(resourceEntity)

        ResourceNodeEntity resourceEntity1 = new ResourceNodeEntity()
        resourceEntity1.setName('res22')
        resourceEntity1.setURL('/user/**')
        resourceEntity1.setMethod(RequestMethod.POST)
        resourceEntity1.setEnable(false)
        resourceEntity1 = resourceRepository.save(resourceEntity1)

        ResourceNodeEntity resourceEntity2 = new ResourceNodeEntity()
        resourceEntity2.setName('ress33')
        resourceEntity2.setURL('/group/**')
        resourceEntity2.setMethod(RequestMethod.PUT)
        resourceEntity2 = resourceRepository.save(resourceEntity2)

        Collection<ResourceNodeEntity> collection = new HashSet<>()
        collection.add(resourceEntity)
        collection.add(resourceEntity1)
        collection.add(resourceEntity2)

        RoleNodeEntity roleEntity = new RoleNodeEntity()
        roleEntity.setName('role')
        roleEntity.addResources(collection)
        roleEntity = roleService.save(roleEntity)

        def result = JSONUtil.parse(get('/auth/roles/' + roleEntity.getId() + '/resources', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 2
        assert get('/auth/roles/sdjflsdkj/resources', HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService.getMessage("pep.auth.common.role.get.failed")


        ResourceNodeEntity resourceEntity3 = new ResourceNodeEntity()
        resourceEntity3.setName('ress44')
        resourceEntity3.setURL('/auto')
        resourceEntity3.setMethod(RequestMethod.DELETE)
        resourceEntity3 = resourceRepository.save(resourceEntity3)

        ResourceNodeEntity resourceEntity4 = new ResourceNodeEntity()
        resourceEntity4.setName('ress55')
        resourceEntity4.setURL('/auto/*')
        resourceEntity4.setMethod(RequestMethod.PUT)
        resourceEntity4 = resourceRepository.save(resourceEntity4)

        Map<String, Object> reqMap = new HashMap<>()
        reqMap.put('ids', resourceEntity3.getId() + ',' + resourceEntity4.getId())
        result = JSONUtil.parse(post('/auth/roles/' + roleEntity.getId() + '/resources', JSONUtil.toJSON(reqMap), HttpStatus.CREATED).getResponse()
            .getContentAsString(), java.lang.Object.class)
        assert result.get('id') == roleEntity.getId()
        assert post('/auth/roles/sdfsdf/resources', JSONUtil.toJSON(reqMap), HttpStatus.BAD_REQUEST).getResponse()
            .getContentAsString() == i18NService.getMessage("pep.auth.common.role.get.failed")

        assert delete('/auth/roles/' + roleEntity.getId() + '/resources?ids=' + reqMap.get('ids'), HttpStatus.NO_CONTENT)
        assert delete('/auth/roles/sdfasdf/resources?ids=' + reqMap.get('ids'), HttpStatus.BAD_REQUEST).getResponse()
            .getContentAsString() == i18NService.getMessage("pep.auth.common.role.get.failed")
    }

}
