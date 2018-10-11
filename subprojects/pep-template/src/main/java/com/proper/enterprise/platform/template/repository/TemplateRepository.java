package com.proper.enterprise.platform.template.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.template.entity.TemplateEntity;

public interface TemplateRepository extends BaseJpaRepository<TemplateEntity, String> {

    /**
     * 查询指定业务的模板
     *
     * @param code 关键字
     * @return 模板
     */
    TemplateEntity findByCode(String code);

}
