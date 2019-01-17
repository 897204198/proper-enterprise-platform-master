package com.proper.enterprise.platform.auth.common.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.auth.common.vo.RoleVO;
import com.proper.enterprise.platform.auth.common.vo.UserGroupVO;
import com.proper.enterprise.platform.auth.common.vo.UserVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth/user-groups")
@Api(tags = "/auth/user-groups")
public class UserGroupController extends BaseController {

    @Autowired
    private UserGroupService service;

    @GetMapping
    @JsonView(value = {UserGroupVO.Single.class})
    @ApiOperation("‍取得查询用户组信息列表")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<UserGroupVO>> getGroups(@ApiParam("‍用户组名") String name, @ApiParam("‍用户组描述") String description,
                                                            @ApiParam("‍用户组状态(ALL;ENABLE为默认;DISABLE)")
                                                            @RequestParam(defaultValue = "ENABLE") EnableEnum userGroupEnable) {
        if (isPageSearch()) {
            return responseOfGet(BeanUtil.convert(service.getGroupsPagination(name, description, userGroupEnable), UserGroupVO.class));
        } else {
            Collection<? extends UserGroup> collection = service.getGroups(name, description, userGroupEnable);
            DataTrunk<UserGroup> dataTrunk = new DataTrunk<>();
            dataTrunk.setCount(collection.size());
            dataTrunk.setData(new ArrayList<>(collection));
            return responseOfGet(BeanUtil.convert(dataTrunk, UserGroupVO.class));
        }
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    @ApiOperation("‍更新用户组列表的状态信息")
    @JsonView(value = {UserGroupVO.Single.class})
    public ResponseEntity<Collection<UserGroupVO>> updateEnable(@RequestBody UserGroupReqMap reqMap) {
        Collection<String> idList = reqMap.getIds();
        boolean enable = reqMap.enable;
        return responseOfPut(BeanUtil.convert(service.updateEnable(idList, enable), UserGroupVO.class));
    }

    @PostMapping
    @JsonView(value = {UserGroupVO.Single.class})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍新增用户组")
    public ResponseEntity<UserGroupVO> create(@RequestBody UserGroupModelVO userGroupModelVO) {
        UserGroupVO userGroupVO = new UserGroupVO();
        BeanUtils.copyProperties(userGroupModelVO, userGroupVO);
        return responseOfPost(BeanUtil.convert(service.save(userGroupVO), UserGroupVO.class));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("‍删除多个用户组信息")
    public ResponseEntity deleteGroups(@ApiParam(value = "‍菜单信息Id列表(使用\",\"分割)‍", required = true) @RequestParam String ids) {
        return responseOfDelete(service.deleteByIds(ids));
    }

    @GetMapping("/{id}")
    @JsonView(value = {UserGroupVO.Single.class})
    @ApiOperation("‍取得指定用户组ID的用户组信息")
    public ResponseEntity<UserGroupVO> get(@ApiParam(value = "‍用户组的ID‍", required = true) @PathVariable String id) {
        return responseOfGet(BeanUtil.convert(service.get(id, EnableEnum.ALL), UserGroupVO.class));
    }

    @PutMapping("/{id}")
    @JsonView(value = {UserGroupVO.Single.class})
    @ApiOperation("‍更新用户组列表的状态信息")
    public ResponseEntity<UserGroupVO> update(@ApiParam(value = "‍用户组的ID‍", required = true) @PathVariable String id,
                                              @RequestBody UserGroupModelVO userGroupModelVO) {
        UserGroupVO userGroupVO = new UserGroupVO();
        BeanUtils.copyProperties(userGroupModelVO, userGroupVO);
        UserGroup group = service.get(id, EnableEnum.ALL);
        if (group != null) {
            userGroupVO.setId(id);
        }
        return responseOfPut(BeanUtil.convert(service.update(userGroupVO), UserGroupVO.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍删除多个用户组信息")
    public ResponseEntity delete(@ApiParam(value = "‍用户组的ID‍", required = true) @PathVariable String id) {
        UserGroup group = service.get(id);
        return responseOfDelete(service.delete(group));
    }

    @PostMapping("/{id}/role/{roleId}")
    @JsonView(value = {UserGroupVO.GroupWithRole.class})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍用户组添加新的角色")
    public ResponseEntity<UserGroupVO> addUserGroupRole(@ApiParam(value = "‍用户组ID‍", required = true) @PathVariable String id,
                                                        @ApiParam(value = "‍角色ID‍", required = true) @PathVariable String roleId) {
        return responseOfPost(BeanUtil.convert(service.saveUserGroupRole(id, roleId), UserGroupVO.class));
    }

    @DeleteMapping("/{id}/role/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("‍用户组删除角色")
    public ResponseEntity deleteUserGroupRole(@ApiParam(value = "‍用户组ID‍", required = true) @PathVariable String id,
                                              @ApiParam(value = "‍‍角色ID‍‍", required = true) @PathVariable String roleId) {
        return responseOfDelete(service.deleteUserGroupRole(id, roleId) != null);
    }

    @GetMapping(path = "/{id}/roles")
    @JsonView(value = {RoleVO.Single.class})
    @ApiOperation("‍取得指定用户组ID的角色列表")
    public ResponseEntity<Collection<RoleVO>> getGroupRoles(@ApiParam(value = "‍用户组ID‍", required = true) @PathVariable String id,
                                                            @ApiParam("‍角色组状态(ALL;ENABLE为默认;DISABLE)‍")
                                                            @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        return responseOfGet(BeanUtil.convert(service.getGroupRoles(id, EnableEnum.ALL, roleEnable), RoleVO.class));
    }

    @PostMapping(path = "/{groupId}/user/{userId}")
    @JsonView(value = {UserGroupVO.Single.class})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍用户组添加用户")
    public ResponseEntity<UserGroupVO> addUserGroup(@ApiParam(value = "‍用户组ID‍", required = true) @PathVariable String groupId,
                                                    @ApiParam(value = "‍用户ID‍", required = true) @PathVariable String userId) {
        return responseOfPost(BeanUtil.convert(service.addGroupUser(groupId, userId), UserGroupVO.class));
    }

    @DeleteMapping(path = "/{groupId}/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("‍用户组删除用户")
    public ResponseEntity deleteUserGroup(@ApiParam(value = "‍用户组ID‍", required = true) @PathVariable String groupId,
                                          @ApiParam(value = "‍用户ID‍", required = true) @PathVariable String userId) {
        return responseOfDelete(service.deleteGroupUser(groupId, userId) != null);
    }

    @DeleteMapping(path = "/{groupId}/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("‍删除指定用户组ID的用户信息")
    public ResponseEntity deleteUserGroupUsers(@ApiParam(value = "‍用户组ID‍", required = true) @PathVariable String groupId,
                                               @RequestParam String ids) {
        return responseOfDelete(service.deleteGroupUsers(groupId, ids) != null);
    }

    @GetMapping(path = "/{id}/users")
    @JsonView(value = {UserVO.Single.class})
    @ApiOperation("‍取得指定用户组ID的用户列表")
    public ResponseEntity<Collection<UserVO>> getGroupUsers(@ApiParam(value = "‍用户组ID‍", required = true) @PathVariable String id,
                                                            @ApiParam("‍用户状态(ALL;ENABLE为默认;DISABLE)‍")
                                                            @RequestParam(defaultValue = "ENABLE") EnableEnum userEnable) {
        return responseOfGet(BeanUtil.convert(service.getGroupUsers(id, EnableEnum.ALL, userEnable), UserVO.class));
    }

    @PutMapping(path = "/{id}/users")
    @JsonView(value = {UserGroupVO.Single.class})
    @ApiOperation("‍更新指定用户组ID的用户信息")
    public ResponseEntity<UserGroupVO> addGroupUserByUserIds(@PathVariable String id, @RequestBody UserIdsMap reqMap) {
        String ids = reqMap.getIds();
        List<String> idsList = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids)) {
            idsList = Arrays.asList(ids.split(","));
        }
        return responseOfGet(BeanUtil.convert(service.addGroupUserByUserIds(id, idsList), UserGroupVO.class));
    }

    public static class UserGroupReqMap {

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

    public static class UserGroupModelVO {

        @ApiModelProperty(name = "‍用户组名称", required = true)
        private String name;

        @ApiModelProperty(name = "‍用户组描述信息", required = true)
        private String description;

        @ApiModelProperty(name = "‍顺序", required = true)
        private Integer seq;

        @ApiModelProperty(name = "‍用户组状态", required = true)
        private Boolean enable;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getSeq() {
            return seq;
        }

        public void setSeq(Integer seq) {
            this.seq = seq;
        }

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }

    public static class UserIdsMap {

        private String ids;

        public String getIds() {
            return ids;
        }

        public void setIds(String ids) {
            this.ids = ids;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }
}
