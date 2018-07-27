package com.proper.enterprise.platform.notice.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.notice.entity.NoticeEntity;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;

import java.util.List;

public interface NoticeRepository extends BaseRepository<NoticeEntity, String> {

    /**
     * 分类查询消息通知列表
     * @param noticeChannel 通知类型
     * @return 消息通知列表
     */
    List<NoticeEntity> findByNoticeChannel(DataDicLiteBean noticeChannel);

}
