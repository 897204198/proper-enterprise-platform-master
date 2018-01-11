package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/user-groups")
public class UserGroupController extends BaseController {

    @Autowired
    private UserGroupService service;

    @GetMapping
    public ResponseEntity list() {
        return responseOfGet(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGroup> get(@PathVariable String id) {
        return responseOfGet(service.get(id));
    }

    @PostMapping
    public ResponseEntity<UserGroup> create(@RequestBody UserGroupEntity entity) {
        return responseOfPost(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGroup> update(@PathVariable String id, @RequestBody UserGroupEntity entity) {
        UserGroup group = service.get(id);
        if (group != null) {
            group = service.save(entity);
        }
        return responseOfPut(group);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        UserGroup group = service.get(id);
        if (group != null && CollectionUtil.isEmpty(group.getUsers())) {
            service.delete(group);
            return responseOfDelete(true);
        }
        return responseOfDelete(false);
    }

}
