package com.proper.enterprise.platform.oopsearch.repository;

import com.proper.enterprise.platform.oopsearch.document.SearchDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;

/**
 * 同步mongo所使用的repository
 */
public interface SyncMongoRepository extends MongoRepository<SearchDocument, String> {

    void deleteAllByTabIn(Collection<String> tabs);

}
