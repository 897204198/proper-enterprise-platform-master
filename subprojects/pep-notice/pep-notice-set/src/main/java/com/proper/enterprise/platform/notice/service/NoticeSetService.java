package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;

import java.util.Map;
import java.util.Set;

public interface NoticeSetService {

    /**
     * 查询指定通知类型下的人员设置
     *
     * @param noticeType 通知类型
     * @param userId     当前人员编号
     * @return 通知设置列表
     */
    NoticeSetDocument findByNoticeTypeAndUserId(String noticeType, String userId);

    /**
     * 查询指定通知类型下的人员设置
     *
     * @param noticeType 通知类型
     * @param userIds    当前人员列表
     * @return 通知设置列表
     */
    Map<String, NoticeSetDocument> findMapByNoticeTypeAndUserIds(String noticeType, Set<String> userIds);
}
