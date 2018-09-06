package com.proper.enterprise.platform.notice.sms.repository;

import com.proper.enterprise.platform.notice.sms.document.SMSDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SMSRepository extends MongoRepository<SMSDocument, String> {

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
    SMSDocument findByAppKey(String appKey);
}
