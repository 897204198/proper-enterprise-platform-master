package com.proper.enterprise.platform.push.vendor;

import java.util.List;

import com.proper.enterprise.platform.push.config.PushGlobalInfo;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.repository.PushDeviceRepository;
import com.proper.enterprise.platform.push.repository.PushMsgRepository;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;

public abstract class AbstractPushVendorService {

    protected String appkey;
    protected PushDeviceType devicetype;
    protected PushMode pushMode;
    protected PushMsgRepository msgRepo;
    protected PushDeviceRepository deviceRepo;
    protected Object pushParams;
    protected PushGlobalInfo globalInfo;

    public PushGlobalInfo getGlobalInfo() {
        return globalInfo;
    }

    public void setGlobalInfo(PushGlobalInfo globalInfo) {
        this.globalInfo = globalInfo;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public PushDeviceType getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(PushDeviceType devicetype) {
        this.devicetype = devicetype;
    }

    public PushMode getPushMode() {
        return pushMode;
    }

    public void setPushMode(PushMode pushMode) {
        this.pushMode = pushMode;
    }

    public PushMsgRepository getMsgRepo() {
        return msgRepo;
    }

    public void setMsgRepo(PushMsgRepository msgRepo) {
        this.msgRepo = msgRepo;
    }

    public PushDeviceRepository getDeviceRepo() {
        return deviceRepo;
    }

    public void setDeviceRepo(PushDeviceRepository deviceRepo) {
        this.deviceRepo = deviceRepo;
    }

    public Object getPushParams() {
        return pushParams;
    }

    public void setPushParams(Object pushParams) {
        this.pushParams = pushParams;
    }

    public abstract int pushMsg(List<PushMsgEntity> lstMsgs);

    /**
     * 发送消息时，如果pushToken无效，需要调用这个方法将设备的状态设为无效
     *
     * @param msg 消息
     */
    public void onPushTokenInvalid(PushMsgEntity msg) {
        // msg.getDevice().setMstatus(DeviceStatus.INVALID);
        // deviceRepo.save((DeviceEntity) msg.getDevice());
    }

}
