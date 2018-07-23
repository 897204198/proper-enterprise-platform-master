package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.notice.entity.NoticeEntity;

import java.util.List;
import java.util.Map;

public interface NoticeService {

    /**
     * 单人创建通知
     * @param businessId            业务ID
     * @param businessName          业务名称
     * @param title                 通知标题
     * @param content               通知正文
     * @param toUserId              通知接收人ID 为空时代表广播通知
     * @param custom                通知自定义字段
     * @param noticeChannelContent  通知渠道 （推送PUSH 短信SMS 邮件 EMAIL，可同时输入多个）
     */
    void sendNotice(String businessId,
                      String businessName,
                      String title,
                      String content,
                      String toUserId,
                      Map<String, Object> custom,
                      String noticeChannelContent);

    /**
     * 人员列表创建通知
     * @param businessId            业务ID
     * @param businessName          业务名称
     * @param title                 通知标题
     * @param content               通知正文
     * @param toUserIds             通知接收人ID集合 为空时代表广播通知
     * @param custom                通知自定义字段
     * @param noticeChannelContent  通知渠道 （推送PUSH 短信SMS 邮件 EMAIL，可同时输入多个）
     */
    void sendNotice(String businessId,
                      String businessName,
                      String title,
                      String content,
                      List<String> toUserIds,
                      Map<String, Object> custom,
                      String noticeChannelContent);

    /**
     * 查询指定通知类型的列表
     * @param noticeChannelName 通知类型名称
     * @return 通知列表
     */
    List<NoticeEntity> findByNoticeChannelName(String noticeChannelName);

}
