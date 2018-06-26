package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

/**
 * 资源
 *
 * 资源代表平台能够对外提供的接口。
 * 资源以 RESTFul 风格进行描述，URL + Method 作为资源的`唯一标识`。
 * 资源`不`与数据库表一一对应，一个资源可能会操作多个表。
 *
 * 资源上可以定义一个或多个`数据约束`。
 */
public interface Resource extends IBase {

    /**
     * 获得资源的 URL
     *
     * @return URL
     */
    String findURL();

    /**
     * 设置资源的 URL
     *
     * @param url URL
     */
    void addURL(String url);

    /**
     * 获得资源的 Method
     *
     * @return HTTP method
     */
    RequestMethod getMethod();

    /**
     * 设置资源的 Method
     *
     * @param method HTTP method
     */
    void setMethod(RequestMethod method);

    /**
     * 获得资源名称
     *
     * @return 资源名称
     */
    String getName();

    /**
     * 设置资源名称
     *
     * @param name 资源名称
     */
    void setName(String name);

    /**
     * 获得资源所受的数据约束集合
     *
     * @return 数据约束集合
     */
    Collection<? extends DataRestrain> getDataRestrains();

    /**
     * 按照表名获得资源所受的数据约束集合
     *
     * @param tableName 表名
     * @return 数据约束集合
     */
    Collection<? extends DataRestrain> getDataRestrains(String tableName);

    /**
     * 为资源添加一个数据约束
     *
     * @param restrain 数据约束
     */
    void add(DataRestrain restrain);

    /**
     * 从资源中移除一个数据约束
     *
     * @param restrain 要移除的数据约束
     */
    void remove(DataRestrain restrain);

    /**
     * 获得资源关联的菜单集合
     *
     * @return 菜单集合
     */
    Collection<? extends Menu> getMenus();

    /**
     * 获得资源关联的角色集合
     *
     * @return 角色集合
     */
    Collection<? extends Role> getRoles();

    /**
     * 获得资源类别
     *
     * @return 资源类别
     */
    DataDicLite getResourceType();

    /**
     * 设置资源类别
     *
     * @param resourceType 资源类别
     */
    void setResourceType(DataDicLite resourceType);

    /**
     * 获得资源标识
     *
     * @return 资源标识
     */
    String getIdentifier();

    /**
     * 设置资源标识
     *
     * @param identifier 资源标识
     */
    void setIdentifier(String identifier);

    /**
     * 获得资源编码
     *
     * @return 资源编码
     */
    String getResourceCode();

    /**
     * 设置资源编码
     *
     * @param resourceCode 资源编码
     */
    void setResourceCode(String resourceCode);

    /**
     * 是否为继承来的资源 true是 false否
     * @return true or false
     */
    Boolean getExtend();

    /**
     * 是否为继承来的资源 true是 false否
     * @param extend true false
     */
    void setExtend(Boolean extend);
}
