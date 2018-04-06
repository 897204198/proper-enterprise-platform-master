package com.proper.enterprise.platform.oopsearch.api.repository;

import com.proper.enterprise.platform.oopsearch.api.document.SearchDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SearchMongoRepository extends MongoRepository<SearchDocument, String> {

    List<SearchDocument> findByTabInAndConLike(List table, String content, Pageable pageable);
}
