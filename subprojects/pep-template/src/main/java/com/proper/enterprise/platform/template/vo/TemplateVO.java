package com.proper.enterprise.platform.template.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.view.BaseView;

public class TemplateVO extends BaseVO {

    public interface Detail extends BaseView {

    }

    /**
     * 标识
     */
    @JsonView(value = {Detail.class})
    private String code;

    /**
     * 名称
     */
    @JsonView(value = {Detail.class})
    private String name;

    /**
     * 推送标题
     */
    @JsonView(value = {Detail.class})
    private String pushTitle;
    /**
     * 短信标题
     */
    @JsonView(value = {Detail.class})
    private String smsTitle;
    /**
     * 邮件标题
     */
    @JsonView(value = {Detail.class})
    private String emailTitle;


    /**
     * 推送模板
     */
    @JsonView(value = {Detail.class})
    private String pushTemplate;

    /**
     * 短信模板
     */
    @JsonView(value = {Detail.class})
    private String smsTemplate;

    /**
     * 邮件模板
     */
    @JsonView(value = {Detail.class})
    private String emailTemplate;


    /**
     * 目录
     */
    @JsonView(value = {Detail.class})
    private String catelog;

    /**
     * 解释
     */

    @JsonView(value = {Detail.class})
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

    public String getCatelog() {
        return catelog;
    }

    public void setCatelog(String catelog) {
        this.catelog = catelog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "id:" + id;
    }
}
