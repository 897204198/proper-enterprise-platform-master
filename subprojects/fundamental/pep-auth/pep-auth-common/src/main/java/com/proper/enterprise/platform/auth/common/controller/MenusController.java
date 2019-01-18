package com.proper.enterprise.platform.auth.common.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.auth.common.vo.MenuVO;
import com.proper.enterprise.platform.auth.common.vo.ResourceVO;
import com.proper.enterprise.platform.auth.common.vo.RoleVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/auth/menus")
@Api(tags = "/auth/menus")
public class MenusController extends BaseController {

    @Autowired
    private MenuService service;

    @GetMapping
    @JsonView(MenuVO.Single.class)
    @ApiOperation("‍管理端取得App所有用户意见反馈的集合")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<DataTrunk<MenuVO>> get(@ApiParam("‍菜单名称‍") String name, @ApiParam("‍菜单描述‍") String description,
                                                 @ApiParam("‍前端路径‍") String route,
                                                 @ApiParam("‍菜单状态‍") @RequestParam(defaultValue = "ENABLE") EnableEnum menuEnable,
                                                 @ApiParam("‍父节点ID‍") String parentId) {
        if (isPageSearch()) {
            return responseOfGet(BeanUtil.convert(service.findMenusPagination(name, description, route, menuEnable, parentId), MenuVO.class));
        } else {
            Collection<? extends Menu> collection = service.getMenus(name, description, route, menuEnable, parentId);
            DataTrunk<Menu> dataTrunk = new DataTrunk<>();
            dataTrunk.setCount(collection.size());
            dataTrunk.setData(new ArrayList<>(collection));
            return responseOfGet(BeanUtil.convert(dataTrunk, MenuVO.class));
        }
    }

    @SuppressWarnings("unchecked")
    @PutMapping
    @JsonView(MenuVO.Single.class)
    @ApiOperation("‍更新菜单列表的状态信息")
    public ResponseEntity<Collection<MenuVO>> updateEnable(@RequestBody MenuReqMap reqMap) {
        Collection<String> idList = reqMap.getIds();
        boolean enable = reqMap.enable;
        return responseOfPut(BeanUtil.convert(service.updateEnable(idList, enable), MenuVO.class));
    }

    @PostMapping
    @JsonView(MenuVO.Single.class)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍新增菜单")
    public ResponseEntity<MenuVO> addMenu(@RequestBody MenuModelVO menuModelVO) {
        MenuVO reqMenu = new MenuVO();
        BeanUtils.copyProperties(menuModelVO, reqMenu);
        return responseOfPost(BeanUtil.convert(service.save(reqMenu), MenuVO.class));
    }

    @DeleteMapping
    @ApiOperation("‍删除多个菜单信息")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteMenu(@ApiParam(value = "‍菜单信息Id列表(使用\",\"分割)‍", required = true) @RequestParam String ids) {
        return responseOfDelete(service.deleteByIds(ids));
    }

    @GetMapping(path = "/{menuId}")
    @JsonView(MenuVO.Single.class)
    @ApiOperation("‍取得指定菜单ID详情信息")
    public ResponseEntity<MenuVO> getMenuDetail(@ApiParam(value = "‍菜单的id", required = true) @PathVariable String menuId) {
        return responseOfGet(BeanUtil.convert(service.get(menuId), MenuVO.class));
    }

    @PutMapping(path = "/{menuId}")
    @JsonView(MenuVO.Single.class)
    @ApiOperation("‍更新指定菜单ID的信息")
    public ResponseEntity<MenuVO> updateMenuDetail(@ApiParam(value = "‍菜单的id", required = true) @PathVariable String menuId,
                                                   @RequestBody MenuModelVO menuModelVO) {
        Menu menu = service.get(menuId);
        MenuVO reqMenu = new MenuVO();
        BeanUtils.copyProperties(menuModelVO, reqMenu);
        if (menu != null) {
            reqMenu.setId(menuId);
            menu = service.update(reqMenu);
        }
        return responseOfPut(BeanUtil.convert(menu, MenuVO.class));
    }

    @GetMapping(path = "/{menuId}/resources")
    @JsonView(ResourceVO.Single.class)
    @ApiOperation("‍取得指定菜单ID的资源列表信息")
    public ResponseEntity<Collection<ResourceVO>> getMenuResources(@ApiParam(value = "‍菜单的id", required = true) @PathVariable String menuId,
                                                                   @ApiParam("‍资源状态(ALL;ENABLE为默认;DISABLE)‍")
                                                                   @RequestParam(defaultValue = "ENABLE") EnableEnum resourceEnable) {
        return responseOfGet(BeanUtil.convert(service.getMenuResources(menuId, EnableEnum.ALL, resourceEnable), ResourceVO.class));
    }

