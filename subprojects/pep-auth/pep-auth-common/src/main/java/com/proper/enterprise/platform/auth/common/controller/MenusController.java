package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.vo.MenuVO;
import com.proper.enterprise.platform.auth.common.vo.ResourceVO;
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
    public ResponseEntity get(String name, String description, String route,
                              @RequestParam(defaultValue = "ENABLE") EnableEnum menuEnable, String parentId) throws Exception {
        return responseOfGet(isPageSearch() ? service.findMenusPagniation(name, description, route, menuEnable, parentId) :
                service.getMenus(name, description, route, menuEnable, parentId));
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends Menu>> updateEnable(@RequestBody Map<String, Object> reqMap) throws Exception {
        userService.checkPermission("/auth/menus", RequestMethod.PUT);
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(service.updateEnable(idList, enable));
    }

    @PostMapping
    public ResponseEntity addMenu(@RequestBody MenuVO reqMenu) throws Exception {
        userService.checkPermission("/auth/menus", RequestMethod.POST);
        return responseOfPost(service.saveOrUpdateMenu(reqMenu));

    }

    @DeleteMapping
    public ResponseEntity deleteMenu(@RequestParam String ids) throws Exception {
        userService.checkPermission("/auth/menus", RequestMethod.DELETE);
        return responseOfDelete(service.deleteByIds(ids));
    }

    @GetMapping(path = "/{menuId}")
    public ResponseEntity<Menu> getMenuDetail(@PathVariable String menuId,
                                              @RequestParam(defaultValue = "ALL") EnableEnum menuEnable) throws Exception {
        userService.checkPermission("/auth/menus/" + menuId, RequestMethod.GET);
        return responseOfGet(service.get(menuId, menuEnable));
    }

    @PutMapping(path = "/{menuId}")
    public ResponseEntity<Menu> updateMenuDetail(@PathVariable String menuId, @RequestBody MenuVO reqMenu) throws Exception {
        userService.checkPermission("/auth/menus/" + menuId, RequestMethod.PUT);
        Menu menu = service.get(menuId, EnableEnum.ALL);
        if (menu != null) {
            reqMenu.setId(menuId);
            menu = service.saveOrUpdateMenu(reqMenu);
        }
        return responseOfPut(menu);
    }

    @GetMapping(path = "/{menuId}/resources")
    public ResponseEntity<Collection<? extends Resource>> getMenuResources(@PathVariable String menuId,
                                                                           @RequestParam(defaultValue = "ALL") EnableEnum menuEnable,
                                                                           @RequestParam(defaultValue = "ENABLE") EnableEnum resourceEnable) {
        userService.checkPermission("/auth/menus/" + menuId + "/resources", RequestMethod.GET);
        return responseOfGet(service.getMenuResources(menuId, menuEnable, resourceEnable));
    }

    @GetMapping(path = "/resources")
    public ResponseEntity<Collection> getMenuResources() {
        userService.checkPermission("/auth/menus/resources", RequestMethod.GET);
        return responseOfGet(service.getMenuAllResources());
    }

    @GetMapping(path = "/{menuId}/roles")
    public ResponseEntity<Collection<? extends Role>> getMenuRoles(@PathVariable String menuId,
                                                                   @RequestParam(defaultValue = "ALL") EnableEnum menuEnable,
                                                                   @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        userService.checkPermission("/auth/menus/" + menuId + "/roles", RequestMethod.GET);
        return responseOfGet(service.getMenuRoles(menuId, menuEnable, roleEnable));
    }

    @PostMapping(path = "/{menuId}/resource/{resourceId}")
    public ResponseEntity<Menu> addMenuResource(@PathVariable String menuId, @PathVariable String resourceId) {
        userService.checkPermission("/auth/menus/" + menuId + "/resource/" + "resourceId", RequestMethod.POST);
        return responseOfPost(service.addMenuResource(menuId, resourceId));
    }

    @DeleteMapping(path = "/{menuId}/resource/{resourceId}")
    public ResponseEntity deleteMenuResource(@PathVariable String menuId, @PathVariable String resourceId) {
        userService.checkPermission("/auth/menus/" + menuId + "/resource/" + resourceId, RequestMethod.DELETE);
        return responseOfDelete(service.deleteMenuResource(menuId, resourceId) != null);
    }

    @GetMapping(path = "/parents")
    public ResponseEntity<Collection<? extends Menu>> getMenuParents() {
        userService.checkPermission("/auth/menus/parents", RequestMethod.GET);
        return responseOfGet(service.getMenuParents());
    }

    @PostMapping(path = "/{menuId}/resources")
    public ResponseEntity<Resource> postMenuResource(@PathVariable String menuId, @RequestBody ResourceVO resourceReq) {
        userService.checkPermission("/auth/menus/" + menuId + "/resources", RequestMethod.POST);
        Menu menu = service.get(menuId, EnableEnum.ALL);
        if (menu != null) {
            menu.setId(menuId);
        }
        return responseOfPost(service.postMenuResource(resourceReq));
    }
}
