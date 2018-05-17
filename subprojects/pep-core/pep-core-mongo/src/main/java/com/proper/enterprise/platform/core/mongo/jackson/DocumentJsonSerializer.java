package com.proper.enterprise.platform.core.mongo.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.Document;
import java.io.IOException;

public class DocumentJsonSerializer extends JsonSerializer<Document> {

    @Override
    public void serialize(Document o, JsonGenerator j, SerializerProvider s) throws IOException {
        if (o == null) {
            j.writeNull();
        } else {
            j.writeRawValue(o.toJson());
        }
    }

}


