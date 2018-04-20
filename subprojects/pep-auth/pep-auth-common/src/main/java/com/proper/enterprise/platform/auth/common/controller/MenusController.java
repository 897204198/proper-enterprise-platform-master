package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.auth.common.vo.MenuVO;
import com.proper.enterprise.platform.auth.common.vo.ResourceVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/auth/menus")
public class MenusController extends BaseController {

    @Autowired
    private I18NService i18NService;

    @Autowired
    private MenuService service;

    @GetMapping
    @AuthcIgnore // TODO necessary?
    public ResponseEntity get(String name, String description, String route,
                              @RequestParam(defaultValue = "ENABLE") EnableEnum menuEnable, String parentId) throws Exception {
        return responseOfGet(isPageSearch() ? service.findMenusPagniation(name, description, route, menuEnable, parentId) :
                service.getMenus(name, description, route, menuEnable, parentId));
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends Menu>> updateEnable(@RequestBody Map<String, Object> reqMap) throws Exception {
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(service.updateEnable(idList, enable));
    }

    @PostMapping
    public ResponseEntity<Menu> addMenu(@RequestBody MenuVO reqMenu) {
        Menu menu;
        try {
            menu = service.saveOrUpdateMenu(reqMenu);
        } catch (DataIntegrityViolationException e) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.menu.route.duplicated"));
        }
        return responseOfPost(menu);
    }

    @DeleteMapping
    public ResponseEntity deleteMenu(@RequestParam String ids) throws Exception {
        return responseOfDelete(service.deleteByIds(ids));
    }

    @GetMapping(path = "/{menuId}")
    public ResponseEntity<Menu> getMenuDetail(@PathVariable String menuId) throws Exception {
        return responseOfGet(service.get(menuId, EnableEnum.ALL));
    }

    @PutMapping(path = "/{menuId}")
    public ResponseEntity<Menu> updateMenuDetail(@PathVariable String menuId, @RequestBody MenuVO reqMenu) {
        Menu menu = service.get(menuId, EnableEnum.ALL);
        if (menu != null) {
            reqMenu.setId(menuId);
            try {
                menu = service.saveOrUpdateMenu(reqMenu);
            } catch (DataIntegrityViolationException e) {
                throw new ErrMsgException(i18NService.getMessage("pep.auth.common.menu.route.duplicated"));
            }
        }
        return responseOfPut(menu);
    }

    @GetMapping(path = "/{menuId}/resources")
    public ResponseEntity<Collection<? extends Resource>> getMenuResources(@PathVariable String menuId,
                                                                           @RequestParam(defaultValue = "ENABLE") EnableEnum resourceEnable) {
        return responseOfGet(service.getMenuResources(menuId, EnableEnum.ALL, resourceEnable));
    }

    @GetMapping(path = "/resources")
    public ResponseEntity<Collection> getMenuResources() {
        return responseOfGet(service.getMenuAllResources());
    }

    @GetMapping(path = "/{menuId}/roles")
    public ResponseEntity<Collection<? extends Role>> getMenuRoles(@PathVariable String menuId,
                                                                   @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        return responseOfGet(service.getMenuRoles(menuId, EnableEnum.ALL, roleEnable));
    }

    @GetMapping(path = "/parents")
    public ResponseEntity<Collection<? extends Menu>> getMenuParents() {
        return responseOfGet(service.getMenuParents());
    }

    @PostMapping(path = "/{menuId}/resources")
    public ResponseEntity<Resource> postMenuResource(@PathVariable String menuId, @RequestBody ResourceVO resourceReq) {
        return responseOfPost(service.addResourceOfMenu(menuId, resourceReq));
    }
}
