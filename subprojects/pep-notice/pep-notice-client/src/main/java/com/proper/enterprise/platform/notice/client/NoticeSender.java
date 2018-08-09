package com.proper.enterprise.platform.notice.client;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.model.Addressee;
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
import java.util.*;

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
     * 发送单人消息接口
     *
     * @param businessId     业务ID
     * @param noticeType     通知类型
     * @param custom         扩展字段
     * @param userId         通知接收人ID
     * @param templateParams 文案参数
     */
    public void sendNotice(String businessId,
                           String noticeType,
                           String code,
                           Map<String, Object> custom,
                           String userId,
                           Map<String, String> templateParams) {
        sendNotice(businessId, noticeType, code, custom, null, userId, templateParams);
    }

    /**
     * 发送单人消息接口
     *
     * @param businessId     业务ID
     * @param noticeType     通知类型
     * @param custom         扩展字段
     * @param from           发起人
     * @param userId         通知接收人ID
     * @param templateParams 文案参数
     */
    public void sendNotice(String businessId,
                           String noticeType,
                           String code,
                           Map<String, Object> custom,
                           String from,
                           String userId,
                           Map<String, String> templateParams) {
        Set<String> userIds = new HashSet<>(1);
        userIds.add(userId);
        sendNotice(businessId, noticeType, code, custom, from, userIds, templateParams);
    }

    /**
     * 发送批量消息接口
     *
     * @param businessId     业务ID
     * @param noticeType     通知类型
     * @param custom         扩展字段
     * @param userIds        通知接收人ID集合
     * @param templateParams 文案参数
     */
    public void sendNotice(String businessId,
                           String noticeType,
                           String code,
                           Map<String, Object> custom,
                           Set<String> userIds,
                           Map<String, String> templateParams) {
        sendNotice(businessId, noticeType, code, custom, null, userIds, templateParams);
    }

    /**
     * 发送批量消息接口
     *
     * @param businessId     业务ID
     * @param noticeType     通知类型
     * @param custom         扩展字段
     * @param from           发起人
     * @param userIds        通知接收人ID集合
     * @param templateParams 文案参数
     */
    @Async
    public void sendNotice(String businessId,
                           String noticeType,
                           String code,
                           Map<String, Object> custom,
                           String from,
                           Set<String> userIds,
                           Map<String, String> templateParams) {
        Addressee addressee = packageAddressee(noticeType, userIds);
        DataDicLiteBean business = new DataDicLiteBean("NOTICE_BUSINESS", businessId);
        Map<String, TemplateVO> templates = templateService.getTemplates(business, code, templateParams);
        sendNoticeChannel(from, addressee, businessId, noticeType, custom, templates, "PUSH", "EMAIL", "SMS");
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
    public void sendNotice(String businessId,
                           String noticeType,
                           Map<String, Object> custom,
                           String userId,
                           String title,
                           String content) {
        sendNotice(businessId, noticeType, custom, null, userId, title, content);
    }

    /**
     * 发送单人消息接口
     *
     * @param businessId 业务ID
     * @param noticeType 通知类型
     * @param custom     扩展字段
     * @param from       发起人
     * @param userId     通知接收人ID
     * @param title      标题
     * @param content    正文
     */
    public void sendNotice(String businessId,
                           String noticeType,
                           Map<String, Object> custom,
                           String from,
                           String userId,
                           String title,
                           String content) {
        Set<String> userIds = new HashSet<>(1);
        userIds.add(userId);
        sendNotice(businessId, noticeType, custom, from, userIds, title, content);
    }

    /**
     * 发送批量消息接口
     *
     * @param businessId 业务ID
     * @param noticeType 通知类型
     * @param custom     扩展字段
     * @param userIds    通知接收人ID集合
     * @param title      标题
     * @param content    正文
     */
    public void sendNotice(String businessId,
                           String noticeType,
                           Map<String, Object> custom,
                           Set<String> userIds,
                           String title,
                           String content) {
        sendNotice(businessId, noticeType, custom, null, userIds, title, content);
    }

    /**
     * 发送批量消息接口
     *
     * @param businessId 业务ID
     * @param noticeType 通知类型
     * @param custom     扩展字段
     * @param from       发起人
     * @param userIds    通知接收人ID集合
     * @param title      标题
     * @param content    正文
     */
    @Async
    public void sendNotice(String businessId,
                           String noticeType,
                           Map<String, Object> custom,
                           String from,
                           Set<String> userIds,
                           String title,
                           String content) {
        Addressee addressee = packageAddressee(noticeType, userIds);
        sendNoticeChannel(from, addressee, businessId, noticeType, custom, title, content, "PUSH", "EMAIL", "SMS");
    }

    private Addressee packageAddressee(String noticeType, Set<String> userIds) {
        Map<String, NoticeSetDocument> noticeSetMap = noticeSetService.findMapByNoticeTypeAndUserIds(noticeType, userIds);
        Collection<? extends User> users = userService.getUsersByIds(new ArrayList<>(userIds));
        Addressee addressee = new Addressee();
        for (User user : users) {
            NoticeSetDocument noticeSetDocument = noticeSetMap.get(user.getId());
            if (noticeSetDocument == null) {
                noticeSetDocument = new NoticeSetDocument(true, true, false);
            }
            if (noticeSetDocument.isPush()) {
                addressee.add("PUSH", user.getUsername());
            }
            if (noticeSetDocument.isEmail()) {
                addressee.add("EMAIL", user.getEmail());
            }
            if (noticeSetDocument.isSms()) {
                addressee.add("SMS", user.getPhone());
            }
        }
        return addressee;
    }

    private void sendNoticeChannel(String from,
                                   Addressee addressee,
                                   String businessId,
                                   String noticeType,
                                   Map<String, Object> custom,
                                   String title,
                                   String content,
                                   String... noticeChannels) {
        for (String noticeChannel : noticeChannels) {
            Set<String> targets = addressee.get(noticeChannel);
            if (targets != null && !targets.isEmpty()) {
                NoticeModel noticeModel = packageNoticeModel(from, targets, businessId, noticeType, custom, title, content, noticeChannel);
                accessNoticeServer(noticeModel);
            }
        }
    }

    private void sendNoticeChannel(String from,
                                   Addressee addressee,
                                   String businessId,
                                   String noticeType,
                                   Map<String, Object> custom,
                                   Map<String, TemplateVO> templates,
                                   String... noticeChannels) {
        for (String noticeChannel : noticeChannels) {
            Set<String> targets = addressee.get(noticeChannel);
            if (targets != null && !targets.isEmpty()) {
                TemplateVO templateVO = templates.get(noticeChannel);
                String title = templateVO.getTitle();
                String content = templateVO.getTemplate();
                NoticeModel noticeModel = packageNoticeModel(from, targets, businessId, noticeType, custom, title, content, noticeChannel);
                accessNoticeServer(noticeModel);
            }
        }
    }

    private NoticeModel packageNoticeModel(String from,
                                           Set<String> targets,
                                           String businessId,
                                           String noticeType,
                                           Map<String, Object> custom,
                                           String title,
                                           String content,
                                           String noticeChannel) {
        NoticeModel noticeModel = new NoticeModel();
        noticeModel.setFrom(from);
        noticeModel.setTarget(targets);
        noticeModel.setSystemId(systemId);
        noticeModel.setBusinessId(businessId);
        noticeModel.setNoticeType(noticeType);
        noticeModel.setTitle(title);
        noticeModel.setContent(content);
        noticeModel.setCustom(custom);
        noticeModel.setNoticeChannel(noticeChannel);
        noticeModel.addCustom("packageName", packageName);
        return noticeModel;
    }

    private String accessNoticeServer(NoticeModel noticeModel) {
        try {
            String data = JSONUtil.toJSON(noticeModel);
            LOGGER.debug("NOTICE SENDER SEND:" + data);
            ResponseEntity<byte[]> response = HttpClient.post(serverUrl + "/notice", MediaType.APPLICATION_JSON, data);
            return StringUtil.toEncodedString(response.getBody());
        } catch (IOException e) {
            return null;
        }
    }

}
