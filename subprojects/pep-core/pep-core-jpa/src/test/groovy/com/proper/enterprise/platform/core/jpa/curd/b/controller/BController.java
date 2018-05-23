package com.proper.enterprise.platform.core.jpa.curd.b.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.jpa.curd.b.service.BService;
import com.proper.enterprise.platform.core.jpa.curd.b.vo.BVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/b")
public class BController extends BaseController {

    @Autowired
    private BService bservice;

    @PostMapping
    public ResponseEntity<BVO> post(@RequestBody BVO bvo) {
        return responseOfPost(bservice.save(bvo), BVO.class);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String id) {
        return responseOfDelete(bservice.deleteById(id));
    }

    @PutMapping
    public ResponseEntity<BVO> put(@RequestBody BVO bvo) {
        return responseOfPut(bservice.updateForSelective(bvo), BVO.class);
    }

    @GetMapping
    public ResponseEntity<?> get() {
        if (isPageSearch()) {
            return responseOfGet(bservice.findPage(), BVO.class);
        }
        return responseOfGet(bservice.findAll(), BVO.class);
    }
}
