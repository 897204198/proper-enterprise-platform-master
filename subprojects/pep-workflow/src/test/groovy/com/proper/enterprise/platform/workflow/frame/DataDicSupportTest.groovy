package com.proper.enterprise.platform.workflow.frame

import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.junit.Test
import org.springframework.test.context.jdbc.Sql

class DataDicSupportTest extends WorkflowAbstractTest {

    private static final String DATADIC_SUPPORT_KEY = 'dataDicSupport'

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql"])
    public void dataDicSupportTest() {
        Map map = new HashMap()
        map.put("a", "{\"catalog\":\"12313\",\"code\":\"4442\"}")
        PEPProcInstVO unFinish = start(DATADIC_SUPPORT_KEY, map)
        assert !isEnded(unFinish.getProcInstId())
        map.put("a", "{\"catalog\":\"12313\",\"code\":\"4441\"}")
        PEPProcInstVO finish = start(DATADIC_SUPPORT_KEY, map)
        assert isEnded(finish.getProcInstId())

    }
}
