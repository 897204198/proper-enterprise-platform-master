package com.proper.enterprise.platform.oopsearch.config.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.oopsearch.api.vo.SearchConfigVO;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oopsearch/config")
public class SearchConfigController extends BaseController {

    private SearchConfigService searchConfigService;

    @Autowired
    public SearchConfigController(SearchConfigService searchConfigService) {
        this.searchConfigService = searchConfigService;
    }

    @GetMapping
    public ResponseEntity<DataTrunk<SearchConfigVO>> getSearchConfig(String name, String url, String tableName,
                                                     String searchColumn, String columnAlias, String columnDesc,
                                                     @RequestParam(defaultValue = "ENABLE") EnableEnum configEnable) {
        return responseOfGet(searchConfigService.findSearchConfigPagination(name, url, tableName,
            searchColumn, columnAlias, columnDesc, configEnable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SearchConfigVO> updateSearchConfig(@PathVariable String id, @RequestBody SearchConfigVO searchConfigVO) {
        return responseOfPut(searchConfigService.updateSearchConfig(id, searchConfigVO));
    }

    @PostMapping
    public ResponseEntity<SearchConfigVO> add(@RequestBody SearchConfigVO searchConfigVO) {
        return responseOfPost(searchConfigService.add(searchConfigVO));
    }

    @DeleteMapping
    public  ResponseEntity delete(@RequestParam String ids) {
        return responseOfDelete(searchConfigService.deleteByIds(ids));
    }
}
