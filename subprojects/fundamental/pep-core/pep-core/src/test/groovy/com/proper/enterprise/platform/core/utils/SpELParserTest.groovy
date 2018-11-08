package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class SpELParserTest extends AbstractSpringTest {

    @Autowired
    SpELParser parser

    @Autowired
    CoreProperties coreProperties

    @Test
    void getBeanInSpEL() {
        mockUser('id', 'name', 'pwdInSpEl')

        String tpl = '{usergroup: {$in: [#{@coreProperties.charset}]}}'
        def val = parser.parse(tpl)

        assert val == "{usergroup: {\$in: [${coreProperties.getCharset()}]}}".toString()
        assert parser.parse('#{@coreProperties.allowUnquotedFieldNames}')
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
