package com.proper.enterprise.platform.auth.common.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.vo.ChangePasswordParam;
import com.proper.enterprise.platform.auth.common.vo.RoleVO;
import com.proper.enterprise.platform.auth.common.vo.UserGroupVO;
import com.proper.enterprise.platform.auth.common.vo.UserVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.security.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping(path = "/auth/users")
public class UsersController extends BaseController {

    @Autowired
    private UserService userService;


    @PostMapping
    @JsonView(UserVO.Single.class)
    public ResponseEntity<UserVO> create(@RequestBody UserVO userVO) {
        return responseOfPost(userService.save(userVO), UserVO.class, UserVO.Single.class);
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
        userService.addUserRole(userId, roleId);
        return responseOfPost("");
    }

    @DeleteMapping
    public ResponseEntity deleteByIds(@RequestParam String ids) {
        return responseOfDelete(userService.deleteByIds(ids));
    }

    /**
     * 删除指定用户ID的用户信息
     */
    @DeleteMapping(path = "/{userId}")
    public ResponseEntity delete(@PathVariable String userId) {
        return responseOfDelete(userService.delete(userId));
    }


    @SuppressWarnings("unchecked")
    @PutMapping
    @JsonView(UserVO.Single.class)
    public ResponseEntity<Collection<UserVO>> updateEnable(@RequestBody Map<String, Object> reqMap) {
        Collection<String> idList = (Collection<String>) reqMap.get("ids");
        boolean enable = (boolean) reqMap.get("enable");
        return responseOfPut(userService.updateEnable(idList, enable), UserVO.class, UserVO.Single.class);
    }

    @SuppressWarnings("unchecked")
    @PutMapping(path = "/password")
    @JsonView(UserVO.Single.class)
    public ResponseEntity<UserVO> changePassword(@RequestBody ChangePasswordParam changePasswordParam) {
        return responseOfPut(userService.updateChangePassword(Authentication.getCurrentUserId(),
            changePasswordParam.getOldPassword(), changePasswordParam.getPassword()),
            UserVO.class, UserVO.Single.class);
    }

    @SuppressWarnings("unchecked")
    @PutMapping(path = "/password/{password}")
    @JsonView(UserVO.Single.class)
    public ResponseEntity<UserVO> resetPassword(@PathVariable String password) {
        return responseOfPut(userService.updateResetPassword(Authentication.getCurrentUserId(),
            password), UserVO.class, UserVO.Single.class);
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
        return responseOfDelete(userService.deleteUserRole(userId, roleId) != null);
    }

    /**
     * 更新指定用户ID的用户信息
     */
    @PutMapping(path = "/{userId}")
    @JsonView(UserVO.Single.class)
    public ResponseEntity<UserVO> update(@PathVariable String userId, @RequestBody UserVO userVO) {
        userVO.setId(userId);
        return responseOfPut(userService.update(userVO), UserVO.class, UserVO.Single.class);
    }


    /**
     * 取得指定用户ID的用户信息
     */
    @GetMapping(path = "/{userId}")
    @JsonView(UserVO.Single.class)
    public ResponseEntity<UserVO> get(@PathVariable String userId) {
        return responseOfGet(userService.get(userId), UserVO.class, UserVO.Single.class);
    }

    @GetMapping
    @JsonView(UserVO.Single.class)
    public ResponseEntity<?> getUsers(String username, String name, String email, String phone,
                                      @RequestParam(defaultValue = "ENABLE") EnableEnum userEnable) {
        return isPageSearch() ? responseOfGet(userService.findUsersPagination(username, name, email, phone, userEnable),
            UserVO.class, UserVO.Single.class) :
            responseOfGet(userService.getUsersByAndCondition(username, name, email, phone, userEnable), UserVO.class, UserVO.Single.class);
    }

    @GetMapping(path = "/query")
    @JsonView(UserVO.Single.class)
    public ResponseEntity<Collection<UserVO>> getUsers(@RequestParam String condition,
                                                       @RequestParam(defaultValue = "ENABLE") EnableEnum enable) {
        return responseOfGet(userService.getUsersByOrCondition(condition, enable), UserVO.class, UserVO.Single.class);
    }

    @GetMapping(path = "/{userId}/user-groups")
    @JsonView(UserGroupVO.Single.class)
    public ResponseEntity<Collection<UserGroupVO>> getUserGroups(@PathVariable String userId,
                                                                 @RequestParam(defaultValue = "ENABLE") EnableEnum userGroupEnable) {
        return responseOfGet(userService.getUserGroups(userId, userGroupEnable), UserGroupVO.class, UserGroupVO.Single.class);
    }

    @GetMapping(path = "/{userId}/roles")
    @JsonView(RoleVO.Single.class)
    public ResponseEntity<Collection<RoleVO>> getUserRoles(@PathVariable String userId,
                                                           @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        return responseOfGet(userService.getUserRoles(userId, roleEnable), RoleVO.class, RoleVO.Single.class);
    }

    @AuthcIgnore
    @GetMapping(path = "/username/{username}/email/{email}")
    public ResponseEntity<String> checkEmail(@PathVariable String username, @PathVariable String email) {
        userService.checkEmail(username, email);
        return responseOfGet("");
    }


}
