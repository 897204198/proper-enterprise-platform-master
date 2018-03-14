package com.proper.enterprise.platform.oopsearch.util

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class SqlInstallUtilTest extends AbstractTest{
    @Test
    void test(){
        String result = SqlInstallUtil.getDefaultTypeOperate("text")
        assert result == "like"

        String[] arr = ['2018-03-01', '2018-02-01']
        String str = SqlInstallUtil.addWhereDateRange("pep_auth_users", "createTime",arr)
        assert str == "t0.createTime >= '2018-02-01 00:00:00.000'  and t0.createTime <= '2018-03-01 23:59:59.999' "
    }
}
