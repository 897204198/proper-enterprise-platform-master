package com.proper.enterprise.platform.workflow.convert

import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.auth.jpa.entity.UserEntity
import spock.lang.Specification

class UserConvertSpec extends Specification{
    private static final USER_NAME="testUser"
    private static final USER_ID="testUserId"
    def "testConvert"(){
        List nullList=new ArrayList()
        nullList.add(null)
        def user=new UserEntity()
        user.setName(USER_NAME)
        user.setId(USER_ID)
        List<User> users=new ArrayList<>()
        users.add(user)
        expect:
        assert null==UserConvert.convert(null)
        assert 0==UserConvert.convertCollection(nullList).size()
        assert USER_NAME==UserConvert.convert(user).firstName
        assert USER_ID==UserConvert.convert(user).id
        assert USER_NAME==UserConvert.convertCollection(users).get(0).firstName
        assert USER_ID==UserConvert.convertCollection(users).get(0).id
    }
}
