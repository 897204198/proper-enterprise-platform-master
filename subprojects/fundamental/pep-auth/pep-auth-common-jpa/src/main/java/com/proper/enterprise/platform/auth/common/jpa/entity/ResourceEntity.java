package com.proper.enterprise.platform.auth.common.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.DataRestrain;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "PEP_AUTH_RESOURCES", uniqueConstraints = @UniqueConstraint(columnNames = {"url", "method"}))
@CacheEntity
public class ResourceEntity extends BaseEntity implements Resource {

    public ResourceEntity() {
    }

    public ResourceEntity(String url, RequestMethod method) {
        this.url = url;
        this.method = method;
    }

    /**
     * 名称
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 权限对应的Url地址
     */
    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestMethod method = RequestMethod.GET;

    /**
     * 菜单类别数据字典
     */
    @Convert(converter = DataDicLiteConverter.class)
    private DataDicLite resourceType;

    /**
     * 标识
     */
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String identifier;

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_RESOURCES_DATARESTRAINS",
        joinColumns = @JoinColumn(name = "RESOURCE_ID"),
        inverseJoinColumns = @JoinColumn(name = "DATARESTRAIN_ID"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"RESOURCE_ID", "DATARESTRAIN_ID"}))
    private Collection<DataRestrainEntity> dataRestrainEntities;

    @ManyToMany(mappedBy = "resourceEntities")
    private Collection<MenuEntity> menuEntities;

    @ManyToMany(mappedBy = "resourcesEntities")
    private Collection<RoleEntity> roleEntities;
    private String resourceCode;

    @Transient
    private Boolean extend;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String findURL() {
        return url;
    }

    public void addURL(String url) {
        this.url = url;
    }

    @Override
    @JsonIgnore
    public Collection<DataRestrainEntity> getDataRestrains() {
        return dataRestrainEntities;
    }

    @Override
    public Collection<DataRestrainEntity> getDataRestrains(String tableName) {
        Collection<DataRestrainEntity> dataList = new ArrayList<>();
        for (DataRestrainEntity set : dataRestrainEntities) {
            if (tableName.equals(set.getTableName())) {
                dataList.add(set);
            }
        }
        return dataList;
    }

    @Override
    public void add(DataRestrain restrain) {
        if (null == dataRestrainEntities) {
            dataRestrainEntities = new ArrayList<>();
        }
        dataRestrainEntities.add((DataRestrainEntity) restrain);
    }

    @Override
    public void remove(DataRestrain restrain) {
        if (null == dataRestrainEntities) {
            dataRestrainEntities = new ArrayList<>();
        }
        dataRestrainEntities.remove(restrain);
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public DataDicLite getResourceType() {
        return resourceType;
    }

    public void setResourceType(DataDicLite resourceType) {
        this.resourceType = resourceType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    @JsonIgnore
    public Collection<MenuEntity> getMenus() {
        return menuEntities;
    }

    @Override
    @JsonIgnore
    public Collection<RoleEntity> getRoles() {
        return roleEntities;
    }

    @Override
    public String getResourceCode() {
        return resourceCode;
    }

    @Override
    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Collection<DataRestrainEntity> getDataRestrainEntities() {
        return dataRestrainEntities;
    }

    public void setDataRestrainEntities(Collection<DataRestrainEntity> dataRestrainEntities) {
        this.dataRestrainEntities = dataRestrainEntities;
    }

    public Collection<MenuEntity> getMenuEntities() {
        return menuEntities;
    }

    public void setMenuEntities(Collection<MenuEntity> menuEntities) {
        this.menuEntities = menuEntities;
    }

    public Collection<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    public void setRoleEntities(Collection<RoleEntity> roleEntities) {
        this.roleEntities = roleEntities;
    }

    public Boolean getExtend() {
        return extend;
    }

    public void setExtend(Boolean extend) {
        this.extend = extend;
    }
}
