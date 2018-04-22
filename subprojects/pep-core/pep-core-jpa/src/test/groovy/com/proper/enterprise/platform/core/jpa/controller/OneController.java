package com.proper.enterprise.platform.core.jpa.controller;

import com.proper.enterprise.platform.core.controller.BaseController;

import com.proper.enterprise.platform.core.jpa.entity.OneEntity;
import com.proper.enterprise.platform.core.jpa.service.OneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/one")
public class OneController extends BaseController {

    @Autowired
    private OneService oneService;

    @PostMapping
    public ResponseEntity<OneEntity> post(@RequestBody OneEntity oneEntity) {
        return responseOfPost(oneService.save(oneEntity));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String id) {
        return responseOfDelete(oneService.deleteById(id));
    }

    @PutMapping
    public ResponseEntity<OneEntity> put(@RequestBody OneEntity oneEntity) {
        return responseOfPut(oneService.save(oneEntity));
    }

    @GetMapping
    public ResponseEntity<?> get() {
        return responseOfGet(isPageSearch() ? oneService.findPage() : oneService.findAll());
    }
}
