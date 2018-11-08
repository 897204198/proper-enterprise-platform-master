package com.proper.enterprise.platform.auth.common.dictionary;

import com.proper.enterprise.platform.sys.datadic.AbstractConcreteDataDic;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import org.springframework.stereotype.Service;

@Service
public class MenuType extends AbstractConcreteDataDic {

    @Override
    public String getCatalog() {
        return "MENU_TYPE";
    }

    public DataDic app() {
        return get("0");
    }

    public DataDic page() {
        return get("1");
    }

    public DataDic function() {
        return get("2");
    }

}
