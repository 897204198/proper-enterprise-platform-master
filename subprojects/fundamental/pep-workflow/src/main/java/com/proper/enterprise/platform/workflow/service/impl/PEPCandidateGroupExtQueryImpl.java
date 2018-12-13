package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.api.auth.service.UserGroupService;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.model.PEPCandidateModel;
import com.proper.enterprise.platform.workflow.service.PEPCandidateExtQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("pepCandidateGroupExtQuery")
public class PEPCandidateGroupExtQueryImpl implements PEPCandidateExtQuery {

    private static final String GROUP_DATADIC_CODE = "GROUP";

    private UserGroupService userGroupService;

    private UserService userService;

    @Autowired
    public PEPCandidateGroupExtQueryImpl(UserGroupService userGroupService, UserService userService) {
        this.userGroupService = userGroupService;
        this.userService = userService;
    }

    @Override
    public List<PEPCandidateModel> findCandidatesByUser(String userId) {
        try {
            return convert(new ArrayList<>(userService.getUserGroups(userId, EnableEnum.ENABLE)));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public PEPCandidateModel findCandidateById(String id) {
        try {
            UserGroup userGroup = userGroupService.get(id, EnableEnum.ENABLE);
            return convert(userGroup);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<PEPCandidateModel> findAllCandidates() {
        try {
            return convert(new ArrayList<>(userGroupService.getGroups(null, null, EnableEnum.ENABLE)));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<PEPCandidateModel> findCandidatesByNameLike(String name) {
        return convert(new ArrayList<>(userGroupService.getGroups(name, null, EnableEnum.ENABLE)));
    }

    private List<PEPCandidateModel> convert(Collection<UserGroup> userGroups) {
        List<PEPCandidateModel> pepCandidateModels = new ArrayList<>();
        if (CollectionUtil.isEmpty(userGroups)) {
            return pepCandidateModels;
        }
        for (UserGroup userGroup : userGroups) {
            pepCandidateModels.add(convert(userGroup));
        }
        return pepCandidateModels;
    }

    private PEPCandidateModel convert(UserGroup userGroup) {
        PEPCandidateModel pepCandidateModel = new PEPCandidateModel();
        pepCandidateModel.setId(userGroup.getId());
        pepCandidateModel.setType(GROUP_DATADIC_CODE);
        pepCandidateModel.setName(userGroup.getName());
        return pepCandidateModel;
    }
}
