package com.proper.enterprise.platform.feedback.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import com.proper.enterprise.platform.core.utils.DateUtil;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 意见反馈.
 */
@Document(collection = "PEP_FEEDBACK")
public class UserFeedBackDocument extends BaseDocument implements Serializable {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户手机号
     */
    private String userTel;
    /**
     * 创建时间
     */
    private String createTime = DateUtil.getTimestamp(true);
    /**
     * 状态编码
     */
    private String statusCode;
    /**
     * 状态名称
     */
    private String status;
    /**
     * 意见反馈集合
     */
    private List<FeedBackDocument> feedBackDocuments = new ArrayList<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    @Override
    public String getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FeedBackDocument> getFeedBackDocuments() {
        return feedBackDocuments;
    }

    public void setFeedBackDocuments(List<FeedBackDocument> feedBackDocuments) {
        this.feedBackDocuments = feedBackDocuments;
    }
}
