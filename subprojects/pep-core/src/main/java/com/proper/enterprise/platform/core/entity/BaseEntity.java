package com.proper.enterprise.platform.core.entity;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.json.JSONObject;
import com.proper.enterprise.platform.core.json.JSONUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 数据模型及实体基类，包含数据表公共字段的 getter 和 setter 方法
 *
 * 公共字段包括：
 *  - id                唯一标识
 *  - createUserId      创建用户 id
 *  - createTime        创建时间
 *  - lastModifyUserId  最后修改用户 id
 *  - lastModifyTime    最后修改时间
 */
@MappedSuperclass
public class BaseEntity implements IBase {

    private static final long serialVersionUID = PEPConstants.VERSION;
    
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
        JSONObject jsonObject = JSONUtil.parseObject(extendProperties);
        return jsonObject!=null && jsonObject.containsKey(key) ? jsonObject.get(key) : null;
    }

    public void putExtendProperty(String key, String value) {
        if (this.extendProperties == null) {
            this.extendProperties = "{}";
        }

        JSONObject jsonObject = JSONUtil.parseObject(extendProperties);
        if (jsonObject != null) {
            jsonObject.put(key, value);
            this.extendProperties = jsonObject.toString();
        }
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
