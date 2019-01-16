package com.proper.enterprise.platform.auth.common.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.model.DataRestrain;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.view.BaseView;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Collection;

@POJORelevance(relevanceDOClassName = "com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity")
public class ResourceVO extends BaseVO implements Resource {

    public ResourceVO() {
    }

    public interface Single extends BaseView {

    }

    /**
     * 名称
     */
    @JsonView(value = {Single.class})
    private String name;
    /**
     * 权限对应的Url地址
     */
    @JsonView(value = {Single.class})
    private String url;

    @JsonView(value = {Single.class})
    private RequestMethod method;
    /**
     * 菜单类别数据字典
     */
    @JsonView(value = {Single.class})
    private DataDicLite resourceType;

    private Collection<MenuVO> menus;

    private Collection<RoleVO> roles;
    /**
     * 标识
     */
    @JsonView(value = {Single.class})
    private String identifier;

    private Collection<DataRestrainVO> dataRestrainEntities;

    @JsonView(value = {Single.class})
    private String resourceCode;

    @JsonView(value = {Single.class})
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
    public Collection<DataRestrainVO> getDataRestrains() {
        return dataRestrainEntities;
    }

    @Override
    public Collection<DataRestrainVO> getDataRestrains(String tableName) {
        return new ArrayList<>();
    }

    @Override
    public void add(DataRestrain restrain) {

    }

    @Override
    public void remove(DataRestrain restrain) {

    }

    @Override
    public RequestMethod getMethod() {
        if (null == this.method) {
            return RequestMethod.GET;
        }
        return method;
    }

    @Override
    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    @Override
    public DataDicLite getResourceType() {
        return DataDicUtil.convert(resourceType);
    }

    @Override
    public void setResourceType(DataDicLite resourceType) {
        this.resourceType = resourceType;
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
    public Collection<MenuVO> getMenus() {
        return menus;
    }


    @Override
    public Collection<RoleVO> getRoles() {
        return roles;
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

    public void setMenus(Collection<MenuVO> menus) {
        this.menus = menus;
    }

    public void setRoles(Collection<RoleVO> roles) {
        this.roles = roles;
    }

    public Collection<DataRestrainVO> getDataRestrainEntities() {
        return dataRestrainEntities;
    }

    public void setDataRestrainEntities(Collection<DataRestrainVO> dataRestrainEntities) {
        this.dataRestrainEntities = dataRestrainEntities;
    }

    public Boolean getExtend() {
        return extend;
    }

    public void setExtend(Boolean extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
