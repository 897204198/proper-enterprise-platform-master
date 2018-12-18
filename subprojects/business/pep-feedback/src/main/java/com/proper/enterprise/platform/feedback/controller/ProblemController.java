package com.proper.enterprise.platform.feedback.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.feedback.entity.ProblemEntity;
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
@RequestMapping("/admin/problem")
@Api(tags = "/admin/problem")
public class ProblemController extends BaseController {

    @Autowired
    ProblemService problemService;

    @AuthcIgnore
    @GetMapping
    @ApiOperation("‍获取问题列表")
    public ResponseEntity<List<ProblemEntity>> findAll(@ApiParam(value = "‍分类id") String categoryId) {
        return responseOfGet(problemService.findByCategoryId(categoryId));

    }

    @PostMapping(path = "/add")
    @ApiOperation("‍增加问题")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity add(@RequestBody ProblemEntityVO reqMap) {
        String name = reqMap.getName();
        String answer = reqMap.getAnswer();
        String categoryId = reqMap.getId();
        problemService.addProblem(name, answer, categoryId);
        return responseOfPost(true);
    }

    @PutMapping(path = "modify/{id}")
    @ApiOperation("‍更新问题")
    public ResponseEntity modify(@ApiParam(value = "‍问题id", required = true) @PathVariable String id,
                                 @RequestBody ProblemEntityVO reqMap) {
        String name = reqMap.getName();
        String answer = reqMap.getAnswer();
        problemService.updateProblem(name, answer, id);
        return responseOfPut(true);
    }

    @DeleteMapping(path = "del")
    @ApiOperation("‍删除问题")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity del(@ApiParam(value = "‍问题id") String id) {
        problemService.delProblem(id);
        return responseOfDelete(true);
    }

    @DeleteMapping(path = "delAll")
    @ApiOperation("‍批量删除问题")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delAll(@ApiParam(value = "‍集合Ids") String ids) {
        problemService.delAllProblems(ids);
        return responseOfDelete(true);
    }

    @GetMapping(path = "/{id}")
    @ApiOperation("‍获取问题详情")
    public ResponseEntity<ProblemEntity> problemInfo(@ApiParam(value = "‍问题id", required = true) @PathVariable String id) {
        ProblemEntity problem = problemService.findProblemById(id);
        return responseOfGet(problem);
    }

    public static class ProblemEntityVO {

        @ApiModelProperty(name = "‍问题名称", required = true)
        private String name;

        @ApiModelProperty(name = "‍问题答案", required = true)
        private String answer;

        @ApiModelProperty(name = "‍id", required = true)
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }
}
