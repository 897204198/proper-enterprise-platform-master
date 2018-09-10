package com.proper.enterprise.platform.notice.server.push.repository;


import com.proper.enterprise.platform.notice.server.push.document.PushConfDocument;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PushConfigMongoRepository extends MongoRepository<PushConfDocument, String> {

    /**
     * 根据appKey删除配置
     *
     * @param appKey      唯一标识
     * @param pushChannel 推送渠道
     * @return Integer
     */
    Integer deleteByAppKeyAndPushChannel(String appKey, PushChannelEnum pushChannel);

    /**
     * 通过appKey获取配置
     *
     * @param appKey      唯一标识
     * @param pushChannel 推送渠道
     * @return 配置详情
     */
    PushConfDocument findByAppKeyAndPushChannel(String appKey, PushChannelEnum pushChannel);
}
