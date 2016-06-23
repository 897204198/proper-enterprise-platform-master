package com.proper.enterprise.platform.auth.common.entity;

import com.proper.enterprise.platform.api.auth.enums.ResourceType;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(
    name = "PEP_AUTH_RESOURCES",
    uniqueConstraints = @UniqueConstraint(columnNames = {"url", "method"})
)
@CacheEntity
public class ResourceEntity extends BaseEntity implements Resource {

    public ResourceEntity() { }

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceEntity.class);

    /**
     * 名称
     */
    private String name;

    /**
     * 父资源
     */
    @OneToOne
    @JoinColumn(name = "PARENT_RES_ID")
    private ResourceEntity parentEntity;

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

    @ManyToMany(mappedBy = "resourceEntities")
    private Collection<RoleEntity> roleEntities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
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

    public Resource getParent() {
        return parentEntity;
    }

    public void setParent(Resource parent) {
        if (parent instanceof ResourceEntity) {
            this.parentEntity = (ResourceEntity)parent;
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

    public Collection<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    public void setRoleEntities(Collection<RoleEntity> roleEntities) {
        this.roleEntities = roleEntities;
    }
}
