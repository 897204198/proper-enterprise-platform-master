package com.proper.enterprise.platform.oopsearch.util

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class SqlInstallUtilTest extends AbstractTest{
    @Test
    void test(){
        String result = SqlInstallUtil.getDefaultTypeOperate("text")
        assert result == "like"

        String[] arr = ['2018-03-01', '2018-02-01']
        String str = SqlInstallUtil.addWhereDateRange("createTime",arr)
        assert str == "createTime >= '2018-02-01'  and createTime <= '2018-03-01' "
    }
}
