package com.proper.enterprise.platform.core.json

import spock.lang.Specification
import spock.lang.Unroll

import com.proper.enterprise.platform.core.entity.BaseEntity

class JSONUtilSpec extends Specification {
    
    static BaseEntity entity = new BaseEntity()
    
    def setupSpec() {
        entity.setId('abcdefg')
        entity.setCreateUserId('999')
        entity.setLastModifyUserId('888')
    }
    
    @Unroll
    def "Object #obj to JSON string is #result"() {
        expect:
        result == JSONUtil.toJSONString(obj)
        
        where:
        obj                                             | result   
        [a: 'a', b: 'b']                                | '{"a":"a","b":"b"}'
        [[a1: 'a1', a2: 'a2'], [b1: 'b1', b2: 'b2']]    | '[{"a1":"a1","a2":"a2"},{"b1":"b1","b2":"b2"}]' 
        [id: 123, text: '中文']                          | '{"id":123,"text":"中文"}'
    }
    
    def "Entity to JSON string"() {
        given:
        def result = JSONUtil.toJSONString(obj)
        println "Entity to JSON string result is: $result"
        
        expect:
        result > ''
        
        where:
        _ | obj
        _ | entity
        _ | [entity, entity]
    }
    
}
