package com.proper.enterprise.platform.sys.datadic.converter;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static com.proper.enterprise.platform.sys.datadic.service.DataDicService.DD_CATALOG_CODE_SEPARATOR;

@Converter
public class DataDicConverter implements AttributeConverter<DataDic, String> {

    private DataDicService service = PEPApplicationContext.getBean(DataDicService.class);

    @Override
    public String convertToDatabaseColumn(DataDic attribute) {
        return attribute.getCatalog() + DD_CATALOG_CODE_SEPARATOR + attribute.getCode();
    }

    @Override
    public DataDic convertToEntityAttribute(String dbData) {
        if (StringUtil.isNull(dbData) || !dbData.contains(DD_CATALOG_CODE_SEPARATOR)) {
            return null;
        }
        String[] args = dbData.split(DD_CATALOG_CODE_SEPARATOR);
        return service.get(args[0], args[1]);
    }

}
