package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.dao.MenuDao;
import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.sort.BeanComparator;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.*;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DataDicService dataDicService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private I18NService i18NService;

    @Value("${auth.ignorePatterns}")
    private String patterns;

    private static PathMatcher matcher = new AntPathMatcher();

    @Override
    public Menu get(String id) {
        Menu menu = menuDao.get(id);
        try {
            if (menu != null) {
                if (menu.isEnable() && menu.isValid()) {
                    return menu;
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Menu is Null!", menu);
        }
        return null;
    }

    @Override
    public Collection<? extends Menu> getByIds(Collection<String> ids) {
        Collection<Menu> menuList = new ArrayList<>();
        Collection<? extends Menu> menus = menuDao.getByIds(ids);
        for (Menu menu : menus) {
            if (menu.isEnable() && menu.isValid()) {
                menuList.add(menu);
            }
        }
        return menuList;
    }

    @Override
    public Menu save(Menu menu) {
        return menuDao.save(menu);
    }

    @Override
    public Menu saveOrUpdateMenu(Menu menuReq) {
        String id = menuReq.getId();
        Menu menuInfo = menuDao.getNewMenuEntity();
        // 更新
        if (StringUtil.isNotNull(id)) {
            menuInfo = this.get(id);
        }
        menuInfo.setIcon(menuReq.getIcon());
        menuInfo.setName(menuReq.getName());
        menuInfo.setRoute(menuReq.getRoute());
        menuInfo.setEnable(menuReq.isEnable());
        menuInfo.setDescription(menuReq.getDescription());
        String parentId = menuReq.getParentId();
        if (StringUtil.isNotNull(parentId)) {
            menuInfo.setParent(this.get(parentId));
        }
        menuInfo.setSequenceNumber(menuReq.getSequenceNumber());
        String menuCode = menuReq.getMenuCode();
        if (StringUtil.isNotNull(menuCode)) {
            menuInfo.setMenuType(dataDicService.get("MENU_TYPE", menuCode));
        }
        return menuDao.save(menuInfo);
    }

    @Override
    public Collection<? extends Menu> getMenus() {
        return getMenus(userService.getCurrentUser());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> getMenus(User user) {
        return userDao.getMenus(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> getMenus(String name, String description, String route, String enable, String parentId) {
        List<Menu> menus = new ArrayList<>();
        Collection<? extends Menu> filterMenus = menuDao.getMenuByCondition(name, description, route, enable, parentId);
        Collection<? extends Menu> roleMenus = getMenus();
        for (Menu menu : roleMenus) {
            if (filterMenus.contains(menu)) {
                menus.add(menu);
            }
        }
        Collections.sort(menus, new BeanComparator("parent", "sequenceNumber"));
        return menus;
    }

    @Override
    public Collection<? extends Menu> getMenuByCondition(String name, String description, String route, String enable, String parentId) {
        return menuDao.getMenuByCondition(name, description, route, enable, parentId);
    }

    @Override
    public Collection<? extends Menu> getFilterMenusAndParent(Collection<? extends Menu> menus) {
        Collection<Menu> result = new HashSet<>();
        for (Menu menu : menus) {
            if (menu.isEnable() && menu.isValid()) {
                result.add(menu);
                if (menu.getParent() != null) {
                    result.add(menu.getParent());
                }
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
            Collection<? extends Menu> list = menuDao.findAll(idList);
            for (Menu menu : list) {
                // 菜单存在关联关系
                if (getMenuByCondition(null, null, null, "Y", menu.getId()).size() > 0) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.menu.delete.relation.failed"));
                }
                // 菜单存在资源
                if (menu.getResources().size() > 0) {
                    for (Resource resource : menu.getResources()) {
                        if (resource.isEnable() && resource.isValid()) {
                            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.menu.delete.relation.resource"));
                        }
                    }
                }
                // 菜单存在角色
                if (menu.getRoles().size() > 0) {
                    for (Role role : menu.getRoles()) {
                        if (role.isEnable() && menu.isValid()) {
                            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.menu.delete.relation.role"));
                        }
                    }
                }
                menu.setValid(false);
            }
            menuDao.save(list);
            ret = true;
        }
        return ret;
    }

    @Override
    public Collection<? extends Menu> getMenuParents() {
        Collection<Menu> menus = new HashSet<>();
        Collection<? extends Menu> list = menuDao.findAll();
        for (Menu menuEntity : list) {
            if (!menuEntity.isEnable() || !menuEntity.isValid()) {
                continue;
            }
            if (!menus.contains(menuEntity)) {
                if (menuEntity.getParent() != null) {
                    menus.add(menuEntity.getParent());
                } else {
                    menus.add(menuEntity);
                }
            }
        }
        return menus;
    }

    @Override
    public Collection<? extends Menu> updateEnable(Collection<String> idList, boolean enable) {
        Collection<? extends Menu> menuList = menuDao.findAll(idList);
        for (Menu menu : menuList) {
            Collection<? extends Menu> list = getMenuByCondition(null, null, null, "Y", menu.getId());
            if (list.size() != 0) {
                LOGGER.debug("{} has parent menu {}, dont update!", menu.getName(), menu.getParent());
                throw new ErrMsgException(i18NService.getMessage("pep.auth.common.menu.parent"));
            }
            menu.setEnable(enable);
        }
        return menuDao.save(menuList);
    }

    @Override
    public Menu addMenuResource(String menuId, String resourceId) {
        Menu menu = this.get(menuId);
        if (menu != null) {
            Resource resource = resourceService.get(resourceId);
            if (resource != null) {
                if (resource.isEnable() && resource.isValid()) {
                    menu.add(resource);
                    menu = save(menu);
                }
            }
        }
        return menu;
    }

    @Override
    public Menu deleteMenuResource(String menuId, String resourceId) {
        Menu menu = this.get(menuId);
        if (menu != null) {
            Resource resource = resourceService.get(resourceId);
            if (resource != null) {
                if (resource.isEnable() && resource.isValid()) {
                    menu.remove(resource);
                    menu = save(menu);
                }
            }
        }
        return menu;
    }

    @Override
    public Collection<? extends Resource> getMenuResources(String menuId) {
        Collection<Resource> filterResources = new ArrayList<>();
        Menu menu = this.get(menuId);
        if (menu != null) {
            Collection<? extends Resource> resources = menu.getResources();
            for (Resource resource : resources) {
                if (resource.isValid()) {
                    filterResources.add(resource);
                }
            }
        }
        return filterResources;
    }

    @Override
    public Collection<? extends Role> getMenuRoles(String menuId) {
        Menu menu = this.get(menuId);
        if (menu != null) {
            return roleService.getFilterRoles(menu.getRoles());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean accessible(Resource resource, String userId) {
        if (resource == null) {
            return true;
        }

        if (shouldIgnore(resource)) {
            return true;
        }

        Collection<? extends Menu> menus = resource.getMenus();
        if (CollectionUtil.isEmpty(menus)) {
            return true;
        }

        User user = userService.get(userId);
        Collection<? extends Menu> userMenus = getMenus(user);
        if (CollectionUtil.isEmpty(userMenus)) {
            return false;
        }

        for (Menu menu : menus) {
            if (userMenus.contains(menu)) {
                return true;
            }
        }

        return false;
    }

    private boolean shouldIgnore(Resource resource) {
        List<String> ignorePatterns = new ArrayList<>();
        String path = resource.getMethod().toString().concat(":").concat(resource.getURL());
        Collections.addAll(ignorePatterns, patterns.split(","));
        for (String pattern : ignorePatterns) {
            if (matcher.match(pattern, path)) {
                LOGGER.debug("{} {} is match {}", resource.getMethod().toString(), resource.getURL(), pattern);
                return true;
            }
        }
        return false;
    }
}
