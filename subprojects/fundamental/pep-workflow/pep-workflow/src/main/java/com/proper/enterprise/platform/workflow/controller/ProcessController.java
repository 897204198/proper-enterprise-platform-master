package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.workflow.service.PEPProcessService;
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO;
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPageVO;
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPathVO;
import com.proper.enterprise.platform.workflow.vo.enums.PEPProcInstStateEnum;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/workflow/process")
@Api(tags = "/workflow/process")
public class ProcessController extends BaseController {

    private PEPProcessService pepProcessService;

    @Autowired
    ProcessController(PEPProcessService pepProcessService) {
        this.pepProcessService = pepProcessService;
    }

    @ApiOperation(value = "‍发起流程")
    @RequestMapping(value = "/{procDefKey}", method = RequestMethod.POST)
    public ResponseEntity<PEPProcInstVO> startProcess(@ApiParam(value = "‍流程定义key", required = true) @PathVariable String procDefKey,
                                                      @ApiParam(value = "‍表单信息") @RequestBody Map<String, Object> variables) {
        return responseOfPost(pepProcessService.startProcess(procDefKey, variables));
    }


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation("‍我发起的流程")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<PEPProcInstVO>> findProcessStartByMePagination(@ApiParam(value = "‍流程定义名称") String processDefinitionName,
                                                                                   @ApiParam(value = "‍流程状态") PEPProcInstStateEnum state) {
        return responseOfGet(pepProcessService.findProcessStartByMePagination(processDefinitionName, state, getPageRequest()));
    }


    @RequestMapping(value = "/{procInstId}/page", method = RequestMethod.GET)
    @ApiOperation("‍流程详情查询")
    public ResponseEntity<PEPWorkflowPageVO> buildPage(@PathVariable @ApiParam(value = "‍流程实例Id", required = true) String procInstId) {
        return responseOfGet(pepProcessService.buildPage(procInstId));
    }

    @RequestMapping(value = "/{procInstId}/path", method = RequestMethod.GET)
    @ApiOperation("‍流程历史轨迹查询")
    public ResponseEntity<PEPWorkflowPathVO> findWorkflowPath(@PathVariable @ApiParam(value = "‍流程实例Id", required = true) String procInstId) {
        return responseOfGet(pepProcessService.findWorkflowPath(procInstId));
    }

}
