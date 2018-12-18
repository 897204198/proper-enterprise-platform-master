package com.proper.enterprise.platform.template.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.template.service.TemplateService;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "/template")
@RequestMapping(path = "/template")
public class TemplateController extends BaseController {

    public TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @AuthcIgnore
    @GetMapping(path = "/tips/{code}")
    @ApiOperation("‍获得纯文本类型的正文")
    public ResponseEntity<String> getTipInfo(@ApiParam(value = "‍关键字", required = true) @PathVariable String code) {
        return responseOfGet(templateService.getTips(code));
    }

    @PostMapping
    @ApiOperation("‍保存模板")
    @JsonView(TemplateVO.Detail.class)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TemplateVO> add(@RequestBody TemplateVO templateVO) {
        templateVO.setMuti(false);
        return responseOfPost(templateService.save(templateVO));
    }

    @PutMapping("/{id}")
    @ApiOperation("‍修改模板")
    @JsonView(TemplateVO.Detail.class)
    public ResponseEntity<TemplateVO> update(@ApiParam(value = "‍id", required = true) @PathVariable String id, @RequestBody TemplateVO templateVO) {
        if (StringUtil.isNotBlank(id)) {
            templateVO.setId(id);
            templateVO.setMuti(false);
            return responseOfPut(templateService.update(templateVO));
        }
        return responseOfPut(null);
    }

    @GetMapping("/{id}")
    @ApiOperation("‍查询指定模板")
    @JsonView(TemplateVO.Detail.class)
    public ResponseEntity<TemplateVO> get(@ApiParam(value = "‍主键id", required = true) @PathVariable String id) {
        return responseOfGet(templateService.get(id));
    }

    @DeleteMapping
    @ApiOperation("‍批量删除")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@ApiParam(value = "‍主键集合ids", required = true) @RequestParam String ids) {
        return responseOfDelete(templateService.deleteByIds(ids));
    }

    @GetMapping
    @ApiOperation("‍获得模板分页信息")
    @JsonView(TemplateVO.Detail.class)
    public ResponseEntity find(@ApiParam(value = "‍关键字") @RequestParam(defaultValue = "") String code,
                               @ApiParam(value = "‍名称") @RequestParam(defaultValue = "") String name,
                               @ApiParam(value = "‍解释") @RequestParam(defaultValue = "") String description,
                               @ApiParam(value = "‍类别") @RequestParam(defaultValue = "") String catalog,
                               @ApiParam(value = "‍启用停用") @RequestParam(defaultValue = "") String enable) {
        return responseOfGet(templateService.findPagination(code, name, description, catalog, enable, false));
    }

}
