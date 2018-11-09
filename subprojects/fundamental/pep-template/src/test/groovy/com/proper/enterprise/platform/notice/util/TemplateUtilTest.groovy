package com.proper.enterprise.platform.notice.util

import com.proper.enterprise.platform.template.util.TemplateUtil
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test

class TemplateUtilTest extends AbstractJPATest {

    @Test
    void paramCheck() {
        int i1 = 1
        assert "1" == TemplateUtil.paramValid(i1)
        int i2
        assert "0" == TemplateUtil.paramValid(i2)

        String s1 = "str"
        assert "str" == TemplateUtil.paramValid(s1)
        String s2 = ""
        assert "" == TemplateUtil.paramValid(s2)
        String s3 = null
        assert "" == TemplateUtil.paramValid(s3)

        double d1 = 3.14
        assert "3.14" == TemplateUtil.paramValid(d1)
        double d2
        assert "0.0" == TemplateUtil.paramValid(d2)

        float f1 = 3.1415f
        assert "3.1415" == TemplateUtil.paramValid(f1)
        float f2
        assert "0.0" == TemplateUtil.paramValid(f2)

        long l1 = 100000
        assert "100000" == TemplateUtil.paramValid(l1)
        long l2
        assert "0" == TemplateUtil.paramValid(l2)

        boolean b1 = true
        assert "true" == TemplateUtil.paramValid(b1)
        boolean b2 = false
        assert "false" == TemplateUtil.paramValid(b2)
        boolean b3
        assert "false" == TemplateUtil.paramValid(b3)

        Date da1 = new Date()
        assert "" != TemplateUtil.paramValid(da1)

    }

}
