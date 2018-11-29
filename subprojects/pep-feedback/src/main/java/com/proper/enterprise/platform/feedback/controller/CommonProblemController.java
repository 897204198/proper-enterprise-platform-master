package com.proper.enterprise.platform.feedback.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.feedback.entity.CategoryEntity;
import com.proper.enterprise.platform.feedback.entity.ProblemEntity;
import com.proper.enterprise.platform.feedback.service.CategoryService;
import com.proper.enterprise.platform.feedback.service.ProblemService;
import com.proper.enterprise.platform.feedback.vo.ProblemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/problem")
public class CommonProblemController extends BaseController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProblemService problemService;

    @AuthcIgnore
    @GetMapping
    public ResponseEntity<List<CategoryEntity>> getCategorys() {
        return responseOfGet(categoryService.findAll());
    }

    @AuthcIgnore
    @GetMapping(path = "/popular")
    public ResponseEntity<List<ProblemEntity>> populars() {

        return responseOfGet(problemService.getPopular(getPageRequest()));
    }

    @AuthcIgnore
    @GetMapping(path = "/info")
    public ResponseEntity<ProblemVo> problemInfo(String problemId, String deviceId) {

        return responseOfGet(problemService.saveProblemInfo(problemId, deviceId));
    }


    @GetMapping(path = "/assess")
    public ResponseEntity<ProblemVo> assess(String problemId, String deviceId, String code) {
        ProblemVo problem = problemService.saveAccessProblem(problemId, deviceId, code);
        return responseOfGet(problem);
    }


}
