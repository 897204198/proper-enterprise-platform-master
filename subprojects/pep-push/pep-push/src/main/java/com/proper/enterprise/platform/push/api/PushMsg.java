package com.proper.enterprise.platform.push.api;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.entity.PushDeviceEntity;

public interface PushMsg extends IBase {

    /**
     * 获取推送应用
     *
     * @return String 推送应用
     */
    public String getAppkey();

    /**
     * 设置推送应用
     *
     * @param appkey 推送应用
     * @return PushMsg
     */
    public PushMsg setAppkey(String appkey);

    /**
     * 获取推送接收方userid
     *
     * @return String 推送应用
     */
    public String getUserid();

    /**
     * 设置推送接收方userid
     *
     * @param userid 推送接收方userid
     * @return PushMsg
     */
    public PushMsg setUserid(String userid);

    /**
     * 获取推动内容
     *
     * @return String 推送内容
     */
    public String getMcontent();

    /**
     * 设置推送内容
     *
     * @param mcontent 推送内容
     * @return PushMsg
     */
    public PushMsg setMcontent(String mcontent);


    /**
     * 获取推送状态
     *
     * @return String 推送状态
     */
    public PushMsgStatus getMstatus();

    /**
     * 设置推送状态
     *
     * @param mstatus 推送状态
     * @return PushMsg
     */
    public PushMsg setMstatus(PushMsgStatus mstatus);

    /**
     * 获取推送渠道
     *
     * @return String 推送渠道
     */
    public PushMode getPushMode();

    /**
     * 设置推送渠道
     *
     * @param pushMode 推送渠道
     * @return PushMsg
     */
    public PushMsg setPushMode(PushMode pushMode);

    /**
     * 设置推送渠道
     *
     * @return 推送实体
     */
    public PushDeviceEntity getDevice();

    /**
     * 设置推送设备
     *
     * @param device 推送设备
     * @return PushMsg
     */
    public PushMsg setDevice(PushDeviceEntity device);

    /**
     * 设置最后一次推动时间
     *
     * @return 最后一次推动时间
     */
    public String getLastPushTime();

    /**
     * 设置最后一次推动时间
     *
     * @param lastPushTime 最后一次推动时间
     * @return PushMsg
     */
    public PushMsg setLastPushTime(String lastPushTime);
}
