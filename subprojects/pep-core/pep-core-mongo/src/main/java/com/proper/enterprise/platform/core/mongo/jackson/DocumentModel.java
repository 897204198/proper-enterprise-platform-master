package com.proper.enterprise.platform.core.mongo.jackson;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.Document;

public class DocumentModel extends SimpleModule {

    public DocumentModel() {
        super(PackageVersion.VERSION);
        addSerializer(Document.class, new DocumentJsonSerializer());
    }

    @Override
    public String getModuleName() {
        return getClass().getSimpleName();
    }

}
