package com.proper.enterprise.platform.push.common.model;

import com.proper.enterprise.platform.core.api.IBase;

public interface PushUser extends IBase {

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

}
