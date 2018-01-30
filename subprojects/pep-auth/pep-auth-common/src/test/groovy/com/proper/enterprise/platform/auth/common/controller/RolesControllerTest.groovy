package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity
import com.proper.enterprise.platform.auth.common.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity
import com.proper.enterprise.platform.auth.common.repository.RoleRepository
import com.proper.enterprise.platform.auth.common.repository.MenuRepository
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository
import com.proper.enterprise.platform.auth.common.repository.UserGroupRepository
import com.proper.enterprise.platform.auth.common.repository.UserRepository
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.bind.annotation.RequestMethod

@Sql(["/com/proper/enterprise/platform/auth/common/roles.sql",
      "/com/proper/enterprise/platform/auth/common/menus.sql",
      "/com/proper/enterprise/platform/auth/common/users.sql",
      "/com/proper/enterprise/platform/auth/common/usergroups.sql",
      "/com/proper/enterprise/platform/auth/common/resources.sql"
    ])
class RolesControllerTest extends AbstractTest {

    @Autowired
    RoleService roleService
    @Autowired
    RoleRepository roleRepository
    @Autowired
    UserRepository userRepository
    @Autowired
    MenuRepository menuRepository
    @Autowired
    ResourceRepository resourceRepository
    @Autowired
    UserGroupRepository userGroupRepository
    @Autowired
    DataDicRepository dataDicRepository
    @Autowired
    I18NService i18NService

