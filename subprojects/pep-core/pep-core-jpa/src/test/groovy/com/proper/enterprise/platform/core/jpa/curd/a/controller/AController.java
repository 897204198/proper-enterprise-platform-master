package com.proper.enterprise.platform.core.jpa.curd.a.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.jpa.curd.a.service.AService;
import com.proper.enterprise.platform.core.jpa.curd.a.vo.AVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/a")
public class AController extends BaseController {

    @Autowired
    private AService aservice;

    @PostMapping
    public ResponseEntity<AVO> post(@RequestBody AVO avo) {
        return responseOfPost(aservice.save(avo), AVO.class);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String id) {
        return responseOfDelete(aservice.deleteById(id));
    }

    @PutMapping
    public ResponseEntity<AVO> put(@RequestBody AVO avo) {
        return responseOfPut(aservice.updateForSelective(avo), AVO.class);
    }

    @GetMapping
    @JsonView(AVO.Single.class)
    public ResponseEntity<?> get() {
        if (isPageSearch()) {
            return responseOfGet(aservice.findPage(), AVO.class, AVO.Single.class);
        }
        return responseOfGet(aservice.findAll(), AVO.class, AVO.Single.class);
    }

    @GetMapping(path = "/bs")
    @JsonView(AVO.WithB.class)
    public ResponseEntity<Collection<AVO>> getWithB() {
        return responseOfGet(aservice.findAll(), AVO.class);
    }

    @PostMapping(path = "/{aid}/b/{bid}")
    @JsonView(AVO.WithB.class)
    public ResponseEntity<AVO> addB(@PathVariable String aid, @PathVariable String bid) {
        return responseOfPost(aservice.addB(aid, bid), AVO.class);
    }

    @PostMapping(path = "/{aid}/c/{cid}")
    public ResponseEntity<AVO> addC(@PathVariable String aid, @PathVariable String cid) {
        return responseOfPost(aservice.addC(aid, cid), AVO.class);
    }

}
