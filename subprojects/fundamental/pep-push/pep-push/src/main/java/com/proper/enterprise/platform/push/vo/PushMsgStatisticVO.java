package com.proper.enterprise.platform.push.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.view.BaseView;
import com.proper.enterprise.platform.push.api.PushMsgStatistic;


public class PushMsgStatisticVO implements PushMsgStatistic {
    private String appkey;
    @JsonView(Single.class)
    private String pushMode;
    @JsonView(Single.class)
    private String msendedDate;
    @JsonView(Single.class)
    private String mstatus;
    @JsonView(Single.class)
    private Integer mnum;

    public PushMsgStatisticVO() {
    }

    @Override
    public String getPushMode() {
        return this.pushMode;
    }

    @Override
    public PushMsgStatistic setPushMode(String pushMode) {
        this.pushMode = pushMode;
        return this;
    }

    @Override
    public String getMsendedDate() {
        return this.msendedDate;
    }

    @Override
    public PushMsgStatistic setMsendedDate(String msendedDate) {
        this.msendedDate = msendedDate;
        return this;
    }

    @Override
    public String getMstatus() {
        return this.mstatus;
    }

    @Override
    public PushMsgStatistic setMstatus(String mstatus) {
        this.mstatus = mstatus;
        return this;
    }

    @Override
    public Integer getMnum() {
        return this.mnum;
    }

    @Override
    public PushMsgStatistic setMnum(Integer mnum) {
        this.mnum = mnum;
        return this;
    }

    public String getWeek() {
        return null;
    }

    public PushMsgStatistic setWeek(String week) {
        return null;
    }

    public String getMonth() {
        return null;
    }

    public PushMsgStatistic setMonth(String month) {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getCreateUserId() {
        return null;
    }

    @Override
    public void setCreateUserId(String createUserId) {

    }

    @Override
    public String getCreateTime() {
        return null;
    }

    @Override
    public void setCreateTime(String createTime) {

    }

    @Override
    public String getLastModifyUserId() {
        return null;
    }

    @Override
    public void setLastModifyUserId(String lastModifyUserId) {

    }

    @Override
    public String getLastModifyTime() {
        return null;
    }

    @Override
    public void setLastModifyTime(String lastModifyTime) {

    }

    @Override
    public Boolean getEnable() {
        return Boolean.FALSE;
    }

    @Override
    public void setEnable(Boolean enable) {
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public interface Single extends BaseView {
    }
}
