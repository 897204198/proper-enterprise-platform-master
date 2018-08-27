package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.dao.MenuDao;
import com.proper.enterprise.platform.api.auth.dao.ResourceDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.*;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.vo.MenuVO;
import com.proper.enterprise.platform.auth.common.vo.ResourceVO;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.sort.BeanComparator;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.*;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);

    private static final String DEFAULT_VALUE = "-1";

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DataDicService dataDicService;

    @Autowired
    private I18NService i18NService;

    @javax.annotation.Resource(name = "ignorePatternsList")
    private List<String> ignorePatternsList;

    private static PathMatcher matcher = new AntPathMatcher();

    @Override
    public Menu get(String id) {
        return menuDao.get(id);
    }


    @Override
    public Menu save(Menu menu) {
        if (null == menu.getEnable()) {
            menu.setEnable(true);
        }
        if (null == menu.getSequenceNumber()) {
            menu.setSequenceNumber(0);
        }
        validate(menu);
        String parentId = menu.getParentId();
        if (StringUtil.isNotNull(parentId) && !DEFAULT_VALUE.equals(parentId)) {
            menu.addParent(this.get(parentId));
        }
        return menuDao.save(menu);
    }

    @Override
    public Menu update(Menu menuReq) {
        validate(menuReq);
        String parentId = menuReq.getParentId();
        String menuCode = menuReq.getMenuCode();
        if (StringUtil.isNotNull(menuCode)) {
            menuReq.setMenuType(new DataDicLiteBean("MENU_TYPE", menuCode));
        }

        Menu updateMenu = menuDao.updateForSelective(menuReq);
        if (StringUtil.isNotNull(parentId) && !DEFAULT_VALUE.equals(parentId)) {
            updateMenu.addParent(this.get(parentId));
            menuDao.save(updateMenu);
        }
        return updateMenu;
    }

    @Override
    public Collection<? extends Menu> getMenus() {
        return getMenus(userService.getCurrentUser());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> getMenus(User user) {
        if (user.getSuperuser()) {
            return menuDao.findAll(new Sort("parent", "sequenceNumber"));
        }
        List<Menu> result = new ArrayList<>(0);
        if (user.getEnable()) {
            Set<Menu> menus = new HashSet<>();
            menus = addRoleMenus(user.getRoles(), menus);
            if (CollectionUtil.isNotEmpty(user.getUserGroups())) {
                for (UserGroup userGroup : user.getUserGroups()) {
                    if (userGroup.getEnable()) {
                        menus = addRoleMenus(userGroup.getRoles(), menus);
                    }
                }
            }
            result = new ArrayList<>(menus.size());
            result.addAll(menus);
            result.sort(new BeanComparator("parent", "sequenceNumber"));
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> getMenus(String name, String description, String route, EnableEnum enable, String parentId) {
        Set<Menu> menus = new HashSet<>();
        Collection<? extends Menu> filterMenus = menuDao.findAll(name, description, route, enable, parentId);
        Collection<? extends Menu> roleMenus = getMenus();
        for (Menu menu : roleMenus) {
            if (filterMenus.contains(menu)) {
                menus.addAll(recursionMenu(menu, menus, enable));
            }
        }
        List<Menu> returnMenus = new ArrayList<>(menus);
        returnMenus.sort(new BeanComparator("parent", "sequenceNumber"));
        return returnMenus;
    }

    private Set<Menu> recursionMenu(Menu menu, Set<Menu> menus, EnableEnum enableEnum) {
        menus.add(menu);
        Boolean isHaveParent = menu.getParent() != null && (
            EnableEnum.ALL == enableEnum
                || EnableEnum.ENABLE == enableEnum && menu.getParent().getEnable()
                || EnableEnum.DISABLE == enableEnum && !menu.getParent().getEnable()
        );
        Boolean isEnable = EnableEnum.ALL == enableEnum
            || EnableEnum.ENABLE == enableEnum && menu.getEnable()
            || EnableEnum.DISABLE == enableEnum && !menu.getEnable();
        if (isHaveParent && isEnable) {
            menus.addAll(recursionMenu(menu.getParent(), menus, enableEnum));
        }
        return menus;
    }

    @Override
    public Collection<? extends Menu> getMenuByCondition(String name, String description, String route, EnableEnum enable, String parentId) {
        return menuDao.findAll(name, description, route, enable, parentId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataTrunk<? extends Menu> findMenusPagination(String name, String description, String route, EnableEnum enable, String parentId) {
        return menuDao.findPage(name, description, route, enable, parentId);
    }


    @Override
    public Collection<? extends Menu> getFilterMenusAndParent(Collection<? extends Menu> menus) {
        Collection<Menu> result = new HashSet<>();
        for (Menu menu : menus) {
            if (menu.getEnable()) {
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
            Collection<? extends Menu> list = menuDao.findAllById(idList);
            for (Menu menu : list) {
                // 菜单存在关联关系
                if (getMenuByCondition(null, null, null, EnableEnum.ENABLE, menu.getId()).size() > 0) {
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.menu.delete.relation.failed"));
                }
                // 菜单存在资源
                // 菜单存在角色
                if (menu.getRoles().size() > 0) {
                    for (Role role : menu.getRoles()) {
                        if (role.getEnable()) {
                            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.menu.delete.relation.role"));
                        }
                    }
                }
            }
            menuDao.delete(list);
            ret = true;
        }
        return ret;
    }

    @Override
    public Collection<? extends Menu> getMenuParents(EnableEnum menuEnable) {
        return menuDao.findParents(menuEnable);
    }

    @Override
    public Collection<? extends Menu> updateEnable(Collection<String> idList, boolean enable) {
        Collection<? extends Menu> menuList = menuDao.findAllById(idList);
        for (Menu menu : menuList) {
            menu.setEnable(enable);
        }
        return menuDao.save(menuList);
    }

    @Override
    public Resource addResourceOfMenu(String menuId, Resource resourceReq) {
        Resource resource = resourceDao.getNewResourceEntity();
        resource.setName(resourceReq.getName());
        resource.addURL(resourceReq.findURL());
        resource.setEnable(resourceReq.getEnable());
        resource.setMethod(resourceReq.getMethod());
        String resourceCode = resourceReq.getResourceCode();
        if (StringUtil.isNotNull(resourceCode)) {
            resource.setResourceType(new DataDicLiteBean("RESOURCE_TYPE", resourceCode));
        }
        Menu menu = this.get(menuId);
        if (menu != null) {
            Collection<? extends Resource> collection = menu.getResources();
            if (CollectionUtil.isNotEmpty(collection)) {
                for (Resource res : collection) {
                    String identification = res.getIdentifier();
                    if (resourceReq.getIdentifier() != null && !resourceReq.getIdentifier().equals(identification)) {
                        continue;
                    } else {
                        throw new ErrMsgException("pep.auth.common.menu.param");
                    }
                }
            }
            resource.setIdentifier(resourceReq.getIdentifier());
        }
        Resource resourcesOfSave = resourceDao.save(resource);
        Menu menus = this.get(menuId);
        if (menus != null) {
            menus.add(resourcesOfSave);
            menuDao.save(menus);
        }
        return resourcesOfSave;
    }

    @Override
    public Collection<? extends Resource> getMenuResources(String menuId, EnableEnum menuEnable, EnableEnum resourceEnable) {
        Menu menu = this.get(menuId);
        if (menu != null) {
            return menu.getResources();
        }
        return new ArrayList<>();
    }

    @Override
    public Collection<? extends Role> getMenuRoles(String menuId, EnableEnum menuEnable, EnableEnum roleEnable) {
        Menu menu = this.get(menuId);
        if (menu != null) {
            return roleService.getFilterRoles(menu.getRoles());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean accessible(Resource resource, String userId) {
        // 资源未定义，无需授权即可访问
        if (resource == null) {
            LOGGER.debug("unknow resource");
            return true;
        }

        // 资源被定义到了忽略列表中，无需授权即可访问
        if (shouldIgnore(resource)) {
            LOGGER.debug("resource ignore resourceId:{}", resource.getId());
            return true;
        }

        // resource 是否有效
        if (!resource.getEnable()) {
            LOGGER.debug("resource disEnable resourceId:{}", resource.getId());
            return false;
        }

        Collection<? extends Menu> menus = resource.getMenus();
        // 资源未被定义到菜单下时，无需授权即可访问
        if (CollectionUtil.isEmpty(menus)) {
            LOGGER.debug("resource unMenu resourceId:{}", resource.getId());
            return true;
        }

        User user = userService.get(userId);
        Collection<? extends Menu> userMenus = getMenus(user);
        if (CollectionUtil.isEmpty(userMenus)) {
            return false;
        }
        boolean hasMenu = false;
        for (Menu menu : menus) {
            if (userMenus.contains(menu)) {
                LOGGER.debug("resource pass resourceId:{}", resource.getId());
                hasMenu = true;
            }
        }

        Collection<? extends Resource> userResources = userService.getUserResources(userId, EnableEnum.ENABLE);
        if (CollectionUtil.isEmpty(userResources)) {
            return false;
        }
        boolean hasResource = userResources.contains(resource);
        return hasMenu && hasResource;
    }


    private Set<Menu> addRoleMenus(Collection<? extends Role> roles, Set<Menu> menus) {
        if (null == roles) {
            return menus;
        }
        for (Role role : roles) {
            if (role.getEnable()) {
                Collection<? extends Menu> userMenus = roleService.getRoleMenus(role.getId(), EnableEnum.ENABLE);
                for (Menu menu : userMenus) {
                    if (menu.getEnable()) {
                        menus.add(menu);
                    }
                }
            }
        }
        return menus;
    }


    private boolean shouldIgnore(Resource resource) {
        String path = resource.getMethod().toString().concat(":").concat(resource.findURL());
        for (String pattern : ignorePatternsList) {
            if (matcher.match(pattern, path)) {
                LOGGER.debug("{} {} is match {}", resource.getMethod().toString(), resource.findURL(), pattern);
                return true;
            }
        }
        return false;
    }


    @Override
    public Collection<? extends Menu> getMenuAllResources() {
        Collection<MenuVO> menusResources = new ArrayList<>();
        Collection<? extends Menu> menuEntity = this.getMenus();
        for (Menu menu : menuEntity) {
            if (menu.getEnable()) {
                MenuVO detail = new MenuVO();
                BeanUtils.copyProperties(menu, detail);
                if (null != menu.getParent()) {
                    detail.setParentId(menu.getParent().getId());
                }

                Collection<ResourceVO> resList = new ArrayList<>();
                Collection<Resource> resourceList = (Collection<Resource>) menu.getResources();
                for (Resource resource : resourceList) {
                    if (resource != null && resource.getEnable()) {
                        ResourceVO resourceDetail = new ResourceVO();
                        BeanUtils.copyProperties(resource, resourceDetail);
                        resList.add(resourceDetail);
                    }
                }
                detail.setResources(resList);
                menusResources.add(detail);
            }
        }
        return menusResources;
    }

    private void validate(Menu menu) {
        if (StringUtil.isEmpty(menu.getRoute())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.menu.route.empty"));
        }
        if (StringUtil.isEmpty(menu.getName())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.menu.name.empty"));
        }
        Collection<? extends Menu> oldNameMenus = menuDao.findAllEq(menu.getName());
        if (oldNameMenus.size() > 0) {
            for (Menu nameMenu : oldNameMenus) {
                if (!nameMenu.getId().equals(menu.getId())) {
                    throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.menu.name.duplicated"));
                }
            }
        }
        Collection<? extends Menu> oldRouteMenus = menuDao.findAllEq(menu.getName());
        if (oldRouteMenus.size() > 0) {
            for (Menu routeMenu : oldRouteMenus) {
                if (!routeMenu.getId().equals(menu.getId())) {
                    throw new ErrMsgException(I18NUtil.getMessage("pep.auth.common.menu.route.duplicated"));
                }
            }
        }
    }
}
