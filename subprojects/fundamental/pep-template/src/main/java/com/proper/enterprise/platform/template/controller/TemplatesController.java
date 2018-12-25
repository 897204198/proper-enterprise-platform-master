package com.proper.enterprise.platform.template.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.template.service.TemplateService;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "/templates")
@RequestMapping(path = "/templates")
public class TemplatesController extends TemplateController {

    public TemplatesController(TemplateService templateService) {
        super(templateService);
    }

    @Override
    @PostMapping
    @ApiOperation("‍保存模板")
    @JsonView(TemplateVO.Details.class)
    public ResponseEntity<TemplateVO> add(@RequestBody TemplateVO templateVO) {
        templateVO.setMuti(true);
        return responseOfPost(templateService.save(templateVO));
    }

    @Override
    @PutMapping("/{id}")
    @ApiOperation("‍修改模板")
    @JsonView(TemplateVO.Details.class)
    public ResponseEntity<TemplateVO> update(@PathVariable String id, @RequestBody TemplateVO templateVO) {
        if (StringUtil.isNotBlank(id)) {
            templateVO.setId(id);
            templateVO.setMuti(true);
            return responseOfPut(templateService.update(templateVO));
        }
        return responseOfPut(null);
    }

    @Override
    @GetMapping("/{id}")
    @ApiOperation("‍查询指定模板")
    @JsonView(TemplateVO.Details.class)
    public ResponseEntity<TemplateVO> get(@PathVariable String id) {
        return responseOfGet(templateService.get(id));
    }

    @Override
    @GetMapping
    @JsonView(TemplateVO.Details.class)
    @ApiOperation("‍获得模板分页信息")
    public ResponseEntity find(@RequestParam(defaultValue = "") String code,
                               @RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "") String description,
                               @RequestParam(defaultValue = "") String catalog,
                               @RequestParam(defaultValue = "") String enable) {
        return responseOfGet(templateService.findPagination(code, name, description, catalog, enable, true));
    }

}
