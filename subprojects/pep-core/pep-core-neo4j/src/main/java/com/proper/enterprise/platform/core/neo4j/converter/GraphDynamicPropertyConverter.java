package com.proper.enterprise.platform.core.neo4j.converter;

import org.neo4j.ogm.typeconversion.CompositeAttributeConverter;

import java.util.HashMap;
import java.util.Map;

public class GraphDynamicPropertyConverter implements CompositeAttributeConverter<Map<String, Object>> {

    @Override
    public Map<String, ?> toGraphProperties(Map<String, Object> value) {
        return value;
    }

    @Override
    public Map<String, Object> toEntityAttribute(Map<String, ?> value) {
        Map<String, Object> properties = new HashMap<>();
        if (value != null) {
            properties = (Map<String, Object>) value;
        }
        return properties;
    }
}
