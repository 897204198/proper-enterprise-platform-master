package com.proper.enterprise.platform.push.entity;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.push.common.model.PushDevice;
import com.proper.enterprise.platform.push.common.model.PushMsg;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;

@Entity
@Table(name = "PEP_PUSH_MSG")
@CacheEntity
public class PushMsgEntity extends BaseEntity implements PushMsg {
    private static final long serialVersionUID = PEPConstants.VERSION;
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMsgEntity.class);

    @Column(nullable = false)
    private String appkey;
    @Column(nullable = false)
    private String msgid;
    @Column(nullable = false)
    private String userid;
    @Column(nullable = false, length = 1000)
    private String mcontent; // 消息正文
    private String mtitle; // 消息标题
    @Column(nullable = false, length = 1000)
    private String mcustoms; // 消息的附加信息，json 字符串
    @Enumerated(EnumType.ORDINAL)
    private PushMsgStatus mstatus; // 消息状态，是否发送
    private Date msendedDate; // 消息成功发送日期

    private Integer sendCount; // 消息发送次数

    @OneToOne
    @JoinColumn(name = "DEVICE_PK_ID")
    private PushDeviceEntity device;
    private static final int MAX_MRESPONSE_LENGTH = 1000;
    @Column(length = MAX_MRESPONSE_LENGTH)
    private String mresponse; // 最后一次推送服务器返回的信息
    private String pushToken; // 最后一次推送消息时的唯一标识

    public String getMtitle() {
        return mtitle;
    }

    public void setMtitle(String mtitle) {
        this.mtitle = mtitle;
    }

    public String getMcustoms() {
        return mcustoms;
    }

    public void setMcustoms(String mcustoms) {
        this.mcustoms = mcustoms;
    }

    public void setDevice(PushDeviceEntity device) {
        this.device = device;
    }

    public PushDevice getDevice() {
        return device;
    }

    public void setDevice(PushDevice device) {
        if (device instanceof PushDeviceEntity) {
            this.device = (PushDeviceEntity) device;
        } else {
            LOGGER.error("setDevice() SHOULD BE  DeviceEntity type, but get {} here.",
                    device.getClass().getCanonicalName());
        }
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMcontent() {
        return mcontent;
    }

    public void setMcontent(String mcontent) {
        this.mcontent = mcontent;
    }

    public PushMsgStatus getMstatus() {
        return mstatus;
    }

    public void setMstatus(PushMsgStatus mstatus) {
        this.mstatus = mstatus;
    }

    public Date getMsendedDate() {
        if (msendedDate == null) {
            return null;
        }
        return (Date) msendedDate.clone();
    }

    public void setMsendedDate(Date msendedDate) {
        if (msendedDate == null) {
            this.msendedDate = null;
        } else {
            this.msendedDate = (Date) msendedDate.clone();
        }
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMcustomDatasMap() {
        Map<String, Object> map = null;
        try {
            if (mcustoms != null) {
                map = (Map<String, Object>) JSONUtil.parse(mcustoms, Map.class);
            }
        } catch (Exception e) {
            LOGGER.error("PushMsgEntity.getMcustomDatasMap error!", e);
        }
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        return map;
    }

    public void setMcustomDatasMap(Map<String, Object> m) {
        try {
            mcustoms = JSONUtil.toJSON(m);
        } catch (IOException e) {
            LOGGER.error("PushMsgEntity.setMcustomDatasMap  error , map is {} !", m, e);
            mcustoms = "{}";
        }
    }

    public String getMresponse() {
        return mresponse;
    }

    public void setMresponse(String mresponse) {
        if (mresponse != null && mresponse.length() > MAX_MRESPONSE_LENGTH) {
            mresponse = mresponse.substring(0, MAX_MRESPONSE_LENGTH);
        }
        this.mresponse = mresponse;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

}
