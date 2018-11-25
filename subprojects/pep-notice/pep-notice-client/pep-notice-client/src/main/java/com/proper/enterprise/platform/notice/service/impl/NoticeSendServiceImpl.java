package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.enums.AnalysisResult;
import com.proper.enterprise.platform.notice.factory.NoticeCollectorFactory;
import com.proper.enterprise.platform.notice.model.TargetModel;
import com.proper.enterprise.platform.notice.repository.NoticeMsgRepository;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.notice.service.NoticeCollector;
import com.proper.enterprise.platform.notice.service.NoticeSetService;
import com.proper.enterprise.platform.notice.util.NoticeAnalysisUtil;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import com.proper.enterprise.platform.template.service.TemplateService;
import com.proper.enterprise.platform.template.vo.TemplateDetailVO;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoticeSendServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeSenderImpl.class);

    @Autowired
    private NoticeSetService noticeSetService;

    @Autowired
    private NoticeMsgRepository noticeMsgRepository;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private UserDao userDao;

    public void sendNoticeChannel(String fromUserId, Set<String> toUserIds, String code, Map<String, Object>
        templateParams, Map<String, Object> custom) {
        toUserIds = checkUserNull(toUserIds);
        TemplateVO template = templateService.getTemplate(code, templateParams);
        Map<String, NoticeSetDocument> noticeSetMap = noticeSetService.findMapByCatalogAndUserIds(template.getCatalog(), toUserIds);
        List<TemplateDetailVO> details = template.getDetails();
        if (details != null && details.size() > 0) {
            for (TemplateDetailVO templateDetailVO : details) {
                if (templateDetailVO == null) {
                    continue;
                }
                sendNoticeChannel(fromUserId,
                                  toUserIds,
                                  noticeSetMap,
                                  templateDetailVO.getTitle(),
                                  templateDetailVO.getTemplate(),
                                  custom,
                                  NoticeType.valueOf(templateDetailVO.getType()));
            }
        }
    }

    public void sendNoticeChannel(String fromUserId, Set<String> toUserIds, String title, String content,
                                  Map<String, Object> custom, String catalog, NoticeType noticeType) {
        toUserIds = checkUserNull(toUserIds);
        Map<String, NoticeSetDocument> noticeSetMap = noticeSetService.findMapByCatalogAndUserIds(catalog, toUserIds);
        sendNoticeChannel(fromUserId, toUserIds, noticeSetMap, title, content, custom, noticeType);
    }

    private void sendNoticeChannel(String fromUserId,
                                   Set<String> toUserIds,
                                   Map<String, NoticeSetDocument> noticeSetMap,
                                   String title,
                                   String content,
                                   Map<String, Object> custom,
                                   NoticeType noticeType) {
        Collection<? extends User> users = this.getUsersByIds(new ArrayList<>(toUserIds));
        NoticeCollector noticeCollector = NoticeCollectorFactory.create(noticeType);
        NoticeDocument noticeDocument = this.packageNoticeDocument(fromUserId, toUserIds, custom, noticeType, title, content);
        noticeCollector.addNoticeDocument(noticeDocument);
        for (User user : users) {
            NoticeSetDocument noticeSetDocument = noticeSetMap.get(user.getId());
            TargetModel targetModel = this.packageTargetModel(user);
            noticeCollector.addNoticeTarget(noticeDocument, targetModel, noticeDocument.getNoticeType(), user, noticeSetDocument);
        }
        analysis(noticeDocument, users);
        NoticeRequest noticeVO = this.saveNotice(noticeDocument);
        if (!NoticeAnalysisUtil.isNecessaryResult(noticeDocument)) {
            accessNoticeServer(noticeVO);
        }
    }

    public Collection<? extends User> getUsersByIds(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return userDao.findAll(ids);
    }

    private void analysis(NoticeDocument noticeDocument, Collection<? extends User> users) {
        NoticeAnalysisUtil.isUsersExist(noticeDocument);
        NoticeAnalysisUtil.isThereATarget(noticeDocument);
        NoticeAnalysisUtil.isUsersMoreThanTargets(noticeDocument, users);
    }

    private NoticeDocument packageNoticeDocument(String fromUserId,
                                                 Set<String> toUserIds,
                                                 Map<String, Object> custom,
                                                 NoticeType noticeType,
                                                 String title,
                                                 String content) {
        NoticeDocument noticeDocument = new NoticeDocument();
        noticeDocument.setTitle(title);
        noticeDocument.setContent(content);
        noticeDocument.setNoticeType(noticeType);
        noticeDocument.setUsers(toUserIds);
        noticeDocument.setNoticeExtMsg(custom);
        noticeDocument.setNoticeExtMsg("from", fromUserId);
        return noticeDocument;
    }

    private TargetModel packageTargetModel(User user) {
        TargetModel targetModel = new TargetModel();
        targetModel.setId(user.getId());
        return targetModel;
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
        NoticeRequest noticeRequest = BeanUtil.convert(noticeDocument, NoticeRequest.class);
        noticeMsgRepository.save(noticeDocument);
        return noticeRequest;
    }

    private void updateNotice(String batchId, String noticeServerUrl, String exception) {
        NoticeDocument noticeDocument = noticeMsgRepository.findByBatchId(batchId);
        NoticeAnalysisUtil.accessNoticeServer(noticeDocument, exception, noticeServerUrl);
        noticeMsgRepository.save(noticeDocument);
    }

    private void updateNotice(String batchId, String exception) {
        NoticeDocument noticeDocument = noticeMsgRepository.findByBatchId(batchId);
        noticeDocument.setAnalysisResult(AnalysisResult.ERROR);
        noticeDocument.setNotes("The notice server return error message ", exception);
        noticeMsgRepository.save(noticeDocument);
    }

    private void accessNoticeServer(NoticeRequest noticeModel) {
        String noticeServerUrl = null;
        DataDic dataDic = DataDicUtil.get("NOTICE_SERVER", "URL");
        if (dataDic != null) {
            noticeServerUrl = dataDic.getName();
        }
        String noticeServerToken = null;
        dataDic = DataDicUtil.get("NOTICE_SERVER", "TOKEN");
        if (dataDic != null) {
            noticeServerToken = dataDic.getName();
        }
        try {
            String data = JSONUtil.toJSON(noticeModel);
            LOGGER.debug("NOTICE SENDER SEND:" + data);
            ResponseEntity<byte[]> response = HttpClient.post(noticeServerUrl
                + "/notice/server/send?access_token="
                + noticeServerToken, MediaType.APPLICATION_JSON, data);
            if (HttpStatus.CREATED != response.getStatusCode()) {
                String str = StringUtil.toEncodedString(response.getBody());
                this.updateNotice(noticeModel.getBatchId(), str);
            }
        } catch (Exception e) {
            LOGGER.error("NoticeSender.accessNoticeServer[Exception]:", e);
            this.updateNotice(noticeModel.getBatchId(), noticeServerUrl, e.getMessage());
        }
    }

}
