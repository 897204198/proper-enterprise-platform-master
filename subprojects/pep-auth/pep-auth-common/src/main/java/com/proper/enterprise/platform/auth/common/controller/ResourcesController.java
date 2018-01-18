package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
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
    public ResponseEntity<Collection<? extends Resource>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        Collection<String> idList = (Collection<String>)reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        // TODO 具体实现
        return responseOfPut(resourceService.updateEanble(idList, enable));
    }

    @PostMapping
    public ResponseEntity<Resource> create(@RequestBody Map<String, Object> reqMap) {
        return responseOfPost(resourceService.save(reqMap));
    }

    @DeleteMapping
    public ResponseEntity deleteResource(@RequestParam String ids) {
        // TODO 具体实现
        return responseOfDelete(resourceService.deleteByIds(ids));
    }

    @GetMapping(path = "/{resourceId}")
    public ResponseEntity<Resource> find(@PathVariable String resourceId) {
        return responseOfGet(resourceService.get(resourceId));
    }

    @PutMapping(path = "/{resourceId}")
    public ResponseEntity<Resource> update(@PathVariable String resourceId, @RequestBody Map<String, Object> reqMap) {
        Resource resource = resourceService.get(resourceId);
        if (resource != null) {
            reqMap.put("id", resourceId);
            resource = resourceService.save(reqMap);
        }
        return responseOfPut(resource);
    }

    @DeleteMapping(path = "/{resourceId}")
    public ResponseEntity delete(@PathVariable String resourceId) {
        Resource resource = resourceService.get(resourceId);
        if (resource != null) {
            resourceService.delete(resource);
        }
        return responseOfDelete(resource != null);
    }

}
