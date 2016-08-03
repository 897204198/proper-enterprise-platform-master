package com.proper.enterprise.platform.core.utils
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class SpELParserTest extends AbstractTest {

    @Autowired
    SpELParser parser

    @Test
    public void getBeanInSpEL() {
        mockUser('id', 'name', 'pwdInSpEl')

        String tpl = '{usergroup: {$in: [#{@hikariConfig.autoCommit}]}}'
        def val = parser.parse(tpl)

        assert val == "{usergroup: {\$in: [${ConfCenter.get('database.autoCommit')}]}}".toString()
        assert '' == parser.parse('#{@hikariConfig.catalog}')
    }

}
