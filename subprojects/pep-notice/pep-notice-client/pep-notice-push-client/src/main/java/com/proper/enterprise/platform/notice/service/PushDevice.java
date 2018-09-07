package com.proper.enterprise.platform.notice.service;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.notice.server.api.enums.PushDeviceType;
import com.proper.enterprise.platform.notice.server.api.enums.PushMode;


public interface PushDevice extends IBase {

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
     * 获取设备id
     *
     * @return String 设备id
     */
    public String getDeviceid();

    /**
     * 赋值设备id
     *
     * @param deviceid 设备id
     */
    public void setDeviceid(String deviceid);

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
     * 获取设备类型
     *
     * @return PushDeviceType 设备类型
     */
    public PushDeviceType getDevicetype();

    /**
     * 赋值设备类型
     *
     * @param devicetype 设备类型
     */
    public void setDevicetype(PushDeviceType devicetype);

    /**
     * 获取其他信息，类似备注
     *
     * @return String 其他信息
     */
    public String getOtherInfo();

    /**
     * 赋值其他信息
     *
     * @param otherInfo 其他信息，类似备注
     */
    public void setOtherInfo(String otherInfo);

    /**
     * 获取推送方式
     *
     * @return PushMode 推送方式
     */
    public PushMode getPushMode();

    /**
     * 赋值推送方式
     *
     * @param pushMode 推送方式
     */
    public void setPushMode(PushMode pushMode);


    /**
     * 获取推送token
     *
     * @return String 推送token
     */
    public String getPushToken();

    /**
     * 赋值推送token
     *
     * @param pushToken 推送token
     */
    public void setPushToken(String pushToken);
}
