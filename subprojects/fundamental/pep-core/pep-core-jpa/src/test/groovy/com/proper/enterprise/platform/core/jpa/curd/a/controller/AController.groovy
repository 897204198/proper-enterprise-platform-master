package com.proper.enterprise.platform.core.jpa.curd.a.controller

import com.fasterxml.jackson.annotation.JsonView
import com.proper.enterprise.platform.core.controller.BaseController
import com.proper.enterprise.platform.core.jpa.curd.a.entity.AEntity
import com.proper.enterprise.platform.core.jpa.curd.a.service.AService
import com.proper.enterprise.platform.core.jpa.curd.a.vo.AVO
import com.proper.enterprise.platform.core.jpa.curd.b.api.B
import com.proper.enterprise.platform.core.jpa.curd.b.entity.BEntity
import com.proper.enterprise.platform.core.utils.BeanUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/a")
public class AController extends BaseController {

    @Autowired
    private AService aservice;

    @PostMapping
    public ResponseEntity<AVO> post(@RequestBody AVO avo) {
        return responseOfPost(aservice.save(BeanUtil.convert(avo, AEntity.class)), AVO.class);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String id) {
        return responseOfDelete(aservice.deleteById(id));
    }

    @PutMapping
    public ResponseEntity<AVO> put(@RequestBody AVO avo) {
        return responseOfPut(aservice.updateForSelective(BeanUtil.convert(avo, AEntity.class)), AVO.class);
    }

    @GetMapping
    @JsonView(AVO.Single.class)
    public ResponseEntity<?> get() {
        if (isPageSearch()) {
            return responseOfGet(aservice.findPage(), AVO.class);
        }
        return responseOfGet(aservice.findAll(), AVO.class);
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


    @PostMapping(path = "/b")
    public ResponseEntity<B> txReadOnlySaveB(@RequestBody BEntity b) {
        return responseOfPost(aservice.txReadOnly(b));
    }
}
