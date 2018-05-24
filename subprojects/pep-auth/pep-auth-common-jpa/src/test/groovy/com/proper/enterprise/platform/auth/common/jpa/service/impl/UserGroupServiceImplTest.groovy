package com.proper.enterprise.platform.auth.common.jpa.service.impl

import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserGroupService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserGroupEntity
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class UserGroupServiceImplTest extends AbstractTest {

    @Autowired
    UserGroupService userGroupService

    @Autowired
    UserService userService

    @Autowired
    RoleService roleService

    @Autowired
    I18NService i18NService

    @Test
    void testDelete() {
        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName('group')
        userGroupEntity = userGroupService.save(userGroupEntity)
        userGroupService.delete(userGroupEntity)
        UserGroupEntity userGroupEntity1 = new UserGroupEntity()
        userGroupEntity1.setName('group1')
        userGroupEntity1 = userGroupService.save(userGroupEntity1)
        assert userGroupService.deleteByIds(userGroupEntity1.getId())
    }

    @Test
    void testErrDelete() {
        UserEntity userEntity = new UserEntity('u1', 'p1')
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)

        mockUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword())

        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName("group1")
        userGroupEntity.add(userEntity)

        delete('/auth/user-groups?ids=' + userGroupEntity.getId(), HttpStatus.NOT_FOUND)

        userGroupEntity = userGroupService.save(userGroupEntity)

        assert delete('/auth/user-groups?ids=' + userGroupEntity.getId(), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString() == i18NService
            .getMessage("pep.auth.common.usergroup.delete.relation.user")

        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('role1')
        roleEntity = roleService.save(roleEntity)

        userGroupEntity.remove(userEntity)
        userGroupEntity.add(roleEntity)
        userGroupEntity = userGroupService.save(userGroupEntity)
        delete('/auth/user-groups?ids=' + userGroupEntity.getId(), HttpStatus.NO_CONTENT)
    }

    @NoTx
    @Test
    void testUserGroupRoleErro1() {
        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setId("id")
        roleEntity.setName("name")
        roleEntity = roleService.save(roleEntity)

        UserEntity userEntity = new UserEntity()
        userEntity.setId("id1")
        userEntity.setUsername("username")
        userEntity.setName("name")
        userEntity.setPassword("123")
        userEntity = userService.save(userEntity)

        UserGroupEntity userGroupEntity = new UserGroupEntity()
        try {
            userGroupService.saveUserGroupRole(userGroupEntity.getId(), roleEntity.getId())
        } catch (Exception e) {
            e.printStackTrace()
        }
        try {
            userGroupService.deleteUserGroupRole(userGroupEntity.getId(), roleEntity.getId())
        } catch (Exception e) {
            e.printStackTrace()
        }
        try {
            userGroupService.addGroupUser(userGroupEntity.getId(), userEntity.getId())
        } catch (Exception e) {
            e.printStackTrace()
        }
        try {
            userGroupService.deleteGroupUser(userGroupEntity.getId(), userEntity.getId())
        } catch (Exception e) {
            e.printStackTrace()
        }
        try {
            userGroupService.deleteGroupUsers(userGroupEntity.getId(), userEntity.getId())
        } catch (Exception e) {
            e.printStackTrace()
        }
        try {
            userGroupService.addGroupUserByUserIds(userGroupEntity.getId(), userEntity.getId())
        } catch (Exception e) {
            e.printStackTrace()
        }
        try {
            userGroupService.getGroupUsers(userGroupEntity.getId(), userEntity.getId())
        } catch (Exception e) {
            i18NService.getMessage("pep.auth.common.usergroup.get.failed")
        }
        try {
            userGroupService.createUserGroup(userGroupEntity.getId(), userEntity.getId())
        } catch (Exception e) {
            i18NService.getMessage("pep.auth.common.usergroup.name.duplicate")
        }
    }

    @NoTx
    @Test
    void testUserGroupRoleErro2() {
        UserGroupEntity userGroup = new UserGroupEntity()
        userGroup.setName("name")
        userGroup.setId("id")
        userGroup = userGroupService.save(userGroup)
        RoleEntity role = new RoleEntity()
        UserEntity userEntity = new UserEntity()

        try {
            userGroupService.saveUserGroupRole(userGroup.getId(), role.getId())
        } catch (Exception e) {
            e.printStackTrace()
        }
        try {
            userGroupService.deleteUserGroupRole(userGroup.getId(), role.getId())
        } catch (Exception e) {
            e.printStackTrace()
        }
        try {
            userGroupService.addGroupUser(userGroup.getId(), userEntity.getId())
        } catch (Exception e) {
            e.printStackTrace()
        }
        try {
            userGroupService.deleteGroupUser(userGroup.getId(), userEntity.getId())
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
