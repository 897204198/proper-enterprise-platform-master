package com.proper.enterprise.platform.auth.rule.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.auth.rule.entity.AuthRuleEntity;
import com.proper.enterprise.platform.auth.rule.vo.RuleVO;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
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
        entity.setType(ruleVO.getType());
        if (null == entity.getSort()) {
            entity.setSort(1);
        }
        return BeanUtil.convert(authRuleRepository.save(entity), RuleVO.class);
    }

    @Override
    public boolean deleteByIds(String ids) {
        String[] idAttr = ids.split("\\,");
        for (String id : idAttr) {
            deleteById(id);
        }
        return true;
    }

    @Override
    public boolean deleteById(String id) {
        return authRuleRepository.delete(id);
    }

    @Override
    public RuleVO update(RuleVO ruleVO) {
        AuthRuleEntity entity = BeanUtil.convert(ruleVO, AuthRuleEntity.class);
        entity.setType(ruleVO.getType());
        return BeanUtil.convert(authRuleRepository.updateForSelective(entity), RuleVO.class);
    }

    @Override
    public RuleVO getCode(String code) {
        return BeanUtil.convert(authRuleRepository.findByCode(code), RuleVO.class);
    }

    @Override
    public RuleVO get(String id) {
        return BeanUtil.convert(authRuleRepository.getOne(id), RuleVO.class);
    }

    @Override
    public Collection<RuleVO> findAll(String code, String name, DataDicLite type) {
        return BeanUtil.convert(authRuleRepository.findAll(code, name, type), RuleVO.class);
    }

    @Override
    public DataTrunk<RuleVO> findAll(String code, String name, DataDicLite type, Pageable pageable) {
        return BeanUtil.convert(authRuleRepository.findAll(code, name, type, pageable), RuleVO.class);
    }

}
