package com.proper.enterprise.platform.template.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.template.service.TemplateService;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/templates")
public class TemplatesController extends TemplateController {

    public TemplatesController(TemplateService templateService) {
        super(templateService);
    }

    @Override
    @PostMapping
    @JsonView(TemplateVO.Details.class)
    public ResponseEntity<TemplateVO> add(@RequestBody TemplateVO templateVO) {
        templateVO.setMuti(true);
        return responseOfPost(templateService.save(templateVO));
    }

    @Override
    @PutMapping("/{id}")
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
    @JsonView(TemplateVO.Details.class)
    public ResponseEntity<TemplateVO> get(@PathVariable String id) {
        return responseOfGet(templateService.get(id));
    }

}
