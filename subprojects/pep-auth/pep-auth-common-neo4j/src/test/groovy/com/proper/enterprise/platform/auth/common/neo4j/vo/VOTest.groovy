package com.proper.enterprise.platform.auth.common.neo4j.vo

import com.proper.enterprise.platform.api.auth.enums.PermissionType
import com.proper.enterprise.platform.api.auth.model.DataRestrain
import com.proper.enterprise.platform.api.auth.model.Menu
import com.proper.enterprise.platform.api.auth.model.Permission
import com.proper.enterprise.platform.auth.common.vo.*
import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMethod

class VOTest extends AbstractNeo4jTest {

    @Autowired
    DataDicService dataDicService

    @Sql("/com/proper/enterprise/platform/auth/common/neo4j/datadics.sql")
    @Transactional(transactionManager = "jpaTransactionManager",propagation=Propagation.REQUIRES_NEW)
    @Test
    void test() {
        coverBean(new DataRestrainVO())

        Menu menu = new MenuVO()
        Permission permission = new Permission(menu, PermissionType.ASSIGN)
        permission.getMenu()
        permission.could(PermissionType.USE)

        coverBean(new DataRestrainVO())

        BaseVO baseVO = new BaseVO()
        baseVO.setId("id")
        baseVO.setCreateTime("creatTime")
        baseVO.setCreateUserId("creatUserId")
        baseVO.setLastModifyTime("time1")
        baseVO.setLastModifyUserId("lastUserId")
        baseVO.setValid(true)
        baseVO.getId() == "id"
        baseVO.getCreateTime() == "creatTime"
        baseVO.getCreateUserId() == "creatUserId"
        baseVO.getLastModifyTime() == "time1"
        baseVO.getLastModifyUserId() == "lastUserId"

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
        ResourceVO resource = new ResourceVO()
        menuVO.add(resource)
        menuVO.remove(resource)
        menuVO.compareTo(menuVO)
        menuVO.setMenuType(dataDicService.get("MENU_TYPE", "1"))
        menuVO.getResources()
        menuVO.getRoles()
        menuVO.getChildren()
        menuVO.getMenuType()
        menuVO.equals(null)
        menuVO.isLeaf()
        menuVO.isRoot()

        ResourceVO resourceVO = new ResourceVO()
        resourceVO.setName("resource")
        resourceVO.setURL("url")
        resourceVO.setMethod(RequestMethod.GET)
        resourceVO.setEnable(true)
        resourceVO.getName() == "resource"
        resourceVO.getURL() == "url"
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
}
