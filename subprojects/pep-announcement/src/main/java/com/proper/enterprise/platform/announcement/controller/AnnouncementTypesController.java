package com.proper.enterprise.platform.announcement.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/sys/announcement/types")
public class AnnouncementTypesController  extends BaseController {
    @Autowired
    private I18NService i18NService;

    /**
     * 取得公告信息类别列表
     * 类型目前就两种，经过商定，后台写死
     */
    @AuthcIgnore
    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getAnnouncementTypes() {
        List<Map<String, String>> result = new ArrayList<>(2);

        Map<String, String> map1 = new TreeMap<>();
        map1.put("infoType", "ACTIVITY_INFORMATION");
        map1.put("infoDescribe", i18NService.getMessage("pep.sys.popup.sms.type"));
        result.add(map1);

        Map<String, String> map2 = new TreeMap<>();
        map2.put("infoType", "NOTICE_INFORMATION");
        map2.put("infoDescribe", i18NService.getMessage("pep.sys.scroll.sms.type"));
        result.add(map2);

        return responseOfGet(result);
    }
}
