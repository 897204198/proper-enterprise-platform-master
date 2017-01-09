package com.proper.enterprise.platform.auth.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.DataRestrain;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

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

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_RESOURCES_DATARESTRAINS",
        joinColumns = @JoinColumn(name = "RESOURCE_ID"),
        inverseJoinColumns = @JoinColumn(name = "DATARESTRAIN_ID"))
    private Collection<DataRestrainEntity> dataRestrainEntities = new ArrayList<>();

    @ManyToMany(mappedBy = "resourceEntities")
    private Collection<MenuEntity> menuEntities = new ArrayList<>();

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

    @Override
    public Collection<? extends Menu> getMenus() {
        return menuEntities;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

}
