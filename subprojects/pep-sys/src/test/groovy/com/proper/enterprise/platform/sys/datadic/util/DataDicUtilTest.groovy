package com.proper.enterprise.platform.sys.datadic.util

import com.proper.enterprise.platform.sys.datadic.DataDic
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.test.context.jdbc.Sql


 class DataDicUtilTest extends AbstractTest {


    @Test
    @Sql("/com/proper/enterprise/platform/sys/datadics.sql")
    void test() {
        DataDic defName = DataDicUtil.getDefault("ModelStatus")
        assert defName.getName() == "未部署"
        Collection<DataDic> dataDics = (Collection<DataDic>) DataDicUtil.findByCatalog("ModelStatus")
        assert dataDics.size() == 3
        DataDic deployed = DataDicUtil.get("ModelStatus", "DEPLOYED")
        assert deployed.getName() == "已部署"
    }

}
