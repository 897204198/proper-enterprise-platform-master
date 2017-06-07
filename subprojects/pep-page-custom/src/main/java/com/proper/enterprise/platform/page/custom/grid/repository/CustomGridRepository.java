package com.proper.enterprise.platform.page.custom.grid.repository;

import com.proper.enterprise.platform.page.custom.grid.document.CustomGridDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomGridRepository extends MongoRepository<CustomGridDocument, String> {

    CustomGridDocument getByCode(String code);

    CustomGridDocument getById(String id);
}
