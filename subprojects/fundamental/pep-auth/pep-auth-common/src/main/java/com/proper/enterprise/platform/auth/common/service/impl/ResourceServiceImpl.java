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
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.core.i18n.I18NService;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
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
        setDefault(resource);
        validateParam(resource);
        return resourceDao.save(resource);
    }

    @Override
    public Resource update(Resource resourceReq) {
        setDefault(resourceReq);
        String resourceCode = resourceReq.getResourceCode();
        if (StringUtil.isNotNull(resourceCode)) {
            resourceReq.setResourceType(new DataDicLiteBean("RESOURCE_TYPE", resourceCode));
        }
        return resourceDao.updateForSelective(resourceReq);
    }

    @Override
    public Resource get(String id) {
        return resourceDao.get(id);
    }

    @Override
    public Resource get(String url, RequestMethod method) {
        StringBuffer strbuf = new StringBuffer();
        strbuf = strbuf.append(method.toString()).append(":").append(url);
        return getBestMatch(findAll(EnableEnum.ALL), strbuf.toString());
    }

    @Override
    public Collection<? extends Resource> findAll(Collection<String> ids, EnableEnum resourceEnable) {
        Collection<Resource> resourceList = new ArrayList<>();
        Collection<? extends Resource> resources = resourceDao.findAll(ids);
        for (Resource resource : resources) {
            if (resourceEnable == EnableEnum.ALL) {
                resourceList.add(resource);
                continue;
            }
            if (resourceEnable == EnableEnum.ENABLE && resource.getEnable()) {
                resourceList.add(resource);
                continue;
            }
            if (resourceEnable == EnableEnum.DISABLE && !resource.getEnable()) {
                resourceList.add(resource);
            }
        }
        return resourceList;
    }

    @Override
    public Collection<? extends Resource> findAll(EnableEnum enableEnum) {
        return resourceDao.findAll(enableEnum);
    }

    @Override
    public void delete(Resource resource) {
        validateDel(resource);
        resourceDao.delete(resource);
    }

    @Override
    public Collection<? extends Resource> getFilterResources(Collection<? extends Resource> resources, EnableEnum resourceEnable) {
        Collection<Resource> result = new HashSet<>();
        boolean shouldFilter;
        for (Resource resource : resources) {
            shouldFilter = resourceEnable == EnableEnum.ENABLE && !resource.getEnable()
                || resourceEnable == EnableEnum.DISABLE && resource.getEnable()
                || null == resourceEnable;
            if (!shouldFilter) {
                result.add(resource);
            }
        }
        return result;
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
                validateDel(resource);
            }
            resourceDao.delete(list);
            ret = true;
        }
        return ret;
    }

    @Override
    public Collection<? extends Resource> updateEnable(Collection<String> idList, boolean enable) {
        Collection<? extends Resource> resourceList = resourceDao.findAllById(idList);
        for (Resource resource : resourceList) {
            resource.setEnable(enable);
        }
        resourceDao.save(resourceList);
        return resourceList;
    }

    @Override
    public Collection<? extends Menu> getResourceMenus(String resourceId, EnableEnum menuEnable) {
        Collection<Menu> filterMenus = new ArrayList<>();
        Resource resource = this.get(resourceId);
        if (resource != null) {
            Collection<? extends Menu> menus = resource.getMenus();
            for (Menu menu : menus) {
                if (menu.getEnable()) {
                    filterMenus.add(menu);
                }
            }
        }
        return filterMenus;
    }

    @Override
    public Collection<? extends Role> getResourceRoles(String resourceId, EnableEnum menuEnable) {
        Resource resource = this.get(resourceId);
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
    public Resource getBestMatch(Collection<? extends Resource> resources, String signature) {
        if (resources != null) {
            Iterator<? extends Resource> it = resources.iterator();
            Resource returnRes = null;
            AntPathMatcher matcher = new AntPathMatcher();

            while (it.hasNext()) {
                Resource resource = it.next();
                StringBuffer strBuf = new StringBuffer();
                strBuf = strBuf.append(resource.getMethod().toString()).append(":").append(resource.findURL());

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
                            .append(returnRes.findURL());
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

    private void setDefault(Resource resource) {
        if (null == resource.getEnable()) {
            resource.setEnable(true);
        }
    }

    private void validateParam(Resource resource) {
        if (StringUtil.isEmpty(resource.getName())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.resource.name.empty"));
        }
        Collection<? extends Resource> oldResources = resourceDao.findAll(resource.getName(), EnableEnum.ALL);
        for (Resource oldResource : oldResources) {
            if (null != oldResource && !oldResource.getId().equals(resource.getId())) {
                throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.resource.name.duplicate"));
            }
        }
    }

    private void validateDel(Resource resource) {
        // 资源存在菜单
        if (resource.getMenus().size() > 0) {
            for (Menu menu : resource.getMenus()) {
                if (menu.getEnable()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.resource.delete.relation.menu"));
                }
            }
        }
        // 资源存在角色
        if (resource.getRoles().size() > 0) {
            for (Role role : resource.getRoles()) {
                if (role.getEnable()) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.resource.delete.relation.role"));
                }
            }
        }
    }
}
