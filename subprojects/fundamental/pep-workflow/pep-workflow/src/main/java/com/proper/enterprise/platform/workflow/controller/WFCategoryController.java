package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.plugin.service.WFCategoryService;
import com.proper.enterprise.platform.workflow.vo.WFCategoryVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repository/wfCategory")
@Api(tags = "/repository/wfCategory")
public class WFCategoryController extends BaseController {

    @Autowired
    private WFCategoryService wfCategoryService;

    @PostMapping
    @ApiOperation("‍新增流程类别")
    public ResponseEntity<WFCategoryVO> post(@ApiParam(value = "‍流程类别对象", required = true) @RequestBody WFCategoryVO wfCategoryVO) {
        return responseOfPost(wfCategoryService.save(wfCategoryVO));
    }

    @DeleteMapping
    @ApiOperation("‍批量删除流程类别")
    public ResponseEntity delete(@ApiParam(value = "‍待删除Id列表(, 分割)", required = true) @RequestParam String ids) {
        return responseOfDelete(wfCategoryService.deleteByIds(ids));
    }

    @PutMapping("/{id}")
    @ApiOperation("‍修改流程类别")
    public ResponseEntity<WFCategoryVO> put(@ApiParam(value = "‍流程类别的id", required = true) @PathVariable String id,
                                            @ApiParam(value = "‍‍流程类别对象", required = true) @RequestBody WFCategoryVO wfCategoryVO) {
        wfCategoryVO.setId(id);
        return responseOfPut(wfCategoryService.update(wfCategoryVO));
    }

    @GetMapping
    @ApiOperation("‍查询流程类别列表")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk> get(@ApiParam(name = "‍流程类别名称") String name,
                                         @ApiParam(name = "‍流程类别编码") String code,
                                         @ApiParam(name = "‍父类别Id") String parentId) {
        WFCategoryVO parent = null;
        if (StringUtil.isNotEmpty(parentId)) {
            parent = wfCategoryService.get(parentId);
        }
        return isPageSearch() ? responseOfGet(wfCategoryService.findAll(name, code, parent, getPageRequest()))
            : responseOfGet(new DataTrunk<>(wfCategoryService.findAll(name, code, parent)));
    }
}
