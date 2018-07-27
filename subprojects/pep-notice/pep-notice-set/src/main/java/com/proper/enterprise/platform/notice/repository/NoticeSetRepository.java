package com.proper.enterprise.platform.notice.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.notice.entity.NoticeSetEntity;

import java.util.List;

public interface NoticeSetRepository extends BaseRepository<NoticeSetEntity, String> {

    /**
     * 查询当前人员的各通知类型设置情况
     *
     * @param userId 当前人员编号
     * @return
     */
    List<NoticeSetEntity> findByUserId(String userId);

    /**
     * 查询指定通知类型下的人员设置
     *
     * @param noticeType 通知类型
     * @param userId     当前人员编号
     * @return
     */
    List<NoticeSetEntity> findByNoticeTypeAndUserId(String noticeType, String userId);

}
