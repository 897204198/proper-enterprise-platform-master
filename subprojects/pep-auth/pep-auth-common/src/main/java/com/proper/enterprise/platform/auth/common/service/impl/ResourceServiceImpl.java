package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.dao.ResourceDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    ResourceDao resourceDao;

    @Autowired
    DataDicService dataDicService;

    @Autowired
    UserService userService;

    @Autowired
    I18NService i18NService;

    @Autowired
    MenuService menuService;

    @Autowired
    RoleService roleService;

    @Override
    public Resource save(Resource resource) {
        return resourceDao.save(resource);
    }

    @Override
    public Resource saveOrUpdateResource(Resource resourceReq) {
        String id = resourceReq.getId();
        Resource resource = resourceDao.getNewResourceEntity();
        // 更新
        if (StringUtil.isNotNull(id)) {
            resource = this.get(id, EnableEnum.ALL);
        }
        resource.setName(resourceReq.getName());
        resource.setURL(resourceReq.getURL());
        resource.setEnable(resourceReq.isEnable());
        resource.setMethod(resourceReq.getMethod());
        Collection<? extends Menu> collection = resourceReq.getMenus();
        for (Menu menus : collection) {
            if (menus != null) {
                Collection<? extends Resource> resources = menus.getResources();
                for (Resource resource1 : resources) {
                    String identification = resource1.getIdentifier();
                    if (!resourceReq.getIdentifier().equals(identification)) {
                        continue;
                    } else {
                        throw new ErrMsgException("pep.auth.common.menu.param");
                    }
                }
                resource.setIdentifier(resourceReq.getIdentifier());
            }
        }
        String resourceCode = resourceReq.getResourceCode();
        if (StringUtil.isNotNull(resourceCode)) {
            resource.setResourceType(dataDicService.get("RESOURCE_TYPE", resourceCode));
        }
        return resourceDao.save(resource);
    }

    @Override
    public Resource get(String id) {
        Resource resource = resourceDao.get(id);
        if (resource != null && resource.isEnable()) {
            return resource;
        }
        return null;
    }

    @Override
    public Resource get(String id, EnableEnum enable) {
        return resourceDao.get(id, enable);
    }

    @Override
    public Resource get(String url, RequestMethod method) {
        StringBuffer strbuf = new StringBuffer();
        strbuf = strbuf.append(method.toString()).append(":").append(url);
        return getBestMatch(find(), strbuf.toString());
    }

    @Override
    public Collection<? extends Resource> getByIds(Collection<String> ids) {
        Collection<Resource> resourceList = new ArrayList<>();
        Collection<? extends Resource> resources = resourceDao.findAll(ids);
        for (Resource resource : resources) {
            if (resource.isEnable()) {
                resourceList.add(resource);
            }
        }
        return resourceList;
    }

    @Override
    public void delete(Resource resource) {
        resourceDao.delete(resource);
    }

    @Override
    public Collection<? extends Resource> getFilterResources(Collection<? extends Resource> resources) {
        Collection<Resource> result = new HashSet<>();
        for (Resource resource : resources) {
            if (!resource.isEnable()) {
                continue;
            }
            result.add(resource);
        }
        return result;
    }

    @Override
    public Collection<Resource> find() {
        Collection<? extends Resource> resources = resourceDao.findAll();
        Collection<Resource> collection = new ArrayList<>(resources.size());
        for (Resource resource : resources) {
            collection.add(resource);
        }
        return collection;
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            Collection<? extends Resource> list = resourceDao.findAll(idList);
            for (Resource resource : list) {
                // 资源存在菜单
                if (resource.getMenus().size() > 0) {
                    for (Menu menu : resource.getMenus()) {
                        if (menu.isEnable()) {
                            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.resource.delete.relation.menu"));
                        }
                    }
                }
                // 资源存在角色
                if (resource.getRoles().size() > 0) {
                    for (Role role : resource.getRoles()) {
                        if (role.isEnable()) {
                            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.resource.delete.relation.role"));
                        }
                    }
                }
            }
            resourceDao.delete(list);
            ret = true;
        }
        return ret;
    }

    @Override
    public Collection<? extends Resource> updateEnable(Collection<String> idList, boolean enable) {
        Collection<? extends Resource> resourceList = resourceDao.findAll(idList);
        for (Resource resource : resourceList) {
            if (!resource.isEnable()) {
                throw new ErrMsgException("pep.auth.common.resource.used");
            }
            resource.setEnable(enable);
        }
        resourceDao.save(resourceList);
        return resourceList;
    }

    @Override
    public Collection<? extends Menu> getResourceMenus(String resourceId, EnableEnum resourceEnable, EnableEnum menuEnable) {
        Collection<Menu> filterMenus = new ArrayList<>();
        Resource resource = this.get(resourceId, resourceEnable);
        if (resource != null) {
            Collection<? extends Menu> menus = resource.getMenus();
            for (Menu menu : menus) {
                if (menu.isEnable()) {
                    filterMenus.add(menu);
                }
            }
        }
        return filterMenus;
    }

    @Override
    public Collection<? extends Role> getResourceRoles(String resourceId, EnableEnum resourceEnable, EnableEnum menuEnable) {
        Resource resource = this.get(resourceId, resourceEnable);
        if (resource != null) {
            return roleService.getFilterRoles(resource.getRoles());
        }
        return new ArrayList<>();
    }

    /**
     * 思路：
     * 传参数signature与所有resources.method+resources.url
     * 字符进行 ANT 风格的路径匹配
     * <p>
     * 匹配原则：
     * 符合条件的如果完全匹配直接return,无完全匹配的比较*号位置，*号越靠后越符合匹配规则，
     * 当*号位置相同时再比较整个字符长度，长度长的更符合条件。
     *
     * @param resources 资源集合
     * @param signature resources.method+":"+resources.url
     * @return 最佳匹配资源
     */
    public Resource getBestMatch(Collection<Resource> resources, String signature) {
        if (resources != null) {
            Iterator<Resource> it = resources.iterator();
            Resource returnRes = null;
            AntPathMatcher matcher = new AntPathMatcher();

            while (it.hasNext()) {
                Resource resource = it.next();
                StringBuffer strBuf = new StringBuffer();
                strBuf = strBuf.append(resource.getMethod().toString()).append(":").append(resource.getURL());

                if (matcher.match(strBuf.toString(), signature)) {
                    // 如果直接可以匹配上（无*） 直接返回
                    if (strBuf.indexOf("*") < 0) {
                        return resource;
                    }
                    // 把符合条件的第一条记录赋值给返回值
                    if (returnRes == null) {
                        returnRes = resource;
                    } else {

                        StringBuffer oldstrbuf = new StringBuffer();
                        oldstrbuf = oldstrbuf.append(resource.getMethod().toString()).append(":")
                                .append(returnRes.getURL());
                        // 比较本次值与返回值*的位置 *位置越靠后，越符合匹配值
                        if (strBuf.indexOf("*") >= oldstrbuf.indexOf("*")) {
                            // 特殊情况 如果* 位置相同 把长度更长的赋值给返回值
                            if (strBuf.indexOf("*") == oldstrbuf.indexOf("*")) {
                                if (strBuf.length() > oldstrbuf.length()) {
                                    returnRes = resource;
                                }
                            } else {
                                returnRes = resource;
                            }
                        }
                    }
                }
            }

            return returnRes;
        } else {
            return null;
        }
    }
}
