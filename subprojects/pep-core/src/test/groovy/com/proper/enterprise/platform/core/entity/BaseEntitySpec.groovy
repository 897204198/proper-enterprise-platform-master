package com.proper.enterprise.platform.core.entity

import spock.lang.Specification

class BaseEntitySpec extends Specification {

    def "Check setter works fine"() {
        given:
        def entity = new BaseEntity()
        def createTime = '2016-06-08 10:42:00'
        def modifyTime = '2016-06-08 10:42:00'

        when:
        entity.setCreateTime(createTime)
        entity.setLastModifyTime(modifyTime)

        then:
        entity.getCreateTime() == createTime
        entity.getLastModifyTime() == modifyTime
    }

}
