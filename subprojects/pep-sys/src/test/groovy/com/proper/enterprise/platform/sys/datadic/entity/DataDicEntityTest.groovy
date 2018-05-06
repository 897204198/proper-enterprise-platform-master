package com.proper.enterprise.platform.sys.datadic.entity

import com.proper.enterprise.platform.sys.datadic.DataDic
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class DataDicEntityTest extends AbstractTest{

    @Test
    void  test(){
        DataDicEntity data = new DataDicEntity()
        DataDic dic = new DataDicEntity()
        data.isDefault()
        data.getOrder()
        data.getName()
        data.equals(data)
        data.equals(dic)
        data.toString()
        data.hashCode()
    }
}
