package com.proper.enterprise.platform.push.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.push.api.openapi.service.PushChannelService;
import com.proper.enterprise.platform.push.vo.PushChannelVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/push/channels")
public class PushChannelController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushChannelController.class);

    private PushChannelService pushChannelService;

    @Autowired
    public PushChannelController(PushChannelService pushChannelService) {
        this.pushChannelService = pushChannelService;
    }


    @PostMapping
    public ResponseEntity<PushChannelVO> add(@RequestBody PushChannelVO pushChannelVO) {
        LOGGER.info(pushChannelVO.toString());
        return responseOfPost(pushChannelService.addChannel(pushChannelVO));
    }

    @PutMapping
    public ResponseEntity<PushChannelVO> update(@RequestBody PushChannelVO pushChannelVO) {
        LOGGER.info(pushChannelVO.toString());
        return responseOfPut(pushChannelService.updateChannel(pushChannelVO));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String ids) {
        return responseOfDelete(pushChannelService.deleteChannel(ids));
    }

    @GetMapping
    public ResponseEntity<DataTrunk<PushChannelVO>> get() {
        return responseOfGet(
            pushChannelService.findAll());
    }
}
