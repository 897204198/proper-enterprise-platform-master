package com.proper.enterprise.platform.workflow.flowable.idm.service.impl;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.entity.WFIdmQueryConfEntity;
import com.proper.enterprise.platform.workflow.factory.PEPCandidateExtQueryFactory;
import com.proper.enterprise.platform.workflow.model.PEPCandidateModel;
import com.proper.enterprise.platform.workflow.util.CandidateIdUtil;
import com.proper.enterprise.platform.workflow.util.WFIdmQueryConfUtil;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.idm.api.Group;
import org.flowable.idm.engine.impl.GroupQueryImpl;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 重新实现flowable的用户组
 * 包含设置的所有非用户候选
 */
public class PEPGroupQueryImpl extends GroupQueryImpl {

    @Override
    public long executeCount(CommandContext commandContext) {
        return executeQuery().size();
    }

    @Override
    public List<Group> executeList(CommandContext commandContext) {
        return executeQuery();
    }

    protected List<Group> executeQuery() {
        if (getUserId() != null) {
            return findCandidatesByUser(getUserId());
        } else if (getId() != null) {
            return findCandidatesById(getId());
        } else {
            return findAllCandidates();
        }
    }

    private List<Group> findCandidatesByUser(String userId) {
        List<Group> groups = new ArrayList<>();

        List<WFIdmQueryConfEntity> wfIdmQueryConfEntities = new ArrayList<>(WFIdmQueryConfUtil.findAll());
        for (WFIdmQueryConfEntity wfIdmQueryConfEntity : wfIdmQueryConfEntities) {
            groups.addAll(convertCollection(PEPCandidateExtQueryFactory
                .product(wfIdmQueryConfEntity.getType()).findCandidatesByUser(userId)));
        }
        return groups;
    }

    private List<Group> findCandidatesById(String id) {
        List<Group> groups = new ArrayList<>();
        List<WFIdmQueryConfEntity> wfIdmQueryConfEntities = new ArrayList<>(WFIdmQueryConfUtil.findAll());
        for (WFIdmQueryConfEntity wfIdmQueryConfEntity : wfIdmQueryConfEntities) {
            Group group = convert(PEPCandidateExtQueryFactory
                .product(wfIdmQueryConfEntity.getType()).findCandidateById(id));
            if (null == group) {
                continue;
            }
            groups.add(group);
        }
        return groups;
    }

    private List<Group> findAllCandidates() {
        List<Group> groups = new ArrayList<>();
        List<WFIdmQueryConfEntity> wfIdmQueryConfEntities = new ArrayList<>(WFIdmQueryConfUtil.findAll());
        for (WFIdmQueryConfEntity wfIdmQueryConfEntity : wfIdmQueryConfEntities) {
            groups.addAll(convertCollection(PEPCandidateExtQueryFactory
                .product(wfIdmQueryConfEntity.getType()).findAllCandidates()));
        }
        return groups;
    }


    private static Group convert(PEPCandidateModel pepCandidateModel) {
        if (null == pepCandidateModel) {
            return null;
        }
        Group group = new GroupEntityImpl();
        group.setId(CandidateIdUtil.encode(pepCandidateModel.getId(), pepCandidateModel.getType()));
        group.setName(pepCandidateModel.getName());
        return group;
    }


    private static List<Group> convertCollection(Collection<PEPCandidateModel> pepCandidateModels) {
        if (CollectionUtil.isEmpty(pepCandidateModels)) {
            return new ArrayList<>();
        }
        List<Group> groups = new ArrayList<>();
        for (PEPCandidateModel pepCandidateModel : pepCandidateModels) {
            Group group = convert(pepCandidateModel);
            if (null == group) {
                continue;
            }
            groups.add(group);
        }
        return groups;
    }
}
