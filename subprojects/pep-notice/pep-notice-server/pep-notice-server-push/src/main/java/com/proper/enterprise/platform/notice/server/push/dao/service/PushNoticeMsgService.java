package com.proper.enterprise.platform.notice.server.push.dao.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.dao.entity.PushNoticeMsgEntity;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgVO;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.springframework.data.domain.PageRequest;

public interface PushNoticeMsgService {

    /**
     * 将消息同步至推送
     *
     * @param readOnlyNotice 只读消息
     * @param pushChannel    推送渠道
     */
    void updatePushMsg(ReadOnlyNotice readOnlyNotice, PushChannelEnum pushChannel);

    /**
     * 将消息同步至推送
     *
     * @param messageId      第三方消息唯一标识
     * @param readOnlyNotice 只读消息
     * @param pushChannel    推送渠道
     */
    void savePushMsg(String messageId, ReadOnlyNotice readOnlyNotice, PushChannelEnum pushChannel);

    /**
     * 将消息同步至推送
     *
     * @param pushNoticeMsg 推送消息
     * @return 推送实体
     */
    PushNoticeMsgEntity saveOrUpdatePushMsg(PushNoticeMsgEntity pushNoticeMsg);

    /**
     * 更新推送状态
     *
     * @param pushId 消息Id
     * @param status 消息状态
     */
    void updateStatus(String pushId, NoticeStatus status);

    /**
     * 更新推送状态
     *
     * @param pushId 消息Id
     * @param status 消息状态
     * @param errMsg 消息异常
     */
    void updateStatus(String pushId, NoticeStatus status, String errMsg);

    /**
     * 分页查询推送消息
     *
     * @param content     消息内容
     * @param status      消息状态
     * @param appKey      系统唯一标识
     * @param pushChannel 推送渠道
     * @param pageRequest 分页参数
     * @return 分页集合
     */
    DataTrunk<PushNoticeMsgVO> findPagination(String content, NoticeStatus status, String appKey,
                                              PushChannelEnum pushChannel, PageRequest pageRequest);

    /**
     * 根据消息id查询推送记录
     *
     * @param noticeId 消息id
     * @return 推送记录
     */
    PushNoticeMsgEntity findPushNoticeMsgEntitiesByNoticeId(String noticeId);
}
