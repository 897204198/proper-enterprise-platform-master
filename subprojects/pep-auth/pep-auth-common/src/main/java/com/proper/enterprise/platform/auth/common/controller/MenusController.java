package com.proper.enterprise.platform.auth.common.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.service.MenuService;
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
@RequestMapping("/auth/menus")
public class MenusController extends BaseController {

    @Autowired
    private MenuService service;

    @GetMapping
    @JsonView(MenuVO.Single.class)
    public ResponseEntity get(String name, String description, String route,
                              @RequestParam(defaultValue = "ENABLE") EnableEnum menuEnable, String parentId) {
        return isPageSearch() ? responseOfGet(service.findMenusPagination(name, description, route, menuEnable, parentId),
            MenuVO.class, MenuVO.Single.class)
            : responseOfGet(service.getMenus(name, description, route, menuEnable, parentId), MenuVO.class, MenuVO.Single.class);
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    @JsonView(MenuVO.Single.class)
    public ResponseEntity<Collection<MenuVO>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(service.updateEnable(idList, enable), MenuVO.class, MenuVO.Single.class);
    }

    @PostMapping
    @JsonView(MenuVO.Single.class)
    public ResponseEntity<MenuVO> addMenu(@RequestBody MenuVO reqMenu) {
        return responseOfPost(service.save(reqMenu), MenuVO.class, MenuVO.Single.class);
    }

    @DeleteMapping
    public ResponseEntity deleteMenu(@RequestParam String ids) {
        return responseOfDelete(service.deleteByIds(ids));
    }

    @GetMapping(path = "/{menuId}")
    @JsonView(MenuVO.Single.class)
    public ResponseEntity<MenuVO> getMenuDetail(@PathVariable String menuId) {
        return responseOfGet(service.get(menuId), MenuVO.class, MenuVO.Single.class);
    }

    @PutMapping(path = "/{menuId}")
    @JsonView(MenuVO.Single.class)
    public ResponseEntity<MenuVO> updateMenuDetail(@PathVariable String menuId, @RequestBody MenuVO reqMenu) {
        Menu menu = service.get(menuId);
        if (menu != null) {
            reqMenu.setId(menuId);
            menu = service.update(reqMenu);
        }
        return responseOfPut(menu, MenuVO.class, MenuVO.Single.class);
    }

    @GetMapping(path = "/{menuId}/resources")
    @JsonView(ResourceVO.Single.class)
    public ResponseEntity<Collection<ResourceVO>> getMenuResources(@PathVariable String menuId,
                                                                   @RequestParam(defaultValue = "ENABLE") EnableEnum resourceEnable) {
        return responseOfGet(service.getMenuResources(menuId, EnableEnum.ALL, resourceEnable), ResourceVO.class, ResourceVO.Single.class);
    }

    @GetMapping(path = "/resources")
    @JsonView(MenuVO.MenuWithResource.class)
    public ResponseEntity<Collection<MenuVO>> getMenuResources() {
        return responseOfGet(service.getMenuAllResources(), MenuVO.class, MenuVO.MenuWithResource.class);
    }

    @GetMapping(path = "/{menuId}/roles")
    @JsonView(RoleVO.Single.class)
    public ResponseEntity<Collection<RoleVO>> getMenuRoles(@PathVariable String menuId,
                                                           @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        return responseOfGet(service.getMenuRoles(menuId, EnableEnum.ALL, roleEnable), RoleVO.class, RoleVO.Single.class);
    }

    @GetMapping(path = "/parents")
    @JsonView(MenuVO.Single.class)
    public ResponseEntity<Collection<MenuVO>> getMenuParents(@RequestParam(defaultValue = "ALL") EnableEnum menuEnable) {
        return responseOfGet(service.getMenuParents(menuEnable), MenuVO.class, MenuVO.Single.class);
    }

    @PostMapping(path = "/{menuId}/resources")
    @JsonView(ResourceVO.Single.class)
    public ResponseEntity<ResourceVO> postMenuResource(@PathVariable String menuId, @RequestBody ResourceVO resourceReq) {
        return responseOfPost(service.addResourceOfMenu(menuId, resourceReq), ResourceVO.class, ResourceVO.Single.class);
    }
}
