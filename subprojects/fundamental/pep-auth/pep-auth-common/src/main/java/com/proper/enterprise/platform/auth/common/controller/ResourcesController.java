package com.proper.enterprise.platform.auth.common.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.auth.common.vo.MenuVO;
import com.proper.enterprise.platform.auth.common.vo.ResourceVO;
import com.proper.enterprise.platform.auth.common.vo.RoleVO;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Api(tags = "/auth/resources")
@RequestMapping("/auth/resources")
public class ResourcesController extends BaseController {

    @Autowired
    ResourceService resourceService;

    @SuppressWarnings("unchecked")
    @PutMapping
    @JsonView(ResourceVO.Single.class)
    @ApiOperation("‍更新资源列表的状态信息")
    public ResponseEntity<Collection<ResourceVO>> updateEnable(@RequestBody ResourceReqMap reqMap) {
        Collection<String> idList = reqMap.getIds();
        boolean enable = reqMap.enable;
        return responseOfPut(resourceService.updateEnable(idList, enable), ResourceVO.class, ResourceVO.Single.class);
    }

    @DeleteMapping
    @ApiOperation("‍删除多个资源信息")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteResource(@ApiParam(value = "‍菜单信息Id列表(使用\",\"分割)‍", required = true) @RequestParam String ids) {
        return responseOfDelete(resourceService.deleteByIds(ids));
    }

    @AuthcIgnore
    @GetMapping(path = "/{resourceId}")
    @JsonView(ResourceVO.Single.class)
    @ApiOperation("‍取得指定资源ID的资源信息")
    public ResponseEntity<ResourceVO> find(@ApiParam(value = "‍资源的id", required = true) @PathVariable String resourceId) {
        return responseOfGet(resourceService.get(resourceId), ResourceVO.class, ResourceVO.Single.class);
    }

    @PutMapping(path = "/{resourceId}")
    @JsonView(ResourceVO.Single.class)
    @ApiOperation("‍更新指定资源ID的资源信息")
    public ResponseEntity<ResourceVO> update(@ApiParam(value = "‍资源的id", required = true) @PathVariable String resourceId,
                                             @RequestBody ResourcesModelVO resourcesModelVO) {
        ResourceVO resourceReq = new ResourceVO();
        BeanUtils.copyProperties(resourcesModelVO, resourceReq);
        Resource resource = resourceService.get(resourceId);
        if (resource != null) {
            resourceReq.setId(resourceId);
            resource = resourceService.update(resourceReq);
        }
        return responseOfPut(resource, ResourceVO.class, ResourceVO.Single.class);
    }

    @DeleteMapping(path = "/{resourceId}")
    @ApiOperation("‍删除指定资源ID的资源信息")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@ApiParam(value = "‍资源的id", required = true) @PathVariable String resourceId) {
        Resource resource = resourceService.get(resourceId);
        if (resource != null) {
            resourceService.delete(resource);
        }
        return responseOfDelete(resource != null);
    }

    @GetMapping(path = "/{resourceId}/menus")
    @JsonView(MenuVO.Single.class)
    @ApiOperation("‍取得指定资源ID的菜单列表")
    public ResponseEntity<Collection<MenuVO>> getResourceMenus(@ApiParam(value = "‍资源的id", required = true) @PathVariable String resourceId,
                                                               @ApiParam("‍菜单状态(ALL;ENABLE为默认;DISABLE)‍") @RequestParam(defaultValue = "ENABLE")
                                                                       EnableEnum menuEnable) {
        return responseOfGet(resourceService.getResourceMenus(resourceId, menuEnable), MenuVO.class, MenuVO.Single.class);
    }

    @GetMapping(path = "/{resourceId}/roles")
    @ApiOperation("‍获取指定资源角色集合")
    @JsonView(RoleVO.Single.class)
    public ResponseEntity<Collection<RoleVO>> getResourceRoles(@ApiParam(value = "‍资源的id", required = true) @PathVariable String resourceId,
                                                               @ApiParam("‍角色状态(ALL;ENABLE为默认;DISABLE)‍") @RequestParam(defaultValue = "ENABLE")
                                                                       EnableEnum roleEnable) {
        return responseOfGet(resourceService.getResourceRoles(resourceId, roleEnable), RoleVO.class, RoleVO.Single.class);
    }

    public static class ResourceReqMap {

        @ApiModelProperty("‍ID列表")
        private Collection<String> ids;

        @ApiModelProperty("‍状态")
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

    public static class ResourcesModelVO {

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

        @Override
        public String toString() {
            return JSONUtil.toJSONIgnoreException(this);
        }
    }

}
