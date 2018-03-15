package com.proper.enterprise.platform.oopsearch.api

import com.proper.enterprise.platform.oopsearch.api.conf.DemoDeptConfigs
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class TestConfigAndModel extends AbstractTest{
    @Autowired
    DemoDeptConfigs demoDeptConfigs

    @Test
    void testAbstractSearchConfigs(){
        def r1 = demoDeptConfigs.getSearchColumnListByType("string")
        assert r1.size() == 2
        def r2 = demoDeptConfigs.getSearchTableColumnMap()
        assert r2.size() == 1
        def r3 = demoDeptConfigs.getTableNameList()
        assert r3.size() == 1
        def r4 = demoDeptConfigs.getSearchColumnListByTable("demo_dept")
        assert r4.size() == 4
        def r5 = demoDeptConfigs.getLimit()
        assert r5 == 10
        def r6 = demoDeptConfigs.getExtendByYearArr()
        assert r6.length == 3
        def r7 = demoDeptConfigs.getExtendByMonthArr()
        assert r7.length == 3
        def r8 = demoDeptConfigs.getExtendByDayArr()
        assert r8.length == 3

    }
}
