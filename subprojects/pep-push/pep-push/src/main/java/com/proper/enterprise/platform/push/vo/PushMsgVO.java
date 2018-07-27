package com.proper.enterprise.platform.push.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.push.api.PushMsg;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.entity.PushDeviceEntity;

public class PushMsgVO extends BaseVO implements PushMsg {

    @JsonView(PushMsgVO.Single.class)
    private PushDeviceEntity device;
    @JsonView(PushMsgVO.Single.class)
    private String appkey;
    @JsonView(PushMsgVO.Single.class)
    private String userid;
    @JsonView(PushMsgVO.Single.class)
    private String mcontent;
    @JsonView(PushMsgVO.Single.class)
    private PushMsgStatus mstatus;
    @JsonView(PushMsgVO.Single.class)
    private PushMode pushMode;
    @JsonView(PushMsgVO.Single.class)
    private String lastPushTime;

    public PushMsgVO() {
    }

    @Override
    public String getAppkey() {
        return this.appkey;
    }

    @Override
    public PushMsg setAppkey(String appkey) {
        this.appkey = appkey;
        return this;
    }

    @Override
    public String getUserid() {
        return this.userid;
    }

    @Override
    public PushMsg setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    @Override
    public String getMcontent() {
        return this.mcontent;
    }

    @Override
    public PushMsg setMcontent(String mcontent) {
        this.mcontent = mcontent;
        return this;
    }

    @Override
    public PushMsgStatus getMstatus() {
        return this.mstatus;
    }

    @Override
    public PushMsg setMstatus(PushMsgStatus mstatus) {
        this.mstatus = mstatus;
        return this;
    }

    @Override
    public PushMode getPushMode() {
        return this.pushMode;
    }

    @Override
    public PushMsg setPushMode(PushMode pushMode) {
        this.pushMode = pushMode;
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public PushDeviceEntity getDevice() {
        return this.device;
    }

    @Override
    public PushMsg setDevice(PushDeviceEntity device) {
        this.device = device;
        return this;
    }

    @Override
    public String getLastPushTime() {
        return this.lastPushTime;
    }

    @Override
    public PushMsg setLastPushTime(String lastPushTime) {
        this.lastPushTime = lastPushTime;
        return this;
    }

    public interface Single extends VOCommonView {
    }
}
