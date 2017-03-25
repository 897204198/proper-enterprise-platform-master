package com.proper.enterprise.platform.push.common.model;

import com.proper.enterprise.platform.core.api.IBase;

public interface PushUser extends IBase {
    public String getAppkey();

    public void setAppkey(String appkey);

    public String getOtherInfo();

    public void setOther_info(String otherInfo);

    public String getUserid();

    public void setUserid(String userid);

}
