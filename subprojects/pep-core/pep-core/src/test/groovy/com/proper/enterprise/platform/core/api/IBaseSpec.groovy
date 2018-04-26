package com.proper.enterprise.platform.core.api

import com.proper.enterprise.platform.core.entity.BaseEntity
import spock.lang.Specification
import spock.lang.Unroll

class IBaseSpec extends Specification {

    @Unroll
    def "Check setter of #clz works fine"() {
        def obj = clz.newInstance()
        obj.setId(id)
        obj.setCreateTime(time)
        obj.setLastModifyTime(time)
        obj.setCreateUserId(user)
        obj.setLastModifyUserId(user)

        expect:
        obj.getId() == id
        obj.getCreateTime() == time
        obj.getLastModifyTime() == time
        obj.getCreateUserId() == user
        obj.getLastModifyUserId() == user

        where:
        clz              | time                  | user | id
        BaseEntity.class | '2016-06-08 10:42:00' | 'a'  | '1'
    }

}
