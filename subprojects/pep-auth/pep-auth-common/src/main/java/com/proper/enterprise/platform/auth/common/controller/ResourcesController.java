package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.auth.common.vo.ResourceVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/auth/resources")
public class ResourcesController extends BaseController {

    @Autowired
    ResourceService resourceService;

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends Resource>> updateEnable(@RequestBody Map<String, Object> reqMap) throws Exception {
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(resourceService.updateEnable(idList, enable));
    }

    @PostMapping
    public ResponseEntity<Resource> create(@RequestBody ResourceVO resourceReq) throws Exception {
        return responseOfPost(resourceService.saveOrUpdateResource(resourceReq));
    }

    @DeleteMapping
    public ResponseEntity deleteResource(@RequestParam String ids) throws Exception {
        return responseOfDelete(resourceService.deleteByIds(ids));
    }

    @AuthcIgnore // TODO necessary?
    @GetMapping(path = "/{resourceId}")
    public ResponseEntity<Resource> find(@PathVariable String resourceId,
                                         @RequestParam(defaultValue = "ALL") EnableEnum resourceEnable) throws Exception {
        return responseOfGet(resourceService.get(resourceId, resourceEnable));
    }

    @PutMapping(path = "/{resourceId}")
    public ResponseEntity<Resource> update(@PathVariable String resourceId, @RequestBody ResourceVO resourceReq) throws Exception {
        Resource resource = resourceService.get(resourceId, EnableEnum.ALL);
        if (resource != null) {
            resourceReq.setId(resourceId);
            resource = resourceService.saveOrUpdateResource(resourceReq);
        }
        return responseOfPut(resource);
    }

    @DeleteMapping(path = "/{resourceId}")
    public ResponseEntity delete(@PathVariable String resourceId) throws Exception {
        Resource resource = resourceService.get(resourceId);
        if (resource != null) {
            resourceService.delete(resource);
        }
        return responseOfDelete(resource != null);
    }

    @GetMapping(path = "/{resourceId}/menus")
    public ResponseEntity<Collection<? extends Menu>> getResourceMenus(@PathVariable String resourceId,
                                                                       @RequestParam(defaultValue = "ALL") EnableEnum resourceEnable,
                                                                       @RequestParam(defaultValue = "ENABLE") EnableEnum menuEnable) {
        return responseOfGet(resourceService.getResourceMenus(resourceId, resourceEnable, menuEnable));
    }

    @GetMapping(path = "/{resourceId}/roles")
    public ResponseEntity<Collection<? extends Role>> getResourceRoles(@PathVariable String resourceId,
                                                                       @RequestParam(defaultValue = "ALL") EnableEnum resourceEnable,
                                                                       @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        return responseOfGet(resourceService.getResourceRoles(resourceId, resourceEnable, roleEnable));
    }
}
