package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Service
public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    DataDicService dataDicService;

    @Autowired
    UserService userService;

    @Autowired
    I18NService i18NService;

    @Autowired
    MenuService menuService;

    @Override
    public Resource save(Resource resource) {
        return resourceRepository.save((ResourceEntity) resource);
    }

    @Override
    public Resource save(Map<String, Object> map) {
        String id = String.valueOf(map.get("id"));
        Resource resource = new ResourceEntity();
        // 更新
        if (map.get("id") != null && StringUtil.isNotNull(id)) {
            resource = this.get(id);
        }
        resource.setName(String.valueOf(map.get("name")));
        resource.setURL(String.valueOf(map.get("url")));
        resource.setEnable((boolean) map.get("enable"));
        resource.setMethod(RequestMethod.valueOf(String.valueOf(map.get("method"))));
        String resourceCode = String.valueOf(map.get("resourceCode"));
        if (map.get("resourceCode") != null && StringUtil.isNotNull(resourceCode)) {
            resource.setResourceType(dataDicService.get("RESOURCE_TYPE", resourceCode));
        }
        return this.save(resource);
    }

    @Override
    public Resource get(String id) {
        ResourceEntity resource = resourceRepository.findOne(id);
        if (resource != null && resource.isEnable() && resource.isValid()) {
            return resource;
        }
        return null;
    }

    @Override
    public Resource get(String url, RequestMethod method) {
        StringBuffer strbuf = new StringBuffer();
        strbuf = strbuf.append(method.toString()).append(":").append(url);
        return getBestMatch(find(), strbuf.toString());
    }

    @Override
    public Collection<? extends Resource> getByIds(Collection<String> ids) {
        List<ResourceEntity> resourceEntities = resourceRepository.findAll(ids);
        for (ResourceEntity resourceEntitie : resourceEntities) {
            if (resourceEntitie.isEnable() && resourceEntitie.isValid()) {
                return resourceEntities;
            }
        }
        return null;
    }

    @Override
    public void delete(Resource resource) {
        resource.setValid(false);
        resourceRepository.save((ResourceEntity) resource);
    }

    @Override
    public Collection<? extends Resource> getFilterResources(Collection<? extends Resource> resources) {
        Collection<ResourceEntity> result = new HashSet<>();
        Iterator iterator = resources.iterator();
        while (iterator.hasNext()) {
            ResourceEntity resourceEntity = (ResourceEntity) iterator.next();
            if (!resourceEntity.isEnable() || !resourceEntity.isValid()) {
                continue;
            }
            result.add(resourceEntity);
        }
        return result;
    }

    @Override
    public Collection<Resource> find() {
        Collection<ResourceEntity> resources = resourceRepository.findAll();
        Collection collection = null;
        for (ResourceEntity resourceEntity : resources) {
            if (resourceEntity.isEnable() && resourceEntity.isValid()) {
                collection = CollectionUtil.convert(resources);
            }
        }
        return collection;
    }

    /**
     * 思路：传参数signature与所有resources.method+resources.url
     * 字符进行 ANT 风格的路径匹配，匹配原则：符合条件的如果完全匹配直接return,无完全匹配的比较*号位置，*号越靠后越符合匹配规则，当*
     * 号位置相同时再比较整个字符长度，长度长的更符合条件。
     *
     * @param resources 资源集合
     * @param signature resources.method+":"+resources.url
     * @return 最佳匹配资源
     */
    public Resource getBestMatch(Collection<Resource> resources, String signature) {
        if (resources != null) {
            Iterator<Resource> it = resources.iterator();
            Resource returnres = null;
            AntPathMatcher matcher = new AntPathMatcher();

            while (it.hasNext()) {
                Resource resource = it.next();
                StringBuffer strbuf = new StringBuffer();
                strbuf = strbuf.append(resource.getMethod().toString()).append(":").append(resource.getURL());

                if (matcher.match(strbuf.toString(), signature)) {
                    // 如果直接可以匹配上（无*） 直接返回
                    if (strbuf.indexOf("*") < 0) {
                        return resource;
                    }
                    // 把符合条件的第一条记录赋值给返回值
                    if (returnres == null) {
                        returnres = resource;
                    } else {

                        StringBuffer oldstrbuf = new StringBuffer();
                        oldstrbuf = oldstrbuf.append(resource.getMethod().toString()).append(":")
                            .append(returnres.getURL());
                        // 比较本次值与返回值*的位置 *位置越靠后，越符合匹配值
                        if (strbuf.indexOf("*") >= oldstrbuf.indexOf("*")) {
                            // 特殊情况 如果* 位置相同 把长度更长的赋值给返回值
                            if (strbuf.indexOf("*") == oldstrbuf.indexOf("*")) {
                                if (strbuf.length() > oldstrbuf.length()) {
                                    returnres = resource;
                                }
                            } else {
                                returnres = resource;
                            }
                        }
                    }
                }
            }

            return returnres;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            List<ResourceEntity> list = resourceRepository.findAll(idList);
            for (ResourceEntity resourceEntity : list) {
                // 资源存在菜单
                if (resourceEntity.getMenus().size() > 0) {
                    for (Menu menu : resourceEntity.getMenus()) {
                        if (menu.isEnable() && menu.isValid()) {
                            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.resource.delete.relation.menu"));
                        }
                    }
                }
                // 资源存在角色
                if (resourceEntity.getRoles().size() > 0) {
                    for (Role role : resourceEntity.getRoles()) {
                        if (role.isEnable() && role.isValid()) {
                            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.resource.delete.relation.role"));
                        }
                    }
                }
                resourceEntity.setValid(false);
                ret = true;
            }
            resourceRepository.save(list);
        }
        return ret;
    }

    @Override
    public Collection<? extends Resource> updateEanble(Collection<String> idList, boolean enable) {
        List<ResourceEntity> resourceList = resourceRepository.findAll(idList);
        for (ResourceEntity resource : resourceList) {
            if (!resource.isEnable() && !resource.isValid()) {
                throw new ErrMsgException("pep.auth.common.resource.used");
            }
            resource.setEnable(enable);
        }
        resourceRepository.save(resourceList);
        return resourceList;
    }

    @Override
    public Collection<? extends Menu> getResourceMenus(String resourceId) {
        Collection<Menu> filterMenus = new ArrayList<>();
        Resource resource = this.get(resourceId);
        if (resource != null) {
            Collection<? extends Menu> menus = resource.getMenus();
            for (Menu menu : menus) {
                if (menu.isEnable() && menu.isValid()) {
                    filterMenus.add(menu);
                }
            }
        }
        return filterMenus;
    }

    @Override
    public Collection<? extends Role> getResourceRoles(String resourceId) {
        Collection<Role> filterRoles = new ArrayList<>();
        Resource resource = this.get(resourceId);
        if (resource != null) {
            Collection<? extends Role> roles = resource.getRoles();
            for (Role role : roles) {
                if (role.isEnable() && role.isValid()) {
                    filterRoles.add(role);
                }
            }
        }
        return filterRoles;
    }

    @Override
    public boolean hasPermissionOfResource(Resource resource, String reqUrl, RequestMethod requestMethod) {
        Resource localResource = this.get(reqUrl, requestMethod);
        if (localResource == null || resource == null || !resource.isValid() || !resource.isEnable()) {
            return false;
        }
        if (localResource.getId().equals(resource.getId())) {
            return true;
        }
        return false;
    }
}
