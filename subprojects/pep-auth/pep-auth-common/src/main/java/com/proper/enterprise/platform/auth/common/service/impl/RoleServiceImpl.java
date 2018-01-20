package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.MenuService;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.auth.common.entity.RoleEntity;
import com.proper.enterprise.platform.auth.common.repository.RoleRepository;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MenuService menuService;

    @Override
    public Role get(String id) {
        return roleRepository.findOne(id);
    }

    @Override
    public Collection<? extends Role> getByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save((RoleEntity) role);
    }

    @Override
    public Role save(Map<String, Object> map) {
        String id = String.valueOf(map.get("id"));
        RoleEntity role = new RoleEntity();
        // 更新
        if (map.get("id") != null && StringUtil.isNotNull(id)) {
            role = (RoleEntity)this.get(id);
        }
        role.setDescription(String.valueOf(map.get("description")));
        role.setName(String.valueOf(map.get("name")));
        role.setEnable((boolean) map.get("enable"));
        String parentId = String.valueOf(map.get("parentId"));
        if (map.get("parentId") != null && StringUtil.isNotNull(parentId)) {
            role.setParent(this.get(parentId));
        }
        return roleRepository.save(role);
    }

    @Override
    public void delete(Role role) {
        roleRepository.delete((RoleEntity) role);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Role> getByCondiction(String name, String description, String parentId, String enable) {
        Specification specification = new Specification<RoleEntity>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtil.isNotNull(name)) {
                    predicates.add(cb.like(root.get("name"), "%".concat(name).concat("%")));
                }
                if (StringUtil.isNotNull(description)) {
                    predicates.add(cb.like(root.get("description"), "%".concat(description).concat("%")));
                }
                if (StringUtil.isNotNull(parentId)) {
                    predicates.add(cb.like(root.get("parent_id"), "%".concat(parentId).concat("%")));
                }
                if (StringUtil.isNotNull(enable)) {
                    predicates.add(cb.equal(root.get("enable"), enable.equals("Y")));
                }
                predicates.add(cb.equal(root.get("valid"), true));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return roleRepository.findAll(specification, new Sort("name"));
    }

    @Override
    public boolean deleteByIds(String ids) {
        boolean ret = false;
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            // TODO 对删除业务进行逻辑判断
            roleRepository.delete(roleRepository.findAll(idList));
            ret = true;
        }
        return ret;
    }

    @Override
    public Collection<? extends Role> getRoleParents() {
        // TODO 获取父节点菜单列表
        return new ArrayList<RoleEntity>();
    }

    @Override
    public Collection<? extends Role> updateEanble(Collection<String> idList, boolean enable) {
        // TODO 具体实现
        List<RoleEntity> roleList = roleRepository.findAll(idList);
        for (RoleEntity role : roleList) {
            role.setEnable(enable);
        }
        return roleRepository.save(roleList);
    }

    @Override
    public Role addRoleMenus(String roleId, String ids) {
        // TODO 具体实现
        Role role = this.get(roleId);
        Collection<? extends Menu> menuList = new ArrayList<>();
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            menuList = menuService.getByIds(idList);
        }
        role.add(menuList);
        return save(role);
    }

    @Override
    public Role deleteRoleMenus(String roleId, String ids) {
        // TODO 具体实现
        Role role = this.get(roleId);
        Collection<? extends Menu> menuList = new ArrayList<>();
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            menuList = menuService.getByIds(idList);
        }
        role.remove(menuList);
        return save(role);
    }

}
