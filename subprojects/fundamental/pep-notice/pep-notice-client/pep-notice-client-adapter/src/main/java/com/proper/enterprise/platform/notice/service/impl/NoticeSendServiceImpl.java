package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.model.NoticeRequestParams;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.notice.service.NoticeSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Service
public class NoticeSendServiceImpl extends AbstractNoticeSendServiceImpl implements NoticeSendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeSendServiceImpl.class);

    @Override
    public NoticeRequest sendNoticeChannel(String fromUserId,
                                           Set<String> toUserIds,
                                           Map<String, NoticeSetDocument> noticeSetMap,
                                           String title,
                                           String content,
                                           Map<String, Object> custom,
                                           NoticeType noticeType,
                                           Collection<? extends User> users) {
        NoticeRequestParams request = new NoticeRequestParams();
        request.setFromUserId(fromUserId);
        request.setToUserIds(toUserIds);
        request.setNoticeSetMap(noticeSetMap);
        request.setTitle(title);
        request.setContent(content);
        request.setCustom(custom);
        request.setNoticeType(noticeType);
        request.setUsers(users);
        String adapter = this.getNoticeDic("ADAPTER");
        String noticeServerToken = this.getNoticeDic("TOKEN");
        request.setAccessToken(noticeServerToken);
        try {
            String data = JSONUtil.toJSON(request);
            LOGGER.debug("NOTICE SENDER SEND:" + data);
            ResponseEntity<byte[]> response = HttpClient.post(adapter
                + "/notice/adapter/send", MediaType.APPLICATION_JSON, data);
            if (HttpStatus.CREATED != response.getStatusCode()) {
                String str = "[" + response.getStatusCode() + "]" + StringUtil.toEncodedString(response.getBody());
                LOGGER.error("NoticeSendServiceImpl.sendNoticeChannel[Exception]:", str);
            }
        } catch (Exception e) {
            LOGGER.error("NoticeSendServiceImpl.sendNoticeChannel[Exception]:", e);
        }
        return null;
    }

    @Override
    public void sendNoticeSMS(String phone, String content, Map<String, Object> custom) {
    }

    @Override
    public void sendNoticeEmail(String to, String cc, String bcc, String title, String content, Map<String, Object> custom, String... attachmentIds) {
    }
}
