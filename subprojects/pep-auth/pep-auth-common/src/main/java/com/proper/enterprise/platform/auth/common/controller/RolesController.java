package com.proper.enterprise.platform.auth.common.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.auth.common.vo.*;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth/roles")
public class RolesController extends BaseController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @JsonView(RoleVO.Single.class)
    public ResponseEntity<?> get(String name, String description, String parentId,
                                 @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        return isPageSearch() ? responseOfGet(roleService.findRolesPagniation(name, description, parentId, roleEnable),
            RoleVO.class, RoleVO.Single.class) :
            responseOfGet(roleService.findRolesLike(name, description, parentId, roleEnable), RoleVO.class, RoleVO.Single.class);
    }

    @PostMapping
    @JsonView(RoleVO.Single.class)
    public ResponseEntity<RoleVO> create(@RequestBody RoleVO roleReq) {
        return responseOfPost(roleService.save(roleReq), RoleVO.class, RoleVO.Single.class);
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    @JsonView(RoleVO.Single.class)
    public ResponseEntity<Collection<RoleVO>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(roleService.updateEnable(idList, enable), RoleVO.class, RoleVO.Single.class);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String ids) {
        return responseOfDelete(roleService.deleteByIds(ids));
    }

    @GetMapping(path = "/{roleId}")
    @JsonView(RoleVO.Single.class)
    public ResponseEntity<RoleVO> find(@PathVariable String roleId) {
        return responseOfGet(roleService.get(roleId), RoleVO.class, RoleVO.Single.class);
    }

    @PutMapping(path = "/{roleId}")
    @JsonView(RoleVO.Single.class)
    public ResponseEntity<RoleVO> update(@PathVariable String roleId, @RequestBody RoleVO roleReq) {
        roleReq.setId(roleId);
        return responseOfPut(roleService.update(roleReq), RoleVO.class, RoleVO.Single.class);
    }

    @GetMapping(path = "/{roleId}/menus")
    @JsonView(value = MenuVO.Single.class)
    public ResponseEntity<Collection<MenuVO>> getRoleMenus(@PathVariable String roleId,
                                                           @RequestParam(defaultValue = "ENABLE") EnableEnum menuEnable) {
        return responseOfGet(roleService.getRoleMenus(roleId, menuEnable), MenuVO.class, MenuVO.Single.class);
    }


    @SuppressWarnings("unchecked")
    @PostMapping(path = "/{roleId}/menus")
    @JsonView(RoleVO.RoleWithMenu.class)
    public ResponseEntity<RoleVO> addRoleMenus(@PathVariable String roleId, @RequestBody Map<String, Object> reqMap) {
        List<String> ids = (List<String>) reqMap.get("ids");
        return responseOfPost(roleService.addRoleMenus(roleId, ids), RoleVO.class, RoleVO.RoleWithMenu.class);
    }

    @DeleteMapping("/{roleId}/menus")
    public ResponseEntity deleteRoleMenus(@PathVariable String roleId, @RequestParam String ids) {
        return responseOfDelete(roleService.deleteRoleMenus(roleId, ids) != null);
    }

    @GetMapping(path = "/{roleId}/resources")
    @JsonView(ResourceVO.Single.class)
    public ResponseEntity<Collection<ResourceVO>> getRoleResources(@PathVariable String roleId,
                                                                   @RequestParam(defaultValue = "ENABLE") EnableEnum resourceEnable) {
        return responseOfGet(roleService.getRoleResources(roleId, resourceEnable), ResourceVO.class, ResourceVO.Single.class);
    }

    @SuppressWarnings("unchecked")
    @PostMapping(path = "/{roleId}/resources")
    @JsonView(RoleVO.RoleWithResource.class)
    public ResponseEntity<RoleVO> addRoleResources(@PathVariable String roleId, @RequestBody Map<String, Object> reqMap) {
        List<String> ids = (List<String>) reqMap.get("ids");
        return responseOfPost(roleService.addRoleResources(roleId, ids), RoleVO.class, RoleVO.RoleWithResource.class);
    }

    @DeleteMapping("/{roleId}/resources")
    public ResponseEntity deleteRoleResources(@PathVariable String roleId, @RequestParam String ids) {
        return responseOfDelete(roleService.deleteRoleResources(roleId, ids) != null);
    }

    @GetMapping(path = "/{roleId}/users")
    @JsonView(value = UserVO.Single.class)
    public ResponseEntity<Collection<UserVO>> getRoleUsers(@PathVariable String roleId,
                                                           @RequestParam(defaultValue = "ENABLE") EnableEnum userEnable) {
        return responseOfGet(roleService.getRoleUsers(roleId, EnableEnum.ALL, userEnable), UserVO.class, UserVO.Single.class);
    }

    @GetMapping(path = "/{roleId}/user-groups")
    @JsonView(value = UserGroupVO.Single.class)
    public ResponseEntity<Collection<UserGroupVO>> getRoleUserGroups(@PathVariable String roleId,
                                                                     @RequestParam(defaultValue = "ENABLE") EnableEnum userGroupEnable) {
        return responseOfGet(roleService.getRoleUserGroups(roleId, EnableEnum.ALL, userGroupEnable), UserGroupVO.class, UserGroupVO.Single.class);
    }

    @GetMapping(path = "/{roleId}/parents")
    @JsonView(value = RoleVO.Single.class)
    public ResponseEntity<Collection<RoleVO>> getMenuParents(@PathVariable String roleId) {
        return responseOfGet(roleService.findRoleParents(roleId), RoleVO.class, RoleVO.Single.class);
    }
}
