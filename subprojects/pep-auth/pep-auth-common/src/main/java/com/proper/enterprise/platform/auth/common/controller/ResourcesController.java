package com.proper.enterprise.platform.auth.common.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.auth.common.vo.MenuVO;
import com.proper.enterprise.platform.auth.common.vo.ResourceVO;
import com.proper.enterprise.platform.auth.common.vo.RoleVO;
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
    @JsonView(ResourceVO.Single.class)
    public ResponseEntity<Collection<ResourceVO>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(resourceService.updateEnable(idList, enable), ResourceVO.class, ResourceVO.Single.class);
    }

    @DeleteMapping
    public ResponseEntity deleteResource(@RequestParam String ids) {
        return responseOfDelete(resourceService.deleteByIds(ids));
    }

    @AuthcIgnore // TODO necessary?
    @GetMapping(path = "/{resourceId}")
    @JsonView(ResourceVO.Single.class)
    public ResponseEntity<ResourceVO> find(@PathVariable String resourceId) {
        return responseOfGet(resourceService.get(resourceId), ResourceVO.class, ResourceVO.Single.class);
    }

    @PutMapping(path = "/{resourceId}")
    @JsonView(ResourceVO.Single.class)
    public ResponseEntity<ResourceVO> update(@PathVariable String resourceId, @RequestBody ResourceVO resourceReq) {
        Resource resource = resourceService.get(resourceId);
        if (resource != null) {
            resourceReq.setId(resourceId);
            resource = resourceService.update(resourceReq);
        }
        return responseOfPut(resource, ResourceVO.class, ResourceVO.Single.class);
    }

    @DeleteMapping(path = "/{resourceId}")
    public ResponseEntity delete(@PathVariable String resourceId) {
        Resource resource = resourceService.get(resourceId);
        if (resource != null) {
            resourceService.delete(resource);
        }
        return responseOfDelete(resource != null);
    }

    @GetMapping(path = "/{resourceId}/menus")
    @JsonView(MenuVO.Single.class)
    public ResponseEntity<Collection<MenuVO>> getResourceMenus(@PathVariable String resourceId,
                                                               @RequestParam(defaultValue = "ENABLE") EnableEnum menuEnable) {
        return responseOfGet(resourceService.getResourceMenus(resourceId, menuEnable), MenuVO.class, MenuVO.Single.class);
    }

    @GetMapping(path = "/{resourceId}/roles")
    @JsonView(RoleVO.Single.class)
    public ResponseEntity<Collection<RoleVO>> getResourceRoles(@PathVariable String resourceId,
                                                               @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        return responseOfGet(resourceService.getResourceRoles(resourceId, roleEnable), RoleVO.class, RoleVO.Single.class);
    }
}
