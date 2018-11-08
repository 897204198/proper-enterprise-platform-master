package com.proper.enterprise.platform.core.jpa.converter;

import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Converter
public class MapJsonStringConverter implements AttributeConverter<Map, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapJsonStringConverter.class);

    @Override
    public String convertToDatabaseColumn(Map map) {
        return JSONUtils.toJSONString(map);
    }

    @Override
    public Map convertToEntityAttribute(String dbData) {
        Map<String, String> map = new HashMap<>(16);
        if (StringUtil.isNotBlank(dbData)) {
            try {
                JsonNode node = JSONUtil.parse(dbData, JsonNode.class);
                Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
                while (iterator.hasNext()) {
                    Map.Entry<String, JsonNode> entry = iterator.next();
                    map.put(entry.getKey(), entry.getValue().textValue());
                }
            } catch (IOException e) {
                LOGGER.warn("Data of json parse exception! data is " + dbData, e);
            }
        }
        return map;
    }

}
