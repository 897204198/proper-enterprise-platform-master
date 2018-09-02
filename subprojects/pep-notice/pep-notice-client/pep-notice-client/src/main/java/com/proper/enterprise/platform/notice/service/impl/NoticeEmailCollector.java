package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.server.api.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.api.request.NoticeRequest;
import com.proper.enterprise.platform.notice.server.api.request.NoticeTarget;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class NoticeEmailCollector implements NoticeCollector {

    @Override
    public NoticeRequest packageNoticeRequest(String fromUserId,
                                              Map<String, Object> custom,
                                              TemplateVO templateVO,
                                              NoticeType noticeType) {
        NoticeRequest noticeVO = new NoticeRequest();
        noticeVO.setTitle(templateVO.getEmailTitle());
        noticeVO.setContent(templateVO.getEmailTemplate());
        noticeVO.setNoticeType(noticeType);
        noticeVO.addNoticeExtMsg("from", fromUserId);
        return noticeVO;
    }

    @Override
    public NoticeRequest addNoticeTarget(NoticeRequest noticeVO, User user) {
        NoticeTarget targetModel = new NoticeTarget();
        String target = user.getUsername() + "<" + user.getEmail() + ">";
        targetModel.setTo(target);
        noticeVO.addTarget(targetModel);
        return noticeVO;
    }

}
