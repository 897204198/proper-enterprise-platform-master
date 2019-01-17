package com.proper.enterprise.platform.core.jpa.curd.c.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.controller.BaseController
import com.proper.enterprise.platform.core.jpa.curd.c.entity.CEntity;
import com.proper.enterprise.platform.core.jpa.curd.c.service.CService;
import com.proper.enterprise.platform.core.jpa.curd.c.vo.CVO
import com.proper.enterprise.platform.core.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/c")
public class CController extends BaseController {

    @Autowired
    private CService cservice;

    @PostMapping
    public ResponseEntity<CVO> post(@RequestBody CVO cvo) {
        return responseOfPost(BeanUtil.convert(cservice.save(BeanUtil.convert(cvo, CEntity.class)), CVO.class));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String id) {
        return responseOfDelete(cservice.deleteById(id));
    }


    @PostMapping(path = "/{cid}/b/{bid}")
    public ResponseEntity<CVO> addC(@PathVariable String cid, @PathVariable String bid) {
        return responseOfPost(BeanUtil.convert(cservice.addB(cid, bid), CVO.class));
    }

    @GetMapping
    @JsonView(value = CVO.Single.class)
    public ResponseEntity<?> get() {
        return responseOfGet(BeanUtil.convert(cservice.findAll(), CVO.class));
    }

}
