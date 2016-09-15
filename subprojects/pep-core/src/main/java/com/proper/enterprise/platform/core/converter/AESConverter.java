package com.proper.enterprise.platform.core.converter;

import com.proper.enterprise.platform.core.utils.ConfCenter;
import com.proper.enterprise.platform.core.utils.cipher.AES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AESConverter implements AttributeConverter<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AESConverter.class);

    private AES aes = new AES(ConfCenter.get("core.secret.aes.mode"),
                              ConfCenter.get("core.secret.aes.padding"),
                              ConfCenter.get("core.secret.aes.key"));

    @Override
    public String convertToDatabaseColumn(String attribute) {
        String result = "";
        try {
            result = aes.encrypt(attribute);
        } catch (Exception e) {
            LOGGER.error("Error occurs when encrypt data: ", e);
        }
        return result;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        String result = "";
        try {
            result = aes.decrypt(dbData);
        } catch (Exception e) {
            LOGGER.error("Error occurs when decrypt data: ", e);
        }
        return result;
    }

}
