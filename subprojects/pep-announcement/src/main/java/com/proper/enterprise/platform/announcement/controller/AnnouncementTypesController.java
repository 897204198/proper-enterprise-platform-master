package com.proper.enterprise.platform.announcement.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "/sys/announcement/types")
@RequestMapping("/sys/announcement/types")
public class AnnouncementTypesController  extends BaseController {
    @Autowired
    private I18NService i18NService;

    @AuthcIgnore
    @GetMapping
    @ApiOperation("‍取得公告信息类别列表，类型目前就两种，经过商定，后台写死")
    public ResponseEntity<List<AnnouncementTypeVO>> getAnnouncementTypes() {
        List<AnnouncementTypeVO> result = new ArrayList<>();

        AnnouncementTypeVO announcementVO = new AnnouncementTypeVO();
        announcementVO.setInfoType("ACTIVITY_INFORMATION");
        announcementVO.setInfoDescribe(i18NService.getMessage("pep.sys.popup.sms.type"));
        result.add(announcementVO);

        AnnouncementTypeVO announcement = new AnnouncementTypeVO();
        announcement.setInfoType("NOTICE_INFORMATION");
        announcement.setInfoDescribe(i18NService.getMessage("pep.sys.scroll.sms.type"));
        result.add(announcement);

        return responseOfGet(result);
    }

    public static class AnnouncementTypeVO {

        @ApiModelProperty(name = "‍公告信息类型编码")
        private String infoType;

        @ApiModelProperty(name = "‍标题信息")
        private String infoDescribe;

        public String getInfoType() {
            return infoType;
        }

        public void setInfoType(String infoType) {
            this.infoType = infoType;
        }

        public String getInfoDescribe() {
            return infoDescribe;
        }

        public void setInfoDescribe(String infoDescribe) {
            this.infoDescribe = infoDescribe;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }
}
