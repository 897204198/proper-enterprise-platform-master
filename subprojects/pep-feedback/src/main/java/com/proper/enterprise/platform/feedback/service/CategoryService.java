package com.proper.enterprise.platform.feedback.service;

import com.proper.enterprise.platform.feedback.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {

    /**
     * 查询分类列表
     * @return 列表
     */
    List<CategoryEntity> findAll();

    /**
     * 新增
     * @param name 分类名称
     * @param icon 分类图标
     * @param pageUrl 跳转Url
     * @param sort 排序字段
     */
    void addCategory(String name, String icon, String pageUrl, String sort);

    /**
     * 修改
     * @param name 分类名称
     * @param icon 分类图标
     * @param pageUrl 跳转Url
     * @param id 分类id
     */
    void updateCategory(String name, String icon, String pageUrl, String id);

    /**
     * 删除
     * @param id 类别Id
     */
    void deleteCategory(String id);

}
