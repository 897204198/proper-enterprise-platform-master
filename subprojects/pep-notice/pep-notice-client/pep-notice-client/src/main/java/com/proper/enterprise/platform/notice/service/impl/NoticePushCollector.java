package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeTarget;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class NoticePushCollector implements NoticeCollector {

    @Value("${pep.push.packageName:unUsed}")
    private String packageName;

    @Override
    public NoticeRequest packageNoticeRequest(String fromUserId,
                                              Map<String, Object> custom,
                                              TemplateVO templateVO,
                                              NoticeType noticeType) {
        NoticeRequest noticeVO = new NoticeRequest();
        noticeVO.setTitle(templateVO.getPushTitle());
        noticeVO.setContent(templateVO.getPushTemplate());
        noticeVO.setNoticeType(noticeType);
        noticeVO.setNoticeExtMsg(custom);
        noticeVO.setNoticeExtMsg("packageName", packageName);
        noticeVO.setNoticeExtMsg("from", fromUserId);
        return noticeVO;
    }

    @Override
    public NoticeRequest addNoticeTarget(NoticeRequest noticeVO, User user) {
        NoticeTarget targetModel = new NoticeTarget();
        targetModel.setTo(user.getUsername());
        noticeVO.addTarget(targetModel);
        //TODO add device
        return noticeVO;
    }

}
