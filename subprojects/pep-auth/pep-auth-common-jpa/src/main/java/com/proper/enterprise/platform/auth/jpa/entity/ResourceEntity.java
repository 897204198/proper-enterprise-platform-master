package com.proper.enterprise.platform.auth.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.DataRestrain;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
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
     * 菜单状态
     */
    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private boolean enable = true;

    @Transient
    private Collection<? extends Menu> menus = new ArrayList<>();

    @Transient
    private Collection<? extends Role> roles = new ArrayList<>();

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
    private Collection<DataRestrainEntity> dataRestrainEntities = new ArrayList<>();

    @ManyToMany(mappedBy = "resourceEntities")
    private Collection<MenuEntity> menuEntities = new ArrayList<>();

    @ManyToMany(mappedBy = "resourcesEntities")
    private Collection<RoleEntity> roleEntities = new ArrayList<>();
    private String resourceCode;

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

    @Override
    @JsonIgnore
    public Collection<? extends DataRestrain> getDataRestrains() {
        return dataRestrainEntities;
    }

    @Override
    public Collection<DataRestrain> getDataRestrains(String tableName) {
        Collection<DataRestrain> dataList = new ArrayList<>();
        for (DataRestrainEntity set : dataRestrainEntities) {
            if (tableName.equals(set.getTableName())) {
                dataList.add(set);
            }
        }
        return dataList;
    }

    @Override
    public void add(DataRestrain restrain) {
        dataRestrainEntities.add((DataRestrainEntity) restrain);
    }

    @Override
    public void remove(DataRestrain restrain) {
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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    @JsonIgnore
    public Collection<? extends Menu> getMenus() {
        return menuEntities;
    }

    @Override
    @JsonIgnore
    public Collection<? extends Role> getRoles() {
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
}
