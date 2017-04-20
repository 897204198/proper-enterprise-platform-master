package com.proper.enterprise.platform.workflow.controller.test
import com.proper.enterprise.platform.workflow.controller.BaseBPMController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bpm")
class TestBPMController extends BaseBPMController {

    private static final String PROC_DEF_KEY = 'aib'

    @GetMapping("/script")
    public ResponseEntity<Integer> scriptTask(Integer initVal) {
        responseOfGet((Integer) getVariableAfterProcessDone(PROC_DEF_KEY, ['myVar': initVal], 'myVar'))
    }

}
