package com.proper.enterprise.platform.sys.datadic;

import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public abstract class AbstractConcreteDataDic {

    @Autowired
    protected DataDicService dataDicService;

    public DataDic get(String code) {
        return dataDicService.get(getCatalog(), code);
    }

    protected abstract String getCatalog();

    public Collection<? extends DataDic> getAll() {
        return dataDicService.findByCatalog(getCatalog());
    }

    public DataDic getDefault() {
        return dataDicService.getDefault(getCatalog());
    }

}
