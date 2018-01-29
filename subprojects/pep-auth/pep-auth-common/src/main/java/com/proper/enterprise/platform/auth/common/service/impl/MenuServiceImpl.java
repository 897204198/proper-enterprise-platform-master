package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.auth.common.entity.MenuEntity;
import com.proper.enterprise.platform.auth.common.repository.MenuRepository;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.sort.BeanComparator;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);


    @Autowired
    private MenuRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private DataDicService dataDicService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private I18NService i18NService;

    @Override
    public Menu get(String id) {
        Menu menu = repository.findOne(id);
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
        List<MenuEntity> menuEntities = repository.findAll(ids);
        for (MenuEntity menuEntity : menuEntities) {
            if (menuEntity.isEnable() && menuEntity.isValid()) {
                return menuEntities;
            }
        }
        return null;
    }

    @Override
    public Menu save(Menu menu) {
        return repository.save((MenuEntity) menu);
    }

    @Override
    public Menu save(Map<String, Object> map) {
        String id = String.valueOf(map.get("id"));
        MenuEntity menuInfo = new MenuEntity();
        // 更新
        if (map.get("id") != null && StringUtil.isNotNull(id)) {
            menuInfo = (MenuEntity) this.get(id);
        }
        menuInfo.setIcon(String.valueOf(map.get("icon")));
        menuInfo.setName(String.valueOf(map.get("name")));
        menuInfo.setRoute(String.valueOf(map.get("route")));
        menuInfo.setEnable((boolean) map.get("enable"));
        menuInfo.setDescription(String.valueOf(map.get("description")));
        String parentId = String.valueOf(map.get("parentId"));
        if (map.get("parentId") != null && StringUtil.isNotNull(parentId)) {
            menuInfo.setParent(this.get(parentId));
        }
        menuInfo.setSequenceNumber(Integer.parseInt(String.valueOf(map.get("sequenceNumber"))));
        String menuCode = String.valueOf(map.get("menuCode"));
        if (map.get("menuCode") != null && StringUtil.isNotNull(menuCode)) {
            menuInfo.setMenuType(dataDicService.get("MENU_TYPE", menuCode));
        }
        return repository.save(menuInfo);
    }

    @Override
    public Collection<? extends Menu> getMenus() {
        return getMenus(userService.getCurrentUser());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> getMenus(User user) {
        Assert.notNull(user, "Could NOT get menus WITHOUT a user");

        if (user.isSuperuser()) {
            return repository.findAll(new Sort("parent", "sequenceNumber"));
        }

        List<Menu> menus = new ArrayList<>();
        for (Role role : user.getRoles()) {
            for (Menu menu : role.getMenus()) {
                if (!menus.contains(menu)) {
                    menus.add(menu);
                }
            }
        }

        Collections.sort(menus, new BeanComparator("parent", "sequenceNumber"));
        return menus;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> getMenus(String name, String description, String route, String enable, String parentId) {
        List<Menu> menus = new ArrayList<>();
        Collection<? extends Menu> filterMenus = getMenuByCondiction(name, description, route, enable, parentId);
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
    public boolean accessible(Resource resource, String userId) {
        if (resource == null) {
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

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Menu> getMenuByCondiction(String name, String description, String route, String enable, String parentId) {
        Specification specification = new Specification<MenuEntity>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtil.isNotNull(name)) {
                    predicates.add(cb.like(root.get("name"), "%".concat(name).concat("%")));
                }
                if (StringUtil.isNotNull(description)) {
                    predicates.add(cb.like(root.get("description"), "%".concat(description).concat("%")));
                }
                if (StringUtil.isNotNull(route)) {
                    predicates.add(cb.like(root.get("route"), "%".concat(route).concat("%")));
                }
                if (StringUtil.isNotNull(enable)) {
                    predicates.add(cb.equal(root.get("enable"), enable.equals("Y")));
                }
                if (StringUtil.isNotNull(parentId)) {
                    predicates.add(cb.equal(root.get("parent").get("id"), "%".concat(parentId).concat("%")));
                }
                predicates.add(cb.equal(root.get("valid"), true));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return repository.findAll(specification, new Sort("parent", "sequenceNumber"));
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            try {
                List<MenuEntity> list = repository.findAll(idList);
                for (MenuEntity menuEntity : list) {
                    if (menuEntity.getRoles().size() == 0 && menuEntity.getResources().size() == 0) {
                        menuEntity.setValid(false);
                        repository.save(list);
                        ret = true;
                    } else {
                        ret = false;
                    }
                }
            } catch (Exception e) {
                throw new ErrMsgException(i18NService.getMessage("pep.auth.common.menu.delete"));
            }
        }
        return ret;
    }

    @Override
    public Collection<? extends Menu> getMenuParents() {
        Collection<MenuEntity> menu = new HashSet<>();
        List<MenuEntity> list = repository.findAll();
        for (MenuEntity menuEntity : list) {
            if (!menuEntity.isEnable() || !menuEntity.isValid()) {
                continue;
            }
            if (menuEntity.getParent() != null) {
                menu.add((MenuEntity) menuEntity.getParent());
            } else {
                menu.add(menuEntity);
            }

        }
        return menu;
    }

    @Override
    public Collection<? extends Menu> updateEanble(Collection<String> idList, boolean enable) {
        List<MenuEntity> menuList = repository.findAll(idList);
        for (Menu menu : menuList) {
            if (menu.isEnable()) {
                Collection<? extends Menu> list = getMenuByCondiction(null, null, null, "Y", menu.getId());
                if (list.size() != 0) {
                    LOGGER.debug("{} has parent menu {}, dont update!", menu.getName(), menu.getParent());
                    throw new ErrMsgException(i18NService.getMessage("pep.auth.common.menu.parent"));
                } else {
                    menu.setEnable(false);
                }
            }
        }
        return repository.save(menuList);
    }

    @Override
    public Menu addMenuResource(String menuId, String resourceId) {
        // TODO 具体业务实现
        Menu menu = this.get(menuId);
        if (menu != null) {
            Resource resource = resourceService.get(resourceId);
            if (resource != null) {
                menu.add(resource);
                menu = save(menu);
            }
        }
        return menu;
    }

    @Override
    public Menu deleteMenuResource(String menuId, String resourceId) {
        // TODO 具体业务实现
        Menu menu = this.get(menuId);
        if (menu != null) {
            Resource resource = resourceService.get(resourceId);
            if (resource != null) {
                menu.remove(resource);
                menu = save(menu);
            }
        }
        return menu;
    }

    @Override
    public Collection<? extends Resource> getMenuResources(String menuId) {
        Collection<Resource> filterResources = new ArrayList<>();
        Menu menu = this.get(menuId); // TODO 过滤invalid以及enable
        if (menu != null) {
            Collection<? extends Resource> resources = menu.getResources();
            // TODO 具体过滤
            filterResources.addAll(resources);
        }
        return filterResources;
    }

    @Override
    public Collection<? extends Role> getMenuRoles(String menuId) {
        Collection<Role> filterRoles = new ArrayList<>();
        Menu menu = this.get(menuId); // TODO 过滤invalid以及enable
        if (menu != null) {
            Collection<? extends Role> roles = menu.getRoles();
            // TODO 具体过滤
            filterRoles.addAll(roles);
        }
        return filterRoles;
    }
}
