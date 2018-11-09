package com.proper.enterprise.platform.notice.server.app.convert;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.notice.server.api.model.Notice;
import com.proper.enterprise.platform.notice.server.api.service.NoticeDaoService;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeTarget;

import java.util.ArrayList;
import java.util.List;

public class RequestConvert {

    private RequestConvert() {
    }

    /**
     * 将NoticeRequest转换成Notice基础模型
     *
     * @param noticeRequest 消息request对象
     * @return 消息基础模型集合
     */
    public static List<Notice> convert(NoticeRequest noticeRequest) {
        List<Notice> noticeBusinessOperations = new ArrayList<>();
        for (NoticeTarget target : noticeRequest.getTargets()) {
            Notice noticeBusinessOperation = PEPApplicationContext.getBean(NoticeDaoService.class).newNotice();
            noticeBusinessOperation.setBatchId(noticeRequest.getBatchId());
            noticeBusinessOperation.setTitle(noticeRequest.getTitle());
            noticeBusinessOperation.setContent(noticeRequest.getContent());
            noticeBusinessOperation.setTargetTo(target.getTo());
            noticeBusinessOperation.setAllTargetExtMsg(target.getTargetExtMsg());
            noticeBusinessOperation.setAllNoticeExtMsg(noticeRequest.getNoticeExtMsg());
            noticeBusinessOperations.add(noticeBusinessOperation);
        }
        return noticeBusinessOperations;
    }

}
