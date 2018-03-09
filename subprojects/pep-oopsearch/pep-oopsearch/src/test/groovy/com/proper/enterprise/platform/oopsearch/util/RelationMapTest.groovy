package com.proper.enterprise.platform.oopsearch.util

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class RelationMapTest extends AbstractTest{

    @Test
    void test(){
        RelationMap relationMap = new RelationMap()
        relationMap.put(null,null)
        assert !relationMap.containsReverseValue("a")
    }
}
