package com.proper.enterprise.platform.sys.datadic.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.sys.datadic.entity.DataDicCatalogEntity;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataDicCatalogRepository extends BaseJpaRepository<DataDicCatalogEntity, String> {

    /**
     * 查询接口
     *
     * @param catalogCode 字典项code
     * @param catalogName 字典项Name
     * @param catalogType 字典项类型
     * @param enable      是否启用
     * @return 字典项集合
     */
    @Query(value = "SELECT c FROM DataDicCatalogEntity c where"
        + " (catalogCode=:catalogCode or :catalogCode is null)"
        + " and (catalogName=:catalogName or :catalogName is null)"
        + " and (catalogType=:catalogType or :catalogType is null)"
        + " and (enable=:enable or :enable is null)"
        + " and (enable=:enable or :enable is null)"
        + " order by sort asc")
    List<DataDicCatalogEntity> findAll(@Param("catalogCode") String catalogCode, @Param("catalogName") String catalogName,
                                       @Param("catalogType") DataDicTypeEnum catalogType, @Param("enable") Boolean enable);

    /**
     * 查询分页接口
     *
     * @param catalogCode 字典项code
     * @param catalogName 字典项Name
     * @param catalogType 字典项类型
     * @param enable      是否启用
     * @param pageable    分页参数
     * @return 字典项集合
     */
    @Query(value = "SELECT c FROM DataDicCatalogEntity c where"
        + " (catalogCode=:catalogCode or :catalogCode is null)"
        + " and (catalogName=:catalogName or :catalogName is null)"
        + " and (catalogType=:catalogType or :catalogType is null)"
        + " and (enable=:enable or :enable is null)"
        + " order by sort asc")
    Page<DataDicCatalogEntity> findAll(@Param("catalogCode") String catalogCode, @Param("catalogName") String catalogName,
                                       @Param("catalogType") DataDicTypeEnum catalogType, @Param("enable") Boolean enable, Pageable pageable);


    /**
     * 根据字典项code获取字典项
     *
     * @param catalogCode 字典项
     * @return 字典项
     */
    DataDicCatalogEntity findByCatalogCode(String catalogCode);
}
