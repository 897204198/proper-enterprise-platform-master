package com.proper.enterprise.platform.sys.datadic.convert

import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.sys.datadic.DataDic
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.test.context.jdbc.Sql

class DataDicLiteConverterTest extends AbstractTest {

    @Test
    void testConvertToDatabaseColumn() {
        String separator = ConfCenter.get("sys.datadic.separator", ";")
        DataDicLiteConverter dataDicLiteConverter = new DataDicLiteConverter()
        DataDicEntity dataDicEntity = new DataDicEntity()
        dataDicEntity.setName("name")
        dataDicEntity.setCode("code")
        dataDicEntity.setCatalog("catalog")
        dataDicEntity.setDefault(true)
        dataDicEntity.setOrder(1)
        assert "catalog" + separator + "code" == dataDicLiteConverter.convertToDatabaseColumn(dataDicEntity)
        assert null == dataDicLiteConverter.convertToDatabaseColumn(null)
    }

    @Test
    void testConvertToEntityAttribute() {
        String separator = ConfCenter.get("sys.datadic.separator", ";")
        DataDicLiteConverter dataDicLiteConverter = new DataDicLiteConverter()
        DataDicLiteBean dataDicEntity = dataDicLiteConverter.convertToEntityAttribute("catalog" + separator + "code" + separator + "name")
        DataDicLiteBean dataDicEntity2 = dataDicLiteConverter.convertToEntityAttribute(null)
        assert "catalog" == dataDicEntity.getCatalog()
        assert null == dataDicEntity2
    }


    @Test
    @Sql("/com/proper/enterprise/platform/sys/datadics.sql")
    void testConvertWithName() {
        DataDic dataDic = DataDicUtil.get("ModelStatus", "DEPLOYED")
        assert dataDic.getCatalog() == "ModelStatus"
        assert dataDic.getCode() == "DEPLOYED"
        assert dataDic.getName() == "已部署"

        DataDic dataDic2 = DataDicUtil.get("ModelStatus", "NO_NAME")
        assert dataDic2.getCatalog() == "ModelStatus"
        assert dataDic2.getCode() == "NO_NAME"
        assert dataDic2.getName() == ""
    }
}
