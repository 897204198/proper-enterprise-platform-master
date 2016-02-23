package com.proper.enterprise.platform.auth.common.entity;

import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.api.auth.enums.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(
    name = "pep_auth_resources",
    uniqueConstraints = @UniqueConstraint(columnNames = {"url", "method"})
)
@CacheEntity
public class ResourceEntity extends BaseEntity implements Resource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceEntity.class);

    private static final long serialVersionUID = PEPConstants.VERSION;
    
    /**
     * 编号
     */
    private String code;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 父资源
     */
    @OneToOne
    private ResourceEntity parent;
    
    /**
     * 类型
     */
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;
    
    /**
     * 权限对应的Url地址
     */
    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestMethod method = RequestMethod.GET;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 显示顺序
     */
    private int sequenceNumber;

    @ManyToMany(mappedBy = "resources")
    private List<RoleEntity> roles;
    
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }

    public Resource getParent() {
        return parent;
    }

    public void setParent(Resource parent) {
        if (parent instanceof ResourceEntity) {
            this.parent = (ResourceEntity)parent;
        } else {
            LOGGER.error("Parent of a Resource SHOULD BE  ResourceEntity type, but get {} here.",
                    parent.getClass().getCanonicalName());
        }
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

}
