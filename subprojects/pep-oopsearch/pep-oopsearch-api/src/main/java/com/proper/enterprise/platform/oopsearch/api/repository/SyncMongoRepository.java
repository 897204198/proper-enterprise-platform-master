package com.proper.enterprise.platform.oopsearch.api.repository;

import com.proper.enterprise.platform.oopsearch.api.document.SearchDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 同步mongo所使用的repository
 * */
public interface SyncMongoRepository extends MongoRepository<SearchDocument, String> {

}
