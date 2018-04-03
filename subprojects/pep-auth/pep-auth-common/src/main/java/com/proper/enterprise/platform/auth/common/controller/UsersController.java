package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.PasswordEncryptService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.auth.common.vo.UserVO;
import com.proper.enterprise.platform.core.controller.BaseController;
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

    @Autowired
    PasswordEncryptService pwdService;

    @GetMapping
    public ResponseEntity<?> getUser(String username, String name, String email, String phone,
                                     @RequestParam(defaultValue = "ENABLE") EnableEnum userEnable) {
        userService.checkPermission("/auth/users", RequestMethod.GET);
        return responseOfGet(isPageSearch() ? userService.findUsersPagniation(username, name, email, phone, userEnable) :
                userService.getUsersByCondition(username, name, email, phone, userEnable));
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    public ResponseEntity<Collection<? extends User>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        userService.checkPermission("/auth/users", RequestMethod.PUT);
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(userService.updateEnable(idList, enable));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserVO userVO) {
        userService.checkPermission("/auth/users", RequestMethod.POST);
        userVO.setPassword(pwdService.encrypt(userVO.getPassword()));
        return responseOfPost(userService.saveOrUpdateUser(userVO));
    }

    @DeleteMapping
    public ResponseEntity deleteByIds(@RequestParam String ids) {
        userService.checkPermission("/auth/users", RequestMethod.DELETE);
        return responseOfDelete(userService.deleteByIds(ids));
    }

    /**
     * 取得指定用户ID的用户信息
     */
    @GetMapping(path = "/{userId}")
    public ResponseEntity<User> get(@PathVariable String userId, @RequestParam(defaultValue = "ALL") EnableEnum userEnable) {
        userService.checkPermission("/auth/users/" + userId, RequestMethod.GET);
        return responseOfGet(userService.get(userId, userEnable));
    }

    /**
     * 更新指定用户ID的用户信息
     */
    @PutMapping(path = "/{userId}")
    public ResponseEntity<User> update(@PathVariable String userId, @RequestBody UserVO userVO) {
        userService.checkPermission("/auth/users/" + userId, RequestMethod.PUT);
        User user = userService.get(userId, EnableEnum.ALL);
        if (user != null) {
            userVO.setId(userId);
            if (!user.getPassword().equals(userVO.getPassword())) {
                userVO.setPassword(pwdService.encrypt(userVO.getPassword()));
            }
        }
        return responseOfPut(userService.saveOrUpdateUser(userVO));
    }

    /**
     * 删除指定用户ID的用户信息
     */
    @DeleteMapping(path = "/{userId}")
    public ResponseEntity delete(@PathVariable String userId) {
        userService.checkPermission("/auth/users/" + userId, RequestMethod.DELETE);
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
    public ResponseEntity<String> addUserRole(@PathVariable String userId, @PathVariable String roleId) {
        userService.checkPermission("/auth/users/" + userId + "/role/" + roleId, RequestMethod.POST);
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
    public ResponseEntity deleteUserRole(@PathVariable String userId, @PathVariable String roleId) {
        userService.checkPermission("/auth/users/" + userId + "/role/" + roleId, RequestMethod.DELETE);
        return responseOfDelete(userService.deleteUserRole(userId, roleId) != null);
    }

    @GetMapping(path = "/{userId}/user-groups")
    public ResponseEntity<Collection<? extends UserGroup>> getUserGroups(@PathVariable String userId,
                                                                         @RequestParam(defaultValue = "ALL") EnableEnum userEnable,
                                                                         @RequestParam(defaultValue = "ENABLE") EnableEnum userGroupEnable) {
        userService.checkPermission("/auth/users/" + userId + "/user-groups", RequestMethod.GET);
        return responseOfGet(userService.getUserGroups(userId, userEnable, userGroupEnable));
    }

    @GetMapping(path = "/{userId}/roles")
    public ResponseEntity<Collection<? extends Role>> getUserRoles(@PathVariable String userId,
                                                                   @RequestParam(defaultValue = "ALL") EnableEnum userEnable,
                                                                   @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        userService.checkPermission("/auth/users/" + userId + "/roles", RequestMethod.GET);
        return responseOfGet(userService.getUserRoles(userId, userEnable, roleEnable));
    }

    @GetMapping(path = "/query")
    public ResponseEntity<Collection<? extends User>> queryUser(@RequestParam String condition) {
        userService.checkPermission("/auth/users/query", RequestMethod.GET);
        return responseOfGet(userService.getUsersByCondition(condition));
    }
}
