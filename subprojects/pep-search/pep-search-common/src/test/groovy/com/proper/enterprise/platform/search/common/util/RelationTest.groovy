package com.proper.enterprise.platform.search.common.util

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class RelationTest extends AbstractTest{

    @Test
    void test(){
        //双向关联，不分先后
        Relation relation = new Relation()
        relation.addRelation("A", "B")
        relation.addRelation("A", "AB")
        relation.addRelation("B", "AB")
        relation.addRelation("AB", "ACB")
        relation.addRelation("B", "ACB")
        relation.addRelation("A", "D")
        relation.addRelation("AB", "D")

        //计算A到B得全路径
        List<RelationNode> list = relation.findRelation("A", "B");

        //输出路径
        relation.printRelation(list);
    }
}
