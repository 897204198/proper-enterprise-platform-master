package com.proper.enterprise.platform.core.mongo.document.mock.repository

import com.proper.enterprise.platform.core.mongo.document.mock.document.MockDocument
import org.springframework.data.mongodb.repository.MongoRepository


interface MockRepository extends MongoRepository<MockDocument, String> {

}
