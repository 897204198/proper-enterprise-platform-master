package com.proper.enterprise.platform.notice.server.push.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;

/**
 * 根据统计时间分组数据分析视图
 */
public class PushServiceDataAnalysisVO {
    /**
     * 统计时间
     */
    private String dataAnalysisDate;
    /**
     * 小米数据分析
     */
    private PushChannelDataAnalysisVO xiaomiDataAnalysis = new PushChannelDataAnalysisVO();
    /**
     * 华为数据分析
     */
    private PushChannelDataAnalysisVO huaweiDataAnalysis = new PushChannelDataAnalysisVO();
    /**
     * IOS数据分析
     */
    private PushChannelDataAnalysisVO iosDataAnalysis = new PushChannelDataAnalysisVO();

    public PushChannelDataAnalysisVO getXiaomiDataAnalysis() {
        return xiaomiDataAnalysis;
    }

    public void setXiaomiDataAnalysis(PushChannelDataAnalysisVO xiaomiDataAnalysis) {
        this.xiaomiDataAnalysis = xiaomiDataAnalysis;
    }

    public PushChannelDataAnalysisVO getHuaweiDataAnalysis() {
        return huaweiDataAnalysis;
    }

    public void setHuaweiDataAnalysis(PushChannelDataAnalysisVO huaweiDataAnalysis) {
        this.huaweiDataAnalysis = huaweiDataAnalysis;
    }

    public PushChannelDataAnalysisVO getIosDataAnalysis() {
        return iosDataAnalysis;
    }

    public void setIosDataAnalysis(PushChannelDataAnalysisVO iosDataAnalysis) {
        this.iosDataAnalysis = iosDataAnalysis;
    }

    public String getDataAnalysisDate() {
        return dataAnalysisDate;
    }

    public void setDataAnalysisDate(String dataAnalysisDate) {
        this.dataAnalysisDate = dataAnalysisDate;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
