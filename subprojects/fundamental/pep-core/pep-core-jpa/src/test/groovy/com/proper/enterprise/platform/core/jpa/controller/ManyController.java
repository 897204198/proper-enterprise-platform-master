package com.proper.enterprise.platform.core.jpa.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.jpa.entity.ManyEntity;
import com.proper.enterprise.platform.core.jpa.service.ManyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/many")
public class ManyController extends BaseController {

    @Autowired
    private ManyService manyService;

    @PostMapping
    public ResponseEntity<ManyEntity> post(@RequestBody ManyEntity manyEntity) {
        return responseOfPost(manyService.save(manyEntity));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String id) {
        return responseOfDelete(manyService.deleteById(id));
    }

    @PutMapping
    public ResponseEntity<ManyEntity> put(@RequestBody ManyEntity manyEntity) {
        return responseOfPut(manyService.save(manyEntity));
    }

    @GetMapping
    public ResponseEntity<?> get() {
        return responseOfGet(isPageSearch() ? manyService.findPage() : manyService.findAll());
    }
}
