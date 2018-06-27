package com.proper.enterprise.platform.auth.common.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.auth.common.vo.RoleVO;
import com.proper.enterprise.platform.auth.common.vo.UserGroupVO;
import com.proper.enterprise.platform.auth.common.vo.UserVO;
import com.proper.enterprise.platform.core.controller.BaseController;
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

    @GetMapping
    @JsonView(value = {UserGroupVO.Single.class})
    public ResponseEntity<?> getGroups(String name, String description, @RequestParam(defaultValue = "ENABLE") EnableEnum userGroupEnable) {
        return isPageSearch() ? responseOfGet(service.getGroupsPagination(name, description, userGroupEnable),
            UserGroupVO.class, UserGroupVO.Single.class)
            : responseOfGet(service.getGroups(name, description, userGroupEnable), UserGroupVO.class, UserGroupVO.Single.class);
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    @JsonView(value = {UserGroupVO.Single.class})
    public ResponseEntity<Collection<UserGroupVO>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(service.updateEnable(idList, enable), UserGroupVO.class, UserGroupVO.Single.class);
    }

    @PostMapping
    @JsonView(value = {UserGroupVO.Single.class})
    public ResponseEntity<UserGroupVO> create(@RequestBody UserGroupVO userGroupVO) {
        return responseOfPost(service.save(userGroupVO), UserGroupVO.class, UserGroupVO.Single.class);
    }

    @DeleteMapping
    public ResponseEntity deleteGroups(@RequestParam String ids) {
        return responseOfDelete(service.deleteByIds(ids));
    }

    @GetMapping("/{id}")
    @JsonView(value = {UserGroupVO.Single.class})
    public ResponseEntity<UserGroupVO> get(@PathVariable String id) {
        return responseOfGet(service.get(id, EnableEnum.ALL), UserGroupVO.class, UserGroupVO.Single.class);
    }

    @PutMapping("/{id}")
    @JsonView(value = {UserGroupVO.Single.class})
    public ResponseEntity<UserGroupVO> update(@PathVariable String id, @RequestBody UserGroupVO userGroupVO) {
        UserGroup group = service.get(id, EnableEnum.ALL);
        if (group != null) {
            userGroupVO.setId(id);
        }
        return responseOfPut(service.update(userGroupVO), UserGroupVO.class, UserGroupVO.Single.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        UserGroup group = service.get(id);
        return responseOfDelete(service.delete(group));
    }

    @PostMapping("/{id}/role/{roleId}")
    @JsonView(value = {UserGroupVO.GroupWithRole.class})
    public ResponseEntity<UserGroupVO> addUserGroupRole(@PathVariable String id, @PathVariable String roleId) {
        return responseOfPost(service.saveUserGroupRole(id, roleId), UserGroupVO.class, UserGroupVO.GroupWithRole.class);
    }

    @DeleteMapping("/{id}/role/{roleId}")
    public ResponseEntity deleteUserGroupRole(@PathVariable String id, @PathVariable String roleId) {
        return responseOfDelete(service.deleteUserGroupRole(id, roleId) != null);
    }

    @GetMapping(path = "/{id}/roles")
    @JsonView(value = {RoleVO.Single.class})
    public ResponseEntity<Collection<RoleVO>> getGroupRoles(@PathVariable String id,
                                                            @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        return responseOfGet(service.getGroupRoles(id, EnableEnum.ALL, roleEnable), RoleVO.class, RoleVO.Single.class);
    }

    /**
     * 添加用户到用户组
     *
     * @param groupId 用户组ID
     * @param userId  用户ID
     * @return 结果
     */
    @PostMapping(path = "/{groupId}/user/{userId}")
    @JsonView(value = {UserGroupVO.Single.class})
    public ResponseEntity<UserGroupVO> addUserGroup(@PathVariable String groupId, @PathVariable String userId) {
        return responseOfPost(service.addGroupUser(groupId, userId), UserGroupVO.class, UserGroupVO.Single.class);
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
        return responseOfDelete(service.deleteGroupUser(groupId, userId) != null);
    }

    /**
     * 从用户组中删除多个用户
     *
     * @param groupId 用户组ID
     * @return 结果
     */
    @DeleteMapping(path = "/{groupId}/users")
    public ResponseEntity deleteUserGroupUsers(@PathVariable String groupId, @RequestParam Map<String, String> req) {
        return responseOfDelete(service.deleteGroupUsers(groupId, req.get("ids")) != null);
    }

    @GetMapping(path = "/{id}/users")
    @JsonView(value = {UserVO.Single.class})
    public ResponseEntity<Collection<UserVO>> getGroupUsers(@PathVariable String id,
                                                            @RequestParam(defaultValue = "ENABLE") EnableEnum userEnable) {
        return responseOfGet(service.getGroupUsers(id, EnableEnum.ALL, userEnable), UserVO.class, UserVO.Single.class);
    }

    @PutMapping(path = "/{id}/users")
    @JsonView(value = {UserGroupVO.Single.class})
    public ResponseEntity<UserGroupVO> addGroupUserByUserIds(@PathVariable String id, @RequestBody Map<String, String> reqMap) {
        String ids = reqMap.get("ids");
        List<String> idsList = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids)) {
            idsList = Arrays.asList(ids.split(","));
        }
        return responseOfGet(service.addGroupUserByUserIds(id, idsList), UserGroupVO.class, UserGroupVO.Single.class);
    }

}
