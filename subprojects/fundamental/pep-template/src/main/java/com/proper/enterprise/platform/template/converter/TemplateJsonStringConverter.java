package com.proper.enterprise.platform.template.converter;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.template.vo.TemplateDetailVO;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Converter
public class TemplateJsonStringConverter implements AttributeConverter<List, String> {

    @Override
    public String convertToDatabaseColumn(List attribute) {
        return JSONUtil.toJSONIgnoreException(attribute);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List convertToEntityAttribute(String dbData) {
        List result = new ArrayList<>();
        if (StringUtil.isNotBlank(dbData)) {
            List<LinkedHashMap> list = JSONUtil.parseIgnoreException(dbData, List.class);
            if (list == null) {
                result = new ArrayList<>();
            } else {
                for (LinkedHashMap<String, String> temp : list) {
                    TemplateDetailVO templateDetailVO = new TemplateDetailVO();
                    templateDetailVO.setTemplate(temp.get("template"));
                    templateDetailVO.setTitle(temp.get("title"));
                    templateDetailVO.setType(temp.get("type"));
                    result.add(templateDetailVO);
                }
            }
        }
        return result;
    }

}