    @GetMapping(path = "/resources")
    @JsonView(MenuVO.MenuWithResource.class)
    @ApiOperation("‍获取用户菜单以及资源的组合列表（用于构造功能树）")
    public ResponseEntity<Collection<MenuVO>> getMenuResources() {
        return responseOfGet(BeanUtil.convert(service.getMenuAllResources(), MenuVO.class));
    }

    @GetMapping(path = "/{menuId}/roles")
    @JsonView(RoleVO.Single.class)
    @ApiOperation("‍取得指定菜单ID的角色列表信息")
    public ResponseEntity<Collection<RoleVO>> getMenuRoles(@ApiParam(value = "‍菜单的id", required = true) @PathVariable String menuId,
                                                           @ApiParam("‍资源状态(ALL;ENABLE为默认;DISABLE)‍")
                                                           @RequestParam(defaultValue = "ENABLE") EnableEnum roleEnable) {
        return responseOfGet(BeanUtil.convert(service.getMenuRoles(menuId, EnableEnum.ALL, roleEnable), RoleVO.class));
    }

    @GetMapping(path = "/parents")
    @JsonView(MenuVO.Single.class)
    @ApiOperation("‍父节点菜单列表")
    public ResponseEntity<Collection<MenuVO>> getMenuParents(@ApiParam("‍资源状态(ALL为默认;ENABLE;DISABLE)‍")
                                                             @RequestParam(defaultValue = "ALL") EnableEnum menuEnable) {
        return responseOfGet(BeanUtil.convert(service.getMenuParents(menuEnable), MenuVO.class));
    }

    @PostMapping(path = "/{menuId}/resources")
    @JsonView(ResourceVO.Single.class)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍新增资源")
    public ResponseEntity<ResourceVO> postMenuResource(@ApiParam(value = "‍菜单的id", required = true) @PathVariable String menuId,
                                                       @RequestBody ResourceModelVO resourceModelVO) {
        ResourceVO resourceReq = new ResourceVO();
        BeanUtils.copyProperties(resourceModelVO, resourceReq);
        return responseOfPost(BeanUtil.convert(service.addResourceOfMenu(menuId, resourceReq), ResourceVO.class));
    }

    @DeleteMapping(path = "/{menuId}/resources/{resourceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("‍删除资源")
    public ResponseEntity deleteMenuResource(@ApiParam(value = "‍菜单的id", required = true) @PathVariable String menuId,
                                             @ApiParam(value = "‍资源的id", required = true) @PathVariable String resourceId) {
        return responseOfDelete(service.deleteResourceOfMenu(menuId, resourceId));
    }

    public static class MenuReqMap {

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

    public static class MenuModelVO {

        @ApiModelProperty(name = "‍菜单图标样式名称", required = true)
        private String icon;

        @ApiModelProperty(name = "‍状态", required = true)
        private String name;

        @ApiModelProperty(name = "‍前端路径", required = true)
        private String route;

        @ApiModelProperty(name = "‍菜单状态", required = true)
        private Boolean enable;

        @ApiModelProperty(name = "‍父节点ID", required = true)
        private String parentId;

        @ApiModelProperty(name = "‍菜单同级排序号", required = true)
        private Integer sequenceNumber;

        @ApiModelProperty(name = "‍菜单类型编码(0:应用/1:页面/2:功能)", required = true)
        private String menuCode;

        @ApiModelProperty(name = "‍功能描述")
        private String description;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
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

        public Integer getSequenceNumber() {
            return sequenceNumber;
        }

        public void setSequenceNumber(Integer sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
        }

        public String getMenuCode() {
            return menuCode;
        }

        public void setMenuCode(String menuCode) {
            this.menuCode = menuCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }

    }

    public static class ResourceModelVO {

        @ApiModelProperty(name = "‍资源名称", required = true)
        private String name;

        @ApiModelProperty(name = "‍权限对应的Url地址", required = true)
        private String url;

        @ApiModelProperty(name = "‍请求方法", required = true)
        private String method;

        @ApiModelProperty(name = "‍资源状态", required = true)
        private String enable;

        @ApiModelProperty(name = "‍资源类型编码(0:方法)", required = true)
        private String resourceCode;

        @ApiModelProperty(name = "‍标识", required = true)
        private String identifier;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getEnable() {
            return enable;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }

        public String getResourceCode() {
            return resourceCode;
        }

        public void setResourceCode(String resourceCode) {
            this.resourceCode = resourceCode;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }
}
