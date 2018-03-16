package com.proper.enterprise.platform.auth.common.neo4j.service

import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.neo4j.entity.ResourceNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.RoleNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserGroupNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserNodeEntity
import com.proper.enterprise.platform.auth.common.neo4j.repository.MenuNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.ResourceNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.RoleNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserGroupNodeRepository
import com.proper.enterprise.platform.auth.common.neo4j.repository.UserNodeRepository
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMethod

class Neo4jUserDaoImplTest extends AbstractNeo4jTest {

    @Autowired
    UserNodeRepository userNodeRepository

    @Autowired
    ResourceNodeRepository resourceRepository

    @Autowired
    RoleNodeRepository roleRepository

    @Autowired
    UserGroupNodeRepository userGroupRepository

    @Autowired
    UserService userService

    @Autowired
    MenuNodeRepository menuNodeRepository

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
        userGroupRepository.deleteAll()
        roleRepository.deleteAll()
        resourceRepository.deleteAll()
        menuNodeRepository.deleteAll()
    }

    @Test
    @NoTx
    void testHasPermissionByUrl() {

        ResourceNodeEntity resourceEntity = new ResourceNodeEntity()
        resourceEntity.setName('res1')
        resourceEntity.setURL('/auth/users/**')
        resourceEntity.setMethod(RequestMethod.POST)
        resourceEntity = resourceRepository.save(resourceEntity)

        ResourceNodeEntity resourceEntity1 = new ResourceNodeEntity()
        resourceEntity1.setName('res2')
        resourceEntity1.setURL('/auth/users/*')
        resourceEntity1.setMethod(RequestMethod.POST)
        resourceEntity1 = resourceRepository.save(resourceEntity1)

        RoleNodeEntity roleEntity1 = new RoleNodeEntity()
        roleEntity1.setName('role1')
        roleEntity1.addResource(resourceEntity)
        roleEntity1.addResource(resourceEntity1)
        roleEntity1 = roleRepository.save(roleEntity1)

        UserNodeEntity userEntity = new UserNodeEntity('user1', 'pwd1')
        userEntity.add(roleEntity1)
        userEntity = userNodeRepository.save(userEntity)

        //------------------------------------------------------------------------------------

        ResourceNodeEntity resourceEntity2 = new ResourceNodeEntity()
        resourceEntity2.setName('res2')
        resourceEntity2.setURL('/auth/users/{userId}/role/{roleId}')
        resourceEntity2.setMethod(RequestMethod.GET)
        resourceEntity2 = resourceRepository.save(resourceEntity2)

        RoleNodeEntity roleEntity2 = new RoleNodeEntity()
        roleEntity2.setName('role2')
        roleEntity2.addResource(resourceEntity2)
        roleEntity2 = roleRepository.save(roleEntity2)

        UserGroupNodeEntity userGroupEntity = new UserGroupNodeEntity()
        userGroupEntity.setName('group1')
        userGroupEntity.setSeq(1)
        userGroupEntity.add(userEntity)
        userGroupEntity.add(roleEntity2)
        userGroupRepository.save(userGroupEntity)

        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())

        assert userService.checkPermission('/auth/users/{userId}/role/{roleId}', RequestMethod.POST) == null
        assert userService.checkPermission('/auth/users/{userId}/role/{roleId}', RequestMethod.GET) == null

        User user = userService.groupHasTheUser(userGroupEntity, userEntity.getId())
        assert user.getId() == userEntity.getId()

    }


    @Test
    void testFindAll() {
        UserNodeEntity userNodeEntity
        List<String> ids = new ArrayList<>()
        for (int i = 0; i < 3; i++) {
            userNodeEntity = new UserNodeEntity()
            userNodeEntity.setName('a' + i)
            ids.add(userNodeRepository.save(userNodeEntity).getId())
        }
        Collection collection = userNodeRepository.findAllByIdIn(ids)
        assert collection.size() == 3
    }

}
