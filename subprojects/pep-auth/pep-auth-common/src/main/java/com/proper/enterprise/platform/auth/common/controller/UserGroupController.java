package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.vo.UserGroupVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth/user-groups")
public class UserGroupController extends BaseController {

    @Autowired
    private UserGroupService service;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getGroups(String name, String description, @RequestParam(defaultValue = "ENABLE") EnableEnum userGroupEnable) {
        userService.checkPermission("/auth/user-groups", RequestMethod.GET);
        return responseOfGet(isPageSearch() ? service.getGroupsPagniation(name, description, userGroupEnable)
            : service.getGroups(name, description, userGroupEnable));
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends UserGroup>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        userService.checkPermission("/auth/user-groups", RequestMethod.PUT);
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(service.updateEnable(idList, enable));
    }

    @PostMapping
    public ResponseEntity<UserGroup> create(@RequestBody UserGroupVO userGroupVO) {
        userService.checkPermission("/auth/user-groups", RequestMethod.POST);
        return responseOfPost(service.saveOrUpdateUserGroup(userGroupVO));
    }

    @DeleteMapping
    public ResponseEntity deleteGroups(@RequestParam String ids) {
        userService.checkPermission("/auth/user-groups", RequestMethod.DELETE);
        return responseOfDelete(service.deleteByIds(ids));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGroup> get(@PathVariable String id, @RequestParam(defaultValue = "ALL") EnableEnum userGroupEnable) {
        userService.checkPermission("/auth/user-groups/" + id, RequestMethod.GET);
        return responseOfGet(service.get(id, userGroupEnable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGroup> update(@PathVariable String id,
                                            @RequestParam(defaultValue = "ALL") EnableEnum userGroupEnable,
                                            @RequestBody UserGroupVO userGroupVO) {
        userService.checkPermission("/auth/user-groups/" + id, RequestMethod.PUT);
        UserGroup group = service.get(id, userGroupEnable);
        if (group != null) {
            userGroupVO.setId(id);
        }
        return responseOfPut(service.saveOrUpdateUserGroup(userGroupVO));
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
    public ResponseEntity<Collection<? extends Role>> getGroupRoles(@PathVariable String id,
                                                                    @RequestParam(defaultValue = "ALL") EnableEnum userGroupEnable,
                                                                    @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        userService.checkPermission("/auth/user-groups/" + id + "/roles", RequestMethod.GET);
        return responseOfGet(service.getGroupRoles(id, userGroupEnable, roleEnable));
    }

    /**
     * 添加用户到用户组
     *
     * @param groupId 用户组ID
     * @param userId  用户ID
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
     * @param userId  用户ID
     * @return 结果
     */
    @DeleteMapping(path = "/{groupId}/user/{userId}")
    public ResponseEntity deleteUserGroup(@PathVariable String groupId, @PathVariable String userId) {
        userService.checkPermission("/auth/user-groups/" + groupId + "/user/" + userId, RequestMethod.DELETE);
        return responseOfDelete(service.deleteGroupUser(groupId, userId) != null);
    }

    @GetMapping(path = "/{id}/users")
    public ResponseEntity<Collection<? extends User>> getGroupUsers(@PathVariable String id,
                                                                    @RequestParam(defaultValue = "ALL") EnableEnum userGroupEnable,
                                                                    @RequestParam(defaultValue = "ENABLE") EnableEnum userEnable) {
        userService.checkPermission("/auth/user-groups/" + id + "/users", RequestMethod.GET);
        return responseOfGet(service.getGroupUsers(id, userGroupEnable, userEnable));
    }

    @PutMapping(path = "/{id}/users")
    public ResponseEntity<UserGroup> addGroupUserByUserIds(@PathVariable String id, @RequestBody Map<String, String> reqMap) {
        userService.checkPermission("/auth/user-groups/" + id + "/users", RequestMethod.PUT);
        String ids = reqMap.get("ids");
        List<String> idsList = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids)) {
            idsList = Arrays.asList(ids.split(","));
        }
        return responseOfGet(service.addGroupUserByUserIds(id, idsList));
    }

    @DeleteMapping(path = "/{id}/users")
    public ResponseEntity<UserGroup> deleteGroupUserByUserIds(@PathVariable String id, @RequestBody Map<String, String> reqMap) {
        userService.checkPermission("/auth/user-groups/" + id + "/users", RequestMethod.DELETE);
        String ids = reqMap.get("ids");
        List<String> idsList = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids)) {
            idsList = Arrays.asList(ids.split(","));
        }
        return responseOfGet(service.deleteGroupUserByUserIds(id, idsList));
    }
}
