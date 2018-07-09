package com.proper.enterprise.platform.feedback.document;

import java.io.Serializable;

public class FeedBackDocument implements Serializable {
    /**
     * 意见时间
     */
    private String opinionTime;
    /**
     * 意见内容
     */
    private String opinion;
    /**
     * 反馈人id
     */
    private String feedbackUserId;
    /**
     * 反馈时间
     */
    private String feedbackTime;
    /**
     * 反馈内容
     */
    private String feedback;

    /**
     *  上传图片的id
     */
    private String pictureId;

    /**
     * 手机型号
     */
    private String mobileModel;

    /**
     * 网络环境
     */
    private String netType;

    /**
     * app的版本号
     */
    private String appVersion;

    /**
     * 手机系统
     */
    private String platform;

    public String getOpinionTime() {
        return opinionTime;
    }

    public void setOpinionTime(String opinionTime) {
        this.opinionTime = opinionTime;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getFeedbackUserId() {
        return feedbackUserId;
    }

    public void setFeedbackUserId(String feedbackUserId) {
        this.feedbackUserId = feedbackUserId;
    }

    public String getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(String feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getMobileModel() {
        return mobileModel;
    }

    public void setMobileModel(String mobileModel) {
        this.mobileModel = mobileModel;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
