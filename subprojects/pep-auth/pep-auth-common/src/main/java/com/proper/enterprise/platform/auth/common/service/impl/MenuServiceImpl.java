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

    @Autowired
    private MenuRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private DataDicService dataDicService;

    @Autowired
    private ResourceService resourceService;

    @Override
    public Menu get(String id) {
        return repository.findOne(id);
    }

    @Override
    public Collection<? extends Menu> getByIds(Collection<String> ids) {
        // TODO 查询有效菜单
        return repository.findAll(ids);
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
            menuInfo = (MenuEntity)this.get(id);
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
    public Collection<? extends Menu> getMenus(String name, String description, String route, String enable) {
        // TODO 具体业务实现
        List<Menu> menus = new ArrayList<>();
        Collection<? extends Menu> filterMenus = getMenuByCondiction(name, description, route, enable);
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
    public Collection<? extends Menu> getMenuByCondiction(String name, String description, String route, String enable) {
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
            // TODO 对删除业务进行逻辑判断
            try {
                repository.delete(repository.findAll(idList));
                ret = true;
            } catch (Exception e) {
                throw new ErrMsgException("Delete error!");
            }
        } else {
            throw new ErrMsgException("Param Error!");
        }
        return ret;
    }

    @Override
    public Collection<? extends Menu> getMenuParents() {
        // TODO 获取父节点菜单列表
        return new ArrayList<MenuEntity>();
    }

    @Override
    public Collection<? extends Menu> updateEanble(Collection<String> idList, boolean enable) {
        // TODO 具体实现
        List<MenuEntity> menuList = repository.findAll(idList);
        for (MenuEntity menu : menuList) {
            menu.setEnable(enable);
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
}
