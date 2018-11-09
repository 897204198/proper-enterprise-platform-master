package com.proper.enterprise.platform.notice.server.push.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.Map;

public class PushConfigVO {

    private Map<String, Object> huaweiConf;

    private Map<String, Object> xiaomiConf;

    private Map<String, Object> iosConf;

    public Map<String, Object> getHuaweiConf() {
        return huaweiConf;
    }

    public void setHuaweiConf(Map<String, Object> huaweiConf) {
        this.huaweiConf = huaweiConf;
    }

    public Map<String, Object> getXiaomiConf() {
        return xiaomiConf;
    }

    public void setXiaomiConf(Map<String, Object> xiaomiConf) {
        this.xiaomiConf = xiaomiConf;
    }

    public Map<String, Object> getIosConf() {
        return iosConf;
    }

    public void setIosConf(Map<String, Object> iosConf) {
        this.iosConf = iosConf;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
