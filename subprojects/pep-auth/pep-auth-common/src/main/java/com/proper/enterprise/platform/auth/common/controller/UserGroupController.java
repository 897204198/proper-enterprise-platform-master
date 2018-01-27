package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/auth/user-groups")
public class UserGroupController extends BaseController {

    @Autowired
    private UserGroupService service;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Collection<? extends UserGroup>> getGroups(String name, String description, String enable) {
        userService.checkPermission("/auth/user-groups", RequestMethod.GET);
        return responseOfGet(service.getGroups(name, description, enable));
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends UserGroup>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        userService.checkPermission("/auth/user-groups", RequestMethod.PUT);
        Collection<String> idList = (Collection<String>)reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        // TODO 具体实现
        return responseOfPut(service.updateEanble(idList, enable));
    }

    @PostMapping
    public ResponseEntity<UserGroup> create(@RequestBody Map<String, Object> reqMap) {
        userService.checkPermission("/auth/user-groups", RequestMethod.POST);
        return responseOfPost(service.save(reqMap));
    }

    @DeleteMapping
    public ResponseEntity deleteGroups(@RequestParam String ids) {
        userService.checkPermission("/auth/user-groups", RequestMethod.DELETE);
        return responseOfDelete(service.deleteByIds(ids));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGroup> get(@PathVariable String id) {
        userService.checkPermission("/auth/user-groups/" + id, RequestMethod.GET);
        return responseOfGet(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGroup> update(@PathVariable String id, @RequestBody Map<String, Object> reqMap) {
        userService.checkPermission("/auth/user-groups/" + id, RequestMethod.PUT);
        UserGroup group = service.get(id);
        if (group != null) {
            reqMap.put("id", id);
            group = service.save(reqMap);
        }
        return responseOfPut(group);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        userService.checkPermission("/auth/user-groups/" + id, RequestMethod.DELETE);
        UserGroup group = service.get(id);
        if (group != null && CollectionUtil.isEmpty(group.getUsers())) {
            service.delete(group);
            return responseOfDelete(true);
        }
        return responseOfDelete(false);
    }

    @PostMapping("/{id}/role/{roleId}")
    public ResponseEntity<UserGroup> addUserGroupRole(@PathVariable String id, @PathVariable String roleId) {
        userService.checkPermission("/auth/user-groups/" + id + "/role/" + roleId, RequestMethod.POST);
        return responseOfPost(service.saveUserGroupRole(id, roleId));
    }

    @DeleteMapping("/{id}/role/{roleId}")
    public ResponseEntity deleteUserGroupRole(@PathVariable String id, @PathVariable String roleId) {
        userService.checkPermission("/auth/user-groups/" + id + "/role/" + roleId, RequestMethod.DELETE);
        return responseOfDelete(service.deleteUserGroupRole(id, roleId) != null);
    }

    @GetMapping(path = "/{id}/roles")
    public ResponseEntity<Collection<? extends Role>> getGroupRoles(@PathVariable String id) {
        // TODO 具体业务实现
        return responseOfGet(service.getGroupRoles(id));
    }

    /**
     * 添加用户到用户组
     *
     * @param groupId 用户组ID
     * @param userId 用户ID
     * @return 结果
     */
    @PostMapping(path = "/{groupId}/user/{userId}")
    public ResponseEntity<UserGroup> addUserGroup(@PathVariable String groupId, @PathVariable String userId) {
        userService.checkPermission("/auth/user-groups/" + groupId + "/user/" + userId, RequestMethod.POST);
        return responseOfPost(service.addGroupUser(groupId, userId));
    }

    /**
     * 从用户组中删除用户
     *
     * @param groupId 用户组ID
     * @param userId 用户ID
     * @return 结果
     */
    @DeleteMapping(path = "/{groupId}/user/{userId}")
    public ResponseEntity deleteUserGroup(@PathVariable String groupId, @PathVariable String userId) {
        userService.checkPermission("/auth/user-groups/" + groupId + "/user/" + userId, RequestMethod.DELETE);
        return responseOfDelete(service.deleteGroupUser(groupId, userId) != null);
    }

    @GetMapping(path = "/{id}/users")
    public ResponseEntity<Collection<? extends User>> getGroupUsers(@PathVariable String id) {
        // TODO 具体业务实现
        return responseOfGet(service.getGroupUsers(id));
    }

}
