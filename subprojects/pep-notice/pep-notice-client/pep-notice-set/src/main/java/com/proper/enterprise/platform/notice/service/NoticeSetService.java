package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.notice.document.NoticeSetDocument;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NoticeSetService {

    /**
     * 查询指定人员通知设置列表
     *
     * @param userId 用户唯一标识
     * @return 通知设置列表
     */
    List<NoticeSetDocument> findByUserId(String userId);

    /**
     * 查询指定通知类型下的人员设置
     *
     * @param catalog 通知类型
     * @param userIds    当前人员列表
     * @return 通知设置映射
     */
    Map<String, NoticeSetDocument> findMapByCatalogAndUserIds(String catalog, Set<String> userIds);

    /**
     * 保存通知设置
     *
     * @param noticeSetDocument 需要保存的通知设置
     * @return 已保存的通知设置
     */
    NoticeSetDocument save(NoticeSetDocument noticeSetDocument);

}
