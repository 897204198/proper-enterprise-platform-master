package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import com.proper.enterprise.platform.notice.enums.AnalysisResult;
import com.proper.enterprise.platform.notice.repository.NoticeMsgRepository;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
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

import java.util.*;

public abstract class AbstractNoticeSendServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNoticeSendServiceImpl.class);

    @Autowired
    private NoticeMsgRepository noticeMsgRepository;

    @Autowired
    private NoticeSetService noticeSetService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private UserService userService;

    public void accessNoticeServer(NoticeRequest noticeModel) {
        String noticeServerToken = this.getNoticeDic("TOKEN");
        accessNoticeServer(noticeModel, noticeServerToken);
    }

    public void accessNoticeServer(NoticeRequest noticeModel, String noticeServerToken) {
        String noticeServerUrl = this.getNoticeDic("URL");
        try {
            String data = JSONUtil.toJSON(noticeModel);
            LOGGER.debug("NOTICE SENDER SEND:" + data);
            ResponseEntity<byte[]> response = HttpClient.post(noticeServerUrl
                + "/notice/server/send?access_token="
                + noticeServerToken, MediaType.APPLICATION_JSON, data);
            if (HttpStatus.CREATED != response.getStatusCode()) {
                String str = "[" + response.getStatusCode() + "]" + StringUtil.toEncodedString(response.getBody());
                this.updateNotice(noticeModel.getBatchId(), str);
            }
        } catch (Exception e) {
            LOGGER.error("NoticeSender.accessNoticeServer[Exception]:", e);
            this.updateNotice(noticeModel.getBatchId(), e.toString());
        }
    }

    public String getNoticeDic(String code) {
        String noticeServerToken = null;
        DataDic dataDic = DataDicUtil.get("NOTICE_SERVER", code);
        if (dataDic != null) {
            noticeServerToken = dataDic.getName();
        }
        return noticeServerToken;
    }

    public Set<String> checkUserNull(Set<String> userIds) {
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

    public NoticeRequest saveNotice(NoticeDocument noticeDocument) {
        noticeDocument.setBatchId(UUID.randomUUID().toString());
        NoticeRequest noticeRequest = BeanUtil.convert(noticeDocument, NoticeRequest.class);
        noticeMsgRepository.save(noticeDocument);
        return noticeRequest;
    }

    public void updateNotice(String batchId, String exception) {
        NoticeDocument noticeDocument = noticeMsgRepository.findByBatchId(batchId);
        noticeDocument.setAnalysisResult(AnalysisResult.ERROR);
        noticeDocument.setNotes("The notice server return error message: " + exception);
        noticeMsgRepository.save(noticeDocument);
    }

    public void analysis(NoticeDocument noticeDocument, Collection<? extends User> users) {
        NoticeAnalysisUtil.isUsersExist(noticeDocument);
        NoticeAnalysisUtil.isThereATarget(noticeDocument);
        NoticeAnalysisUtil.isUsersMoreThanTargets(noticeDocument, users);
    }

    public NoticeDocument packageNoticeDocument(String fromUserId,
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
        noticeDocument.setNoticeUrl(this.getNoticeDic("URL"));
        noticeDocument.setToken(this.getNoticeDic("TOKEN"));
        return noticeDocument;
    }

    public void sendNoticeChannel(String fromUserId,
                                  Set<String> toUserIds,
                                  String code,
                                  Map<String, Object> templateParams,
                                  Map<String, Object> custom) {
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

    public void sendNoticeChannel(String fromUserId,
                                  Set<String> toUserIds,
                                  String title,
                                  String content,
                                  Map<String, Object> custom,
                                  String catalog,
                                  NoticeType noticeType) {
        toUserIds = checkUserNull(toUserIds);
        Map<String, NoticeSetDocument> noticeSetMap = noticeSetService.findMapByCatalogAndUserIds(catalog, toUserIds);
        sendNoticeChannel(fromUserId, toUserIds, noticeSetMap, title, content, custom, noticeType);
    }

    public void sendNoticeChannel(String fromUserId,
                                  Set<String> toUserIds,
                                  Map<String, NoticeSetDocument> noticeSetMap,
                                  String title,
                                  String content,
                                  Map<String, Object> custom,
                                  NoticeType noticeType) {
        Collection<? extends User> users = userService.getUsersByIds(new ArrayList<>(toUserIds));
        this.sendNoticeChannel(fromUserId, toUserIds, noticeSetMap, title, content, custom, noticeType, users);
    }

    /**
     * 单渠道发送消息
     *
     * @param fromUserId   发起人
     * @param toUserIds    通知接收人ID集合
     * @param noticeSetMap 接收人消息接收配置
     * @param title        标题
     * @param content      正文
     * @param custom       扩展字段
     * @param noticeType   消息渠道
     * @param users        接收人信息
     * @return
     */
    public abstract NoticeRequest sendNoticeChannel(String fromUserId,
                                                    Set<String> toUserIds,
                                                    Map<String, NoticeSetDocument> noticeSetMap,
                                                    String title,
                                                    String content,
                                                    Map<String, Object> custom,
                                                    NoticeType noticeType,
                                                    Collection<? extends User> users);

}
