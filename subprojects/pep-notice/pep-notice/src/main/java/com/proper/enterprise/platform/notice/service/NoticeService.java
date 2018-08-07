package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.model.NoticeModel;

import java.util.List;

public interface NoticeService {

    /**
     * 查询指定通知类型的列表
     *
     * @param noticeChannel 通知类型
     * @return 通知列表
     */
    List<NoticeDocument> findByNoticeChannel(String noticeChannel);

    /**
     * 创建通知并转发各消息渠道
     *
     * @param noticeModel 通知
     * @return
     */
    boolean saveNoticeAndCallNoticeChannel(NoticeModel noticeModel);
}
