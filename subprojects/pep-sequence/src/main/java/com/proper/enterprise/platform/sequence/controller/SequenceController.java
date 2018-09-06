package com.proper.enterprise.platform.sequence.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.sequence.vo.SequenceVO;
import com.proper.enterprise.platform.sequence.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sequence")
public class SequenceController extends BaseController {

    @Autowired
    private SequenceService sequenceService;

    @PostMapping
    public ResponseEntity<SequenceVO> post(@RequestBody SequenceVO sequenceVO) {
        return responseOfPost(sequenceService.save(sequenceVO));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String ids) {
        return responseOfDelete(sequenceService.deleteByIds(ids));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SequenceVO> put(@PathVariable String id, @RequestBody SequenceVO sequenceVO) {
        return responseOfPut(sequenceService.update(id, sequenceVO));
    }

    @GetMapping
    public ResponseEntity<?> get(String sequenceCode) {
        return isPageSearch() ? responseOfGet(sequenceService.findAll(sequenceCode, getPageRequest()))
            : responseOfGet(sequenceService.findAll(sequenceCode));
    }
}
