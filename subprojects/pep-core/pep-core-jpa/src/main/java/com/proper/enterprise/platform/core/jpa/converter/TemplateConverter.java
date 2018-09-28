package com.proper.enterprise.platform.core.jpa.converter;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class TemplateConverter implements AttributeConverter<List, String> {

    @Override
    public String convertToDatabaseColumn(List attribute) {
        return JSONUtil.toJSONIgnoreException(attribute);
    }

    @Override
    public List convertToEntityAttribute(String dbData) {
        List list = new ArrayList<>();
        if (StringUtil.isNotBlank(dbData)) {
            list = JSONUtil.parseIgnoreException(dbData, List.class);
        }
        return list;
    }

}
