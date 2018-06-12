package com.proper.enterprise.platform.app.repository;

import com.proper.enterprise.platform.app.document.AppVersionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppVersionRepository extends MongoRepository<AppVersionDocument, String> {

    AppVersionDocument findTopByReleasedTrueOrderByCreateTimeDesc();

    AppVersionDocument findByVersion(String version);

}
