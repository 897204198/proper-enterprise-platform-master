package com.proper.enterprise.platform.auth.rule.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.auth.rule.vo.RuleVO;
import com.proper.enterprise.platform.auth.rule.service.RuleService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/rule")
@Api(tags = "/auth/rule")
public class RuleController extends BaseController {

    @Autowired
    private RuleService ruleService;

    @PostMapping
    @ApiOperation("‍新增rule")
    public ResponseEntity<RuleVO> post(@RequestBody RuleVO ruleVO) {
        return responseOfPost(ruleService.save(ruleVO));
    }

    @DeleteMapping
    @ApiOperation("‍删除rule")
    public ResponseEntity delete(@RequestParam String id) {
        return responseOfDelete(ruleService.deleteById(id));
    }

    @PutMapping
    @ApiOperation("‍修改rule")
    public ResponseEntity<RuleVO> put(@RequestBody RuleVO ruleVO) {
        return responseOfPut(ruleService.update(ruleVO));
    }

    @GetMapping
    @ApiOperation("‍查询rule列表")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk> get(@ApiParam(value = "‍规则编码", required = true) String code,
                                         @ApiParam(value = "‍规则名称", required = true) String name,
                                         @ApiParam(value = "‍规则类型", required = true) String type) {
        return isPageSearch() ? responseOfGet(ruleService.findAll(code, name, type, getPageRequest()))
            : responseOfGet(new DataTrunk<>(ruleService.findAll(code, name, type)));
    }
}
