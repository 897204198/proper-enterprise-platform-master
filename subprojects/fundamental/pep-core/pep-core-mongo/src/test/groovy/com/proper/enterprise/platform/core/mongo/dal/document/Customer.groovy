package com.proper.enterprise.platform.core.mongo.dal.document

import com.proper.enterprise.platform.core.PEPVersion
import com.proper.enterprise.platform.core.mongo.document.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Customer extends BaseDocument {

    private static final long serialVersionUID = PEPVersion.VERSION

    private firstName, lastName

    Customer(String firstName, String lastName) {
        this.firstName = firstName
        this.lastName = lastName
    }

}
