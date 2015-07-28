package com.proper.enterprise.platform.core.conf

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class ConfManagerSpec extends Specification {
    
    static ConfManager manager
    
    def setupSpec() {
        manager = ConfManager.getConf('common.utils')
    }
    
    def "Get '#key' with default value '#defVal'"() {
        expect:
        result.equals manager.getString(key, defVal)
        
        where:
        key         | defVal                                    | result
        'notdefine' | null                                      | null
        'notdefine' | 'this value is not define in config file' | 'this value is not define in config file'
        'novalue'   | null                                      | ''
        'novalue'   | 'default value'                           | ''
        'mode'      | null                                      | 'test'
        
    }
    
    def "Get '#key' with default value #defVal"() {
        expect:
        result == manager.getInt(key, defVal)
        
        where:
        key         | defVal    | result
        'notdefine' | null      | null
        'notdefine' | 20050103  | 20050103
        'priority'  | null      | 16
    }
    
    def "No value and wrong type"() {
        when:
        manager.getInt('novalue')
        
        then:
        thrown(NumberFormatException)
    }
    
    def "One level dir"() {
        when:
        ConfManager mngr = ConfManager.getConf('common')
        
        then:
        mngr.getString('key').equals("i'm in common.properties")
    }
    
    def "Read conf from jar"() {
        when:
        ConfManager mngr = ConfManager.getConf('dfs.mongodb')
        
        then:
        mngr.getInt('port', 27027) == 27018
        mngr.getString('dbname', 'default').equals('default')
        mngr.getString('host').equals('127.0.0.1')
    }
    
    def "Get value directly"() {
        expect:
        ConfManager.getString('dfs.mongodb', 'dbname', 'default').equals('default')
        ConfManager.getInteger('dfs.mongodb', 'port', 27027) == 27018
        ConfManager.getInt('dfs.mongodb', 'port', 27027) == 27018
    }
    
}
