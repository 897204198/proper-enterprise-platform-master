package com.proper.enterprise.platform.sys.datadic.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sys/datadic")
public class DataDicController extends BaseController {

    @Autowired
    DataDicService dataDicService;

    @PostMapping
    public ResponseEntity<DataDic> save(@RequestBody DataDicEntity dataDicEntity) {
        return responseOfPost(dataDicService.save(dataDicEntity));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DataDic> put(@PathVariable String id, @RequestBody DataDicEntity dataDicEntity) {
        dataDicEntity.setId(id);
        return responseOfPost(dataDicService.updateForSelective(dataDicEntity));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String ids) {
        return responseOfDelete(dataDicService.deleteByIds(ids));
    }

    @GetMapping
    public ResponseEntity<?> get(DataDicTypeEnum dataDicType) {
        return isPageSearch() ? responseOfGet(dataDicService.findPage(dataDicType))
            : responseOfGet(dataDicService.find(dataDicType));
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<DataDic> get(@PathVariable String id) {
        return responseOfGet(dataDicService.findOne(id));
    }

    @GetMapping(path = "/catalog/{catalog}")
    public ResponseEntity<?> get(@PathVariable String catalog, DataDicTypeEnum dataDicType,
                                 @RequestParam(defaultValue = "ENABLE") EnableEnum enable) {
        return responseOfGet(dataDicService.findByCatalog(catalog, dataDicType, enable));
    }

    @GetMapping(path = "/catalog/{catalog}/code/{code}")
    public ResponseEntity<?> get(@PathVariable String catalog, @PathVariable String code, DataDicTypeEnum dataDicType) {
        return responseOfGet(dataDicService.get(catalog, code, dataDicType));
    }

}