    @Test
    @NoTx
    void rolesUnionTest() {
        mockUser('test1','t1', 'pwd')
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
        updateReq['enable'] = true
//        req['parentId'] = 'updateReq_test_parentId' TODO
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
        // delete role's menu
        delete('/auth/roles/' + id + '/resources?ids=test-g,test-d', HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/roles/' + id + '/resources', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        // userGroup add role
        post('/auth/users/test1/role/' + id, '', HttpStatus.CREATED)
        resList = JSONUtil.parse(get('/auth/roles/' + id + '/users', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 1
        assert resList.get(0).id == 'test1'
        // delete userGroup's role
        delete('/auth/users/test1/role/' + id, HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/roles/' + id + '/users', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        // userGroup add role
        post('/auth/user-groups/group1/role/' + id, '', HttpStatus.CREATED)
        // delete userGroup's role
        delete('/auth/user-groups/group1/role/' + id, HttpStatus.NO_CONTENT)
        resList = JSONUtil.parse(get('/auth/roles/' + id + '/user-groups', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert resList.size() == 0

        delete('/auth/roles?ids=' + id, HttpStatus.NO_CONTENT)
        get('/auth/roles/' + id,  HttpStatus.OK).getResponse().getContentAsString() == ''

        def parents = JSONUtil.parse(get('/auth/roles/parents',  HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)
        assert parents.size() == 0 //TODO

    }

    @Test
    void testGetMenuParents(){
        mockUser('test1', 't1', 'pwd')

        RoleEntity roleEntity1 = new RoleEntity()
        roleEntity1.setName("role1")
        roleEntity1 = roleService.save(roleEntity1)

        RoleEntity roleEntity2 = new RoleEntity()
        roleEntity2.setName("role2")
        roleEntity2.setParent(roleEntity1)
        roleEntity2 = roleService.save(roleEntity2)

        RoleEntity roleEntity3 = new RoleEntity()
        roleEntity3.setName("role3")
        roleEntity3.setParent(roleEntity2)
        roleService.save(roleEntity3)

        def parents = JSONUtil.parse(get('/auth/roles/parents',  HttpStatus.OK)
            .getResponse().getContentAsString(), List.class)
        assert parents.size() == 2

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
    void testGetRoleUsersAndGroups(){
        mockUser('test1', 't1', 'pwd')

        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('role')
        roleEntity = roleService.save(roleEntity)

        UserEntity userEntity = new UserEntity('u11', 'p11')
        userEntity.add(roleEntity)
        userEntity = userRepository.save(userEntity)

        UserEntity userEntity1 = new UserEntity('u22', 'p22')
        userEntity1.add(roleEntity)
        userEntity1 = userRepository.save(userEntity1)

        UserEntity userEntity2 = new UserEntity('u33', 'p33')
        userEntity2.add(roleEntity)
        userEntity2 = userRepository.save(userEntity2)

        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName('tempgroup')
        userGroupEntity.add(userEntity)
        userGroupEntity.add(userEntity1)
        userGroupEntity = userGroupRepository.save(userGroupEntity)

        UserGroupEntity userGroupEntity1 = new UserGroupEntity()
        userGroupEntity1.setName('tempgroup1')
        userGroupEntity1.add(userEntity1)
        userGroupEntity1.add(userEntity2)
        userGroupEntity1 = userGroupRepository.save(userGroupEntity1)

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
    void testGetResources(){
        mockUser('test1','t1', 'pwd')

        ResourceEntity resourceEntity = new ResourceEntity()
        resourceEntity.setName('res11')
        resourceEntity.setURL('/auto/**')
        resourceEntity.setMethod(RequestMethod.GET)
        resourceEntity = resourceRepository.saveAndFlush(resourceEntity)

        ResourceEntity resourceEntity1 = new ResourceEntity()
        resourceEntity1.setName('res22')
        resourceEntity1.setURL('/user/**')
        resourceEntity1.setMethod(RequestMethod.POST)
        resourceEntity1.setEnable(false)
        resourceEntity1 = resourceRepository.saveAndFlush(resourceEntity1)

        ResourceEntity resourceEntity2 = new ResourceEntity()
        resourceEntity2.setName('ress33')
        resourceEntity2.setURL('/group/**')
        resourceEntity2.setMethod(RequestMethod.PUT)
        resourceEntity2 = resourceRepository.saveAndFlush(resourceEntity2)

        Collection<ResourceEntity> collection = new HashSet<>()
        collection.add(resourceEntity)
        collection.add(resourceEntity1)
        collection.add(resourceEntity2)

        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('role')
        roleEntity.addResources(collection)
        roleEntity = roleService.save(roleEntity)

        def result = JSONUtil.parse(get('/auth/roles/' + roleEntity.getId() + '/resources', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        assert result.size() == 2
        assert get('/auth/roles/sdjflsdkj/resources', HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService.getMessage("pep.auth.common.role.get.failed")


        ResourceEntity resourceEntity3 = new ResourceEntity()
        resourceEntity3.setName('ress44')
        resourceEntity3.setURL('/auto')
        resourceEntity3.setMethod(RequestMethod.DELETE)
        resourceEntity3 = resourceRepository.saveAndFlush(resourceEntity3)

        ResourceEntity resourceEntity4 = new ResourceEntity()
        resourceEntity4.setName('ress55')
        resourceEntity4.setURL('/auto/*')
        resourceEntity4.setMethod(RequestMethod.PUT)
        resourceEntity4 = resourceRepository.saveAndFlush(resourceEntity4)

        Map<String, Object> reqMap = new HashMap<>()
        reqMap.put('ids', resourceEntity3.getId() + ',' + resourceEntity4.getId())
        result = JSONUtil.parse(post('/auth/roles/' + roleEntity.getId() + '/resources', JSONUtil.toJSON(reqMap), HttpStatus.CREATED).getResponse()
            .getContentAsString(), java.lang.Object.class)
        assert result.get('id') == roleEntity.getId()
        assert post('/auth/roles/sdfsdf/resources', JSONUtil.toJSON(reqMap), HttpStatus.BAD_REQUEST).getResponse()
            .getContentAsString() == i18NService.getMessage("pep.auth.common.role.get.failed")

        assert delete('/auth/roles/' + roleEntity.getId() + '/resources?ids='+reqMap.get('ids'), HttpStatus.NO_CONTENT)
        assert delete('/auth/roles/sdfasdf/resources?ids='+reqMap.get('ids'), HttpStatus.BAD_REQUEST).getResponse()
            .getContentAsString() == i18NService.getMessage("pep.auth.common.role.get.failed")
    }


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
