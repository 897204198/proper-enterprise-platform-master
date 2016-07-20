package com.proper.enterprise.platform.auth.common.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.enums.ResourceType;
import com.proper.enterprise.platform.api.auth.model.DataRestrain;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;

@Entity
@Table(name="PEP_AUTH_RESOURCES", uniqueConstraints = @UniqueConstraint(columnNames ={"url", "method"}))
@CacheEntity
public class ResourceEntity extends BaseEntity implements Resource {

    public ResourceEntity() {
    }
    
    public ResourceEntity(String url, RequestMethod method) {
        this.url = url;
        this.method = method;
    }

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

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    private Collection<DataRestrainEntity> dataRestrainEntities = Collections.emptySet();

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

    @Override
    @JsonIgnore
    public Collection<? extends DataRestrain> getDataRestrains() {
        return dataRestrainEntities;
    }

    @Override
    public Collection<DataRestrain> getDataRestrains(String tableName) {
        Collection<DataRestrain> datalist = new ArrayList<>();
        for (DataRestrainEntity set : dataRestrainEntities) {
            if (tableName.equals(set.getTableName())) {
                datalist.add(set);
            }
        }
        return datalist;
    }

    @Override
    public void add(DataRestrain restrain) {
        if (dataRestrainEntities == null || dataRestrainEntities.size() == 0) {
            dataRestrainEntities = new ArrayList<DataRestrainEntity>();
        }
        dataRestrainEntities.add((DataRestrainEntity) restrain);
    }

    @Override
    public void remove(DataRestrain restrain) {
        dataRestrainEntities.remove(restrain);
    }

    @JsonIgnore
    public Resource getParent() {
        return parentEntity;
    }

    public void setParent(Resource parent) {
        if (parent instanceof ResourceEntity) {
            this.parentEntity = (ResourceEntity) parent;
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
