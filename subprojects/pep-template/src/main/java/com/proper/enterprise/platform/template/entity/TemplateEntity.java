package com.proper.enterprise.platform.template.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_NOTICE_TEMPLATE")
@CacheEntity
public class TemplateEntity extends BaseEntity {

    /**
     * 标识
     */
    @Column(nullable = false, unique = true)
    private String code;

    /**
     * 名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 推送标题
     */
    @Column(nullable = true, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String pushTitle;
    /**
     * 短信标题
     */
    @Column(nullable = true, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String smsTitle;

    /**
     * 邮件标题
     */
    @Column(nullable = true, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String emailTitle;

    /**
     * 推送模板
     */
    @Column(nullable = true, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String pushTemplate;

    /**
     * 短信模板
     */
    @Column(nullable = true, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String smsTemplate;

    /**
     * 邮件模板
     */
    @Column(nullable = true, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String emailTemplate;

    /**
     * 目录
     */
    @Convert(converter = DataDicLiteConverter.class)
    private DataDicLite catelog;

    /**
     * 解释
     */
    @Column
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getSmsTitle() {
        return smsTitle;
    }

    public void setSmsTitle(String smsTitle) {
        this.smsTitle = smsTitle;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public String getPushTemplate() {
        return pushTemplate;
    }

    public void setPushTemplate(String pushTemplate) {
        this.pushTemplate = pushTemplate;
    }

    public String getSmsTemplate() {
        return smsTemplate;
    }

    public void setSmsTemplate(String smsTemplate) {
        this.smsTemplate = smsTemplate;
    }

    public String getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(String emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public DataDicLite getCatelog() {
        return catelog;
    }

    public void setCatelog(DataDicLite catelog) {
        this.catelog = catelog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
