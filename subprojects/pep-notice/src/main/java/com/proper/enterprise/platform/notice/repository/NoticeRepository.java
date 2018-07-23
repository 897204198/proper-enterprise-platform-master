package com.proper.enterprise.platform.notice.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.notice.entity.NoticeEntity;

import java.util.List;

public interface NoticeRepository extends BaseRepository<NoticeEntity, String> {

    /**
     * 分类查询消息通知列表
     * @param noticeChannelCode 消息通知渠道
     * @return 消息通知列表
     */
    List<NoticeEntity> findByNoticeChannelCodeLikeOrderByCreateTimeDesc(String noticeChannelCode);

    /**
     * 查询全部消息通知列表
     * @return 消息通知列表
     */
    List<NoticeEntity> findAllByOrderByCreateTimeDesc();

}
