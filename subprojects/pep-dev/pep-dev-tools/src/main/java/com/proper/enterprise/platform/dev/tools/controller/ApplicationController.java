package com.proper.enterprise.platform.dev.tools.controller;

import com.proper.enterprise.platform.app.service.ApplicationService;
import com.proper.enterprise.platform.app.vo.AppCatalogVO;
import com.proper.enterprise.platform.app.vo.ApplicationVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/admin/app/applications")
public class ApplicationController extends BaseController {

    private ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PutMapping("/{appId}")
    public ResponseEntity<ApplicationVO> updateApplication(@PathVariable String appId, @RequestBody ApplicationVO applicationVO) {
        return responseOfPut(applicationService.updateApplication(appId, applicationVO));
    }

    @PostMapping
    public ResponseEntity<ApplicationVO> postApplication(@RequestBody ApplicationVO applicationVO) {
        return responseOfPost(applicationService.addApplication(applicationVO));
    }

    @DeleteMapping
    public ResponseEntity deleteApplications(@RequestParam String ids) {
        return responseOfDelete(applicationService.deleteByIds(ids));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationVO>> getAllOrApplication(@RequestParam(required = false) String code) {
        List<ApplicationVO> list = new ArrayList<>();
        if (code != null) {
            applicationService.getApplicationByCode(code);
        } else {
            applicationService.getApplications();
        }
        return responseOfGet(list);
    }

    @GetMapping("/{appId}")
    public ResponseEntity<ApplicationVO> findApplication(@PathVariable String appId) {
        return responseOfGet(applicationService.getApplication(appId));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<AppCatalogVO>> findCatalogs() {
        return responseOfGet(applicationService.getCatalogs());
    }

    @PutMapping("/catalogs/{code}")
    public ResponseEntity<AppCatalogVO> putCatalog(@PathVariable String code, @RequestParam String typeName) {
        return responseOfGet(applicationService.updateCatalog(code, typeName));
    }

    @PostMapping("/catalogs")
    public ResponseEntity<AppCatalogVO> postCatalog(@RequestBody AppCatalogVO appCatalogVO) {
        return responseOfPost(applicationService.addCatalog(appCatalogVO));
    }

    @GetMapping("/catalogs/{code}")
    public ResponseEntity<AppCatalogVO> findCatalog(@PathVariable String code) {
        return responseOfGet(applicationService.getCatalog(code));
    }

    @DeleteMapping("/catalogs/{code}")
    public ResponseEntity deleteCatalog(@PathVariable String code) {
        AppCatalogVO typeCode = applicationService.getCatalog(code);
        if (typeCode == null) {
            return responseOfDelete(false);
        }
        applicationService.deleteByCode(code);
        return responseOfDelete(true);
    }
}
