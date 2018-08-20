package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.workflow.service.PEPProcessService;
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO;
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPageVO;
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPathVO;
import com.proper.enterprise.platform.workflow.vo.enums.PEPProcInstStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/workflow/process")
public class ProcessController extends BaseController {

    private PEPProcessService pepProcessService;

    @Autowired
    ProcessController(PEPProcessService pepProcessService) {
        this.pepProcessService = pepProcessService;
    }

    @RequestMapping(value = "/{procDefKey}", method = RequestMethod.POST)
    public ResponseEntity<PEPProcInstVO> startProcess(@PathVariable String procDefKey, @RequestBody Map<String, Object> variables) {
        return responseOfPost(pepProcessService.startProcess(procDefKey, variables));
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<DataTrunk<PEPProcInstVO>> findProcessStartByMePagination(String processDefinitionName, PEPProcInstStateEnum state) {
        return responseOfGet(pepProcessService.findProcessStartByMePagination(processDefinitionName, state, getPageRequest()));
    }

    @RequestMapping(value = "/{procInstId}/page", method = RequestMethod.GET)
    public ResponseEntity<PEPWorkflowPageVO> buildPage(@PathVariable String procInstId) {
        return responseOfGet(pepProcessService.buildPage(procInstId));
    }

    @RequestMapping(value = "/{procInstId}/path", method = RequestMethod.GET)
    public ResponseEntity<PEPWorkflowPathVO> findWorkflowPath(@PathVariable String procInstId) {
        return responseOfGet(pepProcessService.findWorkflowPath(procInstId));
    }

}
