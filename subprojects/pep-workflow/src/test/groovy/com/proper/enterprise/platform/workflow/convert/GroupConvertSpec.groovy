package com.proper.enterprise.platform.workflow.convert

import com.proper.enterprise.platform.api.auth.model.UserGroup
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity
import spock.lang.Specification

class GroupConvertSpec extends Specification{
    private static final GROUP_NAME="testGroup"
    private static final GROUP_ID="testGroupId"
    def "testConvert"(){
        List nullList=new ArrayList()
        nullList.add(null)
        def userGroup=new UserGroupEntity()
        userGroup.setName(GROUP_NAME)
        userGroup.setId(GROUP_ID)
        List<UserGroup> userGroups=new ArrayList<>()
        userGroups.add(userGroup)
        expect:
        assert null==GroupConvert.convert(null)
        assert 0==GroupConvert.convertCollection(nullList).size()
        assert GROUP_NAME==GroupConvert.convert(userGroup).name
        assert GROUP_ID==GroupConvert.convert(userGroup).id
        assert GROUP_NAME==GroupConvert.convertCollection(userGroups).get(0).name
        assert GROUP_ID==GroupConvert.convertCollection(userGroups).get(0).id
    }
}
