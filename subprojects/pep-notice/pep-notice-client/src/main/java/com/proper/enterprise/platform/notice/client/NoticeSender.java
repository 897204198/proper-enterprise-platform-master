package com.proper.enterprise.platform.notice.client;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.model.NoticeModel;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Service
public class NoticeSender {

    @Value("${pep.push.properpushAppkey}")
    private String systemId;

    @Value("${pep.push.pushUrl}")
    private String serverUrl;

    @Autowired
    NoticeSetService noticeSetService;

    @Autowired
    UserService userService;

    @Async
    public void sendNotice(String businessId,
                           String businessName,
                           String noticeType,
                           String title,
                           String content,
                           Set<String> userIds,
                           Map<String, Object> custom) {
        NoticeModel noticeModel = new NoticeModel();
        noticeModel.setSystemId(systemId);
        noticeModel.setBusinessId(businessId);
        noticeModel.setBusinessName(businessName);
        noticeModel.setNoticeType(noticeType);
        noticeModel.setTitle(title);
        noticeModel.setCustom(custom);
        for (String userId : userIds) {
            NoticeSetDocument noticeSetDocument = noticeSetService.findByNoticeTypeAndUserId(noticeType, userId);
            User user = userService.get(userId);
            if (noticeSetDocument.isPush()) {
                //TODO templet
                noticeModel.setContent(content);
                noticeModel.setNoticeChannel("PUSH");
                noticeModel.setTarget(user.getUsername());
                accessNoticeServer(noticeModel);
            }
            if (noticeSetDocument.isEmail()) {
                //TODO templet
                noticeModel.setContent(content);
                noticeModel.setNoticeChannel("EMAIL");
                noticeModel.setTarget(user.getEmail());
                accessNoticeServer(noticeModel);
            }
            if (noticeSetDocument.isSms()) {
                //TODO templet
                noticeModel.setContent(content);
                noticeModel.setNoticeChannel("SMS");
                noticeModel.setTarget(user.getPhone());
                accessNoticeServer(noticeModel);
            }
        }
    }

    private String accessNoticeServer(NoticeModel noticeModel) {
        try {
            String date = JSONUtil.toJSON(noticeModel);
            ResponseEntity<byte[]> response = HttpClient.post(serverUrl, MediaType.APPLICATION_FORM_URLENCODED, date);
            return StringUtil.toEncodedString(response.getBody());
        } catch (IOException e) {
            return null;
        }
    }

}
