package com.proper.enterprise.platform.notice.client;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.model.NoticeModel;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeSender.class);

    @Value("${pep.push.properpushAppkey}")
    private String systemId;

    @Value("${pep.push.pushUrl}")
    private String serverUrl;

    @Value("${pep.push.packageName}")
    private String packageName;

    @Autowired
    NoticeSetService noticeSetService;

    @Autowired
    UserService userService;

    /**
     * 发送批量消息接口
     *
     * @param businessId   业务ID
     * @param businessName 业务名称
     * @param noticeType   通知类型
     * @param title        标题
     * @param content      正文
     * @param userIds      通知接收人ID
     * @param custom       扩展字段
     */
    public void sendNotice(String businessId,
                           String businessName,
                           String noticeType,
                           String title,
                           String content,
                           Set<String> userIds,
                           Map<String, Object> custom) {
        for (String userId : userIds) {
            sendNotice(businessId, businessName, noticeType, title, content, userId, custom);
        }
    }

    /**
     * 发送单人消息接口
     *
     * @param businessId   业务ID
     * @param businessName 业务名称
     * @param noticeType   通知类型
     * @param title        标题
     * @param content      正文
     * @param userId       通知接收人ID
     * @param custom       扩展字段
     */
    @Async
    public void sendNotice(String businessId,
                           String businessName,
                           String noticeType,
                           String title,
                           String content,
                           String userId,
                           Map<String, Object> custom) {
        NoticeModel noticeModel = new NoticeModel();
        noticeModel.setSystemId(systemId);
        noticeModel.setBusinessId(businessId);
        noticeModel.setBusinessName(businessName);
        noticeModel.setNoticeType(noticeType);
        noticeModel.setTitle(title);
        NoticeSetDocument noticeSetDocument = noticeSetService.findByNoticeTypeAndUserId(noticeType, userId);
        User user = userService.get(userId);
        if (noticeSetDocument.isPush()) {
            //TODO templet
            noticeModel.setContent(content);
            noticeModel.setNoticeChannel("PUSH");
            noticeModel.setTarget(user.getUsername());
            noticeModel.setCustom(custom);
            custom.put("packageName", packageName);
            accessNoticeServer(noticeModel);
        }
        if (noticeSetDocument.isEmail()) {
            //TODO templet
            noticeModel.setContent(content);
            noticeModel.setNoticeChannel("EMAIL");
            noticeModel.setTarget(user.getEmail());
            noticeModel.setCustom(custom);
            accessNoticeServer(noticeModel);
        }
        if (noticeSetDocument.isSms()) {
            //TODO templet
            noticeModel.setContent(content);
            noticeModel.setNoticeChannel("SMS");
            noticeModel.setTarget(user.getPhone());
            noticeModel.setCustom(custom);
            accessNoticeServer(noticeModel);
        }
    }


    private String accessNoticeServer(NoticeModel noticeModel) {
        try {
            String data = JSONUtil.toJSON(noticeModel);
            LOGGER.debug("NOTICE SENDER POST:" + data);
            ResponseEntity<byte[]> response = HttpClient.post(serverUrl + "/notice", MediaType.APPLICATION_FORM_URLENCODED, data);
            return StringUtil.toEncodedString(response.getBody());
        } catch (IOException e) {
            return null;
        }
    }

}
