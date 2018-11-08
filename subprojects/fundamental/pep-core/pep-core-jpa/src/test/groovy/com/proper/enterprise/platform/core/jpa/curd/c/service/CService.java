package com.proper.enterprise.platform.core.jpa.curd.c.service;

import com.proper.enterprise.platform.core.jpa.curd.c.api.C;
import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;


public interface CService extends BaseJpaService<C, String> {

    /**
     * 添加
     * @param cid cid
     * @param bid bid
     * @return C
     */
    C addB(String cid, String bid);
}
