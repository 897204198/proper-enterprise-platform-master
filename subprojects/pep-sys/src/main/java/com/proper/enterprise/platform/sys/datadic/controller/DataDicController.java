package com.proper.enterprise.platform.sys.datadic.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/sys/datadic")
public class DataDicController extends BaseController {

    private DataDicService dataDicService;

    @Autowired
    public DataDicController(DataDicService dataDicService) {
        this.dataDicService = dataDicService;
    }

    @PostMapping
    public ResponseEntity<DataDic> save(@RequestBody DataDicEntity dataDicEntity) {
        return responseOfPost(dataDicService.save(dataDicEntity));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DataDic> put(@PathVariable String id, @RequestBody DataDicEntity dataDicEntity) {
        dataDicEntity.setId(id);
        return responseOfPut(dataDicService.update(dataDicEntity));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String ids) {
        return responseOfDelete(dataDicService.deleteByIds(ids));
    }

    @GetMapping
    public ResponseEntity<DataTrunk> get(String catalog, String code, String name, DataDicTypeEnum dataDicType, EnableEnum enable) {
        if (isPageSearch()) {
            return responseOfGet(dataDicService.findPage(catalog, code, name, dataDicType, enable));
        } else {
            Collection collection = dataDicService.find(catalog, code, name, dataDicType, enable);
            DataTrunk dataTrunk = new DataTrunk();
            dataTrunk.setData(collection);
            dataTrunk.setCount(collection.size());
            return responseOfGet(dataTrunk);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DataDic> get(@PathVariable String id) {
        return responseOfGet(dataDicService.findById(id));
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
