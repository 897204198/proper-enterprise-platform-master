package com.proper.enterprise.platform.notice.server.push.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;

public class PushMsgPieDataVO {

    private String appKey;
    private String appName;
    private Integer successNum;
    private Integer failNum;
    private Integer totalNum;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getSuccessNum() {
        if (null == successNum) {
            successNum = 0;
        }
        return successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public Integer getFailNum() {
        if (null == failNum) {
            failNum = 0;
        }
        return failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    public Integer getTotalNum() {
        if (null == totalNum) {
            totalNum = 0;
        }
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
