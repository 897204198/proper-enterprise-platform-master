package com.proper.enterprise.platform.core.mongo.jackson;

import com.fasterxml.jackson.databind.JsonSerializer;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service("documentJsonSerializerFactory")
public class DocumentJsonSerializerFactory {

    public Map<Class<?>, JsonSerializer<?>> initDocumentSerializer() {
        Map<Class<?>, JsonSerializer<?>> jsonSerializerMap = new LinkedHashMap<>();
        jsonSerializerMap.put(Document.class, new DocumentJsonSerializer());
        return jsonSerializerMap;
    }
}
