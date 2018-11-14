package com.proper.enterprise.platform.streamline.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.streamline.api.service.StreamlineService;
import com.proper.enterprise.platform.streamline.sdk.request.SignRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/streamline")
public class StreamlineController extends BaseController {

    @Autowired
    private StreamlineService streamlineService;

    @PostMapping
    public ResponseEntity addSign(@RequestBody SignRequest signRequest) {
        streamlineService.addSign(signRequest.getUserName(),
            signRequest.getPassword(), signRequest.getServiceKey());
        return responseOfPost(null);
    }

    @DeleteMapping(value = "/{userName}/{password}")
    public ResponseEntity delete(@PathVariable String userName, @PathVariable String password) {
        streamlineService.deleteSign(userName, password);
        return responseOfDelete(true);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody SignRequest signRequest) {
        streamlineService.updateSign(signRequest.getUserName(), signRequest.getPassword(),
            signRequest.getOldUserName(), signRequest.getOldPassword());
        return responseOfPut(null);
    }

    @GetMapping(value = "/{userName}/{password}")
    public ResponseEntity<String> get(@PathVariable String userName, @PathVariable String password) {
        String serviceKey = streamlineService.getSign(userName, password);
        return StringUtil.isEmpty(serviceKey) ? new ResponseEntity(HttpStatus.NOT_FOUND) : responseOfGet(serviceKey);
    }
}
