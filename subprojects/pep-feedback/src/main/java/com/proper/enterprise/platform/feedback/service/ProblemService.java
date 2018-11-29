package com.proper.enterprise.platform.feedback.service;

import com.proper.enterprise.platform.feedback.entity.ProblemEntity;
import com.proper.enterprise.platform.feedback.vo.ProblemVo;
import org.springframework.data.domain.PageRequest;

import java.util.List;


public interface ProblemService {

    /**
     * 获取问题列表
     * @param categoryId 分类id
     * @return 列表
     */
    List<ProblemEntity> findByCategoryId(String categoryId);

    /**
     * 修改
     * @param name 问题名称
     * @param answer 问题答案
     * @param id 问题id
     */
    void updateProblem(String name, String answer, String id);

    /**
     * 增加
     * @param name 问题名称
     * @param answer 问题答案
     * @param categoryId 分类id
     */
    void addProblem(String name, String answer, String categoryId);

    /**
     * 删除
     * @param problemId 主键Id
     */
    void delProblem(String problemId);

    /**
     * 批量删除
     * @param problemIds 主键Id
     */
    void delAllProblems(String problemIds);

    /**
     * 获取热门问题
     * @param pageRequest 分页对象
     * @return 热门问题列表
     */
    List<ProblemEntity> getPopular(PageRequest pageRequest);

    /**
     * 获取问题详情
     * @param problemId 问题Id
     * @param deviceId 设备号
     * @return 问题详情
     */
    ProblemVo saveProblemInfo(String problemId, String deviceId);

    /**
     * 问题评价
     * @param problemId 问题id
     * @param deviceId 设备id
     * @param code 排序字段
     * @return 问题实体
     */
    ProblemVo saveAccessProblem(String problemId, String deviceId, String code);

    /**
     * 获取问题详情
     * @param problemId 问题id
     * @return 问题的详情
     */
    ProblemEntity findProblemById(String problemId);

}
