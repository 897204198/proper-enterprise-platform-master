package com.proper.enterprise.platform.notice.push.repository;

import com.proper.enterprise.platform.notice.push.document.PushDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PushRepository extends MongoRepository<PushDocument, String> {

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
    PushDocument findByAppKey(String appKey);
}
