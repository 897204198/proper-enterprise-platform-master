package com.proper.enterprise.platform.announcement.controller;

import com.proper.enterprise.platform.announcement.entity.AnnouncementEntity;
import com.proper.enterprise.platform.announcement.service.AnnouncementService;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/announcement")
public class AdminAnnouncementController extends BaseController {

    private AnnouncementService announcementService;

    public AdminAnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @PostMapping
    public ResponseEntity addAnnouncement(@RequestBody AnnouncementEntity announcementEntity) throws Exception {
        announcementService.addNoticeinfo(announcementEntity);
        return responseOfPost(true);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAnnouncement(@PathVariable String id) {
        announcementService.deleteById(id);
        return responseOfDelete(true);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAnnouncement(@PathVariable String id, @RequestBody AnnouncementEntity announcementEntity) {
        announcementService.updateNoticeInfoById(id, announcementEntity);
        return responseOfPut(true);
    }

    @GetMapping
    public ResponseEntity findAnnouncement(@RequestParam String infoType, String title) {
        return isPageSearch() ? responseOfGet(announcementService.findLatestNoticesPage(infoType, title, getPageRequest())) :
                responseOfGet(announcementService.findLatestNotices(infoType, title));
    }
}