package com.proper.enterprise.platform.core.entity
import spock.lang.Specification

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.core.IsEqual.equalTo

class BaseEntitySpec extends Specification {

    def "Check put and get extend properties in BaseEntity"() {
        given:
        def entity = new BaseEntity()

        when:
        entity.putExtendProperty('a', '1')
        entity.putExtendProperty('b', '2')
        entity.putExtendProperty(['c': '3', 'd': '4'])

        then:
        ['a':'1', 'b':'2', 'c':'3', 'd':'4'].entrySet().each { entry ->
            assertThat entity.getExtendProperty(entry.key), equalTo(entry.value)
        }
        entity.getExtendProperty('not exist') == null
    }

    def "Check get extend properties when it's null"() {
        given:
        def entity = new BaseEntity()
        entity.setExtendProperties(null);

        expect:
        entity.getExtendProperty('abc') == null
        entity.getExtendProperties() == null
    }

    def "Put extend properties in string and get as object"() {
        given:
        def entity = new BaseEntity()

        when:
        entity.setExtendProperties('{"abc": "123", "中文": "测试"}')

        then:
        entity.getExtendProperty('abc') == '123'
        entity.getExtendProperty('中文') == '测试'
    }

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
