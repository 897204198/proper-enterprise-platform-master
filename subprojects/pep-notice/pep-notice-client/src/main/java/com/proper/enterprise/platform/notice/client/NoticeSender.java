package com.proper.enterprise.platform.notice.client;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.model.NoticeModel;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import com.proper.enterprise.platform.notice.service.TemplateService;
import com.proper.enterprise.platform.notice.vo.TemplateVO;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
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

    @Autowired
    TemplateService templateService;

    /**
     * 发送批量消息接口
     *
     * @param businessId     业务ID
     * @param noticeType     通知类型
     * @param custom         扩展字段
     * @param userIds        通知接收人ID
     * @param templateParams 文案参数
     */
    public void sendNotice(String businessId,
                           String noticeType,
                           String code,
                           Map<String, Object> custom,
                           Set<String> userIds,
                           Map<String, String> templateParams) {
        for (String userId : userIds) {
            sendNotice(businessId, noticeType, code, custom, userId, templateParams);
        }
    }

    /**
     * 发送单人消息接口
     *
     * @param businessId 业务ID
     * @param noticeType 通知类型
     * @param custom     扩展字段
     * @param userId     通知接收人ID
     * @param title      标题
     * @param content    正文
     */
    @Async
    public void sendNotice(String businessId, String noticeType, Map<String, Object> custom, String userId, String title, String content) {
        NoticeModel noticeModel = new NoticeModel();
        noticeModel.setSystemId(systemId);
        noticeModel.setBusinessId(businessId);
        noticeModel.setNoticeType(noticeType);
        NoticeSetDocument noticeSetDocument = noticeSetService.findByNoticeTypeAndUserId(noticeType, userId);
        User user = userService.get(userId);
        if (user != null) {
            if (noticeSetDocument.isPush()) {
                noticeModel.setTitle(title);
                noticeModel.setContent(content);
                noticeModel.setNoticeChannel("PUSH");
                if (StringUtil.isNotNull(user.getUsername())) {
                    noticeModel.setTarget(user.getUsername());
                    custom.put("packageName", packageName);
                    noticeModel.setCustom(custom);
                    accessNoticeServer(noticeModel);
                } else {
                    LOGGER.error("This is Notice Sender, username is empty.");
                }
            }
            if (noticeSetDocument.isEmail()) {
                noticeModel.setTitle(title);
                noticeModel.setContent(content);
                noticeModel.setNoticeChannel("EMAIL");
                if (StringUtil.isNotNull(user.getEmail())) {
                    noticeModel.setTarget(user.getEmail());
                    noticeModel.setCustom(custom);
                    accessNoticeServer(noticeModel);
                } else {
                    LOGGER.error("This is Notice Sender, email is empty.");
                }
            }
            if (noticeSetDocument.isSms()) {
                noticeModel.setTitle(title);
                noticeModel.setContent(content);
                noticeModel.setNoticeChannel("SMS");
                if (StringUtil.isNotNull(user.getPhone())) {
                    noticeModel.setTarget(user.getPhone());
                    noticeModel.setCustom(custom);
                    accessNoticeServer(noticeModel);
                } else {
                    LOGGER.error("This is Notice Sender, phone is empty.");
                }

            }
        } else {
            LOGGER.error("This is Notice Sender, the user is not exits : " + userId);
        }
    }

    /**
     * 发送单人消息接口
     *
     * @param businessId     业务ID
     * @param noticeType     通知类型
     * @param custom         扩展字段
     * @param userId         通知接收人ID
     * @param templateParams 文案参数
     */
    @Async
    public void sendNotice(String businessId,
                           String noticeType,
                           String code,
                           Map<String, Object> custom,
                           String userId,
                           Map<String, String> templateParams) {
        NoticeModel noticeModel = new NoticeModel();
        noticeModel.setSystemId(systemId);
        noticeModel.setBusinessId(businessId);
        noticeModel.setNoticeType(noticeType);
        NoticeSetDocument noticeSetDocument = noticeSetService.findByNoticeTypeAndUserId(noticeType, userId);
        User user = userService.get(userId);
        if (user != null) {
            DataDicLiteBean business = new DataDicLiteBean("NOTICE_BUSINESS", businessId);
            Map<String, TemplateVO> templates = templateService.getTemplates(business, code, templateParams);
            if (noticeSetDocument.isPush()) {
                TemplateVO templateVO = templates.get("PUSH");
                noticeModel.setTitle(templateVO.getTitle());
                noticeModel.setContent(templateVO.getTemplate());
                noticeModel.setNoticeChannel("PUSH");
                if (StringUtil.isNotNull(user.getUsername())) {
                    noticeModel.setTarget(user.getUsername());
                    custom.put("packageName", packageName);
                    noticeModel.setCustom(custom);
                    accessNoticeServer(noticeModel);
                } else {
                    LOGGER.error("This is Notice Sender, username is empty.");
                }
            }
            if (noticeSetDocument.isEmail()) {
                TemplateVO templateVO = templates.get("EMAIL");
                noticeModel.setTitle(templateVO.getTitle());
                noticeModel.setContent(templateVO.getTemplate());
                noticeModel.setNoticeChannel("EMAIL");
                if (StringUtil.isNotNull(user.getEmail())) {
                    noticeModel.setTarget(user.getEmail());
                    noticeModel.setCustom(custom);
                    accessNoticeServer(noticeModel);
                } else {
                    LOGGER.error("This is Notice Sender, email is empty.");
                }
            }
            if (noticeSetDocument.isSms()) {
                TemplateVO templateVO = templates.get("SMS");
                noticeModel.setTitle(templateVO.getTitle());
                noticeModel.setContent(templateVO.getTemplate());
                noticeModel.setNoticeChannel("SMS");
                if (StringUtil.isNotNull(user.getPhone())) {
                    noticeModel.setTarget(user.getPhone());
                    noticeModel.setCustom(custom);
                    accessNoticeServer(noticeModel);
                } else {
                    LOGGER.error("This is Notice Sender, phone is empty.");
                }

            }
        } else {
            LOGGER.error("This is Notice Sender, the user is not exits : " + userId);
        }
    }

    private String accessNoticeServer(NoticeModel noticeModel) {
        try {
            String data = JSONUtil.toJSON(noticeModel);
            LOGGER.debug("NOTICE SENDER POST:" + data);
            ResponseEntity<byte[]> response = HttpClient.post(serverUrl + "/notice", MediaType.APPLICATION_JSON, data);
            return StringUtil.toEncodedString(response.getBody());
        } catch (IOException e) {
            return null;
        }
    }

}
