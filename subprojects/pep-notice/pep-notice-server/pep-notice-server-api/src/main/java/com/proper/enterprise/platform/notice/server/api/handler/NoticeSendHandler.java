package com.proper.enterprise.platform.notice.server.api.handler;

import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import org.springframework.scheduling.annotation.Async;

public interface NoticeSendHandler {

    /**
     * 根据消息对象发送消息SingletonMap
     *
     * @param notice 消息对象
     */
    void send(ReadOnlyNotice notice);

    /**
     * 发送前处理
     * 在调用具体发送方法前的事件，多用于校验消息对象是否符合业务标准
     * 若不符合标准抛出errMsgException即可
     * 也可根据BusinessNotice所具有的权限 对消息进行赋值
     *
     * @param notice 业务消息模型
     */
    void beforeSend(BusinessNotice notice);

    /**
     * 发送后处理
     * 当消息成功或失败后会回调业务
     * 用于业务同步消息数据做具体的业务统计
     *
     * @param notice 消息对象
     */
    @Async
    void afterSend(ReadOnlyNotice notice);

    /**
     * 根据消息对象获取消息对象的发送状态
     *
     * @param notice 消息对象
     * @return 消息发送状态
     */
    NoticeStatus getStatus(ReadOnlyNotice notice);
}
