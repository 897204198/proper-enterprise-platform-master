package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.workflow.service.PEPTaskService;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workflow/task")
public class TaskController extends BaseController {

    private PEPTaskService pepTaskService;

    @Autowired
    TaskController(PEPTaskService pepTaskService) {
        this.pepTaskService = pepTaskService;
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.POST)
    public ResponseEntity complete(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
        pepTaskService.complete(taskId, variables);
        return responseOfPost(null);

    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<DataTrunk<PEPTaskVO>> get(@RequestParam Map<String, Object> searchParam) {
        return responseOfGet(pepTaskService.findPagination(searchParam, getPageRequest()));
    }

    @RequestMapping(value = "/{procInstId}", method = RequestMethod.GET)
    public ResponseEntity<List<PEPTaskVO>> findHistoricalProcessTrajectory(@PathVariable String procInstId) {
        return responseOfGet(pepTaskService.findHistoricalProcessTrajectory(procInstId));
    }

}
