package com.proper.enterprise.platform.notice.server.push.dao.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum;
import com.proper.enterprise.platform.notice.server.push.vo.PushNoticeMsgVO;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import org.springframework.data.domain.Pageable;

public interface PushNoticeMsgService {

    /**
     * 根据消息服务端框架回调 同步插入推送记录
     *
     * @param readOnlyNotice 只读消息
     * @param pushChannel    推送渠道
     */
    void savePushMsg(ReadOnlyNotice readOnlyNotice, PushChannelEnum pushChannel);

    /**
     * 分页查询推送消息
     *
     * @param content     消息内容
     * @param status      消息状态
     * @param appKey      系统唯一标识
     * @param pushChannel 推送渠道
     * @param pageable    分页参数
     * @return 分页集合
     */
    DataTrunk<PushNoticeMsgVO> findPagination(String content, NoticeStatus status, String appKey,
                                              PushChannelEnum pushChannel, Pageable pageable);
}
