package com.proper.enterprise.platform.feedback.service.impl;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.feedback.entity.CategoryEntity;
import com.proper.enterprise.platform.feedback.repository.CategoryRepository;
import com.proper.enterprise.platform.feedback.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<CategoryEntity> findAll() {
        return categoryRepository.findByEnableOrderBySort(true);
    }

    @Override
    public void addCategory(String name, String icon, String pageUrl, String sort) {

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(name);
        categoryEntity.setIcon(icon);
        categoryEntity.setPageUrl(pageUrl);
        if (StringUtil.isEmpty(sort)) {
            sort = "0";
        }
        categoryEntity.setSort(sort);

        List<CategoryEntity> categoryEntities = categoryRepository.findBySorts(sort);
        if (CollectionUtil.isNotEmpty(categoryEntities)) {
            for (CategoryEntity category : categoryEntities) {
                String sortNum = category.getSort();
                sortNum = String.valueOf(Integer.parseInt(sortNum) + 1);
                category.setSort(sortNum);
                categoryRepository.save(category);

            }
        }

        categoryRepository.save(categoryEntity);
    }

    @Override
    public void updateCategory(String name, String icon, String pageUrl, String id) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(name);
        categoryEntity.setIcon(icon);
        categoryEntity.setPageUrl(pageUrl);
        categoryEntity.setId(id);
        categoryEntity.setEnable(true);
        categoryRepository.save(categoryEntity);
    }

    @Override
    public void deleteCategory(String id) {
        categoryRepository.deleteCategory(id);
    }


}
