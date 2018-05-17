package com.proper.enterprise.platform.core.mongo.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class DocumentObjectMapper extends ObjectMapper {
    public DocumentObjectMapper() {
        this.registerModule(new DocumentModel());
    }
}
