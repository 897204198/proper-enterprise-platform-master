package com.proper.enterprise.platform.core.jpa.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.jpa.entity.DateTimeEntity;
import com.proper.enterprise.platform.core.jpa.service.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/datetime")
public class DateTimeController extends BaseController {

    @Autowired
    private DateTimeService dateTimeService;

    @PostMapping
    public ResponseEntity<DateTimeEntity> post(@RequestBody DateTimeEntity dateTimeEntity) {
        return responseOfPost(dateTimeService.save(dateTimeEntity));
    }

    @GetMapping
    public ResponseEntity<?> get() {
        return responseOfGet(isPageSearch() ? dateTimeService.findPage() : dateTimeService.findAll());
    }
}
