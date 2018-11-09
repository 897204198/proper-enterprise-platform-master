package com.proper.enterprise.platform.notice.server.email.repository;

import com.proper.enterprise.platform.notice.server.email.document.EmailDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<EmailDocument, String> {

    /**
     * 根据appKey删除配置
     *
     * @param appKey 唯一标识
     * @return Integer
     */
    Integer deleteByAppKey(String appKey);

    /**
     * 通过appKey获取配置
     *
     * @param appKey 唯一标识
     * @return 配置详情
     */
    EmailDocument findByAppKey(String appKey);

}
