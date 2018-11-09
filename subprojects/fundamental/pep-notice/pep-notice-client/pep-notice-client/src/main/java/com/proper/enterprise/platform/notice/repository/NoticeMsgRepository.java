package com.proper.enterprise.platform.notice.repository;

import com.proper.enterprise.platform.notice.document.NoticeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoticeMsgRepository extends MongoRepository<NoticeDocument, String> {

    /**
     * 通过batchId查询消息
     *
     * @param batchId 批次号
     * @return 消息
     */
    NoticeDocument findByBatchId(String batchId);

}
