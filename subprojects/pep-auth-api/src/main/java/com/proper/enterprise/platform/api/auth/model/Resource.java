package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.api.auth.enums.ResourceType;
import com.proper.enterprise.platform.core.api.IBase;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

/**
 * 资源
 *
 * 资源代表平台能够对外提供的功能。
 * 资源以 RESTFul 风格进行描述，URL + Method 作为资源的`唯一标识`。
 * 资源`不`与数据库表一一对应，一个资源可能会操作多个表。
 * 资源以树形结构组织，除根节点外，每个资源有一个父资源。允许存在多个根节点，以森林形式展现资源结构。
 *
 * 根据前后端分离的平台结构，资源分为两类：
 * - `API`：即后台服务端能够响应的请求路径。
 * - `APP`, `MENU`：MENU 组织在 APP 下，且可多层，最底层的 MENU（叶子节点）与前台应用中定义的路由一一对应。
 *   MENU 类型叶子节点资源下会定义一组逻辑相关的 API 类型资源。
 *
 * API 类型资源需要定义`数据约束`
 */
public interface Resource extends IBase {

    /**
     * 获得资源的 URL
     *
     * @return URL
     */
    String getURL();

    /**
     * 设置资源的 URL
     *
     * @param url URL
     */
    void setURL(String url);

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
     * 获得上级资源
     *
     * @return 上级资源
     */
    Resource getParent();

    /**
     * 设置上级资源
     *
     * @param parent 上级资源
     */
    void setParent(Resource parent);

    /**
     * 获得资源类型
     *
     * @return 资源类型
     */
    ResourceType getResourceType();

    /**
     * 设置资源类型
     *
     * @param resourceType 资源类型
     */
    void setResourceType(ResourceType resourceType);

    /**
     * 获得资源排序
     *
     * @return 排序号
     */
    int getSequenceNumber();

    /**
     * 设置资源排序
     *
     * @param sequenceNumber 排序号
     */
    void setSequenceNumber(int sequenceNumber);

    /**
     * 获得资源所受的数据约束集合
     *
     * @return 数据约束集合
     */
    Collection<? extends DataRestrain> getDataRestrains();

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

}
