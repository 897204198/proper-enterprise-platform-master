package com.proper.enterprise.platform.announcement.controller;

import com.proper.enterprise.platform.announcement.entity.AnnouncementEntity;
import com.proper.enterprise.platform.announcement.service.AnnouncementService;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "/sys/announcement")
@RequestMapping("/sys/announcement")
public class AnnouncementController extends BaseController {

    private AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @AuthcIgnore
    @ApiOperation("‍取得公告信息列表")
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnnouncementEntity>> getNoticeInfoByInfoType(@ApiParam(value = "‍公告信息类型编码‍", required = true)
                                                                                @RequestParam String infoType) {
        return responseOfGet(announcementService.findLatestValidNoticesByInfoType(infoType));
    }
}
