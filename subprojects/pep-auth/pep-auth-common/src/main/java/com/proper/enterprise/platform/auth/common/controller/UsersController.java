package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.UserEntity;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping(path = "/auth/users")
public class UsersController extends BaseController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<DataTrunk<? extends User>> getUser(String username, String name, String email, String phone, String enable,
                                                             Integer pageNo, Integer pageSize) throws Exception {
        userService.checkPermission("/auth/users", RequestMethod.GET);
        return responseOfGet(userService.getUsersByCondiction(username, name, email, phone, enable, pageNo, pageSize));
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends User>> updateEnable(@RequestBody Map<String, Object> reqMap) throws Exception {
        userService.checkPermission("/auth/users", RequestMethod.PUT);
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(userService.updateEanble(idList, enable));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserEntity userEntity) throws Exception {
        userService.checkPermission("/auth/users", RequestMethod.POST);
        return responseOfPost(userService.save(userEntity));
    }

    @DeleteMapping
    public ResponseEntity deleteByIds(@RequestParam String ids) throws Exception {
        userService.checkPermission("/auth/users", RequestMethod.DELETE);
        return responseOfDelete(userService.deleteByIds(ids));
    }

    /**
     * 取得指定用户ID的用户信息
     */
    @GetMapping(path = "/{userId}")
    public ResponseEntity<User> get(@PathVariable String userId) throws Exception {
        userService.checkPermission("/auth/users/{userId}", RequestMethod.GET);
        return responseOfGet(userService.get(userId));
    }

    /**
     * 更新指定用户ID的用户信息
     */
    @PutMapping(path = "/{userId}")
    public ResponseEntity<String> update(@PathVariable String userId, @RequestBody Map<String, Object> userMap) throws Exception {
        userService.checkPermission("/auth/users/{userId}", RequestMethod.PUT);
        userMap.put("id", userId);
        userService.updateByUser(userMap);
        return responseOfPut("");
    }

    /**
     * 删除指定用户ID的用户信息
     */
    @DeleteMapping(path = "/{userId}")
    public ResponseEntity delete(@PathVariable String userId) throws Exception {
        userService.checkPermission("/auth/users/{userId}", RequestMethod.DELETE);
        return responseOfDelete(userService.delete(userId));
    }

    /**
     * 添加用户权限
     *
     * @param userId 用户ID
     * @param roleId 权限ID
     * @return 结果
     */
    @PostMapping(path = "/{userId}/role/{roleId}")
    public ResponseEntity<String> addUserRole(@PathVariable String userId, @PathVariable String roleId) throws Exception {
        userService.checkPermission("/auth/users/{userId}/role/{roleId}", RequestMethod.POST);
        userService.addUserRole(userId, roleId);
        return responseOfPost("");
    }

    /**
     * 删除用户的权限
     *
     * @param userId 用户ID
     * @param roleId 权限ID
     * @return 结果
     */
    @DeleteMapping(path = "/{userId}/role/{roleId}")
    public ResponseEntity deleteUserRole(@PathVariable String userId, @PathVariable String roleId) throws Exception {
        userService.checkPermission("/auth/users/{userId}/role/{roleId}", RequestMethod.DELETE);
        return responseOfDelete(userService.deleteUserRole(userId, roleId) != null);
    }

    @GetMapping(path = "/{userId}/user-groups")
    public ResponseEntity<Collection<? extends UserGroup>> getUserGroups(@PathVariable String userId) {
        // TODO 具体业务实现
        return responseOfGet(userService.getUserGroups(userId));
    }

    @GetMapping(path = "/{userId}/roles")
    public ResponseEntity<Collection<? extends Role>> getUserRoles(@PathVariable String userId) {
        // TODO 具体业务实现
        return responseOfGet(userService.getUserRoles(userId));
    }

    @GetMapping(path = "/query")
    public ResponseEntity<Collection<? extends User>> queryUser(@RequestParam String condition) {
        // TODO 具体业务实现
        return responseOfGet(userService.getUsersByCondiction(condition));
    }
}
