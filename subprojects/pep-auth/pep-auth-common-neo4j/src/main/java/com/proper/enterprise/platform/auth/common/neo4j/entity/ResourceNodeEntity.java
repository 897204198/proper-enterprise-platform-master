package com.proper.enterprise.platform.auth.common.neo4j.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.DataRestrain;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.auth.common.neo4j.converter.GraphDataDicLiteConverter;
import com.proper.enterprise.platform.auth.common.neo4j.converter.RequestMethodConverter;
import com.proper.enterprise.platform.core.neo4j.entity.BaseNodeEntity;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NodeEntity(label = "PEP_AUTH_RESOURCES")
public class ResourceNodeEntity extends BaseNodeEntity implements Resource {

    private String name;
    /**
     * 权限对应的Url地址
     */
    private String url;

    @Convert(RequestMethodConverter.class)
    private RequestMethod method = RequestMethod.GET;
    /**
     * 菜单类别数据字典
     */
    @Convert(GraphDataDicLiteConverter.class)
    private DataDicLite resourceType;
    private boolean enable = true;
    private String resourceCode;
    /**
     * 标识
     */
    private String identifier = UUID.randomUUID().toString();

    @Relationship(type = "has_resource", direction = Relationship.INCOMING)
    private Set<RoleNodeEntity> roles = new HashSet<>();

    @Relationship(type = "has_resource", direction = Relationship.INCOMING)
    private Set<MenuNodeEntity> menus = new HashSet<>();

    public ResourceNodeEntity() {
    }

    public ResourceNodeEntity(String url, RequestMethod method) {
        this.url = url;
        this.method = method;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Collection<? extends DataRestrain> getDataRestrains() {
        return null;
    }

    @Override
    public Collection<DataRestrain> getDataRestrains(String tableName) {
        return null;
    }

    @Override
    public void add(DataRestrain restrain) {
    }

    public void add(MenuNodeEntity menuNodeEntity) {
        this.menus.add(menuNodeEntity);
    }

    public void add(RoleNodeEntity roleNodeEntity) {
        this.roles.add(roleNodeEntity);
    }

    @Override
    public void remove(DataRestrain restrain) {
    }

    public void remove(MenuNodeEntity menuNodeEntity) {
        this.menus.remove(menuNodeEntity);
    }

    public void remove(RoleNodeEntity roleNodeEntity) {
        this.roles.remove(roleNodeEntity);
    }

    @JsonIgnore
    @Override
    public Collection<? extends Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<MenuNodeEntity> menus) {
        this.menus = menus;
    }

    @JsonIgnore
    @Override
    public Collection<RoleNodeEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleNodeEntity> roles) {
        this.roles = roles;
    }

    @Override
    public DataDicLite getResourceType() {
        return resourceType;
    }

    @Override
    public void setResourceType(DataDicLite resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public void setURL(String url) {
        this.url = url;
    }

    @Override
    public RequestMethod getMethod() {
        return method;
    }

    @Override
    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    @Override
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
