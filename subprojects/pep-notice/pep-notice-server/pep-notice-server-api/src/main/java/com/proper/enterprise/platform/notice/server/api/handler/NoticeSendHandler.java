package com.proper.enterprise.platform.notice.server.api.handler;

import com.proper.enterprise.platform.notice.server.api.enums.NoticeStatus;
import com.proper.enterprise.platform.notice.server.api.model.Notice;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface NoticeSendHandler {

    /**
     * 根据消息对象发送消息
     *
     * @param notice 消息对象
     */
    void send(@NotNull(message = "notice.server.send.content.cantBeEmpty") Notice notice);

    /**
     * 发送前处理
     * 在调用具体发送方法前的事件，多用于校验消息对象是否符合业务标准
     * 若不符合标准抛出errMsgException即可
     *
     * @param notice 消息对象
     */
    void beforeSend(@NotNull(message = "notice.server.send.content.cantBeEmpty") Notice notice);

    /**
     * 发送后处理
     * 当消息成功或失败后会回调业务
     * 用于业务同步消息数据做具体的业务统计
     *
     * @param notice 消息对象
     */
    void afterSend(@NotNull(message = "notice.server.send.content.cantBeEmpty") Notice notice);

    /**
     * 根据消息对象获取消息对象的发送状态
     *
     * @param notice 消息对象
     * @return 消息发送状态
     */
    NoticeStatus getStatus(@NotNull(message = "notice.server.send.content.cantBeEmpty") Notice notice);
}
