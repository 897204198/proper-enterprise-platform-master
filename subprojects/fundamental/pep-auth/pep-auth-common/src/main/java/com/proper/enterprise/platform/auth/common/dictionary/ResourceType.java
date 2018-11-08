package com.proper.enterprise.platform.auth.common.dictionary;

import com.proper.enterprise.platform.sys.datadic.AbstractConcreteDataDic;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import org.springframework.stereotype.Service;

@Service
public class ResourceType extends AbstractConcreteDataDic {

    @Override
    public String getCatalog() {
        return "RESOURCE_TYPE";
    }

    public DataDic method() {
        return get("0");
    }

}
