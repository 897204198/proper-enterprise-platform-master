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
                                                         Integer pageNo, Integer pageSize) {
        // TODO 具体实现
        return responseOfGet(userService.getUsersByCondiction(username, name, email, phone, enable, pageNo, pageSize));
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends User>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        Collection<String> idList = (Collection<String>)reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        // TODO 具体实现
        return responseOfPut(userService.updateEanble(idList, enable));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserEntity userEntity) {
        return responseOfPost(userService.save(userEntity));
    }

    @DeleteMapping
    public ResponseEntity deleteByIds(@RequestParam String ids) throws Exception {
        // TODO 具体实现
        return responseOfDelete(userService.deleteByIds(ids));
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<User> get(@PathVariable String userId) {
        return responseOfGet(userService.get(userId));
    }

    @PutMapping(path = "/{userId}")
    public ResponseEntity<User> update(@PathVariable String userId, @RequestBody Map<String, Object> reqMap) {
        // TODO 具体实现
        return responseOfPut(userService.save(userId, reqMap));
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity delete(@PathVariable String userId) {
        User user = userService.get(userId);
        if (user != null) {
            userService.delete(user);
        }
        return responseOfDelete(user != null);
    }

    /**
     * 添加用户权限
     *
     * @param userId 用户ID
     * @param roleId 权限ID
     * @return 结果
     */
    @PostMapping(path = "/{userId}/role/{roleId}")
    public ResponseEntity<User> addUserRole(@PathVariable String userId, @PathVariable String roleId) {
        // TODO 具体业务实现
        return responseOfPost(userService.addUserRole(userId, roleId));
    }

    /**
     * 删除用户的权限
     *
     * @param userId 用户ID
     * @param roleId 权限ID
     * @return 结果
     */
    @DeleteMapping(path = "/{userId}/role/{roleId}")
    public ResponseEntity deleteUserRole(@PathVariable String userId, @PathVariable String roleId) {
        // TODO 具体业务实现
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
