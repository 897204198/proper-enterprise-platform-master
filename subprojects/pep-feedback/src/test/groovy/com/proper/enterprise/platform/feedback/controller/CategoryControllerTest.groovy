package com.proper.enterprise.platform.feedback.controller

import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.feedback.entity.CategoryEntity
import com.proper.enterprise.platform.feedback.repository.CategoryRepository
import com.proper.enterprise.platform.feedback.service.CategoryService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MvcResult

class CategoryControllerTest extends AbstractTest{

    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    @Before
    void saveData() {

        categoryService.addCategory("icon0", "name0", "www.baidu.com", "")
        categoryService.addCategory("icon1", "name1", "www.baidu.com", "0")
        categoryService.addCategory("icon2", "name2", "www.baidu.com", "1")
        categoryService.addCategory("icon3", "name3", "www.baidu.com", "2")
        categoryService.addCategory("icon4", "name4", "www.baidu.com", "3")
        categoryService.addCategory("icon5", "name5", "www.baidu.com", "4")
        categoryService.addCategory("icon6", "name6", "www.baidu.com", "2")
    }

    @Test
    void testCurd() {

        MvcResult result = get("/admin/category", HttpStatus.OK)
        String resultContent = result.getResponse().getContentAsString()
        List<Map<String, Object>> list = (List<CategoryEntity>) JSONUtil.parse(resultContent, Object.class)
        assert 7 == list.size()

        String id = list.get(0).get("id")
        categoryService.updateCategory("totile", "select", "www.sohu.com", id)
        MvcResult updateResult = get("/admin/category", HttpStatus.OK)
        String updateContent = updateResult.getResponse().getContentAsString()
        List<Map<String, Object>> updatelist = (List<CategoryEntity>) JSONUtil.parse(updateContent, Object.class)
        assert "select" == updatelist.get(0).get("icon")
        assert "totile" == updatelist.get(0).get("name")

        categoryService.deleteCategory(updatelist.get(0).get("id"))
        MvcResult delResult = get("/admin/category", HttpStatus.OK)
        String delContent = delResult.getResponse().getContentAsString()
        List<Map<String, Object>> dellist = (List<CategoryEntity>) JSONUtil.parse(delContent, Object.class)
        assert dellist.size() == 6
    }

    @Test
    void testAdd() {
        post("/admin/category", '{"name": "test", "icon": "测试", "pageUrl": "this is test value!"}', HttpStatus.CREATED);

    }

    @After
    void clearData() {

        categoryRepository.deleteAll();

    }

}
