package com.proper.enterprise.platform.app.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proper.enterprise.platform.app.document.AppVersionDocument;
import com.proper.enterprise.platform.app.service.AppVersionService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "/admin/app/versions")
@RequestMapping("/admin/app/versions")
public class AppVersionManagerController extends BaseController {

    private AppVersionService appVersionService;

    @Autowired
    public AppVersionManagerController(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    @PostMapping
    @ApiOperation("‍添加新的版本，版本号需唯一，返回添加后的版本信息")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AppVersionDocument> create(@RequestBody AppVersionVO appVersionVO) {
        AppVersionDocument appVersionDocument = new AppVersionDocument();
        BeanUtils.copyProperties(appVersionVO, appVersionDocument);
        return responseOfPost(appVersionService.saveOrUpdate(appVersionDocument));
    }

    @PutMapping
    @ApiOperation("‍更新版本信息，返回更新后的版本信息")
    public ResponseEntity<AppVersionDocument> update(@RequestBody AppVersionVO appVersionVO) {
        AppVersionDocument appVersionDocument = new AppVersionDocument();
        BeanUtils.copyProperties(appVersionVO, appVersionDocument);
        return responseOfPut(appVersionService.saveOrUpdate(appVersionDocument));
    }

    @GetMapping
    @ApiOperation("‍获取版本信息列表")
    public ResponseEntity<List<AppVersionDocument>> list() {
        return responseOfGet(appVersionService.list());
    }

    @DeleteMapping(path = "/{version}")
    @ApiOperation("‍删除版本")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@ApiParam(value = "‍版本号", required = true) @PathVariable String version) {
        AppVersionDocument ver = appVersionService.get(version);
        if (ver == null) {
            return responseOfDelete(false);
        }
        appVersionService.delete(ver);
        return responseOfDelete(true);
    }

    @PostMapping(path = "/latest")
    @ApiOperation("‍保存并发布版本")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AppVersionDocument> saveAndRelease(@RequestBody AppVersionVO appVersionVO) {
        AppVersionDocument appVersionDocument = new AppVersionDocument();
        BeanUtils.copyProperties(appVersionVO, appVersionDocument);
        return responseOfPost(appVersionService.release(appVersionDocument));
    }

    public static class AppVersionVO {

        @ApiModelProperty(name = "‍版本号", required = true)
        @JsonProperty("ver")
        private String version;

        @ApiModelProperty(name = "‍android下载地址", required = true)
        @JsonProperty("androidUrl")
        private String androidURL;

        @ApiModelProperty(name = "‍ios下载地址", required = true)
        @JsonProperty("iosUrl")
        private String iosURL;

        @ApiModelProperty(name = "‍版本说明", required = true)
        private String note;

        private AppVersionVO() { }

        private AppVersionVO(String version, String androidURL, String note) {
            this.version = version;
            this.androidURL = androidURL;
            this.note = note;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getAndroidURL() {
            return androidURL;
        }

        public void setAndroidURL(String androidURL) {
            this.androidURL = androidURL;
        }

        public String getIosURL() {
            return iosURL;
        }

        public void setIosURL(String iosURL) {
            this.iosURL = iosURL;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }
}
