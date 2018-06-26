package com.proper.enterprise.platform.push.common.model;

import java.util.Date;
import java.util.Map;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;

public interface PushMsg extends IBase {

    /**
     * 获取应用标识
     *
     * @return String 应用标识
     */
    public String getAppkey();

    /**
     * 赋值应用标识
     *
     * @param appkey 应用标识
     */
    public void setAppkey(String appkey);

    /**
     * 获取消息id
     *
     * @return String 消息id
     */
    public String getMsgid();

    /**
     * 赋值消息id
     *
     * @param msgid 消息id
     */
    public void setMsgid(String msgid);

    /**
     * 获取用户id
     *
     * @return String 用户id
     */
    public String getUserid();

    /**
     * 赋值用户id
     *
     * @param userid 用户id
     */
    public void setUserid(String userid);

    /**
     * 获取消息内容
     *
     * @return String 消息内容
     */
    public String getMcontent();

    /**
     * 赋值消息内容
     *
     * @param mcontent 消息内容
     */
    public void setMcontent(String mcontent);

    /**
     * 获取消息推送状态
     *
     * @return PushMsgStatus 消息推送状态
     */
    public PushMsgStatus getMstatus();

    /**
     * 赋值消息推送状态
     *
     * @param mstatus 消息推送状态
     */
    public void setMstatus(PushMsgStatus mstatus);

    /**
     * 获取消息发送时间
     *
     * @return Date 消息发送时间
     */
    public Date getMsendedDate();

    /**
     * 赋值消息发送时间
     *
     * @param msendedDate 消息发送时间
     */
    public void setMsendedDate(Date msendedDate);

    /**
     * 获取推送设备
     *
     * @return PushDevice 推送设备
     */
    public PushDevice getDevice();

    /**
     * 赋值推送设备
     *
     * @param device 推送设备
     */
    public void setDevice(PushDevice device);

    /**
     * 获取消息发送量
     *
     * @return Integer 消息发送量
     */
    public Integer getSendCount();

    /**
     * 赋值消息发送量
     *
     * @param sendCount 消息发送量
     */
    public void setSendCount(Integer sendCount);

    /**
     * 获取消息标题
     *
     * @return String 消息标题
     */
    public String getMtitle();

    /**
     * 赋值消息标题
     *
     * @param mtitle 消息标题
     */
    public void setMtitle(String mtitle);

    // public String getMcustoms();
    //
    // public void setMcustoms(String mcustoms);

    /**
     * 获取自定义键值对
     *
     * @return 自定义键值对
     */
    public Map<String, Object> getMcustomDatasMap();

    /**
     * 赋值自定义键值对
     *
     * @param m 自定义键值对
     */
    public void setMcustomDatasMap(Map<String, Object> m);

}
