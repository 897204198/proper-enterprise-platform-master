package com.proper.enterprise.platform.auth.neo4j.entity

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class EntityTest extends AbstractTest {

    @Test
    void testMenuNodeEntity(){
        MenuNodeEntity menuNodeEntity = new MenuNodeEntity()
        menuNodeEntity.setBoot(true)
        menuNodeEntity.setIdentifier('')
        menuNodeEntity.setParentId('')
        MenuNodeEntity menuNodeEntity1 = new MenuNodeEntity()
        menuNodeEntity.addChild(menuNodeEntity1)
        menuNodeEntity.removeChild(menuNodeEntity1)
        Set<MenuNodeEntity> set = new HashSet<>()
        set.add(menuNodeEntity1)
        menuNodeEntity.setChildren(set)
        Set<ResourceNodeEntity> resources = new HashSet<>()
        ResourceNodeEntity resourceNodeEntity = new ResourceNodeEntity()
        resources.add(resourceNodeEntity)
        menuNodeEntity.setResources(resources)
        menuNodeEntity.remove(resourceNodeEntity)
        menuNodeEntity.setMenuCode('')
    }

    @Test
    void testRoleNodeEntity() {
        RoleNodeEntity roleNodeEntity = new RoleNodeEntity('role')
        Set<UserGroupNodeEntity> groups = new HashSet<>()
        UserGroupNodeEntity userGroupNodeEntity = new UserGroupNodeEntity()
        groups.add(userGroupNodeEntity)
        roleNodeEntity.setGroups(groups)
        roleNodeEntity.getGroups()
        roleNodeEntity.getUserGroups()
        roleNodeEntity.remove(userGroupNodeEntity)
        MenuNodeEntity menuNodeEntity = new MenuNodeEntity()
        roleNodeEntity.add(menuNodeEntity)
        roleNodeEntity.remove(menuNodeEntity)
        Set<UserNodeEntity> set = new HashSet<>()
        UserNodeEntity userNodeEntity = new UserNodeEntity()
        set.add(userNodeEntity)
        roleNodeEntity.setUserNodes(set)
        roleNodeEntity.remove(userNodeEntity)
    }



}
