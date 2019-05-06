package com.proper.enterprise.platform.workflow.flowable.idm.service.impl;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.factory.PEPCandidateExtQueryFactory;
import com.proper.enterprise.platform.workflow.model.PEPCandidateModel;
import com.proper.enterprise.platform.workflow.plugin.service.impl.PEPCandidateUserExtQueryImpl;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.UserQueryImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 扩展指定用户和候选用户
 */
public class PEPUserQueryImpl extends UserQueryImpl {

    @Override
    public long executeCount(CommandContext commandContext) {
        return executeQuery().size();
    }

    @Override
    public List<User> executeList(CommandContext commandContext) {
        return executeQuery();
    }

    protected List<User> executeQuery() {
        if (getId() != null) {
            List<User> result = new ArrayList<>();
            result.add(convert(PEPCandidateExtQueryFactory
                .product(PEPCandidateUserExtQueryImpl.USER_CONF_CODE).findCandidateById(getId())));
            return result;

        } else if (getIdIgnoreCase() != null) {
            List<User> result = new ArrayList<>();
            result.add(convert(PEPCandidateExtQueryFactory
                .product(PEPCandidateUserExtQueryImpl.USER_CONF_CODE)
                .findCandidateById(getIdIgnoreCase())));
            return result;
        } else if (getFullNameLike() != null) {
            return convert(PEPCandidateExtQueryFactory
                .product(PEPCandidateUserExtQueryImpl.USER_CONF_CODE)
                .findCandidatesByNameLike(getFullNameLike()));
        } else if (getFullNameLikeIgnoreCase() != null) {
            return convert(PEPCandidateExtQueryFactory
                .product(PEPCandidateUserExtQueryImpl.USER_CONF_CODE)
                .findCandidatesByNameLike(getFullNameLikeIgnoreCase()));
        } else {
            return convert(PEPCandidateExtQueryFactory
                .product(PEPCandidateUserExtQueryImpl.USER_CONF_CODE)
                .findAllCandidates());
        }
    }


    public static User convert(PEPCandidateModel pepCandidateModel) {
        if (null == pepCandidateModel) {
            return null;
        }
        User user = new UserEntityImpl();
        user.setFirstName(pepCandidateModel.getName());
        user.setLastName(pepCandidateModel.getName());
        user.setId(pepCandidateModel.getId());
        return user;
    }


    public static List<User> convert(Collection<PEPCandidateModel> pepCandidateModels) {
        if (CollectionUtil.isEmpty(pepCandidateModels)) {
            return new ArrayList<>();
        }
        List<User> users = new ArrayList<>();
        for (PEPCandidateModel pepCandidateModel : pepCandidateModels) {
            User user = convert(pepCandidateModel);
            if (null == user) {
                continue;
            }
            users.add(user);
        }
        return users;
    }
}
