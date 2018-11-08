package com.proper.enterprise.platform.core.mongo.document.mock.document

import com.fasterxml.jackson.annotation.JsonIgnore
import com.proper.enterprise.platform.core.PEPVersion
import com.proper.enterprise.platform.core.mongo.document.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = 'mock')
class MockDocument extends BaseDocument {

    private static final long serialVersionUID = PEPVersion.VERSION

    @JsonIgnore
    String docC1

    @Field('C2')
    String docC2

    String createTime

    MockDocument(String c1, String c2) {
        docC1 = c1
        docC2 = c2
    }

    @Override
    String getCreateTime() {
        return createTime
    }

    @Override
    void setCreateTime(String createTime) {
        this.createTime = createTime
    }

}
