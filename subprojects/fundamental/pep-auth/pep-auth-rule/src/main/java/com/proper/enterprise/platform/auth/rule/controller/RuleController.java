package com.proper.enterprise.platform.auth.rule.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.auth.rule.vo.RuleVO;
import com.proper.enterprise.platform.auth.rule.service.RuleService;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
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

    private static final String RULE_CATALOG = "RULE";

    @PostMapping
    @ApiOperation("‍新增rule")
    public ResponseEntity<RuleVO> post(@RequestBody RuleVO ruleVO) {
        return responseOfPost(ruleService.save(ruleVO));
    }

    @DeleteMapping
    @ApiOperation("‍删除rule")
    public ResponseEntity delete(@ApiParam(value = "‍id集合", required = true) @RequestParam(required = true) String ids) {
        return responseOfDelete(ruleService.deleteByIds(ids));
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
        return isPageSearch() ? responseOfGet(ruleService.findAll(code, name, StringUtil.isEmpty(type)
            ? null
            : new DataDicLiteBean(RULE_CATALOG, type), getPageRequest()))
            : responseOfGet(new DataTrunk<>(ruleService.findAll(code, name, StringUtil.isEmpty(type)
            ? null
            : new DataDicLiteBean(RULE_CATALOG, type))));
    }

    @GetMapping("/{id}")
    @ApiOperation("‍查询rule详情")
    public ResponseEntity<RuleVO> get(@ApiParam(value = "‍规则id", required = true) @PathVariable String id) {
        return responseOfGet(ruleService.get(id));
    }
}
