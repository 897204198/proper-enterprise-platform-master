package com.proper.enterprise.platform.auth.common.jpa.vo

import com.proper.enterprise.platform.api.auth.model.DataRestrain
import com.proper.enterprise.platform.auth.common.jpa.entity.AccessTokenEntity
import com.proper.enterprise.platform.auth.common.vo.DataRestrainVO
import com.proper.enterprise.platform.auth.common.vo.MenuVO
import com.proper.enterprise.platform.auth.common.vo.ResourceVO
import com.proper.enterprise.platform.auth.common.vo.RoleVO
import com.proper.enterprise.platform.auth.common.vo.UserGroupVO
import com.proper.enterprise.platform.auth.common.vo.UserVO
import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMethod

class VoTest extends AbstractTest{
    @Autowired
    DataDicService dataDicService

    @Test
    void test() {
        coverBean(new DataRestrainVO())


        MenuVO menuVO1 = new MenuVO()
        MenuVO menuVO = new MenuVO()
        menuVO.setName("name1")
        menuVO.setRoute("route")
        menuVO.setParent(menuVO1)
        menuVO.setParentId("pId")
        menuVO.setSequenceNumber(1)
        menuVO.setIcon("icon")
        menuVO.setDescription("des1")
        menuVO.setEnable(true)
        menuVO.setIdentifier("identifier")
        menuVO.getName() == "name1"
        menuVO.getRoute() == "route"
        menuVO.getParent()
        menuVO.getParentId() == "pId"
        menuVO.getSequenceNumber() == 1
        menuVO.getIcon() == "icon"
        menuVO.getDescription() == "des1"
        menuVO.enable == true
        menuVO.getIdentifier() == "identifier"
        menuVO.getApplication() == menuVO
        menuVO.addChild(menuVO1)
        menuVO.removeChild(menuVO1)

        ResourceVO resourceVO = new ResourceVO()
        resourceVO.setName("resource")
        resourceVO.addURL("url")
        resourceVO.setMethod(RequestMethod.GET)
        resourceVO.setEnable(true)
        resourceVO.getName() == "resource"
        resourceVO.addURL() == "url"
        resourceVO.getMethod() == RequestMethod.GET
        resourceVO.enable == true
        DataRestrain dataRestrain = new DataRestrainVO()
        resourceVO.add(dataRestrain)
        resourceVO.remove(dataRestrain)
        resourceVO.setResourceType(dataDicService.get("RESOURCE_TYPE", "1"))
        resourceVO.getIdentifier()
        resourceVO.getMenus()
        resourceVO.getRoles()
        resourceVO.setRoles()
        resourceVO.getDataRestrains("")
        resourceVO.getDataRestrains()
        resourceVO.getResourceType()

        RoleVO roleVO = new RoleVO()
        roleVO.setName("role")
        roleVO.setDescription("roleDes")
        roleVO.setParentId("parentId")
        roleVO.setEnable(true)
        roleVO.setParent(roleVO)
        roleVO.equals(menuVO1)
        roleVO.getName() == "role"
        roleVO.getDescription() == "roleDes"
        roleVO.getParentId() == "parentId"
        roleVO.enable == true
        roleVO.getParent() == roleVO
        roleVO.getMenus()
        roleVO.getUserGroups()
        roleVO.getUsers()
        roleVO.getResources()
        roleVO.add()
        roleVO.remove()
        roleVO.addResources()
        roleVO.removeResources()

        UserGroupVO userGroupVO = new UserGroupVO()
        userGroupVO.setName("group")
        userGroupVO.setDescription("desgroup")
        userGroupVO.setSeq(2)
        userGroupVO.setEnable(true)
        userGroupVO.getName() == "group"
        userGroupVO.getDescription() == "desgroup"
        userGroupVO.getSeq() == 2
        userGroupVO.enable == true
        UserVO userVO1 = new UserVO()
        userGroupVO.add(userVO1)
        userGroupVO.add(roleVO)
        userGroupVO.remove(userVO1)
        userGroupVO.remove(roleVO)
        userGroupVO.getUsers()
        userGroupVO.getRoles()


        UserVO userVO = new UserVO()
        userVO.setUsername("username")
        userVO.setPassword("pwd")
        userVO.setEmail("email")
        userVO.setSuperuser(false)
        userVO.setPhone("phone")
        userVO.setName("name")
        userVO.setEnable(true)
        userVO.getUsername() == "username"
        userVO.getPassword() == "pwd"
        userVO.getEmail() == "email"
        userVO.superuser == false
        userVO.getPhone() == "phone"
        userVO.getName() == "name"
        userVO.enable == true
        userVO.equals()
        userVO.add(roleVO)
        userVO.remove(roleVO)
        userVO.setPepDtype("1")
        userVO.getPepDtype()
        userVO.hashCode()
        userVO.getUserGroups()
        userVO.getRoles()

    }

    @Test
    void testAccessToken(){
        coverBean(new AccessTokenEntity())
    }
}
