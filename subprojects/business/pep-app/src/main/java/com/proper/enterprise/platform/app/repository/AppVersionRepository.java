package com.proper.enterprise.platform.app.repository;

import com.proper.enterprise.platform.app.document.AppVersionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppVersionRepository extends MongoRepository<AppVersionDocument, String> {

    /**
     * 查找已经发布的版本信息并按照创建时间的倒序
     * @return AppVersionDocument 版本信息
     */
    AppVersionDocument findTopByReleasedTrueOrderByCreateTimeDesc();

    /**
     * 通过或版本号查找版本信息
     * @param version 版本号
     * @return AppVersionDocument 版本信息
     */
    AppVersionDocument findByVersion(String version);

}
