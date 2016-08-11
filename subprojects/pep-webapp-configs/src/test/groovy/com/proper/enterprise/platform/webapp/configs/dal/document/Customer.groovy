package com.proper.enterprise.platform.webapp.configs.dal.document

import com.proper.enterprise.platform.core.document.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Customer extends BaseDocument {

    private firstName, lastName;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
