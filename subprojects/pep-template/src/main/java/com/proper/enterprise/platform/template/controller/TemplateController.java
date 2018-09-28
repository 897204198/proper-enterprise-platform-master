package com.proper.enterprise.platform.template.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.template.service.TemplateService;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/template")
public class TemplateController extends BaseController {

    public TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @AuthcIgnore
    @GetMapping(path = "/tips/{code}")
    public ResponseEntity<String> getTipInfo(@PathVariable String code) {
        return responseOfGet(templateService.getTips(code));
    }

    @PostMapping
    @JsonView(TemplateVO.Detail.class)
    public ResponseEntity<TemplateVO> add(@RequestBody TemplateVO templateVO) {
        templateVO.setMuti(false);
        return responseOfPost(templateService.save(templateVO));
    }

    @PutMapping("/{id}")
    @JsonView(TemplateVO.Detail.class)
    public ResponseEntity<TemplateVO> update(@PathVariable String id, @RequestBody TemplateVO templateVO) {
        if (StringUtil.isNotBlank(id)) {
            templateVO.setId(id);
            templateVO.setMuti(false);
            return responseOfPut(templateService.update(templateVO));
        }
        return responseOfPut(null);
    }

    @GetMapping("/{id}")
    @JsonView(TemplateVO.Detail.class)
    public ResponseEntity<TemplateVO> get(@PathVariable String id) {
        return responseOfGet(templateService.get(id));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String ids) {
        return responseOfDelete(templateService.deleteByIds(ids));
    }

    @GetMapping
    public ResponseEntity find(@RequestParam(defaultValue = "") String query) {
        return isPageSearch()
            ? responseOfGet(templateService.findPagination(query, getPageRequest())) :
            responseOfGet(templateService.findAll());
    }

}
