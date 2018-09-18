package com.proper.enterprise.platform.notice.server.push.dao.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
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
     * 分页查询推送消息
     *
     * @param content     消息内容
     * @param status      消息状态
     * @param appKey      系统唯一标识
     * @param pushChannel 推送渠道
     * @param pageRequest    分页参数
     * @return 分页集合
     */
    DataTrunk<PushNoticeMsgVO> findPagination(String content, NoticeStatus status, String appKey,
                                              PushChannelEnum pushChannel, PageRequest pageRequest);
}
