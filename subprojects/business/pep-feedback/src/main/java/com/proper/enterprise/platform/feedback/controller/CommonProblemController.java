package com.proper.enterprise.platform.feedback.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.feedback.entity.CategoryEntity;
import com.proper.enterprise.platform.feedback.entity.ProblemEntity;
import com.proper.enterprise.platform.feedback.service.CategoryService;
import com.proper.enterprise.platform.feedback.service.ProblemService;
import com.proper.enterprise.platform.feedback.vo.ProblemVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "/problem")
@RequestMapping("/problem")
public class CommonProblemController extends BaseController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProblemService problemService;

    @AuthcIgnore
    @GetMapping
    @ApiOperation("‍查询分类列表")
    public ResponseEntity<List<CategoryEntity>> getCategorys() {
        return responseOfGet(categoryService.findAll());
    }

    @AuthcIgnore
    @GetMapping(path = "/popular")
    @ApiOperation("‍获取热门问题")
    public ResponseEntity<List<ProblemEntity>> populars() {
        return responseOfGet(problemService.getPopular(getPageRequest()));
    }

    @AuthcIgnore
    @GetMapping(path = "/info")
    @ApiOperation("‍获取问题详情")
    public ResponseEntity<ProblemVo> problemInfo(@ApiParam(value = "‍问题id") String problemId, @ApiParam(value = "‍设备id") String deviceId) {
        return responseOfGet(problemService.saveProblemInfo(problemId, deviceId));
    }


    @GetMapping(path = "/assess")
    @ApiOperation("‍问题评价")
    public ResponseEntity<ProblemVo> assess(@ApiParam(value = "‍问题id") String problemId, @ApiParam(value = "‍设备id") String deviceId,
                                            @ApiParam(value = "‍排序字段") String code) {
        ProblemVo problem = problemService.saveAccessProblem(problemId, deviceId, code);
        return responseOfGet(problem);
    }


}
