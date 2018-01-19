package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.RoleService;
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
    public ResponseEntity<Collection<? extends Role>> get(String name, String description, String parentId, String enable) {
        // TODO 具体实现
        return responseOfGet(roleService.getByCondiction(name, description, parentId, enable));
    }

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody Map<String, Object> reqMap) {
        // TODO 具体实现
        return responseOfPost(roleService.save(reqMap));
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends Role>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        Collection<String> idList = (Collection<String>)reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        // TODO 具体实现
        return responseOfPut(roleService.updateEanble(idList, enable));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam String ids) {
        // TODO 具体实现
        return responseOfDelete(roleService.deleteByIds(ids));
    }

    @GetMapping(path = "/{roleId}")
    public ResponseEntity<Role> find(@PathVariable String roleId) {
        return responseOfGet(roleService.get(roleId));
    }

    @PutMapping(path = "/{roleId}")
    public ResponseEntity<Role> update(@PathVariable String roleId, @RequestBody Map<String, Object> reqMap) {
        Role role = roleService.get(roleId);
        if (role != null) {
            reqMap.put("id", roleId);
            role = roleService.save(reqMap);
        }
        return responseOfPut(role);
    }

    @GetMapping(path = "/parents")
    public ResponseEntity<Collection<? extends Role>> getMenuParents() {
        // TODO 具体实现 获取父节点菜单列表
        return responseOfGet(roleService.getRoleParents());
    }
}
