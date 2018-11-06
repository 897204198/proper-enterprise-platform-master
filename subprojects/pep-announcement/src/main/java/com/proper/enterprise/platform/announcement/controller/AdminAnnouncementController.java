package com.proper.enterprise.platform.announcement.controller;

import com.proper.enterprise.platform.announcement.entity.AnnouncementEntity;
import com.proper.enterprise.platform.announcement.service.AnnouncementService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Api(tags = "/admin/announcement")
@RequestMapping("/admin/announcement")
public class AdminAnnouncementController extends BaseController {

    private AnnouncementService announcementService;

    public AdminAnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @PostMapping
    @ApiOperation("‍添加公告信息")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity addAnnouncement(@RequestBody AnnouncementVO announcementVO) throws Exception {
        AnnouncementEntity announcementEntity = new AnnouncementEntity();
        BeanUtils.copyProperties(announcementVO, announcementEntity);
        announcementService.addNoticeinfo(announcementEntity);
        return responseOfPost(true);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("‍删除公告信息")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteAnnouncement(@ApiParam(value = "‍公告信息ID", required = true) @PathVariable String id) {
        announcementService.deleteById(id);
        return responseOfDelete(true);
    }

    @PutMapping("/{id}")
    @ApiOperation("‍修改公告信息")
    public ResponseEntity updateAnnouncement(@ApiParam(value = "‍公告信息ID", required = true) @PathVariable String id,
                                             @RequestBody AnnouncementVO announcementVO) {
        AnnouncementEntity announcementEntity = new AnnouncementEntity();
        BeanUtils.copyProperties(announcementVO, announcementEntity);
        announcementService.updateNoticeInfoById(id, announcementEntity);
        return responseOfPut(true);
    }

    @GetMapping
    @ApiOperation("‍取得管理端公告信息列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNo", value = "‍页码", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "‍每页条数", paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk> findAnnouncement(@ApiParam(value = "‍公告信息类型编码‍", required = true) @RequestParam String infoType,
                                                      @ApiParam("‍公告标题‍") String title) {
        if (isPageSearch()) {
            return responseOfGet(announcementService.findLatestNoticesPage(infoType, title, getPageRequest()));
        } else {
            Collection<AnnouncementEntity> collection = announcementService.findLatestNotices(infoType, title);
            DataTrunk<AnnouncementEntity> dataTrunk = new DataTrunk<>();
            dataTrunk.setCount(collection.size());
            dataTrunk.setData(collection);
            return responseOfGet(dataTrunk);
        }
    }
    public static class AnnouncementVO {

        @ApiModelProperty(name = "‍公告信息类型编码", required = true)
        private String infoType;

        @ApiModelProperty(name = "‍标题信息", required = true)
        private String title;

        @ApiModelProperty(name = "‍‍公告信息", required = true)
        private String info;

        @ApiModelProperty(name = "‍公告有效起始时间(yyyy-MM-dd HH:mm:ss)", required = true)
        private String beginTime;

        @ApiModelProperty(name = "‍生效终止时间 yyyy-MM-dd HH:mm:ss", required = true)
        private String endTime;

        @ApiModelProperty(name = "‍开启关闭状态,默认关闭状态", required = true)
        private Boolean infoStatus = false;

        public String getInfoType() {
            return infoType;
        }

        public void setInfoType(String infoType) {
            this.infoType = infoType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Boolean getInfoStatus() {
            return infoStatus;
        }

        public void setInfoStatus(Boolean infoStatus) {
            this.infoStatus = infoStatus;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }
}