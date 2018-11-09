package com.proper.enterprise.platform.notice.server.push.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.List;

public class PushNoticeMsgPieVO {

    private List<String> appKeyOrder;

    private List<PushMsgPieDataVO> pieData;

    public List<String> getAppKeyOrder() {
        return appKeyOrder;
    }

    public void setAppKeyOrder(List<String> appKeyOrder) {
        this.appKeyOrder = appKeyOrder;
    }

    public List<PushMsgPieDataVO> getPieData() {
        return pieData;
    }

    public void setPieData(List<PushMsgPieDataVO> pieData) {
        this.pieData = pieData;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
