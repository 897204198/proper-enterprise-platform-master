package com.proper.enterprise.platform.feedback.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.feedback.document.FeedBackDocument;
import com.proper.enterprise.platform.feedback.document.UserFeedBackDocument;
import com.proper.enterprise.platform.feedback.repository.UserFeedBackRepository;
import com.proper.enterprise.platform.feedback.service.UserFeedbackService;
import com.proper.enterprise.platform.notice.service.NoticeSender;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 意见反馈ServiceImpl.
 */
@Service
public class UserFeedbackServiceImpl implements UserFeedbackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFeedbackServiceImpl.class);

    private static final String FEEDBACK_REPLIED = "pep_feedback_replied";

    private final UserService userService;

    private final DataDicService dataDicService;

    private final UserFeedBackRepository userFeedBackRepo;

    private final NoticeSender noticeSender;

    @Autowired
    public UserFeedbackServiceImpl(UserService userService, DataDicService dataDicService, UserFeedBackRepository
        userFeedBackRepo, NoticeSender noticeSender) {
        this.userService = userService;
        this.dataDicService = dataDicService;
        this.userFeedBackRepo = userFeedBackRepo;
        this.noticeSender = noticeSender;
    }

    @Override
    public void save(UserFeedBackDocument feedbackInfo) {
        userFeedBackRepo.save(feedbackInfo);
    }

    @Override
    public DataTrunk<UserFeedBackDocument> findByConditionAndPage(String feedbackStatus, String query, PageRequest
        pageRequest) {
        Page<UserFeedBackDocument> userFeedBackDocumentPage =
            userFeedBackRepo.findByUserAndStatus(feedbackStatus, feedbackStatus + "==-1", query, pageRequest);
        List<UserFeedBackDocument> userFeedBackDocumentList = userFeedBackDocumentPage.getContent();
        for (UserFeedBackDocument userFeedBackDocument : userFeedBackDocumentList) {
            List<FeedBackDocument> documentList = userFeedBackDocument.getFeedBackDocuments();
            if (documentList.size() > 0) {
                userFeedBackDocument.setFeedBackDocuments(Collections.singletonList(documentList.get(documentList
                    .size() - 1)));
            }
        }

        DataTrunk<UserFeedBackDocument> retObj = new DataTrunk<>();
        // 设置总数
        retObj.setCount(userFeedBackDocumentPage.getTotalElements());
        // 设置列表
        retObj.setData(userFeedBackDocumentList);
        return retObj;
    }

    /**
     * 取得列表
     */
    @Override
    public UserFeedBackDocument getUserOpinions(String userId) {
        return userFeedBackRepo.findByUserId(userId);
    }


    /**
     * 关闭状态更新管理端的意见反馈
     *
     * @param userFeedBackDocument 非空
     * @return ""
     */
    @Override
    public String saveCloseFeedback(UserFeedBackDocument userFeedBackDocument) {
        DataDic unReplyDic = dataDicService.get("pep_feedback_colsed");
        userFeedBackDocument.setStatus(unReplyDic.getName());
        userFeedBackDocument.setStatusCode(unReplyDic.getCode());
        save(userFeedBackDocument);
        return "";
    }

    /**
     * 保存APP用户反馈意见
     *
     * @param opinion   反馈意见.
     * @param pictureId 上传图片id.
     */
    @Override
    public String saveFeedback(String opinion, String pictureId, String mobileModel, String netType, String platform,
                               String appVersion) {
        User userInfo = userService.getCurrentUser();
        UserFeedBackDocument feedBackDoc = getUserOpinions(userInfo.getId());
        if (feedBackDoc == null) {
            UserFeedBackDocument userFeedBackDocument = new UserFeedBackDocument();
            userFeedBackDocument.setUserId(userInfo.getId());
            userFeedBackDocument.setUserName(userInfo.getName());
            userFeedBackDocument.setUserTel(userInfo.getPhone());
            feedBackDoc = userFeedBackDocument;
        }
        DataDic unReplyDic = dataDicService.get("pep_feedback_not_reply");
        feedBackDoc.setStatus(unReplyDic.getName());
        feedBackDoc.setStatusCode(unReplyDic.getCode());
        FeedBackDocument feedBackDocument = new FeedBackDocument();
        feedBackDocument.setOpinion(opinion);
        feedBackDocument.setPictureId(pictureId);
        feedBackDocument.setMobileModel(mobileModel);
        feedBackDocument.setNetType(netType);
        feedBackDocument.setPlatform(platform);
        feedBackDocument.setAppVersion(appVersion);
        String opinionTime = DateUtil.toString(LocalDateTime.now(), "yyyy-MM-dd HH:mm");
        feedBackDocument.setOpinionTime(opinionTime);
        List<FeedBackDocument> list = feedBackDoc.getFeedBackDocuments();
        list.add(feedBackDocument);
        feedBackDoc.setFeedBackDocuments(list);
        save(feedBackDoc);
        return "";
    }

    @Override
    public UserFeedBackDocument addAdminReplyFeedbackOpinion(String feedback, UserFeedBackDocument
        userFeedBackDocument) {
        DataDic replyDic = dataDicService.get("pep_feedback_replied");
        userFeedBackDocument.setStatus(replyDic.getName());
        userFeedBackDocument.setStatusCode(replyDic.getCode());
        List<FeedBackDocument> list = userFeedBackDocument.getFeedBackDocuments();
        FeedBackDocument feedBackDocument = new FeedBackDocument();
        feedBackDocument.setFeedbackTime(DateUtil.toString(LocalDateTime.now(), "yyyy-MM-dd HH:mm"));
        feedBackDocument.setFeedback(feedback);
        ListIterator<FeedBackDocument> it = list.listIterator(list.size());
        while (it.hasPrevious()) {
            try {
                FeedBackDocument feedBackDoc = it.previous();
                if (StringUtil.isBlank(feedBackDoc.getFeedback())) {
                    feedBackDocument.setMobileModel(feedBackDoc.getMobileModel());
                    feedBackDocument.setNetType(feedBackDoc.getNetType());
                    feedBackDocument.setPlatform(feedBackDoc.getPlatform());
                    feedBackDocument.setAppVersion(feedBackDoc.getAppVersion());
                    break;
                }
            } catch (Exception e) {
                LOGGER.debug("Not Found Element！", e);
            }
        }
        list.add(feedBackDocument);
        userFeedBackDocument.setFeedBackDocuments(list);
        userFeedBackDocument = userFeedBackRepo.save(userFeedBackDocument);

        if (dataDicService.get(FEEDBACK_REPLIED).getCode().equals(userFeedBackDocument.getStatusCode())) {
            List<Map<String, String>> paramMapList = new ArrayList<>();
            Map<String, String> paramMap = new HashMap<>(1);
            paramMap.put("opinionId", userFeedBackDocument.getId());
            paramMapList.add(paramMap);
            pushInfo(userFeedBackDocument.getUserId(), paramMapList);
            LOGGER.debug("Push feedback information !");
            LOGGER.debug("feedback Id : {}", userFeedBackDocument.getId());
        }
        return userFeedBackDocument;
    }

    /**
     * 获取APP用户意见反馈信息
     *
     * @return 反馈信息列表.
     */
    @Override
    public List<FeedBackDocument> getFeedbackList() {
        User userInfo = userService.getCurrentUser();
        UserFeedBackDocument userFeedBackDocument = getUserOpinions(userInfo.getId());
        List<FeedBackDocument> collection = new ArrayList<>();
        if (userFeedBackDocument != null) {
            collection = userFeedBackDocument.getFeedBackDocuments();
        }
        return collection;
    }

    /**
     * 掌上医院推送反馈意见消息
     *
     * @param userId    接收人
     * @param paramList 推送参数列表
     */
    @Override
    public void pushInfo(String userId, List<Map<String, String>> paramList) {
        Map<String, Object> custom = new HashMap<>(1);
        custom.put("gdpr_mpage", "feedback");
        for (Map<String, String> pushMap : paramList) {
            Iterator<Map.Entry<String, String>> paraIter = pushMap.entrySet().iterator();
            while (paraIter.hasNext()) {
                Map.Entry<String, String> entry = paraIter.next();
                custom.put(entry.getKey(), entry.getValue());
            }
        }
        noticeSender.sendNotice(userId, "feedback", custom);
    }
}
