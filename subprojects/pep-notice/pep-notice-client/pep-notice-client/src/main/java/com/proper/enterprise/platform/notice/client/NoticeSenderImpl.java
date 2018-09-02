package com.proper.enterprise.platform.notice.client;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.server.api.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.api.request.NoticeRequest;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import com.proper.enterprise.platform.notice.service.impl.CollectorBuilder;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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

    private final NoticeSetService noticeSetService;

    private final UserService userService;

    @Autowired
    public NoticeSenderImpl(NoticeSetService noticeSetService, UserService userService) {
        this.noticeSetService = noticeSetService;
        this.userService = userService;
    }

    @Override
    public void sendNotice(String toUserId, TemplateVO templateVO, Map<String, Object> custom) {
        Set<String> userIds = new HashSet<>(1);
        userIds.add(toUserId);
        sendNotice(userIds, templateVO, custom);
    }

    @Override
    public void sendNotice(Set<String> toUserIds, TemplateVO templateVO, Map<String, Object> custom) {
        sendNotice(null, toUserIds, templateVO, custom);
    }

    @Override
    public void sendNotice(String fromUserId, String toUserId, TemplateVO templateVO, Map<String,
        Object> custom) {
        Set<String> userIds = new HashSet<>(1);
        userIds.add(toUserId);
        sendNotice(fromUserId, userIds, templateVO, custom);
    }

    @Async
    @Override
    public void sendNotice(String fromUserId, Set<String> toUserIds, TemplateVO templateVO, Map<String,
        Object> custom) {
        try {
            checkUserNull(toUserIds);
            Map<String, NoticeSetDocument> noticeSetMap = noticeSetService.findMapByNoticeTypeAndUserIds(templateVO
                .getCatelog(), toUserIds);
            sendNoticeChannel(fromUserId, toUserIds, templateVO, custom, noticeSetMap);
        } catch (Exception e) {
            LOGGER.error("NoticeSender.sendNotice[Exception]:", e);
        }
    }

    private void checkUserNull(Set<String> userIds) {
        Iterator<String> it = userIds.iterator();
        while (it.hasNext()) {
            String str = it.next();
            if (StringUtil.isEmpty(str)) {
                it.remove();
            }
        }
    }

    private void sendNoticeChannel(String fromUserId, Set<String> toUserIds, TemplateVO templateVO,
                                   Map<String, Object> custom, Map<String, NoticeSetDocument> noticeSetMap) {
        Collection<? extends User> users = userService.getUsersByIds(new ArrayList<>(toUserIds));
        sendNoticeChannel(fromUserId, users, templateVO, custom, noticeSetMap, NoticeType.PUSH);
        sendNoticeChannel(fromUserId, users, templateVO, custom, noticeSetMap, NoticeType.EMAIL);
        sendNoticeChannel(fromUserId, users, templateVO, custom, noticeSetMap, NoticeType.SMS);
    }

    private void sendNoticeChannel(String fromUserId, Collection<? extends User> toUserIds, TemplateVO templateVO,
                                   Map<String, Object> custom, Map<String, NoticeSetDocument> noticeSetMap,
                                   NoticeType noticeType) {
        NoticeCollector noticeCollector = CollectorBuilder.create(noticeType);
        NoticeRequest noticeVO = noticeCollector.packageNoticeRequest(fromUserId, custom, templateVO, noticeType);
        for (User user : toUserIds) {
            NoticeSetDocument noticeSetDocument = noticeSetMap.get(user.getId());
            if (noticeSetDocument.isPush() && NoticeType.PUSH.equals(noticeType)) {
                noticeCollector.addNoticeTarget(noticeVO, user);
            }
            if (noticeSetDocument.isEmail() && NoticeType.EMAIL.equals(noticeType)) {
                noticeCollector.addNoticeTarget(noticeVO, user);
            }
            if (noticeSetDocument.isSms() && NoticeType.SMS.equals(noticeType)) {
                noticeCollector.addNoticeTarget(noticeVO, user);
            }
        }
        if (noticeVO.getTargets() != null && noticeVO.getTargets().size() > 0) {
            saveNotice(noticeVO);
            accessNoticeServer(noticeVO);
        }
    }

    private void saveNotice(NoticeRequest noticeVO) {
        noticeVO.setBatchId(UUID.randomUUID().toString());
        //TODO Save clint notice.
    }

    private void accessNoticeServer(NoticeRequest noticeModel) {
        try {
            String data = JSONUtil.toJSON(noticeModel);
            LOGGER.debug("NOTICE SENDER SEND:" + data);
            HttpClient.post(serverUrl
                + "/notice/server/send?accessToken="
                + systemId, MediaType.APPLICATION_JSON, data);
        } catch (IOException e) {
            LOGGER.error("NoticeSender.accessNoticeServer[Exception]:", e);
        }
    }

}
