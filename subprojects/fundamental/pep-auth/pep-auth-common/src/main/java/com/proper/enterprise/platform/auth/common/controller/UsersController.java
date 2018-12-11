package com.proper.enterprise.platform.auth.common.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.RetrievePasswordParam;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.vo.ChangePasswordParam;
import com.proper.enterprise.platform.auth.common.vo.RoleVO;
import com.proper.enterprise.platform.auth.common.vo.UserGroupVO;
import com.proper.enterprise.platform.auth.common.vo.UserVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/auth/users")
@Api(tags = "/auth/users")
public class UsersController extends BaseController {

    @Autowired
    private UserService userService;


    @PostMapping
    @JsonView(UserVO.Single.class)
    @ApiOperation("‍新增用户")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserVO> create(@RequestBody UserModelVO userModelVO) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModelVO, userVO);
        return responseOfPost(userService.save(userVO), UserVO.class, UserVO.Single.class);
    }

    @PostMapping(path = "/{userId}/role/{roleId}")
    @ApiOperation("‍添加用户权限")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addUserRole(@ApiParam(value = "‍‍用户ID‍", required = true) @PathVariable String userId,
                                              @ApiParam(value = "‍‍权限ID‍", required = true) @PathVariable String roleId) {
        userService.addUserRole(userId, roleId);
        return responseOfPost("");
    }

    @DeleteMapping
    @ApiOperation("‍删除多个用户信息")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteByIds(@ApiParam(value = "‍用户信息Id列表(使用\",\"分割)‍", required = true) @RequestParam String ids) {
        return responseOfDelete(userService.deleteByIds(ids));
    }

    @DeleteMapping(path = "/{userId}")
    @ApiOperation("‍删除指定用户ID的用户信息")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@ApiParam(value = "‍‍用户ID‍", required = true) @PathVariable String userId) {
        return responseOfDelete(userService.delete(userId));
    }


    @SuppressWarnings("unchecked")
    @PutMapping
    @JsonView(UserVO.Single.class)
    @ApiOperation("‍更新用户列表的状态信息")
    public ResponseEntity<Collection<UserVO>> updateEnable(@RequestBody UserReqMap reqMap) {
        Collection<String> idList = reqMap.getIds();
        boolean enable = reqMap.enable;
        return responseOfPut(userService.updateEnable(idList, enable), UserVO.class, UserVO.Single.class);
    }

    @SuppressWarnings("unchecked")
    @PutMapping(path = "/password")
    @JsonView(UserVO.Single.class)
    @ApiOperation("‍用户修改密码")
    public ResponseEntity<UserVO> changePassword(@RequestBody ChangePasswordParam changePasswordParam) {
        return responseOfPut(userService.updateChangePassword(Authentication.getCurrentUserId(),
                changePasswordParam.getOldPassword(), changePasswordParam.getPassword()),
                UserVO.class, UserVO.Single.class);
    }

    @AuthcIgnore
    @PutMapping(path = "/password/reset")
    @ApiOperation("‍用户重置密码")
    public ResponseEntity resetPassword(@RequestBody RetrievePasswordParam retrievePasswordParam) {
        userService.updateResetPassword(retrievePasswordParam.getUsername(),
                retrievePasswordParam.getValidCode(),
                retrievePasswordParam.getPassword());
        return responseOfPut("");
    }

    @AuthcIgnore
    @GetMapping(path = "/{username}/validCode")
    @ApiOperation("‍发送验证码")
    public ResponseEntity<String> sendValidCode(@ApiParam(value = "‍用户名‍", required = true) @PathVariable String username) {
        return responseOfGet(userService.sendValidCode(username));
    }

    @DeleteMapping(path = "/{userId}/role/{roleId}")
    @ApiOperation("‍删除用户的权限")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteUserRole(@ApiParam(value = "‍‍‍用户ID‍", required = true) @PathVariable String userId,
                                         @ApiParam(value = "‍权限ID‍", required = true) @PathVariable String roleId) {
        return responseOfDelete(userService.deleteUserRole(userId, roleId) != null);
    }

    @PutMapping(path = "/{userId}")
    @JsonView(UserVO.Single.class)
    @ApiOperation("‍更新指定用户ID的用户信息")
    public ResponseEntity<UserVO> update(@ApiParam(value = "‍‍‍用户ID‍", required = true) @PathVariable String userId,
                                         @RequestBody UserModelVO userModelVO) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModelVO, userVO);
        userVO.setId(userId);
        return responseOfPut(userService.update(userVO), UserVO.class, UserVO.Single.class);
    }

    @PutMapping(path = "/current")
    @JsonView(UserVO.Single.class)
    @ApiOperation("‍更新当前用户的用户信息")
    public ResponseEntity<UserVO> updateCurrentUser(@RequestBody UserVO userVO) {
        userVO.setId(Authentication.getCurrentUserId());
        return responseOfPut(userService.update(userVO), UserVO.class, UserVO.Single.class);
    }

    @GetMapping(path = "/{userId}")
    @JsonView(UserVO.Single.class)
    @ApiOperation("‍取得指定用户ID的用户信息")
    public ResponseEntity<UserVO> get(@ApiParam(value = "‍‍‍用户ID‍", required = true) @PathVariable String userId) {
        return responseOfGet(userService.get(userId), UserVO.class, UserVO.Single.class);
    }

    @GetMapping
    @JsonView(UserVO.Single.class)
    @ApiOperation("‍取得查询用户信息列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<UserVO>> getUsers(@ApiParam("‍用户名") String username, @ApiParam("‍用户显示名") String name,
                                                      @ApiParam("‍用户邮箱") String email, @ApiParam("‍用户手机号") String phone,
                                                      @ApiParam("‍用户状态(ALL;ENABLE为默认;DISABLE)")
                                                      @RequestParam(defaultValue = "ENABLE") EnableEnum userEnable) {
        if (isPageSearch()) {
            return responseOfGet(userService.findUsersPagination(username, name, email, phone, userEnable), UserVO.class, UserVO.Single.class);
        } else {
            Collection collection = userService.getUsersByAndCondition(username, name, email, phone, userEnable);
            DataTrunk<User> dataTrunk = new DataTrunk();
            dataTrunk.setCount(collection.size());
            dataTrunk.setData(collection);
            return responseOfGet(dataTrunk, UserVO.class, UserVO.Single.class);
        }
    }

    @GetMapping(path = "/query")
    @JsonView(UserVO.Single.class)
    @ApiOperation("‍按照用户名或者显示名或者手机号查询用户信息列表")
    public ResponseEntity<Collection<UserVO>> getUsers(@ApiParam("‍用户名或者显示名或者手机号‍") @RequestParam String condition,
                                                       @RequestParam(defaultValue = "ENABLE") EnableEnum enable) {
        return responseOfGet(userService.getUsersByOrCondition(condition, enable), UserVO.class, UserVO.Single.class);
    }

    @GetMapping(path = "/{userId}/user-groups")
    @JsonView(UserGroupVO.Single.class)
    @ApiOperation("‍取得指定用户ID的用户组列表")
    public ResponseEntity<Collection<UserGroupVO>> getUserGroups(@ApiParam(value = "‍‍‍用户ID‍", required = true) @PathVariable String userId,
                                                                 @ApiParam("‍用户组状态(ALL;ENABLE为默认;DISABLE)‍")
                                                                 @RequestParam(defaultValue = "ENABLE") EnableEnum userGroupEnable) {
        return responseOfGet(userService.getUserGroups(userId, userGroupEnable), UserGroupVO.class, UserGroupVO.Single.class);
    }

    @GetMapping(path = "/{userId}/roles")
    @JsonView(RoleVO.Single.class)
    @ApiOperation("‍取得指定用户ID的角色列表")
    public ResponseEntity<Collection<RoleVO>> getUserRoles(@ApiParam(value = "‍‍‍用户ID‍", required = true) @PathVariable String userId,
                                                           @ApiParam("‍角色状态(ALL;ENABLE为默认;DISABLE)‍")
                                                           @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        return responseOfGet(userService.getUserRoles(userId, roleEnable), RoleVO.class, RoleVO.Single.class);
    }

    public static class UserModelVO {

        @ApiModelProperty(name = "‍用户名", required = true)
        private String username;

        @ApiModelProperty(name = "‍密码", required = true)
        private String password;

        @ApiModelProperty(name = "‍显示名", required = true)
        private String name;

        @ApiModelProperty(name = "‍邮箱")
        private String email;

        @ApiModelProperty(name = "‍手机号")
        private String phone;

        @ApiModelProperty(name = "‍菜单同级排序号", required = true)
        private Boolean enable;

        @ApiModelProperty(name = "‍是否为超级用户", required = true)
        private Boolean superuser;

        @ApiModelProperty(name = "‍头像", required = true)
        private String avatar;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public Boolean getSuperuser() {
            return superuser;
        }

        public void setSuperuser(Boolean superuser) {
            this.superuser = superuser;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }

    public static class UserReqMap {

        @ApiModelProperty(name = "‍ID列表", required = true)
        private Collection<String> ids;

        @ApiModelProperty(name = "‍状态", required = true)
        private boolean enable;

        public Collection<String> getIds() {
            return ids;
        }

        public void setIds(Collection<String> ids) {
            this.ids = ids;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }

}
