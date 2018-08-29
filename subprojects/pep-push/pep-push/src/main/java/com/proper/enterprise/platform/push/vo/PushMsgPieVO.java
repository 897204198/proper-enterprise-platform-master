package com.proper.enterprise.platform.push.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;

public class PushMsgPieVO {

    /**
     * 消息总数
     */
    private Integer msgSum;

    /**
     * 发送状态 成功:1 失败:0
     */
    private String msgStatus;

    /**
     * app应用分类
     */
    private String appKey;

    public Integer getMsgSum() {
        return msgSum;
    }

    public void setMsgSum(Integer msgSum) {
        this.msgSum = msgSum;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}

