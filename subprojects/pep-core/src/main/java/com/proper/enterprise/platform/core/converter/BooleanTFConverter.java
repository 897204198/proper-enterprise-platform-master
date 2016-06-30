package com.proper.enterprise.platform.core.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA 实体中 boolean 类型属性与数据库表字段实际存储值的自动转换类
 */
@Converter(autoApply = true)
public class BooleanTFConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return Boolean.TRUE.equals(attribute) ? "T" : "F";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "T".equals(dbData);
    }

}
