package com.proper.enterprise.platform.auth.common.service.impl

import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserGroupService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity
import com.proper.enterprise.platform.sys.i18n.I18NService
import com.proper.enterprise.platform.test.AbstractTest
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
    void testDelete(){
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
    void testErrDelete(){
        UserEntity userEntity = new UserEntity('u1','p1')
        userEntity.setSuperuser(true)
        userEntity = userService.save(userEntity)

        mockUser(userEntity.getId(),userEntity.getUsername(), userEntity.getPassword())

        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName("group1")
        userGroupEntity.add(userEntity)

        assert delete('/auth/user-groups?ids='+userGroupEntity.getId(),HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService
            .getMessage("pep.auth.common.usergroup.get.failed")

        userGroupEntity = userGroupService.save(userGroupEntity)

        assert delete('/auth/user-groups?ids='+userGroupEntity.getId(),HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService
            .getMessage("pep.auth.common.usergroup.delete.relation.user")

        RoleEntity roleEntity = new RoleEntity()
        roleEntity.setName('role1')
        roleEntity = roleService.save(roleEntity)

        userGroupEntity.remove(userEntity)
        userGroupEntity.add(roleEntity)
        userGroupEntity = userGroupService.save(userGroupEntity)
        assert delete('/auth/user-groups?ids='+userGroupEntity.getId(),HttpStatus.BAD_REQUEST).getResponse().getContentAsString() == i18NService
            .getMessage("pep.auth.common.usergroup.delete.relation.role")
    }

}
