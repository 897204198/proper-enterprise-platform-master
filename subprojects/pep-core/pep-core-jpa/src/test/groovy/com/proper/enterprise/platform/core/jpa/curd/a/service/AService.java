package com.proper.enterprise.platform.core.jpa.curd.a.service;

import com.proper.enterprise.platform.core.jpa.curd.a.api.A;
import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;

import java.util.Collection;

public interface AService extends BaseJpaService<A, String> {

    A save(A a);

    A addB(String aid, String bid);

    A addC(String aid, String cid);

    Collection<A> findAllWithB();


}
