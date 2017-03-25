package com.proper.enterprise.platform.push.common.model;

import java.util.Date;
import java.util.Map;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;

public interface PushMsg extends IBase {
    public String getAppkey();

    public void setAppkey(String appkey);

    public String getMsgid();

    public void setMsgid(String msgid);

    public String getUserid();

    public void setUserid(String userid);

    public String getMcontent();

    public void setMcontent(String mcontent);

    public PushMsgStatus getMstatus();

    public void setMstatus(PushMsgStatus mstatus);

    public Date getMsendedDate();

    public void setMsendedDate(Date msendedDate);

    public PushDevice getDevice();

    public void setDevice(PushDevice device);

    public Integer getSendCount();

    public void setSendCount(Integer sendCount);

    public String getMtitle();

    public void setMtitle(String mtitle);

    // public String getMcustoms();
    //
    // public void setMcustoms(String mcustoms);

    public Map<String, Object> getMcustomDatasMap();

    public void setMcustomDatasMap(Map<String, Object> m);

}
