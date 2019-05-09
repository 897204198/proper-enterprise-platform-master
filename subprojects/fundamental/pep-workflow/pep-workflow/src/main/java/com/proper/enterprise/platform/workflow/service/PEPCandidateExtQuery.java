package com.proper.enterprise.platform.workflow.service;

import com.proper.enterprise.platform.workflow.model.PEPCandidateModel;

import java.util.List;

public interface PEPCandidateExtQuery {

    /**
     * 根据用户id 获得候选集合
     *
     * @param userId 用户id
     * @return 候选集合
     */
    List<PEPCandidateModel> findCandidatesByUser(String userId);

    /**
     * 根据主键获得候选对象
     *
     * @param id 主键
     * @return 候选对象
     */
    PEPCandidateModel findCandidateById(String id);

    /**
     * 获得全部候选集合
     *
     * @return 全部候选集合
     */
    List<PEPCandidateModel> findAllCandidates();


    /**
     * 根据名称 模糊查询候选集合
     *
     * @param name 名称
     * @return 候选集合
     */
    List<PEPCandidateModel> findCandidatesByNameLike(String name);
}
