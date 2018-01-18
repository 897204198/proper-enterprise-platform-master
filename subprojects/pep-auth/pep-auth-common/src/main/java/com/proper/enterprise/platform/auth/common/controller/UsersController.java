package com.proper.enterprise.platform.auth.common.controller;

import com.proper.enterprise.platform.api.auth.model.User;
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

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> get(@PathVariable String userId) {
        return responseOfGet(userService.get(userId));
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<User> update(@PathVariable String userId, @RequestBody UserEntity userEntity) {
        User user = userService.get(userId);
        if (user != null) {
            user = userService.save(userEntity);
        }
        return responseOfPut(user);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
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
    @RequestMapping(path = "/{userId}/role/{roleId}", method = RequestMethod.POST)
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
    @RequestMapping(path = "/{userId}/role/{roleId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUserRole(@PathVariable String userId, @PathVariable String roleId) {
        // TODO 具体业务实现
        return responseOfDelete(userService.deleteUserRole(userId, roleId) != null);
    }

    /**
     * 添加用户到用户组
     *
     * @param userId 用户ID
     * @param groupId 用户组ID
     * @return 结果
     */
    @RequestMapping(path = "/{userId}/group/{groupId}", method = RequestMethod.POST)
    public ResponseEntity<User> addUserGroup(@PathVariable String userId, @PathVariable String groupId) {
        // TODO 具体业务实现
        return responseOfPost(userService.addGroupUser(userId, groupId));
    }

    /**
     * 从用户组中删除用户
     *
     * @param userId 用户ID
     * @param groupId 用户组ID
     * @return 结果
     */
    @RequestMapping(path = "/{userId}/group/{groupId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUserGroup(@PathVariable String userId, @PathVariable String groupId) {
        // TODO 具体业务实现
        return responseOfDelete(userService.deleteGroupUser(userId, groupId) != null);
    }

}
