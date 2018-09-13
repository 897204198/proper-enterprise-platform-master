package com.proper.enterprise.platform.notice.server.push.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;

/**
 * 渠道数据分析
 */
public class PushChannelDataAnalysisVO {
    /**
     * 发送成功数量
     */
    private Integer successCount = 0;
    /**
     * 发送失败数量
     */
    private Integer failCount = 0;

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailCount() {
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
