package com.proper.enterprise.platform.auth.common.vo;

import com.proper.enterprise.platform.api.auth.model.DataRestrain;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.Convert;
import java.util.ArrayList;
import java.util.Collection;

public class ResourceVO extends BaseVO implements Resource {

    /**
     * 名称
     */
    private String name;
    /**
     * 权限对应的Url地址
     */
    private String url;
    private RequestMethod method = RequestMethod.GET;
    /**
     * 菜单类别数据字典
     */
    @Convert(converter = DataDicLiteConverter.class)
    private DataDicLite resourceType;
    /**
     * 菜单状态
     */
    private boolean enable = true;
    private Collection<? extends Menu> menus = new ArrayList<>();
    private Collection<? extends Role> roles = new ArrayList<>();
    /**
     * 标识
     */
    private String identifier;
    private Collection<DataRestrainVO> dataRestrainEntities = new ArrayList<>();
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
    public Collection<? extends DataRestrain> getDataRestrains() {
        return dataRestrainEntities;
    }

    @Override
    public Collection<DataRestrain> getDataRestrains(String tableName) {
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
        return method;
    }

    @Override
    public void setMethod(RequestMethod method) {
        this.method = method;
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
    public Collection<? extends Menu> getMenus() {
        return menus;
    }

    @Override
    public Collection<? extends Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<? extends Role> roles) {
        this.roles = roles;
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
