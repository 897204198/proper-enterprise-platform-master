package com.proper.enterprise.platform.sys.datadic.entity

import com.proper.enterprise.platform.sys.datadic.DataDic
import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test

class DataDicEntityTest extends AbstractSpringTest{

    @Test
    void  test(){
        DataDicEntity data = new DataDicEntity()
        DataDic dic = new DataDicEntity()
        data.getDeft()
        data.getOrder()
        data.getName()
        data.equals(data)
        data.equals(dic)
        data.toString()
        data.hashCode()
    }
}
