package com.proper.enterprise.platform.dev.tools.controller;

import com.proper.enterprise.platform.app.controller.vo.AppVersionVO;
import com.proper.enterprise.platform.app.document.AppVersionDocument;
import com.proper.enterprise.platform.app.service.AppVersionService;
import com.proper.enterprise.platform.core.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 添加新的版本，版本号需唯一
     *
     * @param  appVersionVO 版本信息
     * @return 返回添加后的版本信息
     */
    @PostMapping
    @ApiOperation("‍添加新的版本，版本号需唯一，返回添加后的版本信息")
    public ResponseEntity<AppVersionDocument> create(@RequestBody AppVersionVO appVersionVO) {
        AppVersionDocument appVersionDocument = new AppVersionDocument();
        BeanUtils.copyProperties(appVersionVO, appVersionDocument);
        return responseOfPost(appVersionService.saveOrUpdate(appVersionDocument));
    }

    /**
     * 更新版本信息
     *
     * @param  appVersionVO 版本信息
     * @return 更新后的版本信息
     */
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

    /**
     * 删除某版本
     *
     * @param  version 版本号
     * @return 删除响应
     */
    @DeleteMapping(path = "/{version}")
    @ApiOperation("‍删除版本")
    public ResponseEntity delete(@ApiParam(value = "‍版本号", required = true) @PathVariable String version) {
        AppVersionDocument ver = appVersionService.get(version);
        if (ver == null) {
            return responseOfDelete(false);
        }
        appVersionService.delete(ver);
        return responseOfDelete(true);
    }

    /**
     * 保存并发布版本
     *
     * @param  appVersionVO 版本信息
     * @return 发布的版本信息
     */
    @PostMapping(path = "/latest")
    @ApiOperation("保存并发布版本")
    public ResponseEntity<AppVersionDocument> saveAndRelease(@RequestBody AppVersionVO appVersionVO) {
        AppVersionDocument appVersionDocument = new AppVersionDocument();
        BeanUtils.copyProperties(appVersionVO, appVersionDocument);
        return responseOfPost(appVersionService.release(appVersionDocument));
    }
}
