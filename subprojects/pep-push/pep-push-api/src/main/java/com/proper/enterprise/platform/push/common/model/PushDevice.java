package com.proper.enterprise.platform.push.common.model;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceStatus;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;

public interface PushDevice extends IBase {

    public String getAppkey();

    public void setAppkey(String appkey);

    public String getDeviceid();

    public void setDeviceid(String deviceid);

    public String getUserid();

    public void setUserid(String userid);

    public PushDeviceType getDevicetype();

    public void setDevicetype(PushDeviceType devicetype);

    public String getOtherInfo();

    public void setOtherInfo(String otherInfo);

    public PushMode getPushMode();

    public void setPushMode(PushMode pushMode);

    public PushDeviceStatus getMstatus();

    public void setMstatus(PushDeviceStatus mstatus);

    public String getPushToken();

    public void setPushToken(String pushToken);
}
