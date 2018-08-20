package com.proper.enterprise.platform.notice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.notice.service.TemplateService;
import com.proper.enterprise.platform.notice.vo.TemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/notice/template")
public class TemplateController extends BaseController {

    @Autowired
    TemplateService templateService;

    @AuthcIgnore
    @GetMapping(path = "/tips/{code}")
    public ResponseEntity<String> getTipInfo(@PathVariable String code) {
        return responseOfGet(templateService.getTips(code));
    }

    @PostMapping
    @JsonView(TemplateVO.Detail.class)
    public ResponseEntity<TemplateVO> add(@RequestBody TemplateVO templateVO) {
        return responseOfPost(templateService.save(templateVO));
    }

    @PutMapping("/{id}")
    @JsonView(TemplateVO.Detail.class)
    public ResponseEntity<TemplateVO> update(@PathVariable String id, @RequestBody TemplateVO templateVO) {
        if (StringUtil.isNotBlank(id)) {
            templateVO.setId(id);
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
    public ResponseEntity find(@RequestParam(defaultValue = "") String code,
                               @RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "") String title,
                               @RequestParam(defaultValue = "") String template,
                               @RequestParam(defaultValue = "") String description) {
        return isPageSearch()
            ? responseOfGet(templateService.findPagination(code, name, title, template, description)) :
            responseOfGet(templateService.findAll());
    }

}
