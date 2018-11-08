package com.proper.enterprise.platform.push.entity;

import com.proper.enterprise.platform.core.PEPVersion;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@Table(name = "PEP_PUSH_STATISTIC")
@CacheEntity
public class PushMsgStatisticEntity extends BaseEntity {
    private static final long serialVersionUID = PEPVersion.VERSION;
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMsgStatisticEntity.class);

    @Column(nullable = false)
    private String appkey;

    /**
     * 渠道
     */
    private String pushMode;

    /**
     * 消息成功发送日期或者最后修改日期
     */
    private String msendedDate;

    /**
     * 消息状态，是否发送
     */
    @Enumerated(EnumType.ORDINAL)
    private PushMsgStatus mstatus;

    /**
     * 消息成功/失败的数量
     */
    private Integer mnum;

    /**
     * 属于第几周
     */
    private String week;

    /**
     * 属于第几月
     */
    private String month;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getPushMode() {
        return pushMode;
    }

    public void setPushMode(String pushMode) {
        this.pushMode = pushMode;
    }

    public String getMsendedDate() {
        return msendedDate;
    }

    public void setMsendedDate(String msendedDate) {
        this.msendedDate = msendedDate;
    }

    public PushMsgStatus getMstatus() {
        return mstatus;
    }

    public void setMstatus(PushMsgStatus mstatus) {
        this.mstatus = mstatus;
    }

    public Integer getMnum() {
        return mnum;
    }

    public void setMnum(Integer mnum) {
        this.mnum = mnum;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("appkey:").append(appkey).append("; msendDate:").append(msendedDate).append("; week:").append(week);
        return sb.toString();
    }
}
