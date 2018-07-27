package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.notice.entity.NoticeEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NoticeService {

    /**
     * 单人创建通知
     *
     * @param systemId      系统ID
     * @param businessId    业务ID
     * @param businessName  业务名称
     * @param noticeType    通知类型
     * @param target        通知接收人ID 为空时代表广播通知
     * @param title         通知标题
     * @param content       通知正文
     * @param custom        通知自定义字段
     * @param noticeChannel 通知渠道
     * @return 通知结果
     */
    boolean sendNotice(String systemId,
                       String businessId,
                       String businessName,
                       String noticeType,
                       String target,
                       String title,
                       String content,
                       Map<String, Object> custom,
                       String noticeChannel);

    /**
     * 人员列表创建通知
     *
     * @param systemId      系统ID
     * @param businessId    业务ID
     * @param businessName  业务名称
     * @param noticeType    通知类型
     * @param targets       通知接收人ID集合 为空时代表广播通知
     * @param title         通知标题
     * @param content       通知正文
     * @param custom        通知自定义字段
     * @param noticeChannel 通知渠道
     * @return 通知结果
     */
    boolean sendNotice(String systemId,
                       String businessId,
                       String businessName,
                       String noticeType,
                       Set<String> targets,
                       String title,
                       String content,
                       Map<String, Object> custom,
                       String noticeChannel);

    /**
     * 查询指定通知类型的列表
     *
     * @param noticeChannel 通知类型
     * @return 通知列表
     */
    List<NoticeEntity> findByNoticeChannel(String noticeChannel);

}
