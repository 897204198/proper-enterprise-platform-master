package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.api.IBase;

/**
 * 数据约束
 *
 * 数据约束定义资源中某属性的范围，相当于 SQL 中的查询条件。
 * 默认无数据约束，并可由各模块自行扩展。
 * 自定义数据约束时，需定义根据什么属性的什么范围进行约束。
 * 数据约束可叠加。
 *
 * 可使用两种方式定义数据约束：
 * 1. 在 Entity 中定义 Hibernate Filter
 * 2. 直接在数据库中设置数据约束具体的 SQL
 */
public interface DataRestrain extends IBase {

    /**
     * 获得数据约束名称
     *
     * @return 数据约束名称
     */
    String getName();

    /**
     * 设置数据约束名称
     *
     * @param name 数据约束名称
     */
    void setName(String name);

    /**
     * 获得数据约束作用表名
     *
     * @return 数据库中表名
     */
    String getTableName();

    /**
     * 设置数据约束作用表名
     *
     * @param tableName 数据库中表名
     */
    void setTableName(String tableName);

    /**
     * 获得数据约束具体 SQL
     *
     * @return SQL 片段
     */
    String getSql();

    /**
     * 设置数据约束具体 SQL
     *
     * @param sql SQL 片段
     */
    void setSql(String sql);

    /**
     * 获得 Entity 中定义的 Filter 名称
     *
     * @return filter 名称
     */
    String getFilterName();

    /**
     * 设置 Entity 中定义的 Filter 名称
     *
     * @param filterName Entity 中定义的 Filter 名称
     */
    void setFilterName(String filterName);

}
