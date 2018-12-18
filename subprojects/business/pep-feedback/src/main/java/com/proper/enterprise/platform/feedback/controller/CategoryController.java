package com.proper.enterprise.platform.feedback.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.feedback.entity.CategoryEntity;
import com.proper.enterprise.platform.feedback.entity.ProblemEntity;
import com.proper.enterprise.platform.feedback.service.CategoryService;
import com.proper.enterprise.platform.feedback.service.ProblemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api(tags = "/admin/category")
@RequestMapping("/admin/category")
public class CategoryController extends BaseController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProblemService problemService;

    @AuthcIgnore
    @GetMapping
    @ApiOperation("‍查询分类列表")
    public ResponseEntity<List<CategoryEntity>> finaAll() {
        return responseOfGet(categoryService.findAll());
    }

    @PostMapping
    @ApiOperation("‍新增分类列表")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity add(@RequestBody ProblemVO reqMap) {
        String name = reqMap.getName();
        String icon = reqMap.getIcon();
        String pageUrl = reqMap.getPageUrl();
        String sort = reqMap.getSort();
        categoryService.addCategory(name, icon, pageUrl, sort);
        return responseOfPost(true);
    }

    @PutMapping(path = "/modify/{id}")
    @ApiOperation("‍修改分类列表")
    public ResponseEntity modify(@ApiParam(value = "‍分类id", required = true) @PathVariable String id, @RequestBody ProblemVO reqMap) {
        String name = reqMap.getName();
        String icon = reqMap.getIcon();
        String pageUrl = reqMap.getPageUrl();
        categoryService.updateCategory(name, icon, pageUrl, id);
        return responseOfPost(true);
    }

    @DeleteMapping(path = "/del")
    @ApiOperation("‍删除分类列表")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity del(@ApiParam(value = "‍分类id") String id) {
        categoryService.deleteCategory(id);
        //该类别下的问题列表也要更新成不可用
        List<ProblemEntity> problems = problemService.findByCategoryId(id);
        for (ProblemEntity problem : problems) {
            problemService.delProblem(problem.getId());
        }
        return responseOfGet(true);
    }

    public static class ProblemVO {

        @ApiModelProperty(name = "‍分类名称", required = true)
        private String name;

        @ApiModelProperty(name = "‍跳转Url", required = true)
        private String pageUrl;

        @ApiModelProperty(name = "‍分类图标", required = true)
        private String icon;

        @ApiModelProperty(name = "‍排序字段", required = true)
        private String sort;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }
}
