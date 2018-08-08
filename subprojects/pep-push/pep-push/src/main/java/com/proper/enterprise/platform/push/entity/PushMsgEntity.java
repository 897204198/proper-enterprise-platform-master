package com.proper.enterprise.platform.push.entity;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.push.api.openapi.model.PushMessage;
import com.proper.enterprise.platform.push.common.model.PushDevice;
import com.proper.enterprise.platform.push.common.model.PushMsg;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "PEP_PUSH_MSG")
@CacheEntity
public class PushMsgEntity extends BaseEntity implements PushMsg {
    private static final long serialVersionUID = PEPConstants.VERSION;
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMsgEntity.class);
    private static final int MAX_MRESPONSE_LENGTH = 1000;
    @Column(nullable = false)
    private String appkey;
    @Column(nullable = false)
    private String msgid;
    @Column(nullable = false)
    private String userid;
    /**
     * 消息正文
     */
    @Column(nullable = false, length = 1000)
    private String mcontent;
    /**
     * 消息标题
     */
    private String mtitle;
    /**
     * 消息的附加信息，json 字符串
     */
    @Column(nullable = false, length = 1500)
    private String mcustoms;
    /**
     * 消息状态，是否发送
     */
    @Enumerated(EnumType.ORDINAL)
    private PushMsgStatus mstatus;
    /**
     * 消息成功发送日期
     */
    private Date msendedDate;
    /**
     * 消息发送次数
     */
    private Integer sendCount;
    @OneToOne
    @JoinColumn(name = "DEVICE_PK_ID")
    private PushDeviceEntity device;
    /**
     * 最后一次推送服务器返回的信息
     */
    @Column(length = MAX_MRESPONSE_LENGTH)
    private String mresponse;

    /**
     * 最后一次推送消息时的唯一标识
     */

    /**
     * 推送渠道
     */
    @Enumerated(EnumType.STRING)
    private PushMode pushMode;

    /**
     * 设备类型
     */
    @Enumerated(EnumType.STRING)
    private PushDeviceType devicetype;

    private String pushToken;

    public PushMsgEntity() {
    }

    public PushMsgEntity(PushMessage msg, String appkey, PushDeviceEntity device) {
        this.msgid = UUID.randomUUID().toString();
        this.mstatus = PushMsgStatus.UNSEND;
        this.userid = device == null ? null : device.getUserid();
        this.device = device;
        this.appkey = appkey;
        this.mcontent = msg.getContent();
        this.setMcustomDatasMap(msg.getCustoms());
        this.mtitle = msg.getTitle();
        this.sendCount = 0;
        this.pushMode = device == null ? null : device.getPushMode();
        this.devicetype = device == null ? null : device.getDevicetype();
        Map<String, Object> maps = this.getMcustomDatasMap();
        maps.put("_proper_userid", device == null ? null : device.getUserid());
        maps.put("_proper_title", msg.getTitle());
        maps.put("_proper_content", msg.getContent());
        this.setMcustomDatasMap(maps);
    }

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

    public void setDevice(PushDevice device) {
        if (device instanceof PushDeviceEntity) {
            this.device = (PushDeviceEntity) device;
        } else {
            LOGGER.error("setDevice() SHOULD BE  DeviceEntity type, but get {} here.",
                device.getClass().getCanonicalName());
        }
    }

    public PushDevice getDevice() {
        return device;
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
            map = new HashMap<String, Object>(0);
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

    public PushMode getPushMode() {
        return pushMode;
    }

    public void setPushMode(PushMode pushMode) {
        this.pushMode = pushMode;
    }

    public PushDeviceType getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(PushDeviceType devicetype) {
        this.devicetype = devicetype;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{id='").append(id).append(", appkey=").append(appkey)
            .append(", msgid='").append(msgid).append(", userid='").append(userid)
            .append(", mcontent='").append(mcontent).append(", mtitle='").append(mtitle)
            .append(", mcustoms='").append(mcustoms).append(", mstatus=").append(mstatus)
            .append(", msendedDate=").append(msendedDate).append(", sendCount=").append(sendCount)
            .append(", mresponse='").append(mresponse).append(", pushMode=").append(pushMode)
            .append(", devicetype=").append(devicetype).append(", pushToken='").append(pushToken)
            .append("}");
        return sb.toString();
    }
}
