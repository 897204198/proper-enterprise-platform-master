package com.proper.enterprise.platform.feedback.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.feedback.document.FeedBackDocument;
import com.proper.enterprise.platform.feedback.document.UserFeedBackDocument;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 意见反馈Service.
 */
public interface UserFeedbackService {

    /**
     * 保存用户意见反馈
     * @param feedbackInfo feedbackInfo
     */
    void save(UserFeedBackDocument feedbackInfo);

    /**
     * 管理端取得App所有用户的意见反馈
     * 按照查询条件获取所有用户的列表
     *
     * @param  feedbackStatus 意见反馈状态
     * @param  query          查询条件
     * @param  pageRequest    分页条件
     * @return 意见反馈分页数据
     */
    DataTrunk<UserFeedBackDocument> findByConditionAndPage(String feedbackStatus, String query, PageRequest pageRequest);

    /**
     * 获取管理端的用户的意见反馈列表
     *
     * @param userId userId
     * @return 意见反馈信息
     */
    UserFeedBackDocument getUserOpinions(String userId);

    /**
     * 保存APP用户反馈意见
     *
     * @param opinion 反馈意见.
     * @param pictureId 图片id
     * @param mobileModel 手机型号
     * @param netType 网络
     * @param platform 手机系统
     * @param appVersion 版本
     * @return ""
     * @throws Exception 异常.
     */
    String saveFeedback(String opinion, String pictureId, String mobileModel, String netType, String platform, String appVersion) throws Exception;

    /**
     * 保存管理端回复反馈意见信息
     * @param feedback 意见
     * @param opinionDocument opinionDocument对象
     * @return UserFeedBackDocument
     * @throws Exception 抛异常
     */
    UserFeedBackDocument addAdminReplyFeedbackOpinion(String feedback, UserFeedBackDocument opinionDocument) throws Exception;

    /**
     * 获取APP用户意见反馈信息列表
     *
     * @return 反馈信息列表.
     * @throws Exception 异常.
     */
    List<FeedBackDocument> getFeedbackList() throws Exception;

    /**
     * 掌上就医推送反馈意见消息
     * @param pushContent pushContent
     * @param pushType pushType
     * @param userNameList userNameList
     * @param paramList paramList
     * @throws Exception 抛异常
     */
    void pushInfo(String pushContent, String pushType, List<String> userNameList, List<Map<String, String>> paramList) throws Exception;

    /**
     * 关闭状态更新管理端的意见反馈
     * @param userFeedBackDocument userFeedBackDocument
     * @return ""
     */
    String saveCloseFeedback(UserFeedBackDocument userFeedBackDocument);

}
