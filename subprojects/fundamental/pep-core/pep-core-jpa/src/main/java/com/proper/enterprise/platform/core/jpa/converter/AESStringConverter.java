package com.proper.enterprise.platform.core.jpa.converter;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.cipher.AES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 可用于 JPA 实体中字符串类型属性与数据库表字段实际存储值的自动加密转换类
 * 默认不自动生效，可通过 @Converter 注解的 autoApply 属性设置是否自动生效
 * 或在实体的属性上通过注解 @Convert(converter = AESStringConverter.class) 启用
 */
@Converter
public class AESStringConverter implements AttributeConverter<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AESStringConverter.class);

    private AES aes = new AES(PEPPropertiesLoader.load(CoreProperties.class).getSecretAesMode(),
        PEPPropertiesLoader.load(CoreProperties.class).getSecretAesPadding(),
        PEPPropertiesLoader.load(CoreProperties.class).getSecretAesKey());

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (StringUtil.isNull(attribute)) {
            return attribute;
        }
        String result = "";
        try {
            result = new String(aes.encrypt(attribute.getBytes(PEPPropertiesLoader.load(CoreProperties.class).getCharset())),
                PEPPropertiesLoader.load(CoreProperties.class).getCharset());
        } catch (Exception e) {
            LOGGER.error("Error occurs when encrypt data: ", e);
        }
        return result;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (StringUtil.isNull(dbData)) {
            return dbData;
        }
        String result = "";
        try {
            result = new String(aes.decrypt(dbData.getBytes(PEPPropertiesLoader
                .load(CoreProperties.class).getCharset())), PEPPropertiesLoader.load(CoreProperties.class).getCharset());
        } catch (Exception e) {
            LOGGER.error("Error occurs when decrypt data: ", e);
        }
        return result;
    }

}
