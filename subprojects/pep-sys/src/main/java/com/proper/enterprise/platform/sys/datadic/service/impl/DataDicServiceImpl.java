package com.proper.enterprise.platform.sys.datadic.service.impl;

import com.proper.enterprise.platform.sys.datadic.DataDic;
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
        return repository.findByCatalogAndCode(catalog, code);
    }

}
