package com.proper.enterprise.platform.announcement.controller;

import com.proper.enterprise.platform.announcement.entity.AnnouncementEntity;
import com.proper.enterprise.platform.announcement.service.AnnouncementService;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/announcement")
public class AnnouncementController extends BaseController {

    private AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    /**
     * 取得公告信息列表
     *
     * @param infoType 公告信息类型编码
     */
    @AuthcIgnore
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnnouncementEntity>> getNoticeInfoByInfoType(@RequestParam String infoType) {
        return responseOfGet(announcementService.findLatestValidNoticesByInfoType(infoType));
    }
}
