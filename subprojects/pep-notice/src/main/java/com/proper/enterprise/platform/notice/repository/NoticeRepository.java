package com.proper.enterprise.platform.notice.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.notice.entity.NoticeEntity;

import java.util.List;

public interface NoticeRepository extends BaseRepository<NoticeEntity, String> {

    /**
     * 分类查询消息通知列表
     * @param isPush 推送类型
     * @param isSms 邮件类型
     * @param isEmail 邮件类型
     * @return 消息通知列表
     */
    List<NoticeEntity> findByIsPushAndIsSmsAndIsEmail(boolean isPush, boolean isSms, boolean isEmail);

    /**
     * 查询全部消息通知列表
     * @return 消息通知列表
     */
    List<NoticeEntity> findAllByOrderByCreateTimeDesc();

    /**
     * 查询邮件类型消息
     * @param isEmail 邮件类型
     * @return 消息通知列表
     */
    List<NoticeEntity> findByIsEmail(boolean isEmail);

    /**
     * 查询推送类型消息
     * @param isPush 推送类型
     * @return 消息通知列表
     */
    List<NoticeEntity> findByIsPush(boolean isPush);

    /**
     * 查询短信类型消息
     * @param isSms 短信类型
     * @return 消息通知列表
     */
    List<NoticeEntity> findByIsSms(boolean isSms);
}
