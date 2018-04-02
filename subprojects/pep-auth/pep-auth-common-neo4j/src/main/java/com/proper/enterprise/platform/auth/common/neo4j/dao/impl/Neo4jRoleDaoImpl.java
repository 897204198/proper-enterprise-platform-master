package com.proper.enterprise.platform.auth.common.neo4j.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.RoleDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.auth.common.neo4j.entity.RoleNodeEntity;
import com.proper.enterprise.platform.auth.common.neo4j.repository.RoleNodeRepository;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.neo4j.service.impl.Neo4jServiceSupport;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class Neo4jRoleDaoImpl extends Neo4jServiceSupport<Role, RoleNodeRepository, String> implements RoleDao {

    @Autowired
    private RoleNodeRepository roleNodeRepository;

    @Autowired
    private Session session;

    @Autowired
    private I18NService i18NService;

    @Override
    public Role get(String id) {
        return roleNodeRepository.findOne(id, 2);
    }

    @Override
    public Role get(String id, EnableEnum enableEnum) {
        switch (enableEnum) {
            case ALL:
                return roleNodeRepository.findOne(id, 2);
            case DISABLE:
                return roleNodeRepository.findByIdAndValidAndEnable(id, true, false);
            case ENABLE:
            default:
                return roleNodeRepository.findByIdAndValidAndEnable(id, true, true);
        }
    }

    @Override
    public Collection<? extends Role> getByName(String name) {
        return roleNodeRepository.findByNameAndValidAndEnable(name, true, true);
    }

    @Override
    public Role save(Role role) {
        return roleNodeRepository.save((RoleNodeEntity) role);
    }

    @Override
    public Role getNewRole() {
        return new RoleNodeEntity();
    }

    @Override
    public Role findByIdAndValid(String id, boolean valid) {
        return roleNodeRepository.findByIdAndValid(id, valid);
    }

    @Override
    public Collection<? extends Role> findAllByNameLike(String name) {
        return roleNodeRepository.findAllByNameLike(name);
    }

    @Override
    public RoleNodeRepository getRepository() {
        return roleNodeRepository;
    }

    @Override
    public Collection<? extends Role> findAll(Collection<String> idList) {
        return (Collection<? extends RoleNodeEntity>) roleNodeRepository.findAll(idList);
    }

    @Override
    public Collection<? extends Role> getByCondition(String name, String description, String parentId, EnableEnum enable) {
        Filters filters = new Filters();
        if (StringUtil.isNotBlank(name)) {
            filters.add(new Filter("name", ComparisonOperator.CONTAINING, name));
        }
        if (StringUtil.isNotBlank(description)) {
            Filter filter = new Filter("description", description);
            filter.setComparisonOperator(ComparisonOperator.CONTAINING);
            filter.setBooleanOperator(BooleanOperator.AND);
            filters.add(filter);
        }
        if (null != enable && EnableEnum.ALL != enable) {
            Filter filter = new Filter("enable", EnableEnum.ENABLE == enable);
            filter.setBooleanOperator(BooleanOperator.AND);
            filters.add(filter);
        }

        Filter filter = new Filter("valid", true);
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);

        SortOrder sortOrder = new SortOrder();
        sortOrder.add(SortOrder.Direction.ASC, "name");

        Collection<RoleNodeEntity> roles = session.loadAll(RoleNodeEntity.class, filters, sortOrder);
        Collection<RoleNodeEntity> result = new HashSet<>();
        for (RoleNodeEntity roleEntity : roles) {
            if (StringUtil.equalsIgnoreCase(roleEntity.getParentId(), parentId)) {
                result.add(roleEntity);
            }
        }
        return result;
    }

    @Override
    public Collection<? extends Role> findAllByValidTrueAndEnableTrue() {
        return roleNodeRepository.findAllByValidTrueAndEnableTrue();
    }

    @Override
    public Collection<? extends Role> getParentRolesByCurrentRoleId(String currentRoleId) {
        Role role = this.get(currentRoleId);
        if (role == null) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.get.failed"));
        }
        if (this.hasCircleInheritForCurrentRole(role)) {
            throw new ErrMsgException(i18NService.getMessage("pep.auth.common.role.circle.error"));
        }
        Collection<RoleNodeEntity> parentRoles = roleNodeRepository.findParentRolesByIdAndValidAndEnable(currentRoleId, true, true);
        return parentRoles;
    }

    @Override
    public boolean hasCircleInheritForCurrentRole(Role currentRole) {
        Boolean result = false;
        if (currentRole == null || StringUtil.isBlank(currentRole.getId())) {
            return result;
        }
        result = roleNodeRepository.hasCircleInheritForCurrentRole(currentRole.getId());
        if (result == null) {
            return false;
        }
        return result;
    }

    @Override
    public Collection<? extends UserGroup> getRoleUserGroups(String roleId) {
        return roleNodeRepository.findUsergroupsByRoleId(roleId);
    }

    @Override
    public DataTrunk<? extends Role> findRolesPagniation(String name, String description, String parentId, EnableEnum enable) {
        SortOrder sortOrder = new SortOrder();
        sortOrder.add(SortOrder.Direction.ASC, "name");
        return this.findPage(RoleNodeEntity.class, buildUserFilters(name, description, parentId, enable), sortOrder);
    }

    private Filters buildUserFilters(String name, String description, String parentId, EnableEnum enable) {
        Filters filters = new Filters();
        if (StringUtil.isNotBlank(name)) {
            filters.add(new Filter("name", ComparisonOperator.CONTAINING, name));
        }
        if (StringUtil.isNotBlank(description)) {
            filters.add(new Filter("description", ComparisonOperator.CONTAINING, description));
        }
        if (StringUtil.isNotBlank(parentId)) {
            filters.add(new Filter("parentId", ComparisonOperator.CONTAINING, parentId));
        }
        if (null != enable && EnableEnum.ALL != enable) {
            Filter filter = new Filter("enable", EnableEnum.ENABLE == enable);
            filter.setBooleanOperator(BooleanOperator.AND);
            filters.add(filter);
        }
        Filter filter = new Filter("valid", true);
        filter.setBooleanOperator(BooleanOperator.AND);
        filters.add(filter);
        return filters;
    }
}
