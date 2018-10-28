package com.proper.enterprise.platform.dev.tools.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.app.service.ApplicationService;
import com.proper.enterprise.platform.app.vo.AppCatalogVO;
import com.proper.enterprise.platform.app.vo.ApplicationVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "/admin/app/applications")
@RequestMapping(value = "/admin/app/applications")
public class ApplicationController extends BaseController {

    private ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PutMapping("/{appId}")
    @ApiOperation("‍修改应用‍")
    public ResponseEntity<ApplicationVO> updateApplication(@ApiParam(value = "‍应用id", required = true) @PathVariable String appId,
                                                           @RequestBody AdminApplicationVO adminApplicationVO) {
        ApplicationVO applicationVO = new ApplicationVO();
        BeanUtils.copyProperties(adminApplicationVO, applicationVO);
        return responseOfPut(applicationService.updateApplication(appId, applicationVO));
    }

    @PostMapping
    @ApiOperation("‍保存应用‍")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApplicationVO> postApplication(@RequestBody AdminApplicationVO adminApplicationVO) {
        ApplicationVO applicationVO = new ApplicationVO();
        BeanUtils.copyProperties(adminApplicationVO, applicationVO);
        return responseOfPost(applicationService.addApplication(applicationVO));
    }

    @DeleteMapping
    @ApiOperation("‍删除应用‍")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiImplicitParam(name = "ids", value = "‍应用Id列表(使用\",\"分割)", required = true, dataType = "String", paramType = "query")
    public ResponseEntity deleteApplications(@RequestParam String ids) {
        return responseOfDelete(applicationService.deleteByIds(ids));
    }

    @GetMapping
    @ApiOperation("‍获取应用（传参数根据参数取应用，不传获取所有）‍")
    public ResponseEntity<DataTrunk> find(@ApiParam("‍‍类别id") @RequestParam(defaultValue = "") String code,
                                          @ApiParam("‍‍应用名称") @RequestParam(defaultValue = "") String applicationName,
                                          @ApiParam("‍应用页") @RequestParam(defaultValue = "") String applicationPage) {
        if (isPageSearch()) {
            return responseOfGet(applicationService.findPagination(code, applicationName, applicationPage));
        } else {
            List<ApplicationVO> list = applicationService.getAllOrApplication(code);
            DataTrunk<ApplicationVO> dataTrunk = new DataTrunk();
            dataTrunk.setData(list);
            dataTrunk.setCount(list.size());
            return responseOfGet(dataTrunk);
        }
    }

    @GetMapping("/{appId}")
    @ApiOperation("‍根据appId获取应用‍")
    public ResponseEntity<ApplicationVO> findApplication(@ApiParam(value = "‍应用id", required = true) @PathVariable String appId) {
        return responseOfGet(applicationService.getApplication(appId));
    }

    @GetMapping("/catalogs")
    @ApiOperation("‍获取应用的类别‍")
    @JsonView(AppCatalogVO.Single.class)
    public ResponseEntity<List<AppCatalogVO>> findCatalogs() {
        return responseOfGet(applicationService.getCatalogs());
    }

    @PutMapping("/catalogs/{code}")
    @ApiOperation("‍修改应用的类别名称‍")
    @JsonView(AppCatalogVO.Single.class)
    public ResponseEntity<AppCatalogVO> putCatalog(@ApiParam(value = "‍应用类别编码", required = true) @PathVariable String code,
                                                   @ApiParam(value = "‍应用类别名称") @RequestParam String typeName) {
        return responseOfGet(applicationService.updateCatalog(code, typeName));
    }

    @PostMapping("/catalogs")
    @ApiOperation("‍保存应用类别‍")
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(AppCatalogVO.Single.class)
    public ResponseEntity<AppCatalogVO> postCatalog(@RequestBody AdminAppCatalogVO adminAppCatalogVO) {
        AppCatalogVO appCatalogVO = new AppCatalogVO();
        BeanUtils.copyProperties(adminAppCatalogVO, appCatalogVO);
        return responseOfPost(applicationService.addCatalog(appCatalogVO));
    }

    @GetMapping("/catalogs/{code}")
    @ApiOperation("‍根据code获取应用的类别信息‍")
    @JsonView(AppCatalogVO.Single.class)
    public ResponseEntity<AppCatalogVO> findCatalog(@ApiParam(value = "‍应用类别编码", required = true) @PathVariable String code) {
        return responseOfGet(applicationService.getCatalog(code));
    }

    @DeleteMapping("/catalogs/{code}")
    @ApiOperation("‍删除应用类别‍")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteCatalog(@ApiParam(value = "‍应用类别编码", required = true) @PathVariable String code) {
        AppCatalogVO typeCode = applicationService.getCatalog(code);
        if (typeCode == null) {
            return responseOfDelete(false);
        }
        applicationService.deleteByCode(code);
        return responseOfDelete(true);
    }

    public static class AdminApplicationVO {

        @ApiModelProperty(name = "‍应用名称", required = true)
        private String name;

        @ApiModelProperty(name = "‍应用跳转页面", required = true)
        private String page;

        @ApiModelProperty(name = "‍应用图标", required = true)
        private String icon;

        @ApiModelProperty(name = "‍跳转页面类型")
        private String style;

        @ApiModelProperty(name = "‍应用页面初始化参数，满足json格式")
        private Map data;

        @ApiModelProperty(name = "‍应用类别编码", required = true)
        private String code;

        @ApiModelProperty(name = "‍是否为默认值")
        private Boolean defaultValue;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Map getData() {
            return data;
        }

        public void setData(Map data) {
            this.data = data;
        }

        public Boolean getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(Boolean defaultValue) {
            this.defaultValue = defaultValue;
        }


        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }

    public static class AdminAppCatalogVO {

        @ApiModelProperty(name = "‍应用类别编码", required = true)
        private String code;

        @ApiModelProperty(name = "‍应用类别名称", required = true)
        private String typeName;

        @ApiModelProperty(name = "‍应用顺序", required = true)
        private String sort;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }
}
