package com.proper.enterprise.platform.notice.server.api.service;

import com.proper.enterprise.platform.notice.server.api.request.NoticeRequest;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface NoticeSendService {

    /**
     * 消息发送
     *
     * @param accessToken   应用授权token
     * @param noticeRequest 消息发送Request
     */
    void send(@NotEmpty String accessToken, @Valid NoticeRequest noticeRequest);

}
