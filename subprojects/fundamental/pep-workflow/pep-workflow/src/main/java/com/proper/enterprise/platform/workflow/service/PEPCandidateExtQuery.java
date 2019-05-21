package com.proper.enterprise.platform.workflow.service;

import com.proper.enterprise.platform.workflow.model.PEPCandidateModel;

import java.util.List;

public interface PEPCandidateExtQuery {

    /**
     * 获取扩展实现的类型
     *
     * @return 扩展实现的类型
     */
    String getType();

    /**
     * 根据用户id 获得候选集合
     * <p>
     * 当流程待办查询的时候 根据当前人的id查询待办
     * 返回当前人id所关联的候选集合
     *
     * @param userId 用户id
     * @return 候选集合
     */
    List<PEPCandidateModel> findCandidatesByUser(String userId);

    /**
     * 根据主键获得候选对象
     * 根据候选Id查询候选对象
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
     * 提供给流程设计器中的候选选择器
     *
     * @param name 名称
     * @return 候选集合
     */
    List<PEPCandidateModel> findCandidatesByNameLike(String name);
}
