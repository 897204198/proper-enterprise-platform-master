package com.proper.enterprise.platform.core.entity;

import com.proper.enterprise.platform.core.json.JSONObject;
import com.proper.enterprise.platform.core.json.JSONUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

@MappedSuperclass
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 8769611929269353212L;
    
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    protected String id;
    
    @Column(updatable = false, nullable = false)
    protected String createUserId;
    
    @Column(updatable = false, nullable = false)
    protected String createTime = DateUtil.getCurrentDateString();
    
    @Column(nullable = false)
    protected String lastModifyUserId;
    
    @Column(nullable = false)
    protected String lastModifyTime = DateUtil.getCurrentDateString();
    
    protected String description;
    
    /**
     *扩展属性
     */
    protected String extendId;
    
    /**
     * 扩展属性
     */
    protected String extendPropertiesText;
    
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
