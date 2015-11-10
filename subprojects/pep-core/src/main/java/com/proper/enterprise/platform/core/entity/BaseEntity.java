package com.proper.enterprise.platform.core.entity;

import com.proper.enterprise.platform.core.json.JSONObject;
import com.proper.enterprise.platform.core.json.JSONUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

    /**
     * 扩展属性
     */
    @Transient
    protected String extendProperties;
    
    public String getExtendProperty(String key) {
        if (StringUtil.isNull(extendProperties)) {
            return null;
        }

        JSONObject jsonObject = JSONUtil.parseObject(extendProperties);
        if (jsonObject.containsKey(key)) {
            return jsonObject.get(key).toString();
        }
        return null;
    }

    public void putExtendProperty(String key, String value) {
        if (this.extendProperties == null) {
            this.extendProperties = "{}";
        }
        JSONObject jsonObject = JSONUtil.parseObject(extendProperties);
        if (jsonObject.containsKey(key)){
            jsonObject.remove(key);
        }
        
        jsonObject.put(key, value);

        this.extendProperties = jsonObject.toString();
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

    public String getExtendProperties() {
        return extendProperties;
    }

    public void setExtendProperties(String extendProperties) {
        this.extendProperties = extendProperties;
    }

}
