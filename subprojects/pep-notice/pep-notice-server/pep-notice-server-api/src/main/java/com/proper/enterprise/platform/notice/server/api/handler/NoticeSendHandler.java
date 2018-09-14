package com.proper.enterprise.platform.notice.server.api.handler;

import com.proper.enterprise.platform.notice.server.api.exception.NoticeException;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNoticeResult;
import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.api.model.BusinessNotice;
import org.springframework.scheduling.annotation.Async;

public interface NoticeSendHandler {

    /**
     * 根据消息对象发送消息SingletonMap
     *
     * @param notice 消息对象
     * @throws NoticeException 消息异常 业务发送失败后抛出
     */

    void send(ReadOnlyNotice notice) throws NoticeException;

    /**
     * 发送前处理
     * 在调用具体发送方法前的事件，多用于校验消息对象是否符合业务标准
     * 若不符合标准抛出errMsgException即可
     * 也可根据BusinessNotice所具有的权限 对消息进行赋值
     *
     * @param notice 业务消息模型
     * @throws NoticeException 消息异常 业务处理失败后抛出
     */
    void beforeSend(BusinessNotice notice) throws NoticeException;

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
     * @return BusinessNoticeResult 业务返回模型
     * @throws NoticeException 消息异常  业务获取状态失败后抛出
     */
    BusinessNoticeResult getStatus(ReadOnlyNotice notice) throws NoticeException;
}
