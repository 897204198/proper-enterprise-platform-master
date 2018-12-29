package com.proper.enterprise.platform.oopsearch.config.repository;

import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.config.entity.SearchConfigEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 对SearchConfig进行操作的repository
 */
public interface SearchConfigRepository extends BaseJpaRepository<SearchConfigEntity, String> {

    /**
     * 根据数据库类型获取数据，并设置CacheQuery，确保在配置信息数据不变动的情况下，
     * 使用缓存查找数据。减少对db的连接使用次数，提高性能。
     *
     * @param dataBaseType 数据库类型
     * @return 配置信息集合
     */
    @CacheQuery
    List<SearchConfigEntity> findByDataBaseType(DataBaseType dataBaseType);

    /**
     * 根据模块名称，查找配置信息方法
     *
     * @param moduleName 模块名称
     * @return 配置信息集合
     */
    @CacheQuery
    List<SearchConfigEntity> findByModuleName(String moduleName);

    /**
     * 根据模块名称及数据库类型，查找配置信息方法
     *
     * @param moduleName 模块名称
     * @param dataBaseType 数据库类型
     * @return 配置信息集合
     */
    @CacheQuery
    List<SearchConfigEntity> findByModuleNameAndDataBaseType(String moduleName, DataBaseType dataBaseType);

    /**
     * 根据表名 查找配置
     *
     * @param tableName 表名
     * @return 配置集合
     */
    @CacheQuery
    List<SearchConfigEntity> findByTableName(String tableName);

    /**
     * 获取配置信息列表
     *
     * @param moduleName 模块名称
     * @param url 访问地址
     * @param tableName 表名
     * @param searchColumn 字段
     * @param columnAlias 字段别名
     * @param columnDesc 字段描述
     * @param configEnable 是否可用
     * @param pageable 分页信息
     * @return 配置信息列表
     */
    @Query("select c "
        + "from SearchConfigEntity c "
        + "where c.moduleName like ?1 "
        + "and c.url like ?2 "
        + "and c.tableName like ?3 "
        + "and c.searchColumn like ?4 "
        + "and c.columnAlias like ?5 "
        + "and c.columnDesc like ?6 "
        + "and (c.enable = ?7 or ?7 is null)")
    Page<SearchConfigEntity> findSearchConfigPagination(String moduleName, String url, String tableName,
                                                        String searchColumn, String columnAlias, String columnDesc,
                                                        Boolean configEnable, Pageable pageable);

    /**
     * 表名和字段名称和url唯一
     *
     * @param tableName    表名
     * @param searchColumn 字段名称
     * @param url          URL
     * @return 配置详情
     */
    SearchConfigEntity findByTableNameAndSearchColumnAndUrl(String tableName, String searchColumn, String url);

    /**
     * 表名和字段别名和url唯一
     *
     * @param tableName   表名
     * @param columnAlias 字段别名
     * @param url         url
     * @return 配置详情
     */
    SearchConfigEntity findByTableNameAndColumnAliasAndUrl(String tableName, String columnAlias, String url);
}
