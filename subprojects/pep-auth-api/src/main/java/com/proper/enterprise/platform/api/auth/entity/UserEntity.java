package com.proper.enterprise.platform.api.authc.entity;

import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.enums.ActiveStatus;
import com.proper.enterprise.platform.core.enums.UseStatus;
import com.proper.enterprise.platform.core.json.JSONObject;
import com.proper.enterprise.platform.core.json.JSONUtil;

@Entity
@Table(name = "pep_authc_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "com.proper.enterprise.platform.api.authc.entity.UserEntity")
public class UserEntity extends BaseEntity {

    private static final long serialVersionUID = -932921035920514049L;
    
    /**
     * 登录名，唯一
     */
    @Column(unique = true, nullable = false)
    private String loginName;
    
    /**
     * 账号，唯一
     */
    @Column(unique = true, nullable = false)
    private String account;

    /**
     * 密码
     */
    @Column(nullable = false)
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 编号
     */
    private String code;
    
    /**
     * 用户分类Id
     */
    private String categoryCode;

    /**
     * 用户分类名称
     */
    private String categoryName;
    
    /**
     * 用户分类名称
     */
    private String categoryId;

    /**
     * 注册机构Id
     */
    private String raId;

    /**
     * 注册机构名称
     */
    private String raName;

    /**
     * 注册机构编号
     */
    private String raCode;

    /**
     * 备注
     */
    private String decription;

    /**
     * 启用状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActiveStatus activeStatus = ActiveStatus.INACTIVE;

    /**
     * 锁定状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UseStatus useStatus = UseStatus.STOP;
    
    /**
     * 永不过期
     */
    @Column(nullable = false)
    @org.hibernate.annotations.Type(type = "yes_no")
    private boolean neverExpired;

    /**
     * 到期的时间
     */
    private String dueDate;
    
    /**
     * 邮箱，用于找回密码
     */
    private String email;
    
    /**
     *扩展属性
     */
    private String extendId;
    
    /**
     * 扩展属性
     */
    private String extendPropertiesText;
    
    @Override
    public String toString() {
        return "User [id=" + id + ", loginName=" + loginName + ", name=" + name + ", raId=" + raId
                + ", extendPropertiesText=" + extendPropertiesText + ", extendId=" + extendId + "]";
    }
    
    public String getExtendProperty(String key) {
        JSONObject jsonObject = JSONUtil.parseObject(extendPropertiesText);

        if (jsonObject.containsKey(key)) {
            return jsonObject.get(key).toString();
        }
        return null;
    }

    public void putExtendProperty(String key, String value) {
        if (this.extendPropertiesText == null) {
            this.extendPropertiesText = "{}";
        }
        JSONObject jsonObject = JSONUtil.parseObject(extendPropertiesText);
        if (jsonObject.containsKey(key)){
            jsonObject.remove(key);
        }
        
        jsonObject.put(key, value);

        this.extendPropertiesText = jsonObject.toString();
    }

    public void putExtendProperty(Map<String, String> extendProperties) {
        for (Entry<String, String> entry : extendProperties.entrySet()) {
            putExtendProperty(entry.getKey(), entry.getValue());
        }
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getRaId() {
        return raId;
    }

    public void setRaId(String raId) {
        this.raId = raId;
    }

    public String getRaName() {
        return raName;
    }

    public void setRaName(String raName) {
        this.raName = raName;
    }

    public String getRaCode() {
        return raCode;
    }

    public void setRaCode(String raCode) {
        this.raCode = raCode;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    public UseStatus getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(UseStatus useStatus) {
        this.useStatus = useStatus;
    }

    public boolean isNeverExpired() {
        return neverExpired;
    }

    public void setNeverExpired(boolean neverExpired) {
        this.neverExpired = neverExpired;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }

    public String getExtendPropertiesText() {
        return extendPropertiesText;
    }

    public void setExtendPropertiesText(String extendPropertiesText) {
        this.extendPropertiesText = extendPropertiesText;
    }
    
}
