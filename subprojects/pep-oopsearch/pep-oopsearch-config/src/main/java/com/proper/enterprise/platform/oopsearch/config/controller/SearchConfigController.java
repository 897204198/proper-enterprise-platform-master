package com.proper.enterprise.platform.oopsearch.config.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.oopsearch.api.vo.SearchConfigVO;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oopsearch/config")
@Api(tags = "/oopsearch/config")
public class SearchConfigController extends BaseController {

    private SearchConfigService searchConfigService;

    @Autowired
    public SearchConfigController(SearchConfigService searchConfigService) {
        this.searchConfigService = searchConfigService;
    }

    @GetMapping
    @ApiOperation("‍取得OopSearch配置信息列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<SearchConfigVO>> getSearchConfig(@ApiParam("‍模块名称‍") String name, @ApiParam("‍路径‍") String url,
                                                                     @ApiParam("‍表名‍") String tableName, @ApiParam("‍字段名称‍") String searchColumn,
                                                                     @ApiParam("‍别名‍") String columnAlias, @ApiParam("‍字段的描述‍") String columnDesc,
                                                             @ApiParam("‍是否启用‍") @RequestParam(defaultValue = "ENABLE") EnableEnum configEnable) {
        return responseOfGet(searchConfigService.findSearchConfigPagination(name, url, tableName,
            searchColumn, columnAlias, columnDesc, configEnable));
    }

    @PutMapping("/{id}")
    @ApiOperation("‍修改OopSearch配置信息")
    public ResponseEntity<SearchConfigVO> updateSearchConfig(@ApiParam(value = "‍信息ID", required = true) @PathVariable String id,
                                                             @RequestBody SearchConfigModelVO searchConfigModelVO) {
        SearchConfigVO searchConfigVO = new SearchConfigVO();
        BeanUtils.copyProperties(searchConfigModelVO, searchConfigVO);
        return responseOfPut(searchConfigService.updateSearchConfig(id, searchConfigVO));
    }

    @PostMapping
    @ApiOperation("‍新增OopSearch配置信息")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SearchConfigVO> add(@RequestBody SearchConfigModelVO searchConfigModelVO) {
        SearchConfigVO searchConfigVO = new SearchConfigVO();
        BeanUtils.copyProperties(searchConfigModelVO, searchConfigVO);
        return responseOfPost(searchConfigService.add(searchConfigVO));
    }

    @DeleteMapping
    @ApiOperation("‍删除OopSearch配置信息")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  ResponseEntity delete(@ApiParam("‍信息Id列表(使用\",\"分割)") @RequestParam String ids) {
        return responseOfDelete(searchConfigService.deleteByIds(ids));
    }

    public static class SearchConfigModelVO {

        @ApiModelProperty("‍模块名称")
        private String moduleName;

        @ApiModelProperty("‍表名称")
        private String tableName;

        @ApiModelProperty("‍字段名称")
        private String searchColumn;

        @ApiModelProperty("‍别名")
        private String columnAlias;

        @ApiModelProperty("‍描述")
        private String columnDesc;

        @ApiModelProperty("‍url")
        private String url;

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getSearchColumn() {
            return searchColumn;
        }

        public void setSearchColumn(String searchColumn) {
            this.searchColumn = searchColumn;
        }

        public String getColumnAlias() {
            return columnAlias;
        }

        public void setColumnAlias(String columnAlias) {
            this.columnAlias = columnAlias;
        }

        public String getColumnDesc() {
            return columnDesc;
        }

        public void setColumnDesc(String columnDesc) {
            this.columnDesc = columnDesc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }

    }
}
