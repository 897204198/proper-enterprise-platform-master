package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserService;
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

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Collection<? extends Role>> get(String name, String description, String parentId, String enable) {
        userService.checkPermission("/auth/roles", RequestMethod.GET);
        return responseOfGet(roleService.getByCondiction(name, description, parentId, enable));
    }

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody Map<String, Object> reqMap) {
        userService.checkPermission("/auth/roles", RequestMethod.POST);
        return responseOfPost(roleService.save(reqMap));
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends Role>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        userService.checkPermission("/auth/roles", RequestMethod.PUT);
        Collection<String> idList = (Collection<String>)reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(roleService.updateEanble(idList, enable));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String ids) {
        userService.checkPermission("/auth/roles", RequestMethod.DELETE);
        return responseOfDelete(roleService.deleteByIds(ids));
    }

    @GetMapping(path = "/{roleId}")
    public ResponseEntity<Role> find(@PathVariable String roleId) {
        userService.checkPermission("/auth/roles/" + roleId, RequestMethod.GET);
        return responseOfGet(roleService.get(roleId));
    }

    @PutMapping(path = "/{roleId}")
    public ResponseEntity<Role> update(@PathVariable String roleId, @RequestBody Map<String, Object> reqMap) {
        userService.checkPermission("/auth/roles/" + roleId, RequestMethod.PUT);
        Role role = roleService.get(roleId);
        if (role != null) {
            reqMap.put("id", roleId);
            role = roleService.save(reqMap);
        }
        return responseOfPut(role);
    }

    @GetMapping(path = "/{roleId}/menus")
    public ResponseEntity<Collection<? extends Menu>> getRoleMenus(@PathVariable String roleId) {
        userService.checkPermission("/auth/roles/" + roleId + "/menus", RequestMethod.GET);
        return responseOfGet(roleService.getRoleMenus(roleId));
    }


    @SuppressWarnings("unchecked")
    @PostMapping(path = "/{roleId}/menus")
    public ResponseEntity<Role> addRoleMenus(@PathVariable String roleId, @RequestBody Map<String, Object> reqMap) {
        userService.checkPermission("/auth/roles/" + roleId + "/menus", RequestMethod.POST);
        String ids = (String)reqMap.get("ids");
        return responseOfPost(roleService.addRoleMenus(roleId, ids));
    }

    @DeleteMapping("/{roleId}/menus")
    public ResponseEntity deleteRoleMenus(@PathVariable String roleId, @RequestParam String ids) {
        userService.checkPermission("/auth/roles/" + roleId + "/menus", RequestMethod.DELETE);
        return responseOfDelete(roleService.deleteRoleMenus(roleId, ids) != null);
    }

    @GetMapping(path = "/{roleId}/resources")
    public ResponseEntity<Collection<? extends Resource>> getRoleResources(@PathVariable String roleId) {
        userService.checkPermission("/auth/roles/" + roleId + "/resources", RequestMethod.GET);
        return responseOfGet(roleService.getRoleResources(roleId));
    }

    @SuppressWarnings("unchecked")
    @PostMapping(path = "/{roleId}/resources")
    public ResponseEntity<Role> addRoleResources(@PathVariable String roleId, @RequestBody Map<String, Object> reqMap) {
        userService.checkPermission("/auth/roles/" + roleId + "/resources", RequestMethod.POST);
        String ids = (String)reqMap.get("ids");
        return responseOfPost(roleService.addRoleResources(roleId, ids));
    }

    @DeleteMapping("/{roleId}/resources")
    public ResponseEntity deleteRoleResources(@PathVariable String roleId, @RequestParam String ids) {
        userService.checkPermission("/auth/roles/" + roleId + "/resources", RequestMethod.DELETE);
        return responseOfDelete(roleService.deleteRoleResources(roleId, ids) != null);
    }

    @GetMapping(path = "/{roleId}/users")
    public ResponseEntity<Collection<? extends User>> getRoleUsers(@PathVariable String roleId) {
        userService.checkPermission("/auth/roles/" + roleId + "/users", RequestMethod.GET);
        return responseOfGet(roleService.getRoleUsers(roleId));
    }

    @GetMapping(path = "/{roleId}/user-groups")
    public ResponseEntity<Collection<? extends UserGroup>> getRoleUserGroups(@PathVariable String roleId) {
        userService.checkPermission("/auth/roles/" + roleId + "/user-groups", RequestMethod.GET);
        return responseOfGet(roleService.getRoleUserGroups(roleId));
    }

    @GetMapping(path = "/parents")
    public ResponseEntity<Collection<? extends Role>> getMenuParents() {
        userService.checkPermission("/auth/roles/parents", RequestMethod.GET);
        return responseOfGet(roleService.getRoleParents());
    }
}
