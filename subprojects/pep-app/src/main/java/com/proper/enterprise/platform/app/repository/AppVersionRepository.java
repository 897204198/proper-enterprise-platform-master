package com.proper.enterprise.platform.app.repository;

import com.proper.enterprise.platform.app.document.AppVersionDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppVersionRepository extends MongoRepository<AppVersionDocument, String> {

    AppVersionDocument findTopByValidTrueOrderByVerDesc();

    AppVersionDocument findByVer(long version);

    Page<AppVersionDocument> findAllByValidTrue(Pageable pageable);

    List<AppVersionDocument> findAllByValidTrue(Sort sort);

    Long countByValidTrue();
}
