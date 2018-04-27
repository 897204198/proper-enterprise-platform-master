package com.proper.enterprise.platform.dev.tools.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.dev.tools.service.CacheManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin/dev/cache")
public class CacheManagerController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(CacheManagerController.class);

    @Value("${pep.dev.tools.homemenus.separator}")
    private String separator;

    @Autowired
    private CacheManagerService cacheManagerService;

    /**
     * 获得缓存区域集合,按照字典序排列返回
     *
     * @return 缓存区名称组成的集合
     */
    @GetMapping
    public ResponseEntity<Collection<String>> listCaches() {
        Collection<String> cacheNames = cacheManagerService.getAllCacheNames();
        if (cacheNames == null) {
            return responseOfGet(new ArrayList<>());
        }
        return responseOfGet(cacheNames);
    }

    /**
     * 批量删除缓存区域
     *
     * @param names 缓存名称
     * @return 要删除的缓存区域名称，逗号间隔
     */
    @DeleteMapping
    public ResponseEntity deleteInBatches(@RequestParam String names) {
        if (StringUtil.isNotBlank(names)) {
            String[] nameArry = names.split(separator);
            cacheManagerService.deleteByNames(Arrays.asList(nameArry));
        }
        return responseOfDelete(true);
    }

    /**
     * 获得缓存区域中的 key 集合,集合按照字典序排列返回
     *
     * @param name 缓存区域名称
     * @return key 集合
     */
    @GetMapping("/{name:.*}")
    public ResponseEntity<Collection> listCacheKeys(@PathVariable String name, Integer pageNo, Integer pageSize) throws IOException {
        return responseOfGet(cacheManagerService.getCacheKeysByCacheNamePageable(name, pageNo, pageSize));
    }

    /**
     * 清理缓存区域，或缓存区域下的 key
     *
     * @param name 缓存区域名称
     * @param keys  要删除的缓存区域下的 key，逗号间隔。此参数存在时，该接口清理缓存区域下的 key；否则清理缓存区域
     * @return 值
     */
    @DeleteMapping("/{name:.*}")
    public ResponseEntity deleteCacheOrKeyByName(@PathVariable String name, @RequestParam(required = false) String keys) {
        if (StringUtil.isNotBlank(keys)) {
            String[] keyArry = keys.split(separator);
            cacheManagerService.deleteByKeys(name, Arrays.asList(keyArry));
        } else {
            cacheManagerService.deleteByName(name);
        }
        return responseOfDelete(true);
    }

    /**
     * 获取缓存区域中某 key 存储的内容
     *
     * @param cacheName 缓存区域名称
     * @param key       key 字符形式表示
     * @param className 存储的内容的类型名称
     * @return 存储内容的字符形式表示
     */
    @GetMapping(value = "/{cacheName}/{key}", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> getCacheWithKey(@PathVariable String cacheName, @PathVariable String key, String className) throws Exception {
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

    /**
     * 清理某缓存区域下的 key
     *
     * @param cacheName 缓存区域名称
     * @param key       key 字符形式表示
     * @return response of delete
     */
    @DeleteMapping("/{cacheName}/{key}")
    public ResponseEntity cleanCacheWithKey(@PathVariable String cacheName, @PathVariable String key) {
        List<String> keys = new ArrayList<>(1);
        keys.add(key);
        cacheManagerService.deleteByKeys(cacheName, keys);
        return responseOfDelete(true);
    }

}
