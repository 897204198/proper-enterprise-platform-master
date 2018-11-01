package com.proper.enterprise.platform.announcement.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_ANNOUNCEMENT")
@CacheEntity
public class AnnouncementEntity extends BaseEntity {

    public AnnouncementEntity() {}

    /**
     * 信息类型
     */
    @Column(nullable = false)
    private String infoType = "";

    /**
     * 标题信息
     */
    @Column(length = 4000)
    private String title = "";

    /**
     * 基本信息
     */
    @Column(length = 4000)
    private String info = "";

    /**
     * 生效起始时间 yyyy-MM-dd HH:mm:ss
     */
    @Column(nullable = false)
    private String beginTime = "";

    /**
     * 生效终止时间 yyyy-MM-dd HH:mm:ss
     */
    @Column
    private String endTime = "";

    /**
     * 开启关闭状态,默认关闭状态
     */
    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private Boolean infoStatus = false;

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Boolean getInfoStatus() {
        return infoStatus;
    }

    public void setInfoStatus(Boolean infoStatus) {
        this.infoStatus = infoStatus;
    }
}