package com.proper.enterprise.platform.sys.datadic.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import com.proper.enterprise.platform.sys.datadic.service.DataDicCatalogService;
import com.proper.enterprise.platform.sys.datadic.vo.DataDicCatalogVO;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/sys/datadic/catalog")
@Api(tags = "/sys/datadic/catalog")
public class DataDicCatalogController extends BaseController {

    private DataDicCatalogService dataDicCatalogService;

    @Autowired
    public DataDicCatalogController(DataDicCatalogService dataDicCatalogService) {
        this.dataDicCatalogService = dataDicCatalogService;
    }

    @PostMapping
    @ApiOperation("‍新增数据字典项信息")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DataDicCatalogVO> save(@RequestBody DataDicCatalogModelVO dataDicCatalogModelVO) {
        DataDicCatalogVO dataDicCatalogVO = new DataDicCatalogVO();
        BeanUtils.copyProperties(dataDicCatalogModelVO, dataDicCatalogVO);
        return responseOfPost(dataDicCatalogService.save(dataDicCatalogVO));
    }

    @PutMapping(path = "/{id}")
    @ApiOperation("‍修改数据字典项信息")
    public ResponseEntity<DataDicCatalogVO> put(@ApiParam(value = "id", required = true) @PathVariable String id,
                                                @RequestBody DataDicCatalogModelVO dataDicCatalogModelVO) {
        DataDicCatalogVO dataDicCatalogVO = new DataDicCatalogVO();
        BeanUtils.copyProperties(dataDicCatalogModelVO, dataDicCatalogVO);
        dataDicCatalogVO.setId(id);
        return responseOfPut(dataDicCatalogService.update(dataDicCatalogVO));
    }

    @DeleteMapping
    @ApiOperation("‍删除单条信息或多条信息")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiImplicitParam(name = "ids", value = "‍信息Id列表(使用\",\"分割)", required = true, dataType = "String", paramType = "query")
    public ResponseEntity delete(@RequestParam String ids) {
        return responseOfDelete(dataDicCatalogService.deleteByIds(ids));
    }

    @GetMapping
    @ApiOperation("‍取得数据字典信息列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk> get(String catalogCode, String catalogName, DataDicTypeEnum catalogType, EnableEnum enable) {
        if (isPageSearch()) {
            return responseOfGet(dataDicCatalogService.findPage(catalogCode, catalogName, catalogType, enable, getPageRequest()));
        } else {
            Collection<DataDicCatalogVO> collection = dataDicCatalogService.findAll(catalogCode, catalogName, catalogType, enable);
            DataTrunk<DataDicCatalogVO> dataTrunk = new DataTrunk<>();
            dataTrunk.setCount(collection.size());
            dataTrunk.setData(collection);
            return responseOfGet(dataTrunk);
        }
    }

    @GetMapping(path = "/id/{id}")
    @ApiOperation("‍根据id获得数据字典节点")
    public ResponseEntity<DataDicCatalogVO> get(@ApiParam(value = "id", required = true) @PathVariable String id) {
        return responseOfGet(dataDicCatalogService.get(id));
    }

    public static class DataDicCatalogModelVO {

        @ApiModelProperty(name = "‍数据字典项编码", required = true)
        private String catalogCode;

        @ApiModelProperty(name = "‍数据字典项名称", required = true)
        private String catalogName;

        @ApiModelProperty(name = "‍‍数据字典项类型", required = true)
        private String catalogType;

        @ApiModelProperty(name = "‍排序", required = true)
        private Integer sort;

        public String getCatalogCode() {
            return catalogCode;
        }

        public void setCatalogCode(String catalogCode) {
            this.catalogCode = catalogCode;
        }

        public String getCatalogName() {
            return catalogName;
        }

        public void setCatalogName(String catalogName) {
            this.catalogName = catalogName;
        }

        public String getCatalogType() {
            return catalogType;
        }

        public void setCatalogType(String catalogType) {
            this.catalogType = catalogType;
        }

        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }
}
