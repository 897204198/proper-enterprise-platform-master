package com.proper.enterprise.platform.oopsearch.util

import com.proper.enterprise.platform.core.repository.NativeRepository
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class RelationTest extends AbstractTest {

    @Autowired
    private NativeRepository nativeRepository;

    @Test
    void testTable() {
        //双向关联，不分先后
        Relation relation = new Relation("tableRelation")
        List<RelationNode> list = relation.findRelation("pep_auth_users", "pep_auth_roles")
        assert list.size() == 3
    }


    /*
        打印所有最短路径
        调试用，代码覆盖率角度考虑注释掉了
    */
//     public void printRelation(List<RelationNode> list) {
//         if (list != null && list.size() > 0) {
//             for (RelationNode relationNode : list) {
//                 printRelation(relationNode);
//             }
//         } else {
//             LOGGER.debug("查无结果");
//         }
//     }

    /*
        打印边情况
    */
//    private void printLines() {
//        StringBuffer stringBuffer = new StringBuffer("\n");
//        for (int i = 0; i < lines.length; i++) {
//            for (int j = 0; j < lines[i].length; j++) {
//                stringBuffer.append(lines[i][j] + " ");
//                if (j == lines.length - 1) {
//                    stringBuffer.append("\n");
//                }
//            }
//        }
//        stringBuffer.append("\n");
//        LOGGER.debug(stringBuffer.toString());
//    }

//    private List<RelationNode> openRelation(RelationNode relationNode) {
//        List<RelationNode> list = new ArrayList<>();
//        list.add(relationNode);
//        while (relationNode.getParentNode() != null) {
//            relationNode = relationNode.getParentNode();
//            list.add(relationNode);
//        }
//        Collections.reverse(list);
//           StringBuffer stringBuffer = new StringBuffer("\n");
//           for (RelationNode temp : list) {
//               stringBuffer.append(temp.getLevel() + ":" + temp.getRelationNodeId() + "\n");
//           }
//           LOGGER.debug(stringBuffer.toString());
//        return list;
//    }
}
