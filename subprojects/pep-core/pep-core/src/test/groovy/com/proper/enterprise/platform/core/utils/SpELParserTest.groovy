package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class SpELParserTest extends AbstractTest {

    @Autowired
    SpELParser parser

    @Test
    void getBeanInSpEL() {
        mockUser('id', 'name', 'pwdInSpEl')

        String tpl = '{usergroup: {$in: [#{@hikariConfig.autoCommit}]}}'
        def val = parser.parse(tpl)

        assert val == "{usergroup: {\$in: [${ConfCenter.get('database.autoCommit')}]}}".toString()
        assert null == parser.parse('#{@hikariConfig.catalog}')
    }

    @Test
    void notUseExpressionTemplate() {
        String spEL = """{
'M149', //abc
'0187', // def, ghi
'0010' // zzz
}[1] == #id ? 'a' : 'b'"""
        assert parser.parse(spEL, ["id": '0187'], false) == 'a'
    }

    @Test
    void booleanExp() {
        String spEL = """{
'M149', //abc
'0187', // def, ghi
'0010' // zzz
}[1] == #id"""
        assert parser.parse(spEL, ["id": '0187'], Boolean.class)
    }

}
