package com.proper.enterprise.platform.feedback.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.feedback.entity.CategoryEntity;
import com.proper.enterprise.platform.feedback.entity.ProblemEntity;
import com.proper.enterprise.platform.feedback.service.CategoryService;
import com.proper.enterprise.platform.feedback.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin/category")
public class CategoryController extends BaseController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProblemService problemService;

    @AuthcIgnore
    @GetMapping
    public ResponseEntity<List<CategoryEntity>> finaAll() {

        return responseOfGet(categoryService.findAll());
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Map<String, String> reqMap) {
        String name = reqMap.get("name");
        String icon = reqMap.get("icon");
        String pageUrl = reqMap.get("pageUrl");
        String sort = reqMap.get("sort");
        categoryService.addCategory(name, icon, pageUrl, sort);
        return responseOfPost(true);
    }

    @PutMapping(path = "/modify/{id}")
    public ResponseEntity modify(@PathVariable String id, @RequestBody Map<String, String> reqMap) {
        String name = reqMap.get("name");
        String icon = reqMap.get("icon");
        String pageUrl = reqMap.get("pageUrl");
        categoryService.updateCategory(name, icon, pageUrl, id);
        return responseOfPost(true);
    }

    @DeleteMapping(path = "/del")
    public ResponseEntity del(String id) {
        categoryService.deleteCategory(id);
        //该类别下的问题列表也要更新成不可用
        List<ProblemEntity> problems = problemService.findByCategoryId(id);
        for (ProblemEntity problem : problems) {
            problemService.delProblem(problem.getId());
        }
        return responseOfGet(true);
    }
}
