package com.proper.enterprise.platform.workflow.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.workflow.plugin.service.PEPTaskService;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPageVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/workflow/task")
@Api(tags = "/workflow/task")
public class TaskController extends BaseController {

    private PEPTaskService pepTaskService;

    @Autowired
    TaskController(PEPTaskService pepTaskService) {
        this.pepTaskService = pepTaskService;
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.POST)
    @ApiOperation("‍提交待办")
    public ResponseEntity complete(@PathVariable @ApiParam(value = "taskId", required = true) String taskId,
                                   @RequestBody @ApiParam("‍表单信息") Map<String, Object> variables) {
        pepTaskService.complete(taskId, variables);
        return responseOfPost(null);
    }

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    @ApiOperation("‍待办查询")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<PEPTaskVO>> get(@ApiParam("‍流程定义名称") String processDefinitionName) {
        return responseOfGet(pepTaskService.findTodoPagination(processDefinitionName, getPageRequest()));
    }

    @RequestMapping(value = "/todo/count", method = RequestMethod.GET)
    @ApiOperation("‍待办数量查询")
    public ResponseEntity<Long> get() {
        return responseOfGet(pepTaskService.getTodoCount());
    }

    @RequestMapping(value = "/assignee", method = RequestMethod.GET)
    @JsonView(value = {PEPTaskVO.ToDoView.class})
    @ApiOperation("‍已办查询")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<PEPTaskVO>> findTaskAssigneeIsMePagination(@ApiParam("‍流程定义名称") String processDefinitionName) {
        return responseOfGet(pepTaskService.findTaskAssigneeIsMePagination(processDefinitionName, getPageRequest()));
    }

    @RequestMapping(value = "/{taskId}/page", method = RequestMethod.GET)
    @ApiOperation("‍待办表单数据查询")
    public ResponseEntity<PEPWorkflowPageVO> buildPage(@PathVariable @ApiParam(value = "taskId", required = true) String taskId) {
        return responseOfGet(pepTaskService.buildPage(taskId));
    }

}
