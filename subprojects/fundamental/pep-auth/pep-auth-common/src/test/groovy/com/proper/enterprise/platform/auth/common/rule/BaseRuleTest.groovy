package com.proper.enterprise.platform.auth.common.rule

import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.api.auth.service.UserGroupService
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.jpa.entity.RoleEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.auth.common.jpa.entity.UserGroupEntity
import com.proper.enterprise.platform.auth.rule.service.RuleService
import com.proper.enterprise.platform.auth.rule.vo.RuleVO
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean
import com.proper.enterprise.platform.test.AbstractJPATest
import org.springframework.beans.factory.annotation.Autowired

abstract class BaseRuleTest extends AbstractJPATest {

    @Autowired
    protected RoleService roleService

    @Autowired
    protected RuleService ruleService

    @Autowired
    protected UserService userService

    @Autowired
    protected UserGroupService userGroupService

    String createUser(String userName) {
        UserEntity userEntity = new UserEntity()
        userEntity.setName(userName)
        userEntity.setUsername(userName)
        userEntity.setPassword(userName)
        userEntity.setPhone("15944001122")
        return userService.save(userEntity).getId()
    }

    void createRule(String code, String type) {
        RuleVO ruleVO = new RuleVO()
        DataDicLiteBean dataDicVO = new DataDicLiteBean()
        dataDicVO.setCode(type)
        dataDicVO.setCatalog("RULE")
        ruleVO.setType(dataDicVO)
        ruleVO.setCode(code)
        ruleVO.setName(UUID.randomUUID().toString())
        ruleService.save(ruleVO)
    }

    void addUserRole(String userId, String roleId) {
        userService.addUserRole(userId, roleId)
    }

    String createGroup(String name, String userId) {
        UserGroupEntity userGroupEntity = new UserGroupEntity()
        userGroupEntity.setName(name)
        userGroupEntity.add(userService.get(userId))
        String id = userGroupService.save(userGroupEntity).getId()
        return id
    }

    String createRole(String roleName, String ruleCode, String ruleValue) {
        return createRole(roleName, ruleCode, ruleValue, true)
    }

    String createRole(String roleName, String ruleCode, String ruleValue, Boolean enable) {
        RoleEntity roleEnable = new RoleEntity()
        roleEnable.setName(roleName)
        roleEnable.setRuleCode(ruleCode)
        roleEnable.setRuleValue(ruleValue)
        roleEnable.setEnable(enable)
        return roleService.save(roleEnable).getId()
    }

}
