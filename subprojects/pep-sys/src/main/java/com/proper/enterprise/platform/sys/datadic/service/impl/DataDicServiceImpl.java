package com.proper.enterprise.platform.sys.datadic.service.impl;

import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity;
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DataDicServiceImpl implements DataDicService {

    @Autowired
    private DataDicRepository repository;

    @Override
    public Collection<? extends DataDic> findByCatalog(String catalog) {
        return repository.findByCatalogOrderByOrder(catalog);
    }

    @Override
    public DataDic get(String catalog, String code) {
        return repository.getByCatalogAndCode(catalog, code);
    }

    @Override
    public DataDic get(String id) {
        return repository.getOne(id);
    }

    @Override
    public DataDic getDefault(String catalog) {
        return repository.getByCatalogAndIsDefaultIsTrue(catalog);
    }

    @Override
    public DataDic save(DataDic dataDic) {
        return repository.save((DataDicEntity) dataDic);
    }

}
