package com.proper.enterprise.platform.oopsearch.sync.mysql.repository;

import com.proper.enterprise.platform.oopsearch.api.document.SearchDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SyncMongoRepository extends MongoRepository<SearchDocument, String> {

}
