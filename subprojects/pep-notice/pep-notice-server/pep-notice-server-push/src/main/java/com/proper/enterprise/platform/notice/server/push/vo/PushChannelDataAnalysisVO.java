package com.proper.enterprise.platform.notice.server.push.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;

/**
 * 渠道数据分析
 */
public class PushChannelDataAnalysisVO {
    /**
     * 发送成功数量
     */
    private Integer successCount;
    /**
     * 发送失败数量
     */
    private Integer failCount;

    public Integer getSuccessCount() {
        if (null == successCount) {
            this.successCount = 0;
        }
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailCount() {
        if (null == failCount) {
            this.failCount = 0;
        }
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
