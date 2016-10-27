package com.proper.enterprise.platform.core.mongo.dal.document

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.document.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Customer extends BaseDocument {

    private static final long serialVersionUID = PEPConstants.VERSION;

    private firstName, lastName;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
