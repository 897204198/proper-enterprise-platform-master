package com.proper.enterprise.platform.dev.tools.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.dev.tools.service.CacheManagerService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin/dev/cache")
@Api(tags = "/admin/dev/cache")
public class CacheManagerController extends BaseController {

    private String separator = ",";

    @Autowired
    private CacheManagerService cacheManagerService;

    @GetMapping
    @ApiOperation("‍获得缓存区域集合,按照字典序排列返回-缓存区名称组成的集合")
    public ResponseEntity<Collection<String>> listCaches() {
        Collection<String> cacheNames = cacheManagerService.getAllCacheNames();
        if (cacheNames == null) {
            return responseOfGet(new ArrayList<>());
        }
        return responseOfGet(cacheNames);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("‍批量删除缓存区域")
    public ResponseEntity deleteInBatches(@ApiParam(value = "‍缓存区域名称，逗号间隔", required = true) @RequestParam String names) {
        if (StringUtil.isNotBlank(names)) {
            String[] nameArry = names.split(separator);
            cacheManagerService.deleteByNames(Arrays.asList(nameArry));
        }
        return responseOfDelete(true);
    }

    @GetMapping("/{name:.*}")
    @ApiOperation("‍获得缓存区域中的 key 集合,集合按照字典序排列返回")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNo", value = "‍页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "‍每页条数", required = true, paramType = "query", dataType = "int")
    })
    public ResponseEntity<Collection> listCacheKeys(@ApiParam(value = "‍缓存区域名称", required = true) @PathVariable String name,
                                                    Integer pageNo, Integer pageSize) throws IOException {
        return responseOfGet(cacheManagerService.getCacheKeysByCacheNamePageable(name, pageNo, pageSize));
    }

    @DeleteMapping("/{name:.*}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍清理缓存区域，或缓存区域下的 key")
    public ResponseEntity deleteCacheOrKeyByName(@ApiParam(value = "‍缓存区域名称", required = true) @PathVariable String name,
                                                 @ApiParam(value = "‍要删除的缓存区域下的 key,逗号间隔.此参数存在时,该接口清理缓存区域下的 key;否则清理缓存区域", required = true)
                                                 @RequestParam(required = false) String keys) {
        if (StringUtil.isNotBlank(keys)) {
            String[] keyArry = keys.split(separator);
            cacheManagerService.deleteByKeys(name, Arrays.asList(keyArry));
        } else {
            cacheManagerService.deleteByName(name);
        }
        return responseOfDelete(true);
    }

    @ApiOperation("‍获得缓存区域中的 key 集合,返回存储内容的字符形式表示")
    @GetMapping(value = "/{cacheName}/{key}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getCacheWithKey(@ApiParam(value = "‍缓存区域名称", required = true) @PathVariable String cacheName,
                                                  @ApiParam(value = "‍key 字符形式表示", required = true) @PathVariable String key,
                                                  @ApiParam(value = "‍存储的内容的类型名称", required = true) String className) throws Exception {
        Cache cache = cacheManagerService.getCacheByName(cacheName);
        if (cache == null) {
            return responseOfGet(null);
        }
        String value = "";
        Object object = cacheManagerService.getKeyValueByClassName(cacheName, key, className);
        if (object instanceof String) {
            return responseOfGet(object.toString());
        }
        if (null != object) {
            value = JSONUtil.toJSON(object);
        }
        return responseOfGet(value);
    }

    @DeleteMapping("/{cacheName}/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("‍清理缓存区域下的 key")
    public ResponseEntity cleanCacheWithKey(@ApiParam(value = "‍缓存区域名称", required = true) @PathVariable String cacheName,
                                            @ApiParam(value = "‍key 字符形式表示", required = true) @PathVariable String key) {
        List<String> keys = new ArrayList<>(1);
        keys.add(key);
        cacheManagerService.deleteByKeys(cacheName, keys);
        return responseOfDelete(true);
    }

}
