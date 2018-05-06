package com.proper.enterprise.platform.sys.datadic.service.impl

import com.proper.enterprise.platform.sys.datadic.DataDic
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity
import com.proper.enterprise.platform.sys.datadic.service.DataDicService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class DataDicServiceImplTest extends AbstractTest{

    @Autowired
    DataDicService dataDicService

    @Test
    void test(){
        DataDic dic = new DataDicEntity()
        dic.setCatalog("catalog")
        dic.setCode("code")
        dic.setId("id")
        dic = dataDicService.save(dic)
        dataDicService.findByCatalog(dic.getCatalog())
        dataDicService.get(dic.getCatalog(), dic.getCode())
        dataDicService.getDefault(dic.getCatalog())
        dataDicService.get("id")
    }
}
