package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.entity.NoticeSetDocument;
import com.proper.enterprise.platform.notice.factory.NoticeCollectorFactory;
import com.proper.enterprise.platform.notice.repository.NoticeMsgRepository;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import com.proper.enterprise.platform.template.service.TemplateService;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoticeSendServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeSenderImpl.class);

    @Value("${pep.push.properpushAppkey:unUsed}")
    private String systemId;

    @Value("${pep.push.pushUrl:unUsed}")
    private String serverUrl;

    private final NoticeSetService noticeSetService;

    private final UserService userService;

    private final NoticeMsgRepository noticeMsgRepository;

    private final TemplateService templateService;

    private static final String UNEXPECTED_URL = "unexpected url";

    @Autowired
    public NoticeSendServiceImpl(NoticeSetService noticeSetService, UserService userService, NoticeMsgRepository
        noticeMsgRepository, TemplateService templateService) {
        this.noticeSetService = noticeSetService;
        this.userService = userService;
        this.noticeMsgRepository = noticeMsgRepository;
        this.templateService = templateService;
    }

    @Async
    public void sendNoticeChannel(String fromUserId, Set<String> toUserIds, String code, Map<String, Object>
        templateParams, Map<String, Object> custom) {
        toUserIds = checkUserNull(toUserIds);
        List<DataDic> noticeTypes = (List<DataDic>) DataDicUtil.findByCatalog("NOTICE_TYPE");
        Map<String, TemplateVO> templates = templateService.getTemplatesByCodeAndTypesWithinCatalog(code,
            getNoticeTypes(noticeTypes), templateParams);
        String catalog = getCatalogFromTemplates(templates);
        Map<String, NoticeSetDocument> noticeSetMap = noticeSetService.findMapByCatalogAndUserIds(catalog, toUserIds);
        Collection<? extends User> users = userService.getUsersByIds(new ArrayList<>(toUserIds));
        for (Map.Entry<String, TemplateVO> entry : templates.entrySet()) {
            TemplateVO templateVO = entry.getValue();
            if (templateVO == null) {
                continue;
            }
            sendNoticeChannel(fromUserId, toUserIds, users, noticeSetMap, templateVO.getTitle(),
                templateVO.getTemplate(), custom, NoticeType.valueOf(entry.getKey()));
        }
    }

    @Async
    public void sendNoticeChannel(String fromUserId, Set<String> toUserIds, String title, String content,
                                  Map<String, Object> custom, String catalog, NoticeType noticeType) {
        toUserIds = checkUserNull(toUserIds);
        Map<String, NoticeSetDocument> noticeSetMap = noticeSetService.findMapByCatalogAndUserIds(catalog, toUserIds);
        Collection<? extends User> users = userService.getUsersByIds(new ArrayList<>(toUserIds));
        sendNoticeChannel(fromUserId, toUserIds, users, noticeSetMap, title, content, custom, noticeType);
    }

    private void sendNoticeChannel(String fromUserId,
                                   Set<String> toUserIds,
                                   Collection<? extends User> users,
                                   Map<String, NoticeSetDocument> noticeSetMap,
                                   String title,
                                   String content,
                                   Map<String, Object> custom,
                                   NoticeType noticeType) {
        NoticeCollector noticeCollector = NoticeCollectorFactory.create(noticeType);
        NoticeDocument noticeDocument = noticeCollector.packageNoticeRequest(fromUserId, toUserIds, custom,
            title, content);
        for (User user : users) {
            NoticeSetDocument noticeSetDocument = noticeSetMap.get(user.getId());
            noticeCollector.addNoticeTarget(noticeDocument, user, noticeSetDocument);
        }
        NoticeRequest noticeVO = saveNotice(noticeDocument);
        if (noticeVO.getTargets() != null && noticeVO.getTargets().size() > 0) {
            accessNoticeServer(noticeVO);
        }
    }

    private List<DataDicLiteBean> getNoticeTypes(List<DataDic> noticeTypes) {
        List<DataDicLiteBean> result = new ArrayList<>();
        for (DataDic dataDic : noticeTypes) {
            DataDicLiteBean dataDicLiteBean = new DataDicLiteBean();
            dataDicLiteBean.setCode(dataDic.getCode());
            dataDicLiteBean.setCatalog(dataDic.getCatalog());
            result.add(dataDicLiteBean);
        }
        return result;
    }

    private String getCatalogFromTemplates(Map<String, TemplateVO> templates) {
        String catalog = null;
        for (Map.Entry<String, TemplateVO> entry : templates.entrySet()) {
            TemplateVO templateVO = entry.getValue();
            if (templateVO != null) {
                catalog = templateVO.getCatalog();
                break;
            }
        }
        return catalog;
    }

    private Set<String> checkUserNull(Set<String> userIds) {
        if (userIds == null) {
            userIds = new HashSet<>();
        }
        Iterator<String> it = userIds.iterator();
        while (it.hasNext()) {
            String str = it.next();
            if (StringUtil.isEmpty(str)) {
                it.remove();
            }
        }
        return userIds;
    }

    private NoticeRequest saveNotice(NoticeDocument noticeDocument) {
        noticeDocument.setBatchId(UUID.randomUUID().toString());
        if (noticeDocument.getTargets() == null && noticeDocument.getUsers().size() == 0) {
            noticeDocument.setNote("Entered empty user.");
        }
        if (noticeDocument.getTargets() == null && noticeDocument.getUsers().size() > 0) {
            noticeDocument.setNote("User does not exist or user does not config " + noticeDocument.getNoticeType()
                + " reminder.");
        }
        NoticeRequest noticeRequest = BeanUtil.convert(noticeDocument, NoticeRequest.class);
        noticeMsgRepository.save(noticeDocument);
        return noticeRequest;
    }

    private void updateNotice(String batchId, String exception) {
        NoticeDocument noticeDocument = noticeMsgRepository.findByBatchId(batchId);
        noticeDocument.setException(exception);
        if (exception.contains(UNEXPECTED_URL)) {
            noticeDocument.setNote("The notice server url configuration error.");
        }
        noticeMsgRepository.save(noticeDocument);
    }

    private void accessNoticeServer(NoticeRequest noticeModel) {
        try {
            String data = JSONUtil.toJSON(noticeModel);
            LOGGER.debug("NOTICE SENDER SEND:" + data);
            HttpClient.post(serverUrl
                + "/notice/server/send?accessToken="
                + systemId, MediaType.APPLICATION_JSON, data);
        } catch (Exception e) {
            LOGGER.error("NoticeSender.accessNoticeServer[Exception]:", e);
            updateNotice(noticeModel.getBatchId(), e.getMessage());
        }
    }

}
