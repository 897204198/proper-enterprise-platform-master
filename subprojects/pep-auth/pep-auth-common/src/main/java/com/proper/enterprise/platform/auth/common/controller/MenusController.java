package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/auth/menus")
public class MenusController extends BaseController {

    @Autowired
    private MenuService service;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity get(String name, String description, String route, String enable) throws Exception {
        userService.checkPermission("/auth/menus", RequestMethod.GET);
        return responseOfGet(service.getMenus(name, description, route, enable, null));
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends Menu>> updateEnable(@RequestBody Map<String, Object> reqMap) throws Exception {
        userService.checkPermission("/auth/menus", RequestMethod.PUT);
        Collection<String> idList = (Collection<String>)reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(service.updateEanble(idList, enable));
    }

    @PostMapping
    public ResponseEntity addMenu(@RequestBody Map<String, Object> reqMap) throws Exception {
        userService.checkPermission("/auth/menus", RequestMethod.POST);
        return responseOfPost(service.save(reqMap));

    }

    @DeleteMapping
    public ResponseEntity deleteMenu(@RequestParam String ids) throws Exception {
        userService.checkPermission("/auth/menus", RequestMethod.DELETE);
        return responseOfDelete(service.deleteByIds(ids));
    }

    @GetMapping(path = "/{menuId}")
    public ResponseEntity<Menu> getMenuDetail(@PathVariable String menuId) throws Exception {
        userService.checkPermission("/auth/menus/{menuId}", RequestMethod.GET);
        return responseOfGet(service.get(menuId));
    }

    @PutMapping(path = "/{menuId}")
    public ResponseEntity<Menu> updateMenuDetail(@PathVariable String menuId, @RequestBody Map<String, Object> reqMap) throws Exception {
        userService.checkPermission("/auth/menus/{menuId}", RequestMethod.PUT);
        Menu menu = service.get(menuId);
        if (menu != null) {
            reqMap.put("id", menuId);
            menu = service.save(reqMap);
        }
        return responseOfPut(menu);
    }

    @GetMapping(path = "/{menuId}/resources")
    public ResponseEntity<Collection<? extends Resource>> getMenuResources(@PathVariable String menuId) {
        // TODO 具体业务实现
        return responseOfGet(service.getMenuResources(menuId));
    }

    @GetMapping(path = "/{menuId}/roles")
    public ResponseEntity<Collection<? extends Role>> getMenuRoles(@PathVariable String menuId) {
        // TODO 具体业务实现
        return responseOfGet(service.getMenuRoles(menuId));
    }

    @PostMapping(path = "/{menuId}/resource/{resourceId}")
    public ResponseEntity<Menu> addMenuResource(@PathVariable String menuId, @PathVariable String resourceId) {
        return responseOfPost(service.addMenuResource(menuId, resourceId));
    }

    @DeleteMapping(path = "/{menuId}/resource/{resourceId}")
    public ResponseEntity deleteMenuResource(@PathVariable String menuId, @PathVariable String resourceId) {
        return  responseOfDelete(service.deleteMenuResource(menuId, resourceId) != null);
    }

    @GetMapping(path = "/parents")
    public ResponseEntity<Collection<? extends Menu>> getMenuParents() {
        return responseOfGet(service.getMenuParents());
    }
}
