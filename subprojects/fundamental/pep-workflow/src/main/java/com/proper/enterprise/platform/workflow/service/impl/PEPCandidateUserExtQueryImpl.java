package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.model.PEPCandidateModel;
import com.proper.enterprise.platform.workflow.service.PEPCandidateExtQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("pepCandidateUserExtQuery")
public class PEPCandidateUserExtQueryImpl implements PEPCandidateExtQuery {

    public static final String USER_DATADIC_CODE = "USER";

    private UserService userService;

    public PEPCandidateUserExtQueryImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PEPCandidateModel> findCandidatesByUser(String userId) {
        return null;
    }

    @Override
    public PEPCandidateModel findCandidateById(String id) {
        return convert(userService.get(id));
    }

    @Override
    public List<PEPCandidateModel> findAllCandidates() {
        return convert(new ArrayList<>(userService
            .getUsersByAndCondition(null, null, null, null, EnableEnum.ENABLE)));
    }

    @Override
    public List<PEPCandidateModel> findCandidatesByNameLike(String name) {
        return convert(new ArrayList<>(userService
            .getUsersByAndCondition(name, null, null, null, EnableEnum.ENABLE)));
    }

    private List<PEPCandidateModel> convert(Collection<User> users) {
        List<PEPCandidateModel> pepCandidateModels = new ArrayList<>();
        if (CollectionUtil.isEmpty(users)) {
            return pepCandidateModels;
        }
        for (User user : users) {
            pepCandidateModels.add(convert(user));
        }
        return pepCandidateModels;
    }

    private PEPCandidateModel convert(User user) {
        PEPCandidateModel pepCandidateModel = new PEPCandidateModel();
        pepCandidateModel.setId(user.getId());
        pepCandidateModel.setType(USER_DATADIC_CODE);
        pepCandidateModel.setName(user.getName());
        return pepCandidateModel;
    }

}
