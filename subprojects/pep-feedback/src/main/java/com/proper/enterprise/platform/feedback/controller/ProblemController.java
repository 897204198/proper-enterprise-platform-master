package com.proper.enterprise.platform.feedback.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.feedback.entity.ProblemEntity;
import com.proper.enterprise.platform.feedback.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin/problem")
public class ProblemController extends BaseController {

    @Autowired
    ProblemService problemService;

    @AuthcIgnore
    @GetMapping
    public ResponseEntity<List<ProblemEntity>> findAll(String categoryId) {

        return responseOfGet(problemService.findByCategoryId(categoryId));

    }

    @PostMapping(path = "/add")
    public ResponseEntity add(@RequestBody Map<String, String> reqMap) {
        String name = reqMap.get("name");
        String answer = reqMap.get("answer");
        String categoryId = reqMap.get("id");
        problemService.addProblem(name, answer, categoryId);
        return responseOfPost(true);
    }

    @PutMapping(path = "modify/{id}")
    public ResponseEntity modify(@PathVariable String id, @RequestBody Map<String, String> reqMap) {
        String name = reqMap.get("name");
        String answer = reqMap.get("answer");
        problemService.updateProblem(name, answer, id);
        return responseOfPut(true);
    }

    @DeleteMapping(path = "del")
    public ResponseEntity del(String id) {
        problemService.delProblem(id);
        return responseOfDelete(true);
    }

    @DeleteMapping(path = "delAll")
    public ResponseEntity delAll(String ids) {
        problemService.delAllProblems(ids);
        return responseOfDelete(true);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProblemEntity> problemInfo(@PathVariable String id) {
        ProblemEntity problem = problemService.findProblemById(id);
        return responseOfGet(problem);
    }

}
