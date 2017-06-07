package com.proper.enterprise.platform.page.custom.grid.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.page.custom.grid.document.CustomGridDocument;
import com.proper.enterprise.platform.page.custom.grid.service.CustomGridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/custom/grid")
public class CustomGridController extends BaseController {

    @Autowired
    CustomGridService customGridService;

    /**
     * 取得配置信息翻页数据
     */
    @GetMapping(path = "/page/configs")
    public ResponseEntity<DataTrunk<CustomGridDocument>> getCustomGridForPage(String title, int pageNo, int pageSize) {
        DataTrunk<CustomGridDocument> page = customGridService.getCustomGridForPage(title, pageNo, pageSize);
        return responseOfGet(page);
    }

    /**
     * 通过code取得配置信息
     */
    @GetMapping(path = "/code/configs")
    public ResponseEntity<CustomGridDocument> getCustomGridByCode(@RequestParam(required = true) String code) {
        CustomGridDocument document = customGridService.getCustomGridByCode(code);
        return responseOfGet(document);
    }

    /**
     * 通过id取得配置信息
     */
    @GetMapping(path = "/configs/{id}")
    public ResponseEntity<CustomGridDocument> getCustomGridById(@PathVariable String id) {
        CustomGridDocument document = customGridService.getCustomGridById(id);
        return responseOfGet(document);
    }

    /**
     * 添加配置信息
     */
    @RequestMapping(path = "/configs", method = RequestMethod.POST)
    public ResponseEntity<String> saveCustomGrid(@RequestBody CustomGridDocument customGridDocument) {
        customGridService.saveOrUpdateCustomGrid(customGridDocument);
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    /**
     * 更新配置信息
     */
    @RequestMapping(path = "/configs/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateCustomGrid(@PathVariable String id, @RequestBody CustomGridDocument customGridDocument) {
        customGridDocument.setId(id);
        customGridService.saveOrUpdateCustomGrid(customGridDocument);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    /**
     * 删除配置信息
     */
    @RequestMapping(path = "/configs", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCustomGrid(@RequestParam(required = true) String ids) {
        customGridService.deleteCustomGridByIds(ids);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
