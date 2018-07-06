package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.workflow.service.PEPProcessService;
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/process")
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
    public ResponseEntity<DataTrunk<PEPProcInstVO>> findProcessStartByMe() {
        return responseOfGet(pepProcessService.findProcessStartByMe());
    }

}
