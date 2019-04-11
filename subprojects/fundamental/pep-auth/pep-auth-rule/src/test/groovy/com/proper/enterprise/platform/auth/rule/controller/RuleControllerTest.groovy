package com.proper.enterprise.platform.auth.rule.controller

import com.proper.enterprise.platform.auth.rule.service.RuleService
import com.proper.enterprise.platform.sys.datadic.DataDicVO
import org.junit.Test
import com.proper.enterprise.platform.auth.rule.vo.RuleVO
import com.proper.enterprise.platform.test.AbstractJPATest
import org.springframework.beans.factory.annotation.Autowired

class RuleControllerTest extends AbstractJPATest {

    private static final URL = "/auth/rule"

    @Autowired
    private RuleService ruleService

    @Test
    void "post"() {
        mockUser('test1', 't1', 'pwd')
        RuleVO ruleVO = new RuleVO()
        ruleVO.setCode("code")
        ruleVO.setName("name")
        ruleVO.setSort(1)
        DataDicVO dataDicVO = new DataDicVO()
        dataDicVO.setName("1")
        dataDicVO.setCode("2")
        dataDicVO.setCatalog("3")
        ruleVO.setType(dataDicVO)
        RuleVO result = resOfPost(URL, ruleVO)
        expect:
        assert null != result.getId()
    }

    @Test
    void "getByCode"() {
        mockUser('test1', 't1', 'pwd')
        RuleVO ruleVO = new RuleVO()
        ruleVO.setCode("code")
        ruleVO.setName("name")
        ruleVO.setSort(1)
        DataDicVO dataDicVO = new DataDicVO()
        dataDicVO.setName("1")
        dataDicVO.setCode("2")
        dataDicVO.setCatalog("3")
        ruleVO.setType(dataDicVO)
        RuleVO result = resOfPost(URL, ruleVO)
        RuleVO getVO = ruleService.getCode("code")
        assert getVO.getName() == "name"
    }
}
