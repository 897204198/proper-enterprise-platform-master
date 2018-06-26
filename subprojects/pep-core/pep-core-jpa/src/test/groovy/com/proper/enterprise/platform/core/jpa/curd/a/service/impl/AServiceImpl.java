package com.proper.enterprise.platform.core.jpa.curd.a.service.impl;

import com.proper.enterprise.platform.core.jpa.curd.a.api.A;
import com.proper.enterprise.platform.core.jpa.curd.a.repository.ARepository;
import com.proper.enterprise.platform.core.jpa.curd.a.service.AService;
import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.curd.b.repository.BRepository;
import com.proper.enterprise.platform.core.jpa.curd.c.api.C;
import com.proper.enterprise.platform.core.jpa.curd.c.repository.CRepository;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AServiceImpl extends AbstractJpaServiceSupport<A, ARepository, String> implements AService {

    @Autowired
    private ARepository arepository;

    @Autowired
    private BRepository brepository;

    @Autowired
    private CRepository crepository;

    @Override
    public ARepository getRepository() {
        return arepository;
    }

    @Override
    public A save(A a) {
        return super.save(a);
    }

    @Override
    public A addB(String aid, String bid) {
        A a = this.findOne(aid);
        B b = brepository.findOne(bid);
        a.add(b);
        return this.save(a);
    }

    @Override
    public A addC(String aid, String cid) {
        A a = this.findOne(aid);
        C c = crepository.findOne(cid);
        a.setCentity(c);
        return this.save(a);
    }

    @Override
    public Collection<A> findAllWithB() {
        return null;
    }


}
