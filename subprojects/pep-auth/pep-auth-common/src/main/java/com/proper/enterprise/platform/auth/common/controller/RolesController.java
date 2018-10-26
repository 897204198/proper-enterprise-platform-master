package com.proper.enterprise.platform.auth.common.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.auth.common.vo.*;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/auth/roles")
@Api(tags = "/auth/roles")
public class RolesController extends BaseController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @ApiOperation("‍获取用户角色列表")
    @JsonView(RoleVO.Single.class)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<RoleVO>> get(@ApiParam("‍角色名称‍") String name, @ApiParam("‍角色描述‍") String description,
                                                 @ApiParam("‍角色父节点ID‍") String parentId,
                                                 @ApiParam("‍角色状态‍") @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        if (isPageSearch()) {
            return responseOfGet(roleService.findRolesPagination(name, description, parentId, roleEnable),
                    RoleVO.class, RoleVO.Single.class);
        } else {
            Collection collection = roleService.findRolesLike(name, description, parentId, roleEnable);
            DataTrunk<Role> dataTrunk = new DataTrunk();
            dataTrunk.setCount(collection.size());
            dataTrunk.setData(collection);
            return responseOfGet(dataTrunk, RoleVO.class, RoleVO.Single.class);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍新增角色")
    @JsonView(RoleVO.Single.class)
    public ResponseEntity<RoleVO> create(@RequestBody RoleReqVO roleReqVO) {
        RoleVO roleReq = new RoleVO();
        BeanUtils.copyProperties(roleReqVO, roleReq);
        return responseOfPost(roleService.save(roleReq), RoleVO.class, RoleVO.Single.class);
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    @JsonView(RoleVO.Single.class)
    @ApiOperation("‍更新角色列表的状态信息")
    public ResponseEntity<Collection<RoleVO>> updateEnable(@RequestBody RoleReqMap reqMap) {
        Collection<String> idList = reqMap.getIds();
        boolean enable = reqMap.enable;
        return responseOfPut(roleService.updateEnable(idList, enable), RoleVO.class, RoleVO.Single.class);
    }

    @DeleteMapping
    @ApiOperation("‍删除多个角色信息")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@ApiParam(value = "‍菜单信息Id列表(使用\",\"分割)‍", required = true) @RequestParam String ids) {
        return responseOfDelete(roleService.deleteByIds(ids));
    }

    @GetMapping(path = "/{roleId}")
    @JsonView(RoleVO.Single.class)
    @ApiOperation("‍取得指定角色ID详情信息")
    public ResponseEntity<RoleVO> find(@ApiParam(value = "‍角色的id", required = true) @PathVariable String roleId) {
        return responseOfGet(roleService.get(roleId), RoleVO.class, RoleVO.Single.class);
    }

    @PutMapping(path = "/{roleId}")
    @JsonView(RoleVO.Single.class)
    @ApiOperation("‍更新指定角色ID的信息")
    public ResponseEntity<RoleVO> update(@ApiParam(value = "‍角色的id", required = true) @PathVariable String roleId,
                                         @RequestBody RoleReqVO roleReqVO) {
        RoleVO roleReq = new RoleVO();
        BeanUtils.copyProperties(roleReqVO, roleReq);
        roleReq.setId(roleId);
        return responseOfPut(roleService.update(roleReq), RoleVO.class, RoleVO.Single.class);
    }

    @GetMapping(path = "/{roleId}/menus")
    @JsonView(value = MenuVO.Single.class)
    @ApiOperation("‍取得指定角色ID的菜单列表")
    public ResponseEntity<Collection<MenuVO>> getRoleMenus(@ApiParam(value = "‍角色的id", required = true) @PathVariable String roleId,
                                                           @ApiParam("‍资源状态(ALL;ENABLE为默认;DISABLE)‍") @RequestParam(defaultValue = "ENABLE")
                                                                   EnableEnum menuEnable) {
        return responseOfGet(roleService.getRoleMenus(roleId, menuEnable), MenuVO.class, MenuVO.Single.class);
    }


    @SuppressWarnings("unchecked")
    @PostMapping(path = "/{roleId}/menus")
    @JsonView(RoleVO.RoleWithMenu.class)
    @ApiOperation("‍角色添加多个菜单")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RoleVO> addRoleMenus(@ApiParam(value = "‍角色的id", required = true) @PathVariable String roleId,
                                               @ApiParam(value = "‍ids列表", required = true) @RequestBody RoleReqList reqMap) {
        List<String> ids = reqMap.getIds();
        return responseOfPost(roleService.addRoleMenus(roleId, ids), RoleVO.class, RoleVO.RoleWithMenu.class);
    }

    @DeleteMapping("/{roleId}/menus")
    @ApiOperation("‍角色删除多个菜单")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteRoleMenus(@ApiParam(value = "‍角色的id", required = true) @PathVariable String roleId,
                                          @ApiParam("‍菜单信息Id列表(使用\",\"分割)") @RequestParam String ids) {
        return responseOfDelete(roleService.deleteRoleMenus(roleId, ids) != null);
    }

    @GetMapping(path = "/{roleId}/resources")
    @JsonView(ResourceVO.Single.class)
    @ApiOperation("‍取得指定角色ID的资源列表")
    public ResponseEntity<Collection<ResourceVO>> getRoleResources(@ApiParam(value = "‍角色的id", required = true) @PathVariable String roleId,
                                                                   @ApiParam("‍资源状态(ALL;ENABLE为默认;DISABLE)‍")
                                                                   @RequestParam(defaultValue = "ENABLE") EnableEnum resourceEnable) {
        return responseOfGet(roleService.getRoleResources(roleId, resourceEnable), ResourceVO.class, ResourceVO.Single.class);
    }

    @SuppressWarnings("unchecked")
    @PostMapping(path = "/{roleId}/resources")
    @JsonView(RoleVO.RoleWithResource.class)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍角色添加多个资源")
    public ResponseEntity<RoleVO> addRoleResources(@ApiParam(value = "‍角色的id", required = true) @PathVariable String roleId,
                                                   @ApiParam(value = "‍资源信息Id列表(使用\",\"分割)", required = true) @RequestBody RoleReqList reqMap) {
        List<String> ids = reqMap.getIds();
        return responseOfPost(roleService.addRoleResources(roleId, ids), RoleVO.class, RoleVO.RoleWithResource.class);
    }

    @DeleteMapping("/{roleId}/resources")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("‍角色删除多个菜单")
    public ResponseEntity deleteRoleResources(@ApiParam(value = "‍角色的id", required = true) @PathVariable String roleId,
                                              @ApiParam(value = "‍菜单信息Id列表(使用\",\"分割)", required = true) @RequestParam String ids) {
        return responseOfDelete(roleService.deleteRoleResources(roleId, ids) != null);
    }

    @GetMapping(path = "/{roleId}/users")
    @JsonView(value = UserVO.Single.class)
    @ApiOperation("‍取得指定角色ID的用户列表")
    public ResponseEntity<Collection<UserVO>> getRoleUsers(@ApiParam(value = "‍角色的id", required = true) @PathVariable String roleId,
                                                           @ApiParam("‍用户状态(ALL;ENABLE为默认;DISABLE)‍") @RequestParam(defaultValue = "ENABLE")
                                                                   EnableEnum userEnable) {
        return responseOfGet(roleService.getRoleUsers(roleId, EnableEnum.ALL, userEnable), UserVO.class, UserVO.Single.class);
    }

    @GetMapping(path = "/{roleId}/user-groups")
    @JsonView(value = UserGroupVO.Single.class)
    @ApiOperation("‍取得指定角色ID的用户组列表")
    public ResponseEntity<Collection<UserGroupVO>> getRoleUserGroups(@ApiParam(value = "‍角色的id", required = true) @PathVariable String roleId,
                                                                     @ApiParam("‍用户组状态(ALL;ENABLE为默认;DISABLE)‍")
                                                                     @RequestParam(defaultValue = "ENABLE") EnableEnum userGroupEnable) {
        return responseOfGet(roleService.getRoleUserGroups(roleId, EnableEnum.ALL, userGroupEnable), UserGroupVO.class, UserGroupVO.Single.class);
    }

    @GetMapping(path = "/{roleId}/parents")
    @JsonView(value = RoleVO.Single.class)
    @ApiOperation("‍获取能够被 roleId 继承的父节点列表")
    public ResponseEntity<Collection<RoleVO>> getMenuParents(@ApiParam(value = "‍角色的id", required = true) @PathVariable String roleId) {
        return responseOfGet(roleService.findRoleParents(roleId), RoleVO.class, RoleVO.Single.class);
    }

    public static class RoleReqVO {

        @ApiModelProperty(name = "‍角色名称", required = true)
        private String name;

        @ApiModelProperty(name = "‍角色描述", required = true)
        private String description;

        @ApiModelProperty(name = "‍角色状态", required = true)
        private Boolean enable;

        @ApiModelProperty(name = "‍角色父节点 ID", required = true)
        private String parentId;

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

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }

    public static class RoleReqMap {

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

    public static class RoleReqList {

        @ApiModelProperty(name = "‍菜单的id集合", required = true)
        private List<String> ids;

        public List<String> getIds() {
            return ids;
        }

        public void setIds(List<String> ids) {
            this.ids = ids;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }

}
