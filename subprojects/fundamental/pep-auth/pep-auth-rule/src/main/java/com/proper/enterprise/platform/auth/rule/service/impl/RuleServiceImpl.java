package com.proper.enterprise.platform.auth.rule.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.auth.rule.entity.AuthRuleEntity;
import com.proper.enterprise.platform.auth.rule.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.proper.enterprise.platform.auth.rule.repository.AuthRuleRepository;
import com.proper.enterprise.platform.auth.rule.service.RuleService;

import java.util.Collection;

@Service
public class RuleServiceImpl implements RuleService {

    private AuthRuleRepository authRuleRepository;

    @Autowired
    public RuleServiceImpl(AuthRuleRepository authRuleRepository) {
        this.authRuleRepository = authRuleRepository;
    }

    @Override
    public RuleVO save(RuleVO ruleVO) {
        AuthRuleEntity entity = BeanUtil.convert(ruleVO, AuthRuleEntity.class);
        if (null == entity.getSort()) {
            entity.setSort(1);
        }
        return BeanUtil.convert(authRuleRepository.save(entity), RuleVO.class);
    }

    @Override
    public boolean deleteById(String id) {
        return authRuleRepository.delete(id);
    }

    @Override
    public RuleVO update(RuleVO ruleVO) {
        AuthRuleEntity entity = BeanUtil.convert(ruleVO, AuthRuleEntity.class);
        return BeanUtil.convert(authRuleRepository.updateForSelective(entity), RuleVO.class);
    }

    @Override
    public RuleVO getCode(String code) {
        return BeanUtil.convert(authRuleRepository.findByCode(code), RuleVO.class);
    }

    @Override
    public Collection<RuleVO> findAll(String code, String name, String type) {
        return BeanUtil.convert(authRuleRepository.findAll(code, name, type), RuleVO.class);
    }

    @Override
    public DataTrunk<RuleVO> findAll(String code, String name, String type, Pageable pageable) {
        return BeanUtil.convert(authRuleRepository.findAll(code, name, type, pageable), RuleVO.class);
    }

}
