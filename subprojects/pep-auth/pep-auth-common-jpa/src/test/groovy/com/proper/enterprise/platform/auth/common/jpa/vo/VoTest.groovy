package com.proper.enterprise.platform.auth.common.jpa.vo

import com.proper.enterprise.platform.auth.common.vo.BaseVO
import com.proper.enterprise.platform.auth.common.vo.DataRestrainVO
import com.proper.enterprise.platform.auth.common.vo.MenuVO
import com.proper.enterprise.platform.auth.common.vo.RoleVO
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class VoTest extends AbstractTest{
    @Test
    void test() {
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
    }
}
