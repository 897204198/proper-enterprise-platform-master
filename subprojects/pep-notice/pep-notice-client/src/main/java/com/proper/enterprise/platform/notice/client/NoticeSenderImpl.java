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
public class NoticeSenderImpl implements NoticeSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeSenderImpl.class);

    @Value("${pep.push.properpushAppkey:unUsed}")
    private String systemId;

    @Value("${pep.push.pushUrl:unUsed}")
    private String serverUrl;

    @Value("${pep.push.packageName:unUsed}")
    private String packageName;

    @Autowired
    NoticeSetService noticeSetService;

    @Autowired
    UserService userService;

    @Autowired
    TemplateService templateService;

    @Override
    public void sendNotice(String code,
                           Map<String, Object> custom,
                           String userId,
                           Map<String, Object> templateParams) {
        sendNotice(code, custom, null, userId, templateParams);
    }

    @Override
    public void sendNotice(String code,
                           Map<String, Object> custom,
                           String from,
                           String userId,
                           Map<String, Object> templateParams) {
        Set<String> userIds = new HashSet<>(1);
        userIds.add(userId);
        sendNotice(code, custom, from, userIds, templateParams);
    }

    @Override
    public void sendNotice(String code,
                           Map<String, Object> custom,
                           Set<String> userIds,
                           Map<String, Object> templateParams) {
        sendNotice(code, custom, null, userIds, templateParams);
    }

    @Override
    @Async
    public void sendNotice(String code,
                           Map<String, Object> custom,
                           String from,
                           Set<String> userIds,
                           Map<String, Object> templateParams) {
        try {
            TemplateVO templateVO = templateService.getTemplateByCode(code);
            String businessId = templateVO.getCatelog();
            String noticeType = templateVO.getType();
            Addressee addressee = packageAddressee(code, userIds);
            Map<String, TemplateVO> templates = templateService.getTemplates(code, templateParams);
            sendNoticeChannel(from, addressee, businessId, noticeType, custom, templates, "PUSH", "EMAIL", "SMS");
        } catch (Exception e) {
            LOGGER.error("NoticeSender.sendNotice[Exception]:{}", e);
        }
    }

    @Override
    public void sendNotice(String businessId,
                           String noticeType,
                           Map<String, Object> custom,
                           String userId,
                           String title,
                           String content) {
        sendNotice(businessId, noticeType, custom, null, userId, title, content);
    }

    @Override
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

    @Override
    public void sendNotice(String businessId,
                           String noticeType,
                           Map<String, Object> custom,
                           Set<String> userIds,
                           String title,
                           String content) {
        sendNotice(businessId, noticeType, custom, null, userIds, title, content);
    }

    @Override
    @Async
    public void sendNotice(String businessId,
                           String noticeType,
                           Map<String, Object> custom,
                           String from,
                           Set<String> userIds,
                           String title,
                           String content) {
        try {
            Addressee addressee = packageAddressee(noticeType, userIds);
            sendNoticeChannel(from, addressee, businessId, noticeType, custom, title, content, "PUSH", "EMAIL", "SMS");
        } catch (Exception e) {
            LOGGER.error("NoticeSender.sendNotice[Exception]:{}", e);
        }
    }

    private Addressee packageAddressee(String noticeType, Set<String> userIds) {
        checkNull(userIds);
        Map<String, NoticeSetDocument> noticeSetMap = noticeSetService.findMapByNoticeTypeAndUserIds(noticeType, userIds);
        Collection<? extends User> users = userService.getUsersByIds(new ArrayList<>(userIds));
        Addressee addressee = new Addressee();
        for (User user : users) {
            NoticeSetDocument noticeSetDocument = noticeSetMap.get(user.getId());
            if (noticeSetDocument == null) {
                noticeSetDocument = new NoticeSetDocument(true, true, false);
                if ("MESSAGE".equals(noticeType)) {
                    noticeSetDocument = new NoticeSetDocument(true, false, false);
                }
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

    private void checkNull(Set<String> userIds) {
        Iterator<String> it = userIds.iterator();
        while (it.hasNext()) {
            String str = it.next();
            if (StringUtil.isEmpty(str)) {
                it.remove();
            }
        }
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
                try {
                    NoticeModel noticeModel = packageNoticeModel(from, targets, businessId, noticeType, custom, title, content, noticeChannel);
                    accessNoticeServer(noticeModel);
                } catch (Exception e) {
                    LOGGER.error("NoticeSender.sendNoticeChannel[Exception]:{}", e);
                }
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
            try {
                Set<String> targets = addressee.get(noticeChannel);
                if (targets != null && !targets.isEmpty()) {
                    TemplateVO templateVO = templates.get(noticeChannel);
                    if (templateVO == null) {
                        continue;
                    }
                    String title = templateVO.getTitle();
                    String content = templateVO.getTemplate();
                    NoticeModel noticeModel = packageNoticeModel(from, targets, businessId, noticeType, custom, title, content, noticeChannel);
                    accessNoticeServer(noticeModel);
                }
            } catch (Exception e) {
                LOGGER.error("NoticeSender.sendNoticeChannel[Exception]:{}", e);
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

    private String accessNoticeServer(NoticeModel noticeModel) throws IOException {
        String data = JSONUtil.toJSON(noticeModel);
        LOGGER.debug("NOTICE SENDER SEND:" + data);
        ResponseEntity<byte[]> response = HttpClient.post(serverUrl + "/notice", MediaType.APPLICATION_JSON, data);
        return StringUtil.toEncodedString(response.getBody());
    }

}
