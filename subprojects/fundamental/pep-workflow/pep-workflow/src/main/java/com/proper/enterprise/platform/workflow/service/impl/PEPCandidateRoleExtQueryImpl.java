package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.service.RoleService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.model.PEPCandidateModel;
import com.proper.enterprise.platform.workflow.service.PEPCandidateExtQuery;
import com.proper.enterprise.platform.workflow.util.WFIdmQueryConfUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("pepCandidateRoleExtQuery")
public class PEPCandidateRoleExtQueryImpl implements PEPCandidateExtQuery {

    private RoleService roleService;

    private UserService userService;

    public PEPCandidateRoleExtQueryImpl(RoleService roleService, UserService userService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public String getType() {
        return "ROLE";
    }

    @Override
    public List<PEPCandidateModel> findCandidatesByUser(String userId) {
        try {
            return convert(new ArrayList<>(userService.getUserRoles(userId, EnableEnum.ENABLE)));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public PEPCandidateModel findCandidateById(String id) {
        try {
            return convert(roleService.get(id));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<PEPCandidateModel> findAllCandidates() {
        try {
            return convert(new ArrayList<>(roleService.findRolesLike(null, EnableEnum.ENABLE)));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<PEPCandidateModel> findCandidatesByNameLike(String name) {
        return convert(new ArrayList<>(roleService.findRolesLike(name, EnableEnum.ENABLE)));
    }

    private List<PEPCandidateModel> convert(Collection<Role> roles) {
        List<PEPCandidateModel> pepCandidateModels = new ArrayList<>();
        if (CollectionUtil.isEmpty(roles)) {
            return pepCandidateModels;
        }
        for (Role role : roles) {
            pepCandidateModels.add(convert(role));
        }
        return pepCandidateModels;
    }

    private PEPCandidateModel convert(Role role) {
        PEPCandidateModel pepCandidateModel = new PEPCandidateModel();
        pepCandidateModel.setId(role.getId());
        pepCandidateModel.setType(getType());
        pepCandidateModel.setTypeName(WFIdmQueryConfUtil.findByType(getType()).getName());
        pepCandidateModel.setName(role.getName());
        return pepCandidateModel;
    }

}
