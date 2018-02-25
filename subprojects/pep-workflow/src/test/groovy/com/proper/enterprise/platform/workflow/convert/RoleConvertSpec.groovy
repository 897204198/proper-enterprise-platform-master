package com.proper.enterprise.platform.workflow.convert

import com.proper.enterprise.platform.api.auth.model.Role
import com.proper.enterprise.platform.auth.common.entity.RoleEntity
import spock.lang.Specification

class RoleConvertSpec extends Specification{
    private static final ROLE_NAME="testRole"
    private static final ROLE_ID="testRoleId"
    def "testConvert"(){
        List nullList=new ArrayList()
        nullList.add(null)
        def role=new RoleEntity()
        role.setName(ROLE_NAME)
        role.setId(ROLE_ID)
        List<Role> roles=new ArrayList<>()
        roles.add(role)
        expect:
        assert null==RoleConvert.convert(null)
        assert 0==RoleConvert.convertCollection(nullList).size()
        assert ROLE_NAME==RoleConvert.convert(role).name
        assert ROLE_ID==RoleConvert.convert(role).id
        assert ROLE_NAME==RoleConvert.convertCollection(roles).get(0).name
        assert ROLE_ID==RoleConvert.convertCollection(roles).get(0).id
    }
}
