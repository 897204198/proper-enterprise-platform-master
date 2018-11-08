package com.proper.enterprise.platform.push.api;

import com.proper.enterprise.platform.core.api.IBase;

public interface PushMsgStatistic extends IBase {

    /**
     * 获取推送渠道
     *
     * @return String 推送渠道
     */
    public String getPushMode();

    /**
     * 设置推送渠道
     *
     * @param pushMode 推送渠道
     * @return PushMsgStatistic
     */
    public PushMsgStatistic setPushMode(String pushMode);

    /**
     * 获取推送时间
     *
     * @return 推送时间
     */
    public String getMsendedDate();

    /**
     * 设置推送时间
     *
     * @param msendedDate 推送时间
     * @return PushMsgStatistic
     */
    public PushMsgStatistic setMsendedDate(String msendedDate);

    /**
     * 获取推送状态
     *
     * @return String 推送状态
     */
    public String getMstatus();

    /**
     * 设置推送状态
     *
     * @param mstatus 推送状态
     * @return PushMsgStatistic
     */
    public PushMsgStatistic setMstatus(String mstatus);

    /**
     * 获取推送数量
     *
     * @return Integer 推送数量
     */
    public Integer getMnum();

    /**
     * 获取推送数量
     *
     * @param mnum 推送数量
     * @return PushMsgStatistic
     */
    public PushMsgStatistic setMnum(Integer mnum);
}
