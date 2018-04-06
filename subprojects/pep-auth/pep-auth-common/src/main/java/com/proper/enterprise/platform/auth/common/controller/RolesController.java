package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.auth.common.vo.RoleVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/auth/roles")
public class RolesController extends BaseController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<?> get(String name, String description, String parentId,
                                 @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        return responseOfGet(isPageSearch() ? roleService.findRolesPagniation(name, description, parentId, roleEnable) :
                roleService.getByCondition(name, description, parentId, roleEnable));
    }

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody RoleVO roleReq) {
        return responseOfPost(roleService.saveOrUpdateRole(roleReq));
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends Role>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(roleService.updateEnable(idList, enable));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String ids) {
        return responseOfDelete(roleService.deleteByIds(ids));
    }

    @GetMapping(path = "/{roleId}")
    public ResponseEntity<Role> find(@PathVariable String roleId, @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        return responseOfGet(roleService.get(roleId, roleEnable));
    }

    @PutMapping(path = "/{roleId}")
    public ResponseEntity<Role> update(@PathVariable String roleId, @RequestBody RoleVO roleReq) {
        Role role = roleService.get(roleId, EnableEnum.ALL);
        if (role != null) {
            roleReq.setId(roleId);
            role = roleService.saveOrUpdateRole(roleReq);
        }
        return responseOfPut(role);
    }

    @GetMapping(path = "/{roleId}/menus")
    public ResponseEntity<Collection<? extends Menu>> getRoleMenus(@PathVariable String roleId,
                                                                   @RequestParam(defaultValue = "ALL") EnableEnum roleEnable,
                                                                   @RequestParam(defaultValue = "ENABLE") EnableEnum menuEnable) {
        return responseOfGet(roleService.getRoleMenus(roleId, roleEnable, menuEnable));
    }


    @SuppressWarnings("unchecked")
    @PostMapping(path = "/{roleId}/menus")
    public ResponseEntity<Role> addRoleMenus(@PathVariable String roleId, @RequestBody Map<String, Object> reqMap) {
        String ids = (String) reqMap.get("ids");
        return responseOfPost(roleService.addRoleMenus(roleId, ids));
    }

    @DeleteMapping("/{roleId}/menus")
    public ResponseEntity deleteRoleMenus(@PathVariable String roleId, @RequestParam String ids) {
        return responseOfDelete(roleService.deleteRoleMenus(roleId, ids) != null);
    }

    @GetMapping(path = "/{roleId}/resources")
    public ResponseEntity<Collection<? extends Resource>> getRoleResources(@PathVariable String roleId,
                                                                           @RequestParam(defaultValue = "ALL") EnableEnum roleEnable,
                                                                           @RequestParam(defaultValue = "ENABLE") EnableEnum resourceEnable) {
        return responseOfGet(roleService.getRoleResources(roleId, roleEnable, resourceEnable));
    }

    @SuppressWarnings("unchecked")
    @PostMapping(path = "/{roleId}/resources")
    public ResponseEntity<Role> addRoleResources(@PathVariable String roleId, @RequestBody Map<String, Object> reqMap) {
        String ids = (String) reqMap.get("ids");
        return responseOfPost(roleService.addRoleResources(roleId, ids));
    }

    @DeleteMapping("/{roleId}/resources")
    public ResponseEntity deleteRoleResources(@PathVariable String roleId, @RequestParam String ids) {
        return responseOfDelete(roleService.deleteRoleResources(roleId, ids) != null);
    }

    @GetMapping(path = "/{roleId}/users")
    public ResponseEntity<Collection<? extends User>> getRoleUsers(@PathVariable String roleId,
                                                                   @RequestParam(defaultValue = "ALL") EnableEnum roleEnable,
                                                                   @RequestParam(defaultValue = "ENABLE") EnableEnum userEnable) {
        return responseOfGet(roleService.getRoleUsers(roleId, roleEnable, userEnable));
    }

    @GetMapping(path = "/{roleId}/user-groups")
    public ResponseEntity<Collection<? extends UserGroup>> getRoleUserGroups(@PathVariable String roleId,
                                                                             @RequestParam(defaultValue = "ALL") EnableEnum roleEnable,
                                                                             @RequestParam(defaultValue = "ENABLE") EnableEnum userGroupEnable) {
        return responseOfGet(roleService.getRoleUserGroups(roleId, roleEnable, userGroupEnable));
    }

    @GetMapping(path = "/{roleId}/parents")
    public ResponseEntity<Collection<? extends Role>> getMenuParents(@PathVariable String roleId) {
        return responseOfGet(roleService.getRoleParents(roleId));
    }
}
