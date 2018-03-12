package com.proper.enterprise.platform.auth.neo4j.converter;

import org.neo4j.ogm.typeconversion.AttributeConverter;
import org.springframework.web.bind.annotation.RequestMethod;

public class RequestMethodConverter implements AttributeConverter<RequestMethod, String> {

    @Override
    public String toGraphProperty(RequestMethod value) {
        return value.name();
    }

    @Override
    public RequestMethod toEntityAttribute(String value) {
        return RequestMethod.valueOf(value);
    }
}
