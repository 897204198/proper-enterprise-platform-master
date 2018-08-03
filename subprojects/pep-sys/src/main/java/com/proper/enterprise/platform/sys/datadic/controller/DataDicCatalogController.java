package com.proper.enterprise.platform.sys.datadic.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import com.proper.enterprise.platform.sys.datadic.service.DataDicCatalogService;
import com.proper.enterprise.platform.sys.datadic.vo.DataDicCatalogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sys/datadic/catalog")
public class DataDicCatalogController extends BaseController {

    private DataDicCatalogService dataDicCatalogService;

    @Autowired
    public DataDicCatalogController(DataDicCatalogService dataDicCatalogService) {
        this.dataDicCatalogService = dataDicCatalogService;
    }

    @PostMapping
    public ResponseEntity<DataDicCatalogVO> save(@RequestBody DataDicCatalogVO dataDicCatalogVO) {
        return responseOfPost(dataDicCatalogService.save(dataDicCatalogVO));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<DataDicCatalogVO> put(@PathVariable String id, @RequestBody DataDicCatalogVO dataDicCatalogVO) {
        dataDicCatalogVO.setId(id);
        return responseOfPut(dataDicCatalogService.update(dataDicCatalogVO));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String ids) {
        return responseOfDelete(dataDicCatalogService.deleteByIds(ids));
    }

    @GetMapping
    public ResponseEntity<?> get(String catalogCode, String catalogName, DataDicTypeEnum catalogType, EnableEnum enable) {
        return isPageSearch() ? responseOfGet(dataDicCatalogService.findPage(catalogCode, catalogName,
            catalogType, enable, getPageRequest()))
            : responseOfGet(dataDicCatalogService.findAll(catalogCode, catalogName, catalogType, enable));
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<DataDicCatalogVO> get(@PathVariable String id) {
        return responseOfGet(dataDicCatalogService.get(id));
    }
}
