package com.proper.enterprise.platform.feedback.service.impl;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.feedback.document.FeedBackDocument;
import com.proper.enterprise.platform.feedback.document.UserFeedBackDocument;
import com.proper.enterprise.platform.feedback.repository.UserFeedBackRepository;
import com.proper.enterprise.platform.feedback.service.UserFeedbackService;
import com.proper.enterprise.platform.push.client.PusherApp;
import com.proper.enterprise.platform.push.client.model.PushMessage;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 意见反馈ServiceImpl.
 */
@Service
public class UserFeedbackServiceImpl implements UserFeedbackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFeedbackServiceImpl.class);

    private static final String FEEDBACK_REPLIED = "pep_feedback_replied";

    @Autowired
    private UserService userService;

    @Autowired
    private DataDicService dataDicService;

    @Autowired
    private UserFeedBackRepository userFeedBackRepo;

    @Autowired
    private PusherApp pusherApp;

    @Autowired
    private I18NService i18NService;

    @Override
    public void save(UserFeedBackDocument feedbackInfo) {
        userFeedBackRepo.save(feedbackInfo);
    }

    @Override
    public DataTrunk<UserFeedBackDocument> findByConditionAndPage(String feedbackStatus, String query, PageRequest pageRequest) {
        Page<UserFeedBackDocument> userFeedBackDocumentPage =
                userFeedBackRepo.findByUserAndStatus(feedbackStatus, feedbackStatus + "==-1", query, pageRequest);
        List<UserFeedBackDocument> userFeedBackDocumentList = userFeedBackDocumentPage.getContent();
        for (UserFeedBackDocument userFeedBackDocument : userFeedBackDocumentList) {
            List<FeedBackDocument> documentList = userFeedBackDocument.getFeedBackDocuments();
            if (documentList.size() > 0) {
                userFeedBackDocument.setFeedBackDocuments(Collections.singletonList(documentList.get(documentList.size() - 1)));
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
     * @throws Exception 异常.
     */
    @Override
    public String saveFeedback(String opinion, String pictureId, String mobileModel, String netType, String platform,
                               String appVersion) throws Exception {
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
        String opinionTime = DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm");
        feedBackDocument.setOpinionTime(opinionTime);
        List<FeedBackDocument> list = feedBackDoc.getFeedBackDocuments();
        list.add(feedBackDocument);
        feedBackDoc.setFeedBackDocuments(list);
        save(feedBackDoc);
        return "";
    }

    @Override
    public UserFeedBackDocument addAdminReplyFeedbackOpinion(String feedback, UserFeedBackDocument userFeedBackDocument) throws Exception {
        DataDic replyDic = dataDicService.get("pep_feedback_replied");
        userFeedBackDocument.setStatus(replyDic.getName());
        userFeedBackDocument.setStatusCode(replyDic.getCode());
        List<FeedBackDocument> list = userFeedBackDocument.getFeedBackDocuments();
        FeedBackDocument feedBackDocument = new FeedBackDocument();
        feedBackDocument.setFeedbackTime(DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm"));
        feedBackDocument.setFeedback(feedback);
        list.add(feedBackDocument);
        userFeedBackDocument.setFeedBackDocuments(list);
        userFeedBackDocument = userFeedBackRepo.save(userFeedBackDocument);

        if (dataDicService.get(FEEDBACK_REPLIED).getCode().equals(userFeedBackDocument.getStatusCode())) {
            List<String> userNameList = new ArrayList<>();
            userNameList.add(userFeedBackDocument.getUserTel());
            String pushContent = i18NService.getMessage("pep.feedback.message.content");
            String pushType = "feedback";
            List<Map<String, String>> paramMapList = new ArrayList<>();
            Map<String, String> paramMap = new HashMap<>(1);
            paramMap.put("opinionId", userFeedBackDocument.getId());
            paramMapList.add(paramMap);
            pushInfo(pushContent, pushType, userNameList, paramMapList);
            LOGGER.debug("Push feedback information !");
            LOGGER.debug("feedback Id :" + userFeedBackDocument.getId());
        }
        return userFeedBackDocument;
    }

    /**
     * 获取APP用户意见反馈信息
     *
     * @return 反馈信息列表.
     * @throws Exception 异常.
     */
    @Override
    public List<FeedBackDocument> getFeedbackList() throws Exception {
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
     * @param pushContent  推送消息内容
     * @param pushType     推送消息类别
     * @param userNameList 推送人列表(手机号)
     * @param paramList    推送参数列表
     * @throws Exception 异常信息
     */
    @Override
    public void pushInfo(String pushContent, String pushType, List<String> userNameList, List<Map<String, String>> paramList) throws Exception {
        LOGGER.debug("Push info: {}, {}, {}, {}", pushContent, pushType, userNameList, paramList);

        // 拼接推送消息
        PushMessage msg = new PushMessage();
        msg.setTitle(i18NService.getMessage("pep.feedback.message.title"));
        msg.setContent(pushContent);
        msg.addCustomData("pageUrl", pushType);
        // 推送参数
        String paramKey;
        String paramValue;
        //在推送消息时，设置推送消息对应的角标
        //badgeNumber >=0 合法的角标数;=0时，清空手机端对应的角标；>0时，设置对应的角标数
        // 比如，进到程序里，用户查看对应的消息后，手机端App要根据推送SDK清空对应的角标。
        for (Map<String, String> pushMap : paramList) {
            Iterator<Map.Entry<String, String>> paraIter = pushMap.entrySet().iterator();
            while (paraIter.hasNext()) {
                Map.Entry<String, String> entry = paraIter.next();
                paramKey = entry.getKey();
                paramValue = entry.getValue();
                msg.addCustomData(paramKey, paramValue);
            }
        }

        LOGGER.debug("Invoke PusherAPP to push {} to {}", msg, userNameList);
        // 推送消息
        pusherApp.pushMessageToUsers(msg, userNameList);
    }
}
