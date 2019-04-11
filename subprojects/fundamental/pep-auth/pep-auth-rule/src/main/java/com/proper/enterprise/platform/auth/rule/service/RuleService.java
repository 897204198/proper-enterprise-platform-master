package com.proper.enterprise.platform.auth.rule.service;

import com.proper.enterprise.platform.auth.rule.vo.RuleVO;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Collection;

@Validated
public interface RuleService {

    /**
     * 保存
     *
     * @param ruleVO vo
     * @return 保存后的vo
     */
    RuleVO save(@Valid RuleVO ruleVO);

    /**
     * 根据Id删除
     *
     * @param id id
     * @return true删除成功  false未删除
     */
    boolean deleteById(String id);

    /**
     * 单表修改
     *
     * @param ruleVO vo
     * @return 修改后的vo
     */
    RuleVO update(RuleVO ruleVO);

    /**
     * 根据编码获取规则
     *
     * @param code 编码
     * @return 规则VO
     */
    RuleVO getCode(String code);

    /**
     * 查询
     *
     * @param code 编码
     * @param name 名称
     * @param type 类型
     * @return VO集合
     */
    Collection<RuleVO> findAll(String code, String name, String type);

    /**
     * 分页查询
     *
     * @param code     编码
     * @param name     名称
     * @param type     类型
     * @param pageable 分页参数
     * @return 分页VO对象
     */
    DataTrunk<RuleVO> findAll(String code, String name, String type, Pageable pageable);

}
