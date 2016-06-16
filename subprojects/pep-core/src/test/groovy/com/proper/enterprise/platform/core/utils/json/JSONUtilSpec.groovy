package com.proper.enterprise.platform.core.utils.json
import com.proper.enterprise.platform.core.entity.BaseEntity
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class JSONUtilSpec extends Specification {

    static BaseEntity entity = new BaseEntity()

    def setupSpec() {
        entity.setId('abcdefg')
        entity.setCreateUserId('999')
        entity.setLastModifyUserId('888')
    }

    def "Object #obj to JSON string is #result"() {
        expect:
        result == JSONUtil.toJSON(obj)

        where:
        obj                                             | result
        [a: 'a', b: 'b']                                | '{"a":"a","b":"b"}'
        [[a1: 'a1', a2: 'a2'], [b1: 'b1', b2: 'b2']]    | '[{"a1":"a1","a2":"a2"},{"b1":"b1","b2":"b2"}]'
        [id: 123, text: '中文']                          | '{"id":123,"text":"中文"}'
        ['a', 'b', 'c']                                 | '["a","b","c"]'
    }

    def "Entity to JSON string"() {
        given:
        def result = JSONUtil.toJSON(obj)
        println "Entity to JSON string result is: $result"

        expect:
        result ==~ regEx

        where:
        obj                 | regEx
        entity              | /\{(".*":"?.*"?,?)+\}/
        [entity, entity]    | /\[\{.*\},\{.*\}\]/
    }

    def "Parse JSON string to generic object"() {
        given:
        def result = JSONUtil.parseObject(json)

        expect:
        keys.split('.').each {

        }
        result.get(key) == value

        where:
        keys        | value    | json
        'email'     | 'aaaa'   | '{"firstName":"Brett","lastName":"McLaughlin","email":"aaaa"}'
        'child.id'  | '113000' | """
{
  id: '100000',
  text: '廊坊银行总行',
  child: {
          id: '113000',
          text: '廊坊银行开发区支行',
          leaf: true
        }
}
"""
    }

    def "Parse JSON string to object"() {
        given:
        def result = JSONUtil.parse(str)
        def len = 0
        if (result instanceof JSONObject) {
            len = result.size()
            println result.keySet()
        } else if (result instanceof JSONObject[]) {
            len = result.length
            result.each {
                println it.keySet()
            }
        }

        expect:
        result != null
        len == size

        where:
        size    | str
        2       | '{"a":"a1","b":"b2"}'
        2       | '[{"a1":"a1","a2":"a2","a3":"a3"},{"b1":"b1","b2":"b2","b3":"b3"}]'
    }

}
