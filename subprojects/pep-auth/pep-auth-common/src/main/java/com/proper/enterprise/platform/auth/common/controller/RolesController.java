package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.auth.common.entity.RoleEntity;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/roles")
public class RolesController extends BaseController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Role> create(@RequestBody RoleEntity res) {
        return responseOfPost(roleService.save(res));
    }

    @RequestMapping(path = "/{roleId}", method = RequestMethod.GET)
    public ResponseEntity<Role> find(@PathVariable String roleId) {
        return responseOfGet(roleService.get(roleId));
    }

    @RequestMapping(path = "/{roleId}", method = RequestMethod.PUT)
    public ResponseEntity<Role> update(@PathVariable String roleId, @RequestBody RoleEntity roleEntity) {
        Role role = roleService.get(roleId);
        if (role != null) {
            role = roleService.save(roleEntity);
        }
        return responseOfPut(role);
    }

    @RequestMapping(path = "/{roleId}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String roleId) {
        Role role = roleService.get(roleId);
        if (role != null) {
            roleService.delete(role);
        }
        return responseOfDelete(role != null);
    }
}
