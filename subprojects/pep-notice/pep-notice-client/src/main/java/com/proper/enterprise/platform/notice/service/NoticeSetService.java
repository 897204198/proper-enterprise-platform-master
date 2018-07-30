package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;

public interface NoticeSetService {

    /**
     * 查询指定通知类型下的人员设置
     *
     * @param noticeType 通知类型
     * @param userId 当前人员编号
     * @return 人员ID集合
     */
    NoticeSetDocument findByNoticeTypeAndUserId(String noticeType, String userId);

}
