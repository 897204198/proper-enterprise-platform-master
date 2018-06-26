package com.proper.enterprise.platform.core.jpa.curd.c.service.impl;

import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.curd.b.entity.BEntity;
import com.proper.enterprise.platform.core.jpa.curd.b.service.BService;
import com.proper.enterprise.platform.core.jpa.curd.c.api.C;
import com.proper.enterprise.platform.core.jpa.curd.c.entity.CEntity;
import com.proper.enterprise.platform.core.jpa.curd.c.repository.CRepository;
import com.proper.enterprise.platform.core.jpa.curd.c.service.CService;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CServiceImpl extends AbstractJpaServiceSupport<C, CRepository, String> implements CService {

    @Autowired
    private CRepository crepository;

    @Autowired
    private BService bservice;

    @Override
    public CRepository getRepository() {
        return crepository;
    }

    @Override
    public C addB(String cid, String bid) {
        C c = this.findOne(cid);
        B b = bservice.findOne(bid);
        ((CEntity) c).setBentity((BEntity) b);
        return this.save(c);
    }
}
