package com.proper.enterprise.platform.sys.datadic;

import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractConcreteDataDic {

    @Autowired
    private DataDicService dataDicService;

    public DataDic get(String code) {
        return dataDicService.get(getCatalog(), code);
    }

    protected abstract String getCatalog();

}
