package com.proper.enterprise.platform.notice.server.api.sender;

import com.proper.enterprise.platform.notice.server.api.model.ReadOnlyNotice;
import com.proper.enterprise.platform.notice.server.api.model.Notice;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Validated
public interface NoticeSender {

    /**
     * 发送前处理
     * 主要用于检查参数和解析token
     *
     * @param appKey        应用唯一标识
     * @param noticeRequest 消息发送Request
     * @return 消息对象集合
     */
    @Validated(value = NoticeRequest.NoticeSendApi.class)
    List<Notice> beforeSend(@NotEmpty(message = "{notice.server.param.appKey.cantBeEmpty}") String appKey,
                            @Valid NoticeRequest noticeRequest);

    /**
     * 消息发送
     *
     * @param notices 消息集合
     */
    @Async
    void sendAsync(List<Notice> notices);


    /**
     * 消息发送
     *
     * @param notice 只读消息
     */
    @Async
    void sendAsync(@Valid Notice notice);


    /**
     * 回调  消息发送成功或失败后
     *
     * @param notice 只读消息
     */
    void afterSend(ReadOnlyNotice notice);

    /**
     * 同步周期时间内发送中消息的状态
     * 同步集合
     *
     * @param startModifyTime 从修改时间
     * @param endModifyTime   至修改时间
     */

    @Async
    void syncPendingNoticesStatusAsync(LocalDateTime startModifyTime, LocalDateTime endModifyTime);

    /**
     * 同步发送中消息的状态
     *
     * @param notice 只读消息
     */
    @Async
    void syncPendingNoticeStatusAsync(ReadOnlyNotice notice);

    /**
     * 重试周期时间内待重试的消息
     * 重试集合
     *
     * @param startModifyTime 从修改时间
     * @param endModifyTime   至修改时间
     */

    void retryNoticesAsync(LocalDateTime startModifyTime, LocalDateTime endModifyTime);

    /**
     * 重试消息
     *
     * @param notice 只读消息
     */

    void retryNoticeAsync(ReadOnlyNotice notice);

}
