package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.template.vo.TemplateVO;

import java.util.Map;

public interface NoticeCollector {

    /**
     * 拼装消息请求
     *
     * @param fromUserId 消息发起人
     * @param custom     自定义扩展字段
     * @param templateVO 正文模板
     * @param noticeType 消息渠道
     * @return 消息请求
     */
    NoticeRequest packageNoticeRequest(String fromUserId,
                                       Map<String, Object> custom,
                                       TemplateVO templateVO,
                                       NoticeType noticeType);

    /**
     * 添加一个接收人信息
     *
     * @param noticeVO 原消息请求
     * @param user     用户信息
     * @return 添加完接收人的消息请求
     */
    NoticeRequest addNoticeTarget(NoticeRequest noticeVO, User user);

}
