package com.proper.enterprise.platform.notice.repository;

import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoticeRepository extends MongoRepository<NoticeDocument, String> {

    /**
     * 分类查询消息通知列表
     * @param noticeChannel 通知类型
     * @return 消息通知列表
     */
    List<NoticeDocument> findByNoticeChannel(DataDicLiteBean noticeChannel);

}
