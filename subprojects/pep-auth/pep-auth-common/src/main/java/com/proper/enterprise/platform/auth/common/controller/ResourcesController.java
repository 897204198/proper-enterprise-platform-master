package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.api.auth.service.UserService;
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

    @Autowired
    UserService userService;

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends Resource>> updateEnable(@RequestBody Map<String, Object> reqMap) throws Exception {
        userService.checkPermission("/auth/resources", RequestMethod.GET);
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(resourceService.updateEnable(idList, enable));
    }

    @PostMapping
    public ResponseEntity<Resource> create(@RequestBody Map<String, Object> reqMap) throws Exception {
        userService.checkPermission("/auth/resources", RequestMethod.POST);
        return responseOfPost(resourceService.save(reqMap));
    }

    @DeleteMapping
    public ResponseEntity deleteResource(@RequestParam String ids) throws Exception {
        userService.checkPermission("/auth/resources", RequestMethod.DELETE);
        return responseOfDelete(resourceService.deleteByIds(ids));
    }

    @GetMapping(path = "/{resourceId}")
    public ResponseEntity<Resource> find(@PathVariable String resourceId) throws Exception {
        return responseOfGet(resourceService.get(resourceId));
    }

    @PutMapping(path = "/{resourceId}")
    public ResponseEntity<Resource> update(@PathVariable String resourceId, @RequestBody Map<String, Object> reqMap) throws Exception {
        userService.checkPermission("/auth/resources/" + resourceId, RequestMethod.PUT);
        Resource resource = resourceService.get(resourceId);
        if (resource != null) {
            reqMap.put("id", resourceId);
            resource = resourceService.save(reqMap);
        }
        return responseOfPut(resource);
    }

    @DeleteMapping(path = "/{resourceId}")
    public ResponseEntity delete(@PathVariable String resourceId) throws Exception {
        userService.checkPermission("/auth/resources/" + resourceId, RequestMethod.DELETE);
        Resource resource = resourceService.get(resourceId);
        if (resource != null) {
            resourceService.delete(resource);
        }
        return responseOfDelete(resource != null);
    }

    @GetMapping(path = "/{resourceId}/menus")
    public ResponseEntity<Collection<? extends Menu>> getResourceMenus(@PathVariable String resourceId) {
        userService.checkPermission("/auth/resources/" + resourceId + "/menus", RequestMethod.GET);
        return responseOfGet(resourceService.getResourceMenus(resourceId));
    }

    @GetMapping(path = "/{resourceId}/roles")
    public ResponseEntity<Collection<? extends Role>> getResourceRoles(@PathVariable String resourceId) {
        userService.checkPermission("/auth/resources/" + resourceId + "/roles", RequestMethod.GET);
        return responseOfGet(resourceService.getResourceRoles(resourceId));
    }
}
