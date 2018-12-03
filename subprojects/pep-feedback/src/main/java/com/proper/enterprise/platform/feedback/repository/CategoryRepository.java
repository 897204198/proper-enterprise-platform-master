package com.proper.enterprise.platform.feedback.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.feedback.entity.CategoryEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends BaseRepository<CategoryEntity, String> {

    /**
     * 查询分类列表
     * @param enable 可用
     * @return 分类列表
     */
    List<CategoryEntity> findByEnableOrderBySort(boolean enable);

    /**
     * 修改状态
     * @param id 分类id
     */
    @Modifying
    @Query("update CategoryEntity c set c.enable = false where c.id = :id")
    void deleteCategory(@Param("id") String id);

    /**
     * 更新
     * @param name 分类名称
     * @param icon 图标
     * @param pageUrl 跳转地址
     * @param id 分类id
     */
    @Modifying
    @Query("update CategoryEntity c set c.name = :name,c.icon = :icon,c.pageUrl = :pageUrl where c.id = :id")
    void updateCategory(@Param("name") String name,
                        @Param("icon") String icon,
                        @Param("pageUrl") String pageUrl,
                        @Param("id") String id);

    /**
     *  排序
     * @param sort 排序字段
     * @return 获取排序列表
     */
    @Query("select c from CategoryEntity c where c.sort >= :sort")
    List<CategoryEntity> findBySorts(@Param("sort") String sort);
}
