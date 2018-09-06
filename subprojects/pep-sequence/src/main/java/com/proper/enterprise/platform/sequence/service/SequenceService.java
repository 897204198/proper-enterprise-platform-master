package com.proper.enterprise.platform.sequence.service;

import com.proper.enterprise.platform.sequence.vo.SequenceVO;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Collection;

@Validated
public interface SequenceService {

    /**
     * 保存
     *
     * @param sequenceVO vo
     * @return 保存后的vo
     */
    SequenceVO save(@Valid SequenceVO sequenceVO);

    /**
     * 根据Ids删除
     *
     * @param ids 待删除的id集合(,分隔)
     * @return true删除成功  false未删除
     */
    boolean deleteByIds(String ids);

    /**
     * 单表修改
     *
     * @param id idSerialNumberService
     * @param sequenceVO vo
     * @return 修改后的vo
     */
    SequenceVO update(String id, @Valid SequenceVO sequenceVO);

    /**
     * 查询
     * @param sequenceCode 查询参数
     * @return vo集合
     */
    Collection<SequenceVO> findAll(String sequenceCode);

    /**
     * 分页查询
     *
     * @param sequenceCode 查询参数
     * @param pageable     分页参数
     * @return 分页VO对象
     */
    DataTrunk<SequenceVO> findAll(String sequenceCode, Pageable pageable);

    /**
     * 通过流水Code获取流水信息
     *
     * @param sequenceCode 流水Code
     * @return 流水信息
     */
    SequenceVO findBySequenceCode(String sequenceCode);

}
