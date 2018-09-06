package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeTarget;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class NoticeSmsCollector implements NoticeCollector {

    @Override
    public NoticeRequest packageNoticeRequest(String fromUserId,
                                              Map<String, Object> custom,
                                              TemplateVO templateVO,
                                              NoticeType noticeType) {
        NoticeRequest noticeVO = new NoticeRequest();
        noticeVO.setTitle(templateVO.getSmsTitle());
        noticeVO.setContent(templateVO.getSmsTemplate());
        noticeVO.setNoticeType(noticeType);
        noticeVO.setNoticeExtMsg("from", fromUserId);
        return noticeVO;
    }

    @Override
    public NoticeRequest addNoticeTarget(NoticeRequest noticeVO, User user) {
        NoticeTarget targetModel = new NoticeTarget();
        targetModel.setTo(user.getPhone());
        noticeVO.addTarget(targetModel);
        return noticeVO;
    }

}
