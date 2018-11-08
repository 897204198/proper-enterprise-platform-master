package com.proper.enterprise.platform.core.jpa.curd.b.service;

import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;


public interface BService extends BaseJpaService<B, String> {
    /**
     * 保存B
     *
     * @param b b
     * @return b
     */
    B saveB(B b);

}
