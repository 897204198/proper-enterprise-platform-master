package com.proper.enterprise.platform.sys.datadic.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/sys/datadic")
@Api(tags = "/sys/datadic")
public class DataDicController extends BaseController {

    private DataDicService dataDicService;

    @Autowired
    public DataDicController(DataDicService dataDicService) {
        this.dataDicService = dataDicService;
    }

    @PostMapping
    @ApiOperation("‍新增数据字典信息")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DataDic> save(@RequestBody DataDicEntityVO dataDicEntityVO) {
        DataDicEntity dataDicEntity = new DataDicEntity();
        BeanUtils.copyProperties(dataDicEntityVO, dataDicEntity);
        return responseOfPost(dataDicService.save(dataDicEntity));
    }

    @PutMapping(path = "/{id}")
    @ApiOperation("‍修改数据字典信息")
    public ResponseEntity<DataDic> put(@ApiParam(value = "id", required = true) @PathVariable String id,
                                       @RequestBody DataDicEntityVO dataDicEntityVO) {
        DataDicEntity dataDicEntity = new DataDicEntity();
        BeanUtils.copyProperties(dataDicEntityVO, dataDicEntity);
        dataDicEntity.setId(id);
        return responseOfPut(dataDicService.update(dataDicEntity));
    }

    @DeleteMapping
    @ApiOperation("‍删除单条信息或多条信息")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiImplicitParam(name = "ids", value = "‍信息Id列表(使用\",\"分割)", required = true, dataType = "String", paramType = "query")
    public ResponseEntity delete(@RequestParam String ids) {
        return responseOfDelete(dataDicService.deleteByIds(ids));
    }

    @GetMapping
    @ApiOperation("‍取得数据字典信息列表")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    @SuppressWarnings("unchecked")
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
    @ApiOperation("‍获取单条详细信息")
    public ResponseEntity<DataDic> get(@ApiParam(value = "id", required = true) @PathVariable String id) {
        return responseOfGet(dataDicService.findById(id));
    }

    @GetMapping(path = "/catalog/{catalog}")
    @ApiOperation("‍根据分类获得分类下的数据字典集合")
    public ResponseEntity<?> get(@ApiParam(value = "‍应用id", required = true) @PathVariable String catalog,
                                 @ApiParam("‍‍字典类型") DataDicTypeEnum dataDicType,
                                 @ApiParam("‍启用停用") @RequestParam(defaultValue = "ENABLE") EnableEnum enable) {
        return responseOfGet(dataDicService.findByCatalog(catalog, dataDicType, enable));
    }

    @GetMapping(path = "/catalog/{catalog}/code/{code}")
    @ApiOperation("‍根据分类及编码获得数据字典")
    public ResponseEntity<?> get(@ApiParam(value = "‍应用id", required = true) @PathVariable String catalog,
                                 @ApiParam(value = "‍‍编码", required = true) @PathVariable String code,
                                 @ApiParam("‍‍字典类型") DataDicTypeEnum dataDicType) {
        return responseOfGet(dataDicService.get(catalog, code, dataDicType));
    }

    public static class DataDicEntityVO {

        @ApiModelProperty(name = "‍数据字典项", required = true)
        private String catalog;

        @ApiModelProperty(name = "‍数据字典值", required = true)
        private String code;

        @ApiModelProperty(name = "‍数据名称", required = true)
        private String name;

        @ApiModelProperty(name = "‍排序", required = true)
        private Integer order;

        @ApiModelProperty(name = "‍是否为默认", required = true)
        private Boolean deft;

        @ApiModelProperty(name = "‍是否为系统字典", required = true)
        private DataDicTypeEnum dataDicType;

        public String getCatalog() {
            return catalog;
        }

        public void setCatalog(String catalog) {
            this.catalog = catalog;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public Boolean getDeft() {
            return deft;
        }

        public void setDeft(Boolean deft) {
            this.deft = deft;
        }

        public DataDicTypeEnum getDataDicType() {
            return dataDicType;
        }

        public void setDataDicType(DataDicTypeEnum dataDicType) {
            this.dataDicType = dataDicType;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }
}
